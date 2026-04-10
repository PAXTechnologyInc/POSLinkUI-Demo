# 数据模型：Entry Action 覆盖补齐

## 实体 1：ActionCoverageItem

表示单个动作在本次需求中的覆盖状态。

| 字段 | 类型 | 说明 |
|------|------|------|
| category | string | 动作所属分类（Text/Option/Confirmation/Information） |
| actionId | string | 完整 action 常量值 |
| baselineGroup | string | `baseline` 或 `incremental` |
| routable | boolean | 是否可被入口识别并进入对应交互 |
| submittable | boolean | 是否可完成用户提交 |
| writable | boolean | 提交结果是否可回写 |
| manualVerified | boolean | 是否完成人工验收 |
| autoCovered | boolean | 是否纳入自动化回归 |
| note | string | 补充说明（失败原因、限制、依赖） |

## 实体 2：CategoryCoverageSummary

按 category 汇总覆盖结果，用于验收报告。

| 字段 | 类型 | 说明 |
|------|------|------|
| category | string | 分类名 |
| totalInDoc | int | 文档定义动作总数 |
| implemented | int | 已实现动作数 |
| missing | int | 未实现动作数 |
| baselineMissingResolved | int | 本期基线缺口已补齐数 |
| incrementalFound | int | 本期新发现增量缺口数 |
| incrementalResolved | int | 本期增量缺口已补齐数 |

## 实体 3：EntrySubmitResult

代表一次动作交互的提交结果。

| 字段 | 类型 | 说明 |
|------|------|------|
| actionId | string | 触发动作 |
| accepted | boolean | 用户是否确认提交 |
| payloadPresence | string | `none`/`text`/`option`/`confirm` |
| writeBackStatus | string | `success`/`failed` |
| errorCode | string | 失败时错误码（可空） |

## 状态迁移

`defined -> routable -> submittable -> writable -> verified`

- 任一步失败都进入 `blocked`，需记录 `note` 并回归修复。
- `verified` 必须满足：人工验收通过；且若该 category 尚无自动化代表动作，则当前动作需进入自动化覆盖。
