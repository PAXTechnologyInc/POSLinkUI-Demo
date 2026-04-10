---
description: 根据现有设计产物，将任务转换为可执行、按依赖排序的 GitHub Issue。
tools: ['github/github-mcp-server/issue_write']
---

## 用户输入

```text
$ARGUMENTS
```

在继续之前，你**必须**考虑用户输入（若非空）。

## 概要

1. 在仓库根目录运行 `.specify/scripts/bash/check-prerequisites.sh --json --require-tasks --include-tasks`，解析 FEATURE_DIR 与 AVAILABLE_DOCS。所有路径须为绝对路径。参数中含单引号（如 "I'm Groot"）时使用转义：例如 `'I'\''m Groot'`（或尽量用双引号：`"I'm Groot"`）。
2. 从已执行脚本的结果中提取 **tasks** 的路径。
3. 通过以下命令获取 Git 远程地址：

```bash
git config --get remote.origin.url
```

> [!CAUTION]
> **仅当** remote 为 GitHub URL 时才继续后续步骤

4. 对列表中的每个任务，使用 GitHub MCP 在**与 Git remote 所代表仓库一致**的仓库中创建新 Issue。

> [!CAUTION]
> **在任何情况下**都不得在**与 remote URL 不匹配**的仓库中创建 Issue
