# Quickstart：008 Sonar 按模块 / 按文件闭环

**分支**：`008-sonar-module-remediation`  
**规格**：[spec.md](spec.md) | **计划**：[plan.md](plan.md) | **交接契约**：[contracts/remediation-workflow-contract.md](contracts/remediation-workflow-contract.md)

## 前置条件

- 已检出 `008-sonar-module-remediation`（或与该 spec 同前缀的分支）。
- 可访问部门 Sonar `http://172.16.3.60:9090`（或已配置 **user-SonarQube** MCP 指向该实例）。
- 已阅读 `.cursor/agents/android-sonarlint-quality-orchestrator.md` 与 `.cursor/agents/sonarqube-minimal-fixer.md`。

## 单文件标准闭环（推荐）

1. **审查编排**：对仓库相对路径调用 orchestrator 能力（MCP：`search_sonar_issues_in_projects` 等，**先读工具 schema**）。保存输出中的「结论 + 条目列表」。
2. **交接**：若有待处理项，按 [remediation-workflow-contract.md](contracts/remediation-workflow-contract.md) 生成交接单。
3. **修复**：将交接单交给 **sonarqube-minimal-fixer**（或同等人工），只做最小必要改动。
4. **触发分析**（如需要）：以团队惯例触发 Sonar 分析并等待完成。
5. **复查**：同一文件再次走 orchestrator / MCP，直到无 OPEN 或已 **waived** 登记。
6. **归档**：在 `specs/008-sonar-module-remediation/tasks.md` 勾选对应 `Txxx`（见 task-completion-archive 技能）。

## 模块收尾（当前即 `:app`）

1. 运行：

   ```bash
   ./gradlew :app:assembleDebug :app:testDebugUnitTest
   ```

   （Windows 可用 `gradlew.bat`。）

2. 全部通过后，执行 **模块级** `git commit`（提交说明包含 `app` 或 Sonar 摘要）。
3. 在 `tasks.md` 勾选模块级或批次级任务。

## 项目键与路径

- `sonar.projectKey`：`POSLinkUI-Demo`（见 `sonar-project.properties`）。
- 文件路径一律用 **仓库相对路径**，与 Git 一致。

## 下一步

- 运行 **`/speckit.tasks`** 生成 `tasks.md` 中的模块顺序与文件/批次任务列表。
- 大范围修复收尾可再执行 `.cursor/skills/android-post-change-build/SKILL.md` 中的完整门禁。
