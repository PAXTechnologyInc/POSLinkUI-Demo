# 数据模型：Kotlin Task Management Optimization

**特性**： 005-kotlin-task-management  
**日期**: 2025-03-19

## 概述

本特性定义任务管理相关实体，用于设计边界、接口契约与验收输入。不新增业务交易数据模型。

## Core Entities

### 1) TaskScope

| Field | Type | Description |
|-------|------|-------------|
| scopeId | String | 作用域标识（如 Fragment tag、Activity 实例 id） |
| ownerType | Enum | `ACTIVITY` / `FRAGMENT` / `CALLER_MANAGED` |
| lifecycleOwner | LifecycleOwner? | 生命周期拥有者，CALLER_MANAGED 时为 null |
| jobs | MutableSet<Job> | 该作用域下的活跃 Job/任务句柄 |

**Validation rules**
- `scopeId` 必填且在同一 owner 下唯一
- `CALLER_MANAGED` 时 `lifecycleOwner` 为 null，由调用方显式 cancel

**State**
- `ACTIVE`：作用域有效，可提交任务
- `CANCELLED`：已取消，不再接受新任务，既有任务已取消

---

### 2) ScheduledTaskHandle

| Field | Type | Description |
|-------|------|-------------|
| taskId | String | 任务唯一标识 |
| taskType | Enum | `TIMEOUT` / `FINISH` / `COUNTDOWN` / `DELAYED_UI` |
| scopeId | String | 所属 TaskScope |
| delayMs | Long | 延迟毫秒数 |
| replacePolicy | Enum | `REPLACE`（新替换旧） / `REJECT` |
| job | Job | 协程 Job，用于取消 |

**Validation rules**
- 同一 scopeId + taskType 下，`REPLACE` 策略时仅保留最新一个
- `job` 在 scope 取消时必须已 cancel

---

### 3) BackgroundTaskHandle

| Field | Type | Description |
|-------|------|-------------|
| taskId | String | 任务唯一标识 |
| scopeId | String | 所属 TaskScope（可为 CALLER_MANAGED） |
| job | Job | 协程 Job，用于取消 |
| callbackDispatched | Boolean | 是否已向主线程派发回调 |

**Validation rules**
- 若 scope 已取消，`callbackDispatched` 必须为 false（不派发回调）
- 回调必须在主线程执行

---

### 4) TaskGroup

| Field | Type | Description |
|-------|------|-------------|
| groupId | String | 组标识 |
| taskHandles | MutableSet<TaskHandle> | 组内任务句柄（ScheduledTaskHandle 或 BackgroundTaskHandle） |

**Validation rules**
- `cancel()` 时取消组内所有任务
- 组内任务可属于不同 scope，取消时逐项 cancel

---

### 5) MainThreadPost

| Field | Type | Description |
|-------|------|-------------|
| runnable | Runnable | 待执行逻辑 |
| lifecycleOwner | LifecycleOwner? | 可选，绑定生命周期 |
| executed | Boolean | 是否已执行 |
| skipped | Boolean | 是否因生命周期销毁而跳过 |

**Validation rules**
- `lifecycleOwner` 为 DESTROYED 时，`executed` 为 false，`skipped` 为 true
- 执行必须在主线程

---

## Relationships

- `TaskScope` 1:N `ScheduledTaskHandle` / `BackgroundTaskHandle`
- `TaskGroup` N:N `TaskHandle`（通过 taskHandles 集合）
- `MainThreadPost` 0..1 `LifecycleOwner`（可选绑定）

## State Transitions

### TaskScope

`ACTIVE -> CANCELLED`（当 lifecycleOwner 进入 DESTROYED 或调用方显式 cancel）

约束：
- 进入 `CANCELLED` 后，所有关联 Job 必须已 cancel
- 禁止在 `CANCELLED` 后提交新任务

### ScheduledTaskHandle

- `PENDING` -> `RUNNING` -> `COMPLETED`（正常执行）
- `PENDING` -> `CANCELLED`（scope 取消或显式 cancel）
- `REPLACE` 策略：新任务创建时，旧任务 `PENDING`/`RUNNING` -> `CANCELLED`

### BackgroundTaskHandle

- `RUNNING` -> `COMPLETED`（回调已派发）
- `RUNNING` -> `CANCELLED`（scope 取消或显式 cancel，不派发回调）

## Scope Notes

- 本数据模型服务于任务管理设计与测试验收，不变更交易业务字段。
- 实现时可使用 Kotlin 协程 `Job`、`CoroutineScope` 等作为句柄，此处为逻辑抽象。
