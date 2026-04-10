# 实施计划：Kotlin 任务管理与调度优化

**分支**：`005-kotlin-task-management` | **日期**：2025-03-19 | **规格**：[spec.md](spec.md)
**输入**：功能规格说明，来源 `specs/005-kotlin-task-management/spec.md`

## 摘要

在 Kotlin 迁移基础上，重构线程与任务管理，实现生命周期感知的定时任务、后台 IO、主线程投递与结构化取消。采用 Kotlin 协程作为核心实现，保留 Executor 作为 IO 池底层；统一主线程投递与生命周期绑定，满足 spec 中同步取消、替换语义、拒绝回退等约束。

## 技术上下文

**语言/版本**: Kotlin 1.9.22 / 2.1.0，JVM target 11  
**主要依赖**: kotlinx-coroutines-android、AndroidX Lifecycle、Hilt、现有 ThreadPoolManager/TaskScheduler  
**存储**: N/A（无持久化变更）  
**测试**: JUnit4、AndroidX Instrumentation、LeakCanary（debug）、现有 MemoryVerificationTest/PostDestroyLeakTest  
**目标平台**: Android minSdk 22, targetSdk 31  
**项目类型**: mobile-app（Android POS）  
**性能目标**: 任务取消在 onDestroy 内同步完成；主线程投递无额外延迟  
**约束**: Neptune/POSLink 契约不变；支付流程行为不变；CallerRunsPolicy 保留（拒绝时回退到调用线程）  
**规模/范围**: 100+ Fragment，核心改造类为 TaskScheduler、ThreadPoolManager、EntryActivity、ShowSignatureBoxFragment、SignatureFragment 及分散 Handler 调用点

## 宪章检查

*门禁：必须在 Phase 0 研究前通过，Phase 1 设计后再次检查。*

### 设计前检查

- I. 需求可测试性：通过（FR-001~FR-007 与 SC-001~SC-005 可度量/可执行）
- II. 需求无歧义：通过（同步取消、替换语义、用户反馈范围已澄清）
- III. 需求与技术无关：通过（spec 层不绑定实现框架）
- IV. 需求范围约束：通过（Assumptions、非屏幕绑定任务显式取消已明确）
- V. 合规性需求：通过（支付流程状态机、日志不打印敏感数据已在 spec 体现）

### 设计后检查

- 通过：协程方案与 contracts 一致，未引入宪章违规项

## 项目结构

### 文档（本特性）

```text
specs/005-kotlin-task-management/
├── plan.md
├── research.md
├── data-model.md
├── quickstart.md
├── contracts/
│   └── task-management-contract.md
└── tasks.md             # /speckit.tasks 输出，本命令不创建
```

### 源码（仓库根目录）

```text
app/
└── src/main/java/com/paxus/pay/poslinkui/demo/
    ├── entry/
    │   ├── EntryActivity.kt
    │   ├── BaseEntryFragment.kt
    │   ├── poslink/
    │   │   ├── ShowSignatureBoxFragment.kt
    │   │   ├── ShowDialogFragment.kt
    │   │   └── ...
    │   ├── signature/
    │   │   └── SignatureFragment.kt
    │   └── task/
    │       ├── ScheduledTask.kt
    │       ├── TimeoutTask.kt
    │       └── FinishTask.kt
    ├── utils/
    │   ├── TaskScheduler.kt
    │   ├── ThreadPoolManager.kt
    │   ├── TaskFactory.kt
    │   ├── MainThreadRunner.kt          # 新增：统一主线程投递
    │   └── ...
    └── [其他 Handler 调用点]
```

**结构决策**: 保持现有目录结构，新增 `MainThreadRunner` 统一主线程投递；改造 `TaskScheduler` 为生命周期感知；`ThreadPoolManager` 保留 CallerRunsPolicy，增加可选协程桥接。

## 阶段 0：研究产出

- [research.md](research.md) 完成：协程 vs Executor、生命周期绑定、主线程投递、拒绝策略

## 阶段 1：设计与契约产出

- [data-model.md](data-model.md)
- [contracts/task-management-contract.md](contracts/task-management-contract.md)
- [quickstart.md](quickstart.md)

## 阶段 1：Agent 上下文更新

- 脚本 `.specify/scripts/bash/update-agent-context.sh cursor-agent` 在 bash 可用时执行；否则手动更新 `.cursor/rules` 或 `.specify/memory` 中技术栈说明

## 阶段 2 规划截止

本命令止于规划阶段。下一步运行 `/speckit.tasks` 进行任务拆分。

## 架构一致性检查点

- 任务取消必须在 onDestroy 内同步完成（协程 `lifecycleScope.cancel()` 或等效）
- 主线程投递必须通过统一入口（MainThreadRunner 或 Dispatchers.Main），禁止裸 `Handler()` 无 Looper
- 拒绝任务必须回退到调用线程执行（CallerRunsPolicy）
- 重复调度超时：新任务替换旧任务（Job.cancel() + 新 launch）

## 复杂度跟踪

无宪章违规，无例外说明。
