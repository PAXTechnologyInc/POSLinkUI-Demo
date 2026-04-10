---
name: auto-commit-after-test-author
description: 在 POSLinkUI-Demo 中，当一次 action 相关改动已完成且 test-author 验证通过后，自动执行一次规范化 git commit。用于“改完 action 并测试通过就提交”的交付节奏。
---

# Action 改动后自动提交

## 何时使用

- 用户希望采用“完成一个 action -> 测试通过 -> 立即提交”的节奏。
- 当前改动包含 Entry action/category 或其参数解析、路由、Manifest 映射等相关代码。
- `@.cursor/agents/test-author.md` 定义的验证流程已通过（例如 `:app:testDebugUnitTest` 通过，或约定脚本通过）。

## 触发判定（必须同时满足）

1. **action 改动已完成**
   - 相关实现文件已落盘，不再处于“待继续修改”的中间态。
2. **test-author 验证通过**
   - 已有明确的“通过”证据：成功的测试结果、报告结论、或主对话确认通过。
3. **工作区可提交**
   - 仅提交与本次 action 任务相关的文件；不混入明显无关改动。

## 自动提交流程

1. 读取状态：`git status --short`
2. 检查变更：`git diff --staged && git diff`
3. 检查最近提交风格：`git log -5 --oneline`
4. 暂存本次 action 相关文件：`git add <files...>`
5. 生成并执行 commit（使用 HEREDOC 传入消息）
6. 提交后确认：`git status`

## Commit message 规范

- 类型优先：`feat` / `fix` / `refactor` / `test`
- 第一行聚焦“为什么要改”而非仅“改了什么”。
- **必须包含页面标识信息**：至少写出本次页面对应的 `action/category`（必要时补充页面名或 route）。
- 建议格式：

```text
<type>(entry-action): <简短目的> [<action> | <category> | <page/route>]

Align <action/category> handling with validated test-author scenarios to avoid runtime routing or extras parsing regressions.
```

### 页面信息示例

```text
fix(entry-action): align amount page visual with expected result [com.pax.us.pay.action.ENTER_AMOUNT | com.pax.us.pay.ui.category.TEXT | AmountScreen]

Keep amount entry layout and input field behavior consistent with validated screenshot-based checks on target device profile.
```

## 本项目建议映射

- 新增 action 能力：`feat(entry-action): ...`
- 修复 action/category 错配或 extras 解析问题：`fix(entry-action): ...`
- 仅重构路由实现且行为不变：`refactor(entry-action): ...`
- 同步补测试：`test(entry-action): ...`

## 安全与边界

- 不提交敏感文件（如密钥、凭据、`.env`）。
- 不使用破坏性 git 命令（如 `reset --hard`、`push --force`）。
- 未通过 test-author 验证前，禁止执行自动提交。
- 若存在冲突或无法判断是否“通过”，先停止自动提交并向用户确认。

## 交付输出

自动提交完成后，输出以下信息：

- 本次 action 标识（action/category）
- 本次页面标识（page/screen/route）
- 通过的验证项（测试命令或报告结论）
- commit hash 与提交标题
- 是否存在未提交残留文件
