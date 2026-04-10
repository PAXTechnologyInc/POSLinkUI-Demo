# 数据模型：009 扫描基线与任务包

本文档将 [spec.md](spec.md) 中 **ScanBaseline**、**TaskPackage（Txxx）**、**VerificationRecord** 落实为可填写字段；**ModuleBatch / FileFinding / RemediationHandoff** 与008 一致，详见 [../008-sonar-module-remediation/data-model.md](../008-sonar-module-remediation/data-model.md)。

## ScanBaseline（扫描基线）

| 字段 | 类型 | 说明 |
|------|------|------|
| `capturedAt` | string | ISO 或可读时间戳 |
| `gitBranch` | string | 如 `009-resolve-sonar-issues` |
| `gitRevision` | string | commit SHA（短或全） |
| `projectKey` | string | 默认 `POSLinkUI-Demo` |
| `issueStatusFilter` | string | 默认 `OPEN`（若含 CONFIRMED 须写明） |
| `openTotal` | int | 平台返回的 OPEN 总数 |
| `severityCounts` | map |可选：`BLOCKER`、`CRITICAL`、`MAJOR`、`MINOR`、`INFO`（若未分项则记「未分项」） |
| `source` | string | 如 `MCP search_sonar_issues_in_projects` 或 Web UI 导出标识 |

**校验规则**：`openTotal` 须与当时查询口径一致；后续 SC-002 以此行或 `tasks.md` 顶部表格为基准。

## TaskPackage（Txxx）

在008 **TaskLineItem** 基础上扩展：

| 字段 | 类型 | 说明 |
|------|------|------|
| `id` | string | 如 `T001` |
| `done` | boolean | `- [ ]` / `- [x]` |
| `scopeDescription` | string | 路径 glob、文件列表或规则键前缀 |
| `baselineRef` | string | 指向 `ScanBaseline.capturedAt` 或「tasks顶部 Baseline 块」 |
| `targetOutcome` | string | 如「本包内文件 OPEN 清零」或「关闭 N 条 MAJOR」 |
| `definitionOfDone` | string | 平台复查条件 + 本地门禁命令 |
| `linked008Task` | string? | 若与 008 模块批次对齐，填 `008-Txxx` 或「无」 |
| `waivers` | list? | 本包登记的例外（规则、原因、共识） |

**状态迁移**：仅当 DoD 满足且 VerificationRecord 齐备后 `done = true`。

## VerificationRecord（验证记录）

| 字段 | 类型 | 说明 |
|------|------|------|
| `taskId` | string | `Txxx` |
| `executedCommands` | string | 如 `assembleDebug`、`testDebugUnitTest`（实际执行的 Gradle 任务名） |
| `result` | enum | `pass` \| `fail` |
| `sonarRecheckSummary` | string | 可选：复查时间、OPEN 条数或「已 waived」引用 |
| `notes` | string | 失败原因、flake 说明等 |

## 关系（摘要）

```text
ScanBaseline1 ——< TaskPackage (many)
TaskPackage 1 ——1..* FileFinding（逻辑上；可仅存于交接单）
TaskPackage 1 —— 1 VerificationRecord（完成时至少一条）
```
