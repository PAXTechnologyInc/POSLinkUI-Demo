# 研究记录：008 Sonar 模块化治理

**功能**：`008-sonar-module-remediation`  
**日期**：2026-04-08

## 1. Sonar 工程标识与实例

- **决策**：以仓库根目录 `sonar-project.properties` 为权威配置：`sonar.host.url=http://172.16.3.60:9090`，`sonar.projectKey=POSLinkUI-Demo`。
- **理由**：与部门实例及 CI/扫描器一致，MCP 与 orchestrator 文档均指向同一 host。
- **备选**：本地仅 SonarLint 不落服务器——可作为断网 fallback，但**复查结论**以服务器 OPEN issue 为准（与 spec 中「复查」一致）。

## 2. 质量门禁口径

- **决策**：以 `.cursor/rules/sonarqube-quality-gate.mdc` 所述 **PAX Sonar way** 为审查依据；新增代码零 BLOCKER / CRITICAL / MAJOR（及门禁中的 new code 指标）。
- **理由**：与组织门禁及 orchestrator 代理说明一致，避免「修了但不达标」。
- **备选**：仅清 Blocker——**拒绝**，不满足 spec FR-005。

## 3. 「模块」与批次提交

- **决策**：**模块** = Gradle `include` 子项目；当前 `settings.gradle.kts` 仅有 `:app`，故首个里程碑为 **整 app 一轮**，但仍按 **文件** 做审查/修复/复查；提交可按「完成 app 内约定文件列表」或「单模块一次提交」组织，须满足 spec「每模块至少一次可追溯提交」。
- **理由**：与 spec FR-001、FR-004 一致且贴合仓库现状。
- **备选**：按源码子包拆分多次提交——在单模块下可作为 **补充** 策略写入 `tasks.md`，但不替代「模块级」提交要求。

## 4. 审查与修复角色分工

- **决策**：**android-sonarlint-quality-orchestrator** 负责 MCP 拉取事实、出审查报告与交接单、修复后复查；**sonarqube-minimal-fixer** 负责按交接单做最小代码改动。
- **理由**：与 `.cursor/agents/` 文档及 spec Assumptions 一致。
- **备选**：单人全流程——可行，但须沿用同一交接单与复查步骤，保证可审计。

## 5. 构建与归档门禁

- **决策**：模块（或批次）收尾执行 `./gradlew :app:assembleDebug` 与 `./gradlew :app:testDebugUnitTest`；任务状态以 `specs/008-sonar-module-remediation/tasks.md` 勾选为准。
- **理由**：对齐 `.specify/memory/constitution.md` 构建验证与 `task-completion-archive` 技能。
- **备选**：仅 assemble 不测——**不推荐**，与宪章测试倾向不一致。

## 结论

无未解决的 `NEEDS CLARIFICATION`；可进入 Phase 1 设计与契约固化。
