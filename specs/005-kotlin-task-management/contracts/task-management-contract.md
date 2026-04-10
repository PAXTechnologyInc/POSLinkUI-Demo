# 任务管理契约

**特性**： 005-kotlin-task-management  
**日期**: 2025-03-19

## 1) 生命周期感知调度器（Lifecycle-Aware Scheduler）

### 职责

- 支持按 `LifecycleOwner`（Activity/Fragment）提交延迟/周期任务
- 当 owner 进入 `DESTROYED` 时，**同步**取消该 owner 下所有任务
- 支持“新任务替换旧任务”语义（同一 scope + taskType）

### 接口契约（逻辑层，非具体 API）

| 操作 | 前置条件 | 后置条件 |
|------|----------|----------|
| `schedule(owner, taskType, delayMs)` | owner 未 DESTROYED | 返回可取消句柄；若已有同 type 任务则取消旧任务 |
| `cancel(handle)` | handle 有效 | 对应任务不再执行，无回调 |
| `cancelAll(owner)` | owner 有效 | 该 owner 下所有任务已取消 |

### 不变量

- 任务回调不会在 owner DESTROYED 之后执行
- 取消在 `onDestroy` 内同步完成

---

## 2) 后台执行与主线程回调（Background Execution）

### 职责

- 在非主线程执行 IO/计算
- 结果通过主线程回调交付
- 支持 lifecycle-bound（owner 销毁时取消）或 caller-managed（显式取消）

### 接口契约

| 操作 | 前置条件 | 后置条件 |
|------|----------|----------|
| `execute(block, onResult, owner?)` | block 纯计算/IO | 完成时 onResult 在主线程调用；owner 销毁则取消且不调用 onResult |
| `cancel(handle)` | handle 有效 | 任务取消，onResult 不调用 |

### 拒绝策略

- 当底层 Executor 拒绝时：在**调用线程**上同步执行 block，保证执行
- 不崩溃、不静默丢失

---

## 3) 主线程投递（Main Thread Posting）

### 职责

- 统一入口，保证 runnable 在主线程执行
- 支持可选生命周期绑定：owner DESTROYED 则跳过执行

### 接口契约

| 操作 | 前置条件 | 后置条件 |
|------|----------|----------|
| `post(runnable)` | 无 | runnable 在主线程执行 |
| `postWhenAlive(owner, runnable)` | owner 未 DESTROYED | 若执行时 owner 已 DESTROYED，则跳过，不崩溃不泄漏 |

### 不变量

- 目标已销毁时，不执行、不持有引用、不崩溃

---

## 4) 结构化取消（Task Group）

### 职责

- 支持将多个任务归为一组，一次性取消

### 接口契约

| 操作 | 前置条件 | 后置条件 |
|------|----------|----------|
| `createGroup()` | 无 | 返回空组 |
| `addToGroup(group, handle)` | handle 有效 | handle 加入 group |
| `cancelGroup(group)` | group 有效 | 组内所有任务已取消 |

---

## 5) 日志与可观测性

- 任务取消、失败必须打日志
- 禁止日志中包含 PAN、track2、PIN block、CVV、密钥等敏感数据
- 日志应包含：taskId、scopeId、event（cancelled/failed）、errorCode（如有）

---

## 6) 与现有组件的兼容

- `TaskScheduler`：保留 `TASK` 枚举、`generateTaskRequestBundle` 等对外契约，内部实现迁移到协程
- `ThreadPoolManager`：保留 `execute`/`submit`，保留 `CallerRunsPolicy`；可选增加协程桥接 `asCoroutineDispatcher()`
- `TaskFactory`：保留 `create(taskType, activity)` 工厂，返回类型可扩展为支持协程 Job 的句柄
