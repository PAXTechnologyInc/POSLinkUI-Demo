"""
Remove unused Kotlin import lines (heuristic: simple name must appear as its own token in file body).

Skips removal for Compose/Kotlin delegation imports: [getValue] and [setValue] are often required for `byremember { mutableStateOf(...) }` but do not appear as explicit identifiers in source.

Usage (repo root): python scripts/strip_unused_imports.py path/to/File.kt [...]
"""

from __future__ import annotations

import re
import sys
from pathlib import Path

IMPORT_RE = re.compile(r"^import\s+([\w.]+)(?:\s+as\s+(\w+))?\s*$")

# Names that must never be auto-stripped (delegation / compiler magics).
_PRESERVE_SIMPLE_NAMES = frozenset(
    {
        "getValue",
        "setValue",
    }
)


def body_after_imports(lines: list[str]) -> str:
    i = 0
    while i < len(lines) and (
        lines[i].startswith("package")
        or lines[i].strip() == ""
        or lines[i].startswith("import")
    ):
        i += 1
    return "\n".join(lines[i:])


def strip_unused_from_text(text: str) -> tuple[str, int]:
    raw_lines = text.splitlines()
    body = body_after_imports(raw_lines)
    kept: list[str] = []
    removed = 0
    for raw in raw_lines:
        if raw.rstrip().endswith(".*"):
            kept.append(raw)
            continue
        m = IMPORT_RE.match(raw)
        if not m:
            kept.append(raw)
            continue
        fq = m.group(1)
        alias = m.group(2)
        name = alias or fq.split(".")[-1]
        if name in _PRESERVE_SIMPLE_NAMES:
            kept.append(raw)
            continue
        if re.search(r"\b" + re.escape(name) + r"\b", body):
            kept.append(raw)
        else:
            removed += 1
    new_text = "\n".join(kept)
    if text.endswith("\n"):
        new_text += "\n"
    return new_text, removed


def main() -> int:
    if len(sys.argv) < 2:
        print("Usage: strip_unused_imports.py <file.kt> [...]", file=sys.stderr)
        return 2
    total = 0
    for arg in sys.argv[1:]:
        p = Path(arg)
        text = p.read_text(encoding="utf-8-sig")
        new_text, n = strip_unused_from_text(text)
        if n:
            p.write_text(new_text, encoding="utf-8", newline="\n")
            print(f"{p}: removed {n} import(s)")
            total += n
        else:
            print(f"{p}: no unused imports")
    print(f"Total removed: {total}")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
