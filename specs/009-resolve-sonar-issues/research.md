# 研究记录：009 扫描驱动 Sonar 任务包治理

**功能**：`009-resolve-sonar-issues`  
**日期**：2026-04-10

## 1. Sonar 工程与 Baseline

- **决策**：与008 相同，以 `sonar-project.properties` 为权威：`sonar.host.url=http://172.16.3.60:9090`，`sonar.projectKey=POSLinkUI-Demo`。本功能启动时在 `tasks.md`（或计划附录）**书面记录**一次 **ScanBaseline**：分支/commit、UTC/本地时间、**OPEN** 总数及严重度分布（来源：MCP `search_sonar_issues_in_projects` 的 `paging.total` + 可选分严重度查询）。
- **理由**：满足 spec FR-001、SC-002「相对 Baseline 单调下降」的可验证性。
- **备选**：仅以「当前感觉」为基线——**拒绝**，不可度量。

## 2. 质量门禁口径

- **决策**：与008 一致，以 **PAX Sonar way**（`.cursor/rules/sonarqube-quality-gate.mdc`）为修复侧约束；任务包关闭时新增/改动代码不得新引入 BLOCKER / CRITICAL / MAJOR。
- **理由**：与组织门禁及008 计划一致。
- **备选**：仅清 INFO——**拒绝**，不满足 spec 与宪章 VIII。

## 3. 任务包（Txxx）切片策略

- **决策**：默认优先 **严重度降序**（BLOCKER → … → INFO）；同一文件多条 CRITICAL/MAJOR 时可合并为同一包；单文件 issue 数过多时再按 **规则键前缀** 拆包。每包须在 `tasks.md` 写明 **DoD**与 **验证命令**。
- **理由**：平衡「可评审 diff大小」与「闭环次数」。
- **备选**：纯字母路径排序——可作辅助排序键，不作为唯一策略。

## 4. 与 008 的关系

- **决策**：008 的 **ModuleBatch / 模块级提交** 仍适用；009 的 **Txxx** 是更细的扫描驱动切片。若两规格同时活跃，**同一文件**不得在两个 `tasks.md` 中无协调地并行派工；以先写入或负责人指定为准，另一处引用「见 009-T0xx」或「见 008-T0yy」。
- **理由**：满足 spec FR-007。
- **备选**：完全合并两规格——**本期不采纳**，避免改写已冻结的 008 编号。

## 5. 构建与归档

- **决策**：每个 `Txxx` 标完成前执行 `:app:assembleDebug` 与 `:app:testDebugUnitTest`（与宪章及 android-post-change-build 技能一致）；需要时再跑 `lintDebug`。
- **理由**：spec FR-004、SC-003。
- **备选**：仅编译——**不推荐**。

## 结论

无未解决的 `NEEDS CLARIFICATION`；可进入 Phase 1 设计与契约固化。
