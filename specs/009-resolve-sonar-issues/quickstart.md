# Quickstart：009 扫描基线与任务包闭环

**分支**：`009-resolve-sonar-issues`  
**规格**：[spec.md](spec.md) | **计划**：[plan.md](plan.md) | **任务包契约**：[contracts/task-package-workflow-contract.md](contracts/task-package-workflow-contract.md)

## 前置条件

- 已检出 `009-resolve-sonar-issues`（或与 `009-*` 同前缀且共用本 `specs/009-resolve-sonar-issues/` 的分支）。
- 可访问 Sonar `http://172.16.3.60:9090` 或已配置 **user-SonarQube** MCP。
- 已阅读 [../008-sonar-module-remediation/quickstart.md](../008-sonar-module-remediation/quickstart.md) 中的单文件闭环；009 在其上增加 **Baseline** 与 **Txxx** 约定。

## 第一步：记录 ScanBaseline

1. 确认当前分支与 `git rev-parse HEAD`。
2. 使用 MCP（**先读工具 schema**）或 Web UI查询 `projects=["POSLinkUI-Demo"]`、`issueStatuses=["OPEN"]`，记录 `paging.total` 与（可选）各严重度计数。
3. 将结果写入 **`tasks.md` 顶部**（由 `/speckit.tasks` 生成时首段插入），或写入本目录 `plan.md` 附录（仍以 `tasks.md` 便于执行者第一眼看到为佳）。

## 第二步：定义 Txxx 任务包

1. 按 [plan.md](plan.md) Phase 2 优先序切片。
2. 每个 `Txxx` 填写 [contracts/task-package-workflow-contract.md](contracts/task-package-workflow-contract.md) 中的最小字段块。
3. 若 [../008-sonar-module-remediation/tasks.md](../008-sonar-module-remediation/tasks.md) 已覆盖同一文件，在本任务中 **引用 008 任务编号** 或调整范围，避免重复。

## 第三步：执行单包闭环

1. **审查编排**：对任务包 `scopeDescription` 拉取 OPEN列表，生成交接单（可逐文件复用 008 的交接单格式）。
2. **修复**：最小改动；public API 补 KDoc 以满足注释门禁。
3. **复查**：同一范围再次查询，直至 OPEN 清零或 **waived** 已登记。
4. **门禁**：`./gradlew :app:assembleDebug :app:testDebugUnitTest`（Windows：`gradlew.bat`）。
5. **归档**：勾选 `Txxx`，并写一句 VerificationRecord 摘要（见 [data-model.md](data-model.md)）。

## 下一步

- 运行 **`/speckit.tasks`** 生成带 Baseline 表头的 `tasks.md` 与 `Txxx` 列表。
