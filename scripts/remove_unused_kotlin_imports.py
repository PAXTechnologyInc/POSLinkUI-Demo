#!/usr/bin/env python3
"""
Best-effort removal of unused single-name Kotlin imports in a source tree.

Skips: wildcard imports (import x.*), and imports where the effective simple name is shorter than 2 chars.

Usage:
  python scripts/remove_unused_kotlin_imports.py [root_dir ...]
  Default roots: app/src/main/java app/src/test/java app/src/androidTest/java
"""

from __future__ import annotations

import re
import sys
from pathlib import Path

# import foo.bar.Baz
# import foo.bar.Baz as Qux
IMPORT_LINE_RE = re.compile(
    r"^(?P<indent>\s*)import\s+(?P<fq>[\w.]+)(?:\s+as\s+(?P<alias>\w+))?\s*(?://.*)?$"
)

# Used by Kotlin `by` delegates / property delegates; names rarely appear verbatim in source.
_DELEGATE_PROTOCOL_NAMES = frozenset({"getValue", "setValue", "provideDelegate"})


def effective_simple_name(fq: str, alias: str | None) -> str | None:
    if alias:
        return alias
    if fq.endswith(".*"):
        return None
    parts = fq.split(".")
    return parts[-1] if parts else None


def body_without_imports_and_package(lines: list[str]) -> str:
    out: list[str] = []
    for line in lines:
        s = line.strip()
        if s.startswith("import ") or s.startswith("package "):
            continue
        if s.startswith("@file:"):
            continue
        out.append(line)
    return "".join(out)


def is_used_in_body(name: str, body: str) -> bool:
    if len(name) < 2:
        return True
    # Avoid matching inside longer identifiers; allow backticks for Kotlin keywords as names.
    return re.search(r"(?<![\w`])" + re.escape(name) + r"(?![\w`])", body) is not None


def process_file(path: Path) -> bool:
    text = path.read_text(encoding="utf-8")
    lines = text.splitlines(keepends=True)
    if not any(l.lstrip().startswith("import ") for l in lines):
        return False

    body = body_without_imports_and_package(lines)
    new_lines: list[str] = []
    removed_any = False

    for line in lines:
        stripped_nl = line.rstrip("\n")
        m = IMPORT_LINE_RE.match(stripped_nl)
        if not m:
            new_lines.append(line)
            continue
        fq = m.group("fq")
        alias = m.group("alias")
        simple = effective_simple_name(fq, alias)
        if simple is None:
            new_lines.append(line)
            continue
        if simple in _DELEGATE_PROTOCOL_NAMES:
            new_lines.append(line)
            continue
        if is_used_in_body(simple, body):
            new_lines.append(line)
        else:
            removed_any = True

    if not removed_any:
        return False

    new_text = "".join(new_lines)
    path.write_text(new_text, encoding="utf-8", newline="\n")
    return True


def main() -> int:
    repo = Path(__file__).resolve().parents[1]
    roots = [Path(p) for p in sys.argv[1:]] if len(sys.argv) > 1 else [
        repo / "app" / "src" / "main" / "java",
        repo / "app" / "src" / "test" / "java",
        repo / "app" / "src" / "androidTest" / "java",
    ]
    changed = 0
    scanned = 0
    for root in roots:
        if not root.is_dir():
            continue
        for path in sorted(root.rglob("*.kt")):
            scanned += 1
            try:
                if process_file(path):
                    changed += 1
                    print(f"cleaned: {path.relative_to(repo)}")
            except OSError as e:
                print(f"skip {path}: {e}", file=sys.stderr)
    print(f"Done. Scanned {scanned} files, modified {changed}.", file=sys.stderr)
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
