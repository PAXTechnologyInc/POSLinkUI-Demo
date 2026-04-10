---
name: task-completion-archive
description: 在 specs/*/tasks.md 中将 SpecKit 任务标为已完成并记录收尾信息，便于后续会话或其它 agent 看到进度。在单项任务完成、用户要求归档或勾选、或完成 Txxx 后交接时使用。
---

# 任务完成归档

## 何时使用

- 某条**具体任务**的实现（及规格要求的测试）已成功完成后**立即**执行。
- 用户表示任务已做完、需要归档，或需要让**下一个 agent** 知道该条已完成时。

## 唯一事实来源

- **以仓库为准**：对应 feature 的 `specs/<feature-slug>/tasks.md`（复选框 `- [ ]` / `- [x]`，任务编号如 `T012`）。
- **不够**：仅依赖 Cursor 内置 Todo 或对话摘要——必须把状态写进 `tasks.md`。

## 操作步骤

1. **定位 feature 目录**  
   - 若用户或上下文已给出（例如 `005-kotlin-task-management`），使用 `specs/<name>/tasks.md`。  
   - 否则在仓库中搜索任务编号（如 `T012`）或任务描述中的唯一短语，定位 `specs/**/tasks.md`。

2. **更新对应行**  
   - 仅将已完成项的 `- [ ]` 改为 `- [x]`。  
   - **不要**改写任务正文，除非修正笔误；保留同一 `Txxx` 编号与原有描述。

3. **若无对应复选框**（临时工作 or 清单漏项）  
   - 在同一 feature 的 `tasks.md` **末尾**增加小节（若尚不存在）：

```markdown
## 完成记录（临时）

- [x] YYYY-MM-DD — 用中文简述交付内容（可附路径或 PR）。
```

   - 日期优先用用户环境当日；若无法取得，可用相对说明。

4. **交接说明（可选）**  
   - 用一句话写明更新了哪个 `tasks.md`、完成了哪条 `Txxx`（或完成记录中的哪一条），便于下一任检索。

## 禁止

- 在未满足该条验收条件（构建、规格约定测试等）前标为完成。
- 删除或批量改写无关任务。
- 在 feature 使用 SpecKit 任务清单时，仅在聊天中说「做完了」而不改 `tasks.md`。

## 示例

修改前：

```markdown
- [ ] T015 [US2] Refactor `app/.../Foo.kt` for bar behavior
```

修改后：

```markdown
- [x] T015 [US2] Refactor `app/.../Foo.kt` for bar behavior
```
