---
description: 根据交互或用户提供的原则输入创建或更新项目宪章，并确保相关模板保持同步。
handoffs: 
  - label: 编写功能规格
    agent: speckit.specify
    prompt: 根据更新后的宪章编写功能规格。我要实现……
---

## 用户输入

```text
$ARGUMENTS
```

在继续之前，你**必须**考虑用户输入（若非空）。

## 概要

你正在更新位于 `.specify/memory/constitution.md` 的项目宪章。该文件为**模板**，含方括号占位符（如 `[PROJECT_NAME]`、`[PRINCIPLE_1_NAME]`）。你的工作是：（a）收集/推导具体值，（b）精确填充模板，（c）将修订传播到依赖产物。

**说明**：若 `.specify/memory/constitution.md` 尚不存在，应在项目搭建时从 `.specify/templates/constitution-template.md` 初始化。若缺失，请先复制模板。

按以下流程执行：

1. 加载 `.specify/memory/constitution.md` 现有内容。
   - 识别形如 `[ALL_CAPS_IDENTIFIER]` 的每个占位符。
   **重要**：用户所需原则数量可能少于或多于模板默认。若指定了数量，须遵守——按通用模板结构调整文档。

2. 收集/推导占位符取值：
   - 若用户输入（对话）提供值，则采用。
   - 否则从仓库上下文推断（README、文档、若嵌入则从前版宪章）。
   - 治理日期：`RATIFICATION_DATE` 为最初通过日期（未知则询问或标 TODO）；若有变更则 `LAST_AMENDED_DATE` 为今日，否则保留原值。
   - `CONSTITUTION_VERSION` 须按语义化版本规则递增：
     - MAJOR：治理/原则删除或重新定义且不向后兼容。
     - MINOR：新增原则/章节或实质性扩展指导。
     - PATCH：澄清、措辞、错别字、非语义微调。
   - 若版本 bump 类型模糊，在定稿前说明理由。

3. 起草更新后的宪章正文：
   - 用具体文本替换每个占位符（除项目明确保留的模板槽位外不得留括号标记——须说明保留理由）。
   - 保持标题层级；注释在替换后可删，除非仍有助于理解。
   - 每个原则节：简短名称行、段落（或要点）列出不可协商规则、若非显而易见则写明依据。
   - 治理节须列出修订程序、版本策略、合规评审期望。

4. 一致性传播检查清单（将原清单转为可执行校验）：
   - 阅读 `.specify/templates/plan-template.md`，确保「Constitution Check」或规则与更新后的原则一致。
   - 阅读 `.specify/templates/spec-template.md`，核对范围/需求对齐——若宪章增删强制章节或约束则更新。
   - 阅读 `.specify/templates/tasks-template.md`，确保任务分类反映新增或删除的原则驱动任务类型（如可观测性、版本、测试纪律）。
   - 阅读 `.specify/templates/commands/*.md` 下各命令文件（含本文件），确认无过时引用（在需要通用指导时不应残留仅针对某 agent 的名称如 CLAUDE）。
   - 阅读运行时指导文档（如 `README.md`、`docs/quickstart.md` 或若存在的 agent 专用指导）。原则变更时更新引用。

5. 生成**同步影响报告**（更新后置于宪章文件顶部的 HTML 注释中）：
   - 版本变更：旧 → 新
   - 修改的原则列表（若重命名则旧标题 → 新标题）
   - 新增章节
   - 删除章节
   - 需更新的模板（✅ 已更新 / ⚠ 待处理）及文件路径
   - 若有故意推迟的占位符，列出后续 TODO

6. 最终输出前校验：
   - 无未解释的残留括号占位符。
   - 版本行与报告一致。
   - 日期为 ISO 格式 YYYY-MM-DD。
   - 原则为陈述式、可测试，避免模糊措辞（适当将 "should" 换为 MUST/SHOULD 并说明理由）。

7. 将完成的宪章写回 `.specify/memory/constitution.md`（覆盖）。

8. 向用户输出最终摘要：
   - 新版本与 bump 理由。
   - 标为需人工跟进的所有文件。
   - 建议的提交说明（例如 `docs: amend constitution to vX.Y.Z (principle additions + governance update)`）。

**格式与风格要求**：

- Markdown 标题层级与模板完全一致（勿升降级）。
- 长理由行可适当换行以利阅读（理想 <100 字符）但勿为硬性换行破坏语句。
- 节与节之间保留单个空行。
- 勿留行尾空格。

若用户仅提供部分更新（例如只改一条原则），仍须执行校验与版本决策步骤。

若关键信息缺失（例如通过日期确实未知），插入 `TODO(<FIELD_NAME>): explanation` 并在同步影响报告的推迟项下列出。

勿新建模板；始终操作现有 `.specify/memory/constitution.md` 文件。
