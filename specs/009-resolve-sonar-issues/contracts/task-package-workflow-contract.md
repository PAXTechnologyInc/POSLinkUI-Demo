# 契约：009 任务包（Txxx）定义与验收

**功能**：`009-resolve-sonar-issues`  
**角色**：审查编排（Reviewer） / 最小改动修复（Fixer） / 负责人（Owner）

## 目的

在单文件交接（见 [008 remediation-workflow-contract.md](../../008-sonar-module-remediation/contracts/remediation-workflow-contract.md)）之上，约定 **一个 `Txxx` 任务包** 的最小信息集合，使执行方知道「包边界、相对何 baseline 验收、如何算完成」。

## 任务包开工条件（Owner 须在 tasks.md 或交接中提供）

1. **Txxx 编号**：如 `T003`。
2. **ScanBaseline 引用**：本包验收所参照的基线记录位置（`tasks.md` 顶部表格或明确日期+总数）。
3. **范围（scope）**：仓库相对路径列表、目录前缀，或规则键前缀；避免「整个 app」无界包，除非已说明为「仅 INFO 清扫」等且可评审。
4. **目标（target）**：例如「范围内 OPEN 清零」或「关闭全部 CRITICAL」或「文件 F 上 rule X 清零」。
5. **完成定义（DoD）**：
   - 平台：对 scope 内复查无未处理 OPEN，或 **waived** 已登记；
   - 本地：`:app:assembleDebug` 与 `:app:testDebugUnitTest` 相对合并基线无新增失败。
6. **008 协调**：`linked008Task` 填具体编号或 `无`。

## 审查编排输出

- 任务包内各文件的 **待处理条目摘要**（可附 MCP 导出或规则列表）。
- 与008 一致的 **禁止事项**（TLS、密钥、`@Suppress` 滥用等）。

## 修复方输出

- 限定于 scope 的代码变更与必要 KDoc。
- 若无法在不改行为下修复，**停工并升级** Owner（spec：用户故事 2）。

## 验收输出（Owner / Reviewer）

- **结论**：`DONE` / `NOT DONE`
- **依据**：Baseline 对比（条数或文件级 OPEN）；Gradle 结果；waived 链接（如有）。

## 与 spec 的对应关系

- **FR-002**：本契约即 `Txxx` 的可测试「容器」。
- **SC-001、SC-002**：Done 的任务须同时满足本契约 DoD 与 Baseline 演进记录。

## 版本

- **v1**：2026-04-10，随 009 plan Phase 1 建立。
