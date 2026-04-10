---
description: 根据已有设计产物，生成功能的可执行、按依赖排序的 tasks.md。
handoffs: 
  - label: 一致性分析
    agent: speckit.analyze
    prompt: 运行项目一致性分析
    send: true
  - label: 实现项目
    agent: speckit.implement
    prompt: 分阶段开始实现
    send: true
---

## 用户输入

```text
$ARGUMENTS
```

在继续之前，你**必须**考虑用户输入（若非空）。

## 执行前检查

**扩展钩子（生成任务前）**：
- 检查项目根目录是否存在 `.specify/extensions.yml`。
- 若存在，读取并在 `hooks.before_tasks` 键下查找条目
- 若 YAML 无法解析或无效，静默跳过钩子检查并照常继续
- 仅筛选 `enabled: true` 的钩子
- 对剩余每个钩子，**不要**尝试解释或求值钩子 `condition` 表达式：
  - 若钩子无 `condition` 字段或为 null/空，视为可执行
  - 若钩子定义了非空 `condition`，跳过该钩子，将 condition 求值留给 HookExecutor 实现
- 对每个可执行钩子，根据其 `optional` 标志输出如下：
  - **可选钩子**（`optional: true`）：
    ```
    ## Extension Hooks

    **Optional Pre-Hook**: {extension}
    Command: `/{command}`
    Description: {description}

    Prompt: {prompt}
    To execute: `/{command}`
    ```
  - **必选钩子**（`optional: false`）：
    ```
    ## Extension Hooks

    **Automatic Pre-Hook**: {extension}
    Executing: `/{command}`
    EXECUTE_COMMAND: {command}
    
    Wait for the result of the hook command before proceeding to the Outline.
    ```
- 若未注册钩子或不存在 `.specify/extensions.yml`，静默跳过

## 概要

1. **搭建**：在仓库根目录运行 `.specify/scripts/bash/check-prerequisites.sh --json`，解析 FEATURE_DIR 与 AVAILABLE_DOCS。所有路径须为绝对路径。参数中含单引号（如 "I'm Groot"）时使用转义：例如 `'I'\''m Groot'`（或尽量用双引号：`"I'm Groot"`）。

2. **加载设计文档**：自 FEATURE_DIR 读取：
   - **必填**：plan.md（技术栈、库、结构）、spec.md（带优先级的用户故事）
   - **可选**：data-model.md（实体）、contracts/（接口契约）、research.md（决策）、quickstart.md（测试场景）
   - 说明：并非所有项目具备全部文档。根据已有内容生成任务。

3. **执行任务生成工作流**：
   - 加载 plan.md，提取技术栈、库、项目结构
   - 加载 spec.md，提取用户故事及优先级（P1、P2、P3 等）
   - 若存在 data-model.md：提取实体并映射到用户故事
   - 若存在 contracts/：将接口契约映射到用户故事
   - 若存在 research.md：提取决策用于搭建类任务
   - 按用户故事组织任务（见下文「任务生成规则」）
   - 生成用户故事完成顺序的依赖图
   - 为每个用户故事生成并行执行示例
   - 校验任务完整性（每个故事具备所需任务且可独立测试）

4. **生成 tasks.md**：以 `.specify/templates/tasks-template.md` 为结构，填入：
   - 来自 plan.md 的正确功能名
   - Phase 1：搭建任务（项目初始化）
   - Phase 2：基础任务（所有用户故事的阻塞前置）
   - Phase 3+：每个用户故事一阶段（按 spec.md 优先级顺序）
   - 每阶段含：故事目标、独立测试标准、测试（若要求）、实现任务
   - 最后阶段：打磨与横切关注点
   - 所有任务须符合严格清单格式（见下文「任务生成规则」）
   - 每项任务含清晰文件路径
   - 依赖节说明故事完成顺序
   - 每故事的并行执行示例
   - 实现策略节（MVP 优先、增量交付）

5. **报告**：输出生成的 tasks.md 路径及摘要：
   - 任务总数
   - 每用户故事任务数
   - 识别的并行机会
   - 每故事的独立测试标准
   - 建议的 MVP 范围（通常仅为用户故事 1）
   - 格式校验：确认**所有**任务符合清单格式（复选框、ID、标签、文件路径）

6. **扩展钩子**：tasks.md 生成后，检查项目根目录是否存在 `.specify/extensions.yml`。
   - 若存在，读取并在 `hooks.after_tasks` 键下查找条目
   - 若 YAML 无法解析或无效，静默跳过钩子检查并照常继续
   - 仅筛选 `enabled: true` 的钩子
   - 对剩余每个钩子，**不要**尝试解释或求值钩子 `condition` 表达式：
     - 若钩子无 `condition` 字段或为 null/空，视为可执行
     - 若钩子定义了非空 `condition`，跳过该钩子，将 condition 求值留给 HookExecutor 实现
   - 对每个可执行钩子，根据其 `optional` 标志输出如下：
     - **可选钩子**（`optional: true`）：
       ```
       ## Extension Hooks

       **Optional Hook**: {extension}
       Command: `/{command}`
       Description: {description}

       Prompt: {prompt}
       To execute: `/{command}`
       ```
     - **必选钩子**（`optional: false`）：
       ```
       ## Extension Hooks

       **Automatic Hook**: {extension}
       Executing: `/{command}`
       EXECUTE_COMMAND: {command}
       ```
   - 若未注册钩子或不存在 `.specify/extensions.yml`，静默跳过

任务生成上下文：$ARGUMENTS

tasks.md 应可直接执行——每项任务须足够具体，使 LLM 无需额外上下文即可完成。

## 任务生成规则

**关键**：任务**必须**按用户故事组织，以支持独立实现与测试。

**测试为可选**：仅在功能规格明确要求或用户要求 TDD 时生成测试任务。

### 清单格式（必填）

每项任务**必须**严格符合：

```text
- [ ] [TaskID] [P?] [Story?] Description with file path
```

**格式组成**：

1. **复选框**：始终以 `- [ ]` 开头（Markdown 复选框）
2. **任务 ID**：执行顺序中的连续编号（T001、T002、T003…）
3. **[P] 标记**：仅当任务可并行（不同文件、不依赖未完成任务）时加入
4. **[Story] 标签**：仅用户故事阶段任务**必填**
   - 格式：[US1]、[US2]、[US3] 等（对应 spec.md 中的用户故事）
   - 搭建阶段：无故事标签
   - 基础阶段：无故事标签
   - 用户故事阶段：**必须**有故事标签
   - 打磨阶段：无故事标签
5. **描述**：明确动作 + 确切文件路径

**示例**：

- ✅ 正确：`- [ ] T001 Create project structure per implementation plan`
- ✅ 正确：`- [ ] T005 [P] Implement authentication middleware in src/middleware/auth.py`
- ✅ 正确：`- [ ] T012 [P] [US1] Create User model in src/models/user.py`
- ✅ 正确：`- [ ] T014 [US1] Implement UserService in src/services/user_service.py`
- ❌ 错误：`- [ ] Create User model`（缺 ID 与 Story 标签）
- ❌ 错误：`T001 [US1] Create model`（缺复选框）
- ❌ 错误：`- [ ] [US1] Create User model`（缺任务 ID）
- ❌ 错误：`- [ ] T001 [US1] Create model`（缺文件路径）

### 任务组织

1. **来自用户故事（spec.md）** — **主要组织方式**：
   - 每个用户故事（P1、P2、P3…）单独一阶段
   - 将该故事相关组件全部映射到该故事：
     - 该故事所需模型
     - 该故事所需服务
     - 该故事所需接口/UI
     - 若要求测试：该故事专用测试
   - 标注故事依赖（多数故事应相互独立）

2. **来自契约**：
   - 将每条接口契约 → 映射到其服务的用户故事
   - 若要求测试：每条接口契约 → 在该故事阶段实现前增加契约测试任务 [P]

3. **来自数据模型**：
   - 将每个实体映射到需要它的用户故事
   - 若实体服务多个故事：放入最早的故事或搭建阶段
   - 关系 → 在对应故事阶段的服务层任务中体现

4. **来自搭建/基础设施**：
   - 共享基础设施 → 搭建阶段（Phase 1）
   - 基础/阻塞任务 → 基础阶段（Phase 2）
   - 故事专用搭建 → 该故事阶段内

### 阶段结构

- **Phase 1**：搭建（项目初始化）
- **Phase 2**：基础（阻塞前置——**必须**在用户故事前完成）
- **Phase 3+**：按优先级顺序的用户故事（P1、P2、P3…）
  - 每故事内：测试（若要求）→ 模型 → 服务 → 端点 → 集成
  - 每阶段应为完整、可独立测试的增量
- **最后阶段**：打磨与横切关注点
