# Spec-Kit 使用步骤

本文档指导你使用 Spec-Kit 完成 POSLinkUI-Demo 的两个迁移需求：**Java→Kotlin**（含 Idea 转换后的代码审查）和 **UI→Compose**。

**设计文档说明**：`POSLinkUI-Design_V1.03.00.docx` 无法被 AI 直接解析，以该设计为 UI 规范来源，具体布局、字体、颜色等数值需从文档中手工提取。

---

## 1. 前置条件

### 1.1 分支命名

- 当前分支需为 `NNN-feature-name` 格式（如 `001-kotlin-migration`、`002-compose-migration`）
- 脚本通过分支名自动定位 `specs/NNN-feature-name/` 目录

### 1.2 脚本依赖

- `.specify/scripts/bash/create-new-feature.sh`：创建新 feature 分支与 spec 目录
- `.specify/scripts/bash/check-prerequisites.sh`：检查 plan、tasks 等前置文件
- `.specify/scripts/bash/setup-plan.sh`：初始化 plan 模板

### 1.3 运行环境

- **Windows**：使用 Git Bash 或 WSL，从仓库根目录执行脚本
- **macOS/Linux**：直接使用 bash

---

## 2. Spec-Kit 命令速查

| 阶段 | 命令 | 用途 | 触发方式 | 输出 |
|------|------|------|----------|------|
| 需求 | `/speckit.specify` | 从自然语言描述生成 feature spec | `/speckit.specify 将 Java 转为 Kotlin...` | `specs/NNN-xxx/spec.md`、`checklists/requirements.md` |
| 需求 | `/speckit.clarify` | 澄清 spec 中的模糊点（最多 5 个问题） | `/speckit.clarify` | 更新 `spec.md`，补充 Clarifications 节 |
| 设计 | `/speckit.plan` | 生成技术方案与设计产物 | `/speckit.plan` | `plan.md`、`research.md`、`data-model.md`、`contracts/`、`quickstart.md` |
| 设计 | `/speckit.checklist` | 生成领域检查清单（如 ux、security） | `/speckit.checklist 生成代码审查清单` | `checklists/ux.md` 等 |
| 执行 | `/speckit.tasks` | 将 plan 拆解为可执行任务 | `/speckit.tasks` | `tasks.md` |
| 执行 | `/speckit.implement` | 按 tasks 逐项执行实现 | `/speckit.implement` | 代码修改、任务勾选 |
| 可选 | `/speckit.analyze` | 分析 spec/plan/tasks 一致性（只读） | `/speckit.analyze` | 分析报告 |
| 可选 | `/speckit.taskstoissues` | 将 tasks 转为 GitHub issues | `/speckit.taskstoissues` | GitHub issues |

**核心命令顺序**：`specify` → `clarify`（可选）→ `plan` → `checklist`（可选）→ `tasks` → `implement`

---

## 3. 需求一：Java→Kotlin 迁移（含 Idea 转换后的审查）

### 3.1 流程概览

```mermaid
flowchart TD
    A[创建/切换分支 001-kotlin-migration] --> B[/speckit.specify]
    B --> C[/speckit.clarify 可选]
    C --> D[/speckit.plan]
    D --> E[/speckit.checklist 可选]
    E --> F[/speckit.tasks]
    F --> G[手动: Idea 完成 Java→Kotlin 转换]
    G --> H[/speckit.implement]
```

### 3.2 详细步骤

1. **创建分支并初始化 spec**
   ```bash
   git checkout -b 001-kotlin-migration
   ```
   在 Cursor 中执行：
   ```
   /speckit.specify 将 Java 转为 Kotlin：先用 Idea 自动转换，再对转换后的代码进行审查，解决编译问题和不恰当的代码
   ```

2. **（可选）澄清需求**
   ```
   /speckit.clarify
   ```
   用于明确审查范围、不恰当代码的定义等。

3. **生成技术方案**
   ```
   /speckit.plan
   ```

4. **（可选）生成代码审查清单**
   ```
   /speckit.checklist 生成 Kotlin 代码审查清单，覆盖空安全、命名、生命周期、支付安全
   ```

5. **生成任务列表**
   ```
   /speckit.tasks
   ```

6. **手动步骤：Idea 转换**
   - 在 Android Studio / Idea 中使用 Code → Convert Java File to Kotlin File 完成转换
   - 提交转换后的代码

7. **执行实现（审查与修复）**
   ```
   /speckit.implement
   ```
   AI 将按 `tasks.md` 逐项审查、修复编译问题和不恰当代码。

### 3.3 审查重点（建议写入 spec）

- **审查范围**：BaseEntryFragment、UIFragmentHelper、EntryActivity、MainActivity 等核心入口
- **不恰当代码**：空安全、命名、生命周期、支付安全（参考 [.cursor/skills/android-code-review/SKILL.md](../.cursor/skills/android-code-review/SKILL.md)）
- **支付项目规则**：遵守 [.cursor/rules/payment-project-hard-problems.mdc](../.cursor/rules/payment-project-hard-problems.mdc) 中的状态、幂等、8583、设备 SDK、安全与可观测性要求

---

## 4. 需求二：UI→Compose 迁移

### 4.1 流程概览

```mermaid
flowchart TD
    A[创建/切换分支 002-compose-migration] --> B[/speckit.specify]
    B --> C[/speckit.clarify 可选]
    C --> D[/speckit.plan]
    D --> E[/speckit.checklist 可选]
    E --> F[/speckit.tasks]
    F --> G[/speckit.implement]
```

### 4.2 详细步骤

1. **创建分支并初始化 spec**
   ```bash
   # 推荐：001 已合并后，从 main 创建
   git checkout main && git pull && git checkout -b 002-compose-migration
   # 或并行开发时，从 001 分支创建
   # git checkout 001-kotlin-migration && git checkout -b 002-compose-migration
   ```
   在 Cursor 中执行：
   ```
   /speckit.specify 将 UI 实现转为 Compose 实现，按 POSLinkUI-Design_V1.03.00 设计规范还原。迁移顺序：Text → Security → Confirmation → POSLink → Option → Information
   ```

2. **（可选）澄清需求**
   ```
   /speckit.clarify
   ```

3. **生成技术方案**
   ```
   /speckit.plan
   ```

4. **（可选）生成 UI 检查清单**
   ```
   /speckit.checklist 生成 Compose UI 需求检查清单
   ```

5. **生成任务列表**
   ```
   /speckit.tasks
   ```

6. **执行实现**
   ```
   /speckit.implement
   ```

### 4.3 关键点

- **设计规范**：以 `POSLinkUI-Design_V1.03.00.docx` 为 UI 规范来源。该 .docx 格式无法被 AI 直接解析，具体布局、字体、颜色需从文档中手工提取
- **模块顺序**：Text → Security → Confirmation → POSLink → Option → Information（可参考 002-compose-migration spec 或设计文档）
- **蓝湖还原**：若需参考蓝湖设计图，可配合 [.cursor/skills/lanhu-mcp-troubleshooting/SKILL.md](../.cursor/skills/lanhu-mcp-troubleshooting/SKILL.md) 使用

---

## 5. 分支与目录约定

| 项目 | 说明 |
|------|------|
| 分支名 | `NNN-short-name`（如 `001-kotlin-migration`、`002-compose-migration`） |
| Spec 目录 | `specs/NNN-short-name/` |
| 核心文件 | `spec.md`、`plan.md`、`tasks.md` |
| 可选文件 | `research.md`、`data-model.md`、`quickstart.md`、`contracts/` |
| 检查清单 | `checklists/requirements.md`、`checklists/ux.md` 等 |

`check-prerequisites.sh` 通过当前分支自动定位 `FEATURE_DIR`，无需手动指定路径。

### 5.5 001 与 002 的合并策略

001 和 002 为两个独立 feature 分支，均修改同一批源文件（`app/.../entry/` 等），合并顺序会影响冲突量。

**推荐：顺序合并**

1. 先完成并合并 `001-kotlin-migration` 到 main（Java → Kotlin）
2. 从 main 创建 `002-compose-migration` 分支，在 Kotlin 代码上做 Compose 迁移
3. 002 修改的是 `.kt` 文件，与 001 无冲突

**若并行开发**：001 与 002 均从 main 拉出时，两分支会同时修改同一批 `.java` 文件，合并时冲突量大。建议：
- 将 002 分支基于 `001-kotlin-migration` 创建（`git checkout -b 002-compose-migration 001-kotlin-migration`），使 002 直接在 Kotlin 上开发
- 或先合并 001，再将 002 rebase 到 main 后解决冲突

---

## 6. 常见问题

### 6.1 非 feature 分支时报错

**现象**：`ERROR: Not on a feature branch. Current branch: main`

**解决**：
- 创建并切换到 feature 分支：`git checkout -b 001-kotlin-migration`
- 或设置环境变量：`export SPECIFY_FEATURE=001-kotlin-migration`（非 git 场景）

### 6.2 脚本在 Windows 下执行失败

**解决**：使用 Git Bash 或 WSL，从仓库根目录执行：
```bash
cd /d/Project/US/POSLinkUI-Demo
.specify/scripts/bash/create-new-feature.sh "描述" --json --short-name "short-name"
```

### 6.3 如何跳过 clarify

在 `specify` 后直接执行 `plan`，但需注意下游返工风险。建议至少完成一次 clarify 以明确边界。

### 6.4 plan.md 或 tasks.md 不存在

- `plan.md` 不存在：先执行 `/speckit.plan`
- `tasks.md` 不存在：先执行 `/speckit.tasks`

### 6.5 多个 spec 目录共享同一数字前缀

`common.sh` 的 `find_feature_dir_by_prefix` 会按 `NNN-*` 匹配。若存在 `001-kotlin-migration` 和 `001-other-feature`，会报错，需确保每个数字前缀只对应一个 spec 目录。

---

## 7. 与现有 spec 的关系

- `001-kotlin-migration`：Java→Kotlin 迁移（含 Idea 转换与代码审查）
- `002-compose-migration`：UI→Compose 迁移；**建议**在 001 合并后再从 main 拉分支，避免与 001 的 Java→Kotlin 变更冲突
- `099-kotlin-compose-migration-superseded`：原整体迁移 spec，已拆分为 001 与 002，标记为 Superseded

---

## 8. 参考链接

| 资源 | 路径 |
|------|------|
| Spec 模板 | `.specify/templates/spec-template.md` |
| Plan 模板 | `.specify/templates/plan-template.md` |
| Tasks 模板 | `.specify/templates/tasks-template.md` |
| Android 代码审查 Skill | `.cursor/skills/android-code-review/SKILL.md` |
| 支付项目难点规则 | `.cursor/rules/payment-project-hard-problems.mdc` |
| 蓝湖 MCP 故障排查 Skill | `.cursor/skills/lanhu-mcp-troubleshooting/SKILL.md` |
