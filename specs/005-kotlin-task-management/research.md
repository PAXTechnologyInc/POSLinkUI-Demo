# 研究：Kotlin Task Management Optimization

**特性**： 005-kotlin-task-management  
**日期**: 2025-03-19

## 1) Concurrency Model: Coroutines vs Executor-Only

### Decision

采用 **Kotlin Coroutines 为主**，Executor 作为 IO 池底层保留。生命周期绑定任务（超时、完成、倒计时）使用 `lifecycleScope`/`viewLifecycleOwner.lifecycleScope`；主线程投递使用 `Dispatchers.Main`；后台 IO 可使用 `withContext(Dispatchers.IO)` 或继续通过 ThreadPoolManager 执行，由调用方选择。

### Rationale

- **同步取消**：`lifecycleScope.cancel()` 在 onDestroy 中调用时，所有子协程立即取消，满足 spec 同步取消要求
- **结构化并发**：Job 树天然支持 Task Group 取消
- **主线程投递**：`Dispatchers.Main.immediate` 替代分散的 Handler，统一且可测试
- **替换语义**：`Job.cancel()` + 新 `launch` 实现“新任务替换旧任务”
- **与 Jetpack 协同**：lifecycleScope 已集成到 Activity/Fragment，无需额外绑定

### Alternatives considered

- **纯 Executor**：需手动在 onDestroy 中 cancel Future 并保证同步；多 Fragment 需 per-Fragment 的 ScheduledExecutor，复杂度高
- **RxJava**：项目未使用，引入成本高；协程与 Kotlin 生态更一致

---

## 2) Lifecycle Binding Strategy

### Decision

- **Activity 级任务**（如 EntryActivity 的 TIMEOUT/FINISH）：使用 `lifecycleScope`，在 `onDestroy` 中 `lifecycleScope.cancel()` 或依赖 Activity 销毁时自动取消
- **Fragment 级任务**（如倒计时、Fragment 专属超时）：使用 `viewLifecycleOwner.lifecycleScope`，Fragment 销毁时自动取消
- **非屏幕绑定任务**（如打印检查）：使用 `GlobalScope` 或显式 `CoroutineScope`，由调用方在适当时机 `scope.cancel()`

### Rationale

- `lifecycleScope` 在 `onDestroy` 时自动取消，满足“同步取消”语义（取消在 onDestroy 流程内完成）
- `viewLifecycleOwner` 避免使用 `lifecycleOwner` 导致 Fragment 已 detach 但 Activity 未销毁时的误绑定
- 非屏幕绑定任务不自动取消，符合 spec 澄清

### Alternatives considered

- **仅 Activity 级**：无法满足“仅该 Fragment 的任务取消”的验收场景
- **ProcessLifecycleOwner**：适用于 Application 级，不适用于本需求

---

## 3) Main-Thread Posting

### Decision

提供统一入口 `MainThreadRunner`（或等价工具），内部使用 `Dispatchers.Main.immediate` 或 `Handler(Looper.getMainLooper())`。支持：
- 无生命周期：`post { }` 直接投递
- 生命周期绑定：`postWhenAlive(lifecycleOwner) { }`，在 `DESTROYED` 前不执行则跳过

### Rationale

- 消除 `Handler()` 无 Looper 风险
- 消除每次 `Handler(Looper.getMainLooper())` 新建
- 生命周期绑定满足“目标已销毁则跳过”的验收

### Alternatives considered

- **保留分散 Handler**：不符合“统一、可测试”要求
- **仅 Dispatchers.Main**：需协程作用域，对简单延迟场景略重；可提供两种 API

---

## 4) Rejected Task Fallback

### Decision

保留 **CallerRunsPolicy**。当 Executor 拒绝任务时，在调用线程上同步执行 `task.run()`，保证任务一定执行且不崩溃。

### Rationale

- 与 spec 澄清一致
- 与现有 ThreadPoolManager 行为一致
- 支付场景下任务丢失风险高于短暂阻塞调用线程

### Alternatives considered

- **Drop and log**：任务丢失，不符合支付可靠性
- **Retry**：增加复杂度，且队列满时重试可能仍失败

---

## 5) Per-Fragment Timeout Ownership

### Decision

TaskScheduler 需支持 **per-requestor 的 task 映射**。当前按 `TASK` 枚举（TIMEOUT/FINISH）单例映射，需改为按 `(requestorId, taskType)` 或等价键映射，使不同 Fragment 可拥有独立超时；Fragment 销毁时仅取消其自己的任务。

### Rationale

- 满足用户故事 1 验收场景 3：“仅该 Fragment 的任务取消；其他继续”
- 当前设计为 Activity 级单例，需扩展为支持多 requestor

### Alternatives considered

- **保持 Activity 级单例**：无法满足 per-Fragment 取消
- **每 Fragment 一个 TaskScheduler**：可满足，但需调整 EntryActivity 与 Fragment 的协作方式（Fragment 持有自己的 scheduler 或通过接口请求）

---

## 6) Dependency Addition

### Decision

新增 `org.jetbrains.kotlinx:kotlinx-coroutines-android`（版本与 Kotlin 兼容，如 1.7.3 或 1.8.x）。`kotlinx-coroutines-core` 通常由 android 依赖传递引入。

### Rationale

- 协程为 Android 官方推荐并发方案
- 与 lifecycle 集成良好

### Alternatives considered

- **不引入协程**：难以满足同步取消与结构化取消，需大量手写逻辑
