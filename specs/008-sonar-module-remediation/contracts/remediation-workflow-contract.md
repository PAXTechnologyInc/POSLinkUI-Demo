# 契约：Sonar 审查—修复—复查交接

**功能**：`008-sonar-module-remediation`  
**角色**：审查编排（Reviewer） / 最小改动修复（Fixer）

## 目的

在 **不规定具体 MCP 工具名** 的前提下，约定双方交换的 **最小信息集合**，使修复方可独立动工、审查方可客观复查。

## 输入

- **目标文件**：仓库相对路径（例：`app/src/main/java/com/paxus/pay/poslinkui/demo/Foo.kt`）。
- **Sonar 工程**：`sonar.projectKey`（默认 `POSLinkUI-Demo`）与组件/路径口径与服务器一致。

## 交接单（Fixer 的开工条件）

审查编排方必须提供以下块（可复制为 Markdown 或纯文本）：

1. **文件**：`<repoRelativePath>`
2. **结论摘要**：无 OPEN / N 条待处理（含严重度分布可选）
3. **待办条目列表**（每条）：`[severity] <ruleKey> L<line?> — <message 摘要>`
4. **规则依据**：指向 `show_rule` 要点或 `.cursor/rules/sonarqube-quality-gate.mdc` 中的相关约束
5. **禁止事项**（示例）：不得弱化 TLS/证书校验；不得硬编码密钥；不得以无业务理由的 `@Suppress` 规避
6. **完成定义（DoD）**：修复后审查方用同一 Sonar 工程对该文件复查，OPEN 为 0；或已提供 **waived** 登记（规则键、原因、范围、共识）

## 修复方输出

- **代码变更**：限定于交接单范围；必要时应附带 **KDoc**（public API）以满足注释门禁。
- **说明**：简述每条 issue 的修复方式（一两句即可），便于审查。

## 复查输出

审查编排方必须给出：

- **结论**：`PASS` / `FAIL`
- **依据**：MCP 或 UI 上该文件 issue 列表摘要；若 `FAIL`，列出仍 OPEN 的 ruleKey + 说明

## 与 spec 的对应关系

- 对应 **FR-002、FR-003** 的「可测试」闭环：任何 `PASS` 必须可指向一次复查输出；**waived** 必须可指向登记文本。

## 版本

- **v1**：2026-04-08，随 008 plan Phase 1 建立。
