# 数据模型：Sonar 治理闭环（008）

本文档将 [spec.md](spec.md) 中的关键实体落实为可填写、可追踪的字段约定，供 `tasks.md`、交接单与提交说明引用。

## ModuleBatch（模块批次）

| 字段 | 类型 | 说明 |
|------|------|------|
| `modulePath` | string | Gradle 路径，如 `:app` |
| `displayName` | string | 可读名称，与 `modulePath` 一致或同义 |
| `plannedFiles` | list\<string\> | 本批次计划处理的仓库相对路径（可选，可从 tasks 推导） |
| `status` | enum | `pending` \| `in_progress` \| `done` |
| `commitRefs` | list\<string\> | 完成本模块后的 commit SHA 或 `main..HEAD` 范围说明 |
| `notes` | string | 例外、阻塞、跳过的文件及原因 |

**关系**：包含多个 `FileFinding` / `RemediationHandoff` 记录（可仅存于聊天或 issue 中，但 **tasks.md** 须能映射回模块）。

## FileFinding（文件级发现）

| 字段 | 类型 | 说明 |
|------|------|------|
| `repoRelativePath` | string | 如 `app/src/main/java/.../Foo.kt` |
| `modulePath` | string | 所属模块 `:app` |
| `issues` | list\<IssueItem\> | 见下表 |
| `fileStatus` | enum | `open` \| `fixed_pending_verify` \| `closed` \| `waived` |

### IssueItem（嵌套）

| 字段 | 类型 | 说明 |
|------|------|------|
| `ruleKey` | string | 如 `kotlin:S3776` |
| `severity` | string | Sonar 严重度 |
| `message` | string | 摘要 |
| `line` | int? | 起始行（若有） |
| `sonarIssueKey` | string? | 服务器 issue key（若 MCP 返回） |

**校验规则**：`waived` 必须附带登记项（规则、原因、批准人/日期中的至少一项书面记录）。

## RemediationHandoff（交接单）

| 字段 | 类型 | 说明 |
|------|------|------|
| `targetFile` | string | 仓库相对路径 |
| `findingsSummary` | string | 待处理项概述或引用 orchestrator 报告章节 |
| `constraints` | string | 禁止事项（如禁止关证书校验、禁止压认知复杂度糊弄） |
| `definitionOfDone` | string | 如「MCP 无 OPEN」「或已登记 waived」 |
| `createdAt` | string | ISO 日期或会话标识 |

**关系**：由审查编排方创建，修复方消费；复查后由审查编排方更新 `FileFinding.fileStatus`。

## TaskLineItem（tasks.md 行）

| 字段 | 类型 | 说明 |
|------|------|------|
| `id` | string | 如 `T012` |
| `done` | boolean | 对应 `- [ ]` / `- [x]` |
| `modulePath` | string? | 可选标注 |
| `fileOrScope` | string? | 单文件路径或「批次描述」 |
| `links` | string? | US、spec FR 引用（可选） |

**状态迁移**：仅当 **定义完成**（DoD）满足后才可将 `done` 置为 true（见 task-completion-archive 技能）。

## 状态迁移（摘要）

```text
FileFinding: open → fixed_pending_verify（修复提交后）
            → closed（复查通过或 waived 已登记）
            → open（复查仍失败，重新交接）

ModuleBatch: pending → in_progress → done（批次内全部目标文件达 closed 或 waived，且已模块级提交）
```
