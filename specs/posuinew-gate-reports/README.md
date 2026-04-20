---
summary: "存放 POSUInew SSIM 门禁跑批后由测试 Skill 生成的 Markdown 缺陷单与汇总，供开发 Skill 读取修复。"
read_when:
  - 需要查看或编写基于截图门禁的 Bug 报告
  - 从 ui-parity-golive-bugfix-from-report 反查报告存放约定
title: "POSUInew 门禁报告目录说明"
---

# POSUInew 门禁报告（Markdown）

## 用途

本目录存放 **`posuinew-ssim-gate-bug-report`** Skill 在 pytest/HTML 报告解析后生成的文档：

- **`gate-run-summary.md`**：单次跑批元数据、通过/失败统计、HTML 报告路径。
- **`BUG-*.md`**：单条失败用例的结构化缺陷单（含 SSIM、分类、Actual/Expected 路径）。

**开发侧**由 **`ui-parity-golive-bugfix-from-report`** Skill 读取此处 `.md`，按门禁与分类做 golive 对齐修复。

## 目录约定

建议按日期分子目录，避免覆盖：

`specs/posuinew-gate-reports/<YYYY-MM-DD>/`

每条 **`BUG-*.md`** 宜含同级 **`assets/<slug>/`** 目录，存放从 POSUInew **复制**的 **actual.png / expected.png**，并在 Markdown 内用相对路径嵌入，便于开发与 Agent **直接看图**并对照 **SSIM 数值**（见 `_template-bug-report.md`）。

## 模板

单条缺陷见同目录 **`_template-bug-report.md`**。
