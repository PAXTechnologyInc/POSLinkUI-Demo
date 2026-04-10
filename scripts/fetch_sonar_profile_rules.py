#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Export activated SonarQube rules for Kotlin/Java Quality Profiles bound to a project.

Same outputs as fetch-sonar-profile-rules.ps1 (docs/sonarqube-rules-export/).

Environment:
  SONAR_TOKEN (required) — user token, never commit
Optional:
  SONAR_HOST (default from sonar-project.properties: http://172.16.3.60:9090)
  SONAR_PROJECT_KEY (default POSLinkUI-Demo)
"""

from __future__ import annotations

import base64
import json
import os
import sys
import urllib.error
import urllib.parse
import urllib.request
from collections import Counter
from datetime import datetime, timezone
from pathlib import Path


def _repo_root() -> Path:
    return Path(__file__).resolve().parent.parent


def _load_default_host() -> str:
    props = _repo_root() / "sonar-project.properties"
    if props.is_file():
        for line in props.read_text(encoding="utf-8", errors="replace").splitlines():
            line = line.strip()
            if line.startswith("sonar.host.url="):
                return line.split("=", 1)[1].strip().rstrip("/")
    return "http://172.16.3.60:9090"


def _auth_header(token: str) -> dict[str, str]:
    raw = f"{token}:".encode("ascii")
    b64 = base64.b64encode(raw).decode("ascii")
    return {"Authorization": f"Basic {b64}"}


def _get_json(base: str, path: str, headers: dict[str, str]) -> dict:
    url = f"{base}{path}"
    req = urllib.request.Request(url, headers=headers, method="GET")
    try:
        with urllib.request.urlopen(req, timeout=120) as resp:
            return json.loads(resp.read().decode("utf-8"))
    except urllib.error.HTTPError as e:
        body = e.read().decode("utf-8", errors="replace")
        raise RuntimeError(f"HTTP {e.code} for {url}: {body}") from e


def _fetch_activated_rules(base: str, profile_key: str, headers: dict[str, str]) -> list[dict]:
    page = 1
    page_size = 500
    all_rules: list[dict] = []
    enc = urllib.parse.quote(profile_key, safe="")
    while True:
        path = (
            f"/api/rules/search?activation=true&qprofile={enc}"
            f"&ps={page_size}&p={page}"
        )
        data = _get_json(base, path, headers)
        rules = data.get("rules") or []
        all_rules.extend(rules)
        total = int(data.get("total", 0))
        if len(all_rules) >= total:
            break
        page += 1
    return all_rules


def _write_md(path: Path, title: str, rules: list[dict]) -> None:
    lines = [
        f"# {title}",
        "",
        "| Rule Key | Name | Severity | Type |",
        "|----------|------|----------|------|",
    ]
    for r in sorted(rules, key=lambda x: x.get("key", "")):
        name = (r.get("name") or "").replace("|", "/")
        lines.append(
            f"| {r.get('key', '')} | {name} | {r.get('severity', '')} | {r.get('type', '')} |"
        )
    lines.extend(["", f"**Count:** {len(rules)}", ""])
    path.write_text("\n".join(lines), encoding="utf-8")


def _write_tsv(path: Path, rules: list[dict]) -> None:
    rows = ["key\tname\tseverity\ttype\tlang"]
    for r in sorted(rules, key=lambda x: x.get("key", "")):
        name = (r.get("name") or "").replace("\t", " ").replace("\n", " ").replace("\r", "")
        rows.append(
            f"{r.get('key', '')}\t{name}\t{r.get('severity', '')}\t{r.get('type', '')}\t{r.get('lang', '')}"
        )
    path.write_text("\n".join(rows), encoding="utf-8")


def main() -> int:
    token = os.environ.get("SONAR_TOKEN", "").strip()
    if not token:
        print("Set SONAR_TOKEN to your SonarQube user token.", file=sys.stderr)
        return 1

    base = os.environ.get("SONAR_HOST", _load_default_host()).rstrip("/")
    project_key = os.environ.get("SONAR_PROJECT_KEY", "POSLinkUI-Demo")
    headers = _auth_header(token)

    out_dir = _repo_root() / "docs" / "sonarqube-rules-export"
    out_dir.mkdir(parents=True, exist_ok=True)

    print(f"Fetching quality profiles for project={project_key} ...")
    enc_proj = urllib.parse.quote(project_key, safe="")
    qp = _get_json(base, f"/api/qualityprofiles/search?project={enc_proj}", headers)
    profiles = qp.get("profiles") or []
    if not profiles:
        print(f"No profiles returned: {json.dumps(qp, ensure_ascii=False)[:500]}", file=sys.stderr)
        return 1

    meta: dict = {
        "exportedAtUtc": datetime.now(timezone.utc).isoformat(),
        "sonarHost": base,
        "projectKey": project_key,
        "languages": {},
    }

    targets = ("kotlin", "java")
    rules_by_lang: dict[str, list] = {}
    for lang in targets:
        candidates = [p for p in profiles if p.get("language") == lang]
        if not candidates:
            print(f"Warning: no quality profile for language '{lang}', skip.")
            continue
        default_ones = [p for p in candidates if p.get("isDefault") is True]
        profile = default_ones[0] if default_ones else candidates[0]
        pkey = profile["key"]
        pname = profile.get("name", "")
        print(f"Language={lang} profile key={pkey} name={pname} ...")
        rules = _fetch_activated_rules(base, pkey, headers)
        print(f"  -> {len(rules)} activated rules")

        meta["languages"][lang] = {
            "profileKey": pkey,
            "profileName": pname,
            "ruleCount": len(rules),
        }

        _write_md(
            out_dir / f"rules-{lang}.md",
            f"Sonar activated rules: {lang} ({pname})",
            rules,
        )
        _write_tsv(out_dir / f"rules-{lang}.tsv", rules)
        rules_by_lang[lang] = rules

    meta_path = out_dir / "EXPORT_META.json"
    meta_path.write_text(json.dumps(meta, indent=2, ensure_ascii=False), encoding="utf-8")
    print(f"Done. Output: {out_dir}")

    _write_summary_markdown(
        _repo_root() / "doc" / "sonarqube-rules-summary.md",
        meta,
        rules_by_lang,
    )
    print(f"Summary: {_repo_root() / 'doc' / 'sonarqube-rules-summary.md'}")
    return 0


def _write_summary_markdown(
    path: Path,
    meta: dict,
    rules_by_lang: dict[str, list],
) -> None:
    """
    Human-readable summary for doc/ (offline overview + links to full export tables).
    """
    path.parent.mkdir(parents=True, exist_ok=True)
    lines = [
        "# SonarQube 规则导出摘要",
        "",
        "本文档由 `scripts/fetch_sonar_profile_rules.py` 根据 **Sonar 实例上与项目绑定的 Quality Profile** 自动生成。",
        "规则范围是 **Profile 内已激活规则**（Kotlin / Java），与服务器配置一致；非全实例规则库。",
        "",
        "## 导出元数据",
        "",
        f"- **Sonar 地址:** {meta.get('sonarHost', '')}",
        f"- **项目 Key:** {meta.get('projectKey', '')}",
        f"- **导出时间 (UTC):** {meta.get('exportedAtUtc', '')}",
        "",
        "## 各语言 Profile 与条数",
        "",
        "| 语言 | Profile 名称 | Profile Key | 已激活规则数 |",
        "|------|--------------|-------------|--------------|",
    ]
    langs = meta.get("languages") or {}
    for lang in ("kotlin", "java"):
        info = langs.get(lang) or {}
        lines.append(
            f"| {lang} | {info.get('profileName', '—')} | `{info.get('profileKey', '')}` | {info.get('ruleCount', 0)} |"
        )
    total = sum(int((langs.get(l) or {}).get("ruleCount") or 0) for l in ("kotlin", "java"))
    lines.extend(["", f"**合计（Kotlin + Java）:** {total} 条", ""])

    lines.append("## 按严重级别与类型汇总（各语言）")
    lines.append("")
    for lang in ("kotlin", "java"):
        rules = rules_by_lang.get(lang) or []
        if not rules:
            continue
        lines.append(f"### {lang}")
        lines.append("")
        sev = Counter((r.get("severity") or "UNKNOWN") for r in rules)
        typ = Counter((r.get("type") or "UNKNOWN") for r in rules)
        lines.append("| Severity | Count |")
        lines.append("|----------|-------|")
        for k, v in sorted(sev.items(), key=lambda x: (-x[1], x[0])):
            lines.append(f"| {k} | {v} |")
        lines.append("")
        lines.append("| Type | Count |")
        lines.append("|------|-------|")
        for k, v in sorted(typ.items(), key=lambda x: (-x[1], x[0])):
            lines.append(f"| {k} | {v} |")
        lines.append("")

    lines.extend(
        [
            "## 完整列表位置",
            "",
            "详细规则表（Markdown / TSV）位于仓库内：",
            "",
            "- `docs/sonarqube-rules-export/rules-kotlin.md`",
            "- `docs/sonarqube-rules-export/rules-kotlin.tsv`",
            "- `docs/sonarqube-rules-export/rules-java.md`",
            "- `docs/sonarqube-rules-export/rules-java.tsv`",
            "- `docs/sonarqube-rules-export/EXPORT_META.json`",
            "",
            "说明与手动导出命令见 `docs/sonarqube-rules-export/README.md`。",
            "",
            "## 与质量门禁文档的关系",
            "",
            "- `.cursor/rules/sonarqube-quality-gate.mdc`：PAX 门禁阈值与高频规则摘要。",
            "- 本导出：服务器上 **实际启用** 的规则全表；若部门调整 Profile，以本导出为准。",
            "",
        ]
    )
    path.write_text("\n".join(lines), encoding="utf-8")


if __name__ == "__main__":
    raise SystemExit(main())
