# 数据模型：Jetpack 可读性与内存优化重构

**特性**： 003-jetpack-refactor  
**日期**: 2026-03-19

## 概述

本特性不新增业务交易数据模型，聚焦“架构与内存治理”实体。以下实体用于定义重构边界、生命周期规则和验收输入。

## Core Entities

### 1) EntryFlowSession

| Field | Type | Description |
|------|------|-------------|
| sessionId | String | 单次 Entry 流程标识 |
| action | String | 入口 action（由 Intent 触发） |
| fragmentName | String | 当前承载 Fragment 名称 |
| startedAtMs | Long | 流程开始时间 |
| finishedAtMs | Long? | 流程结束时间 |
| status | Enum | `ACTIVE` / `FINISHED` / `CANCELLED` / `FAILED` |

**Validation rules**
- `sessionId` 必填且唯一（单次执行上下文）
- `finishedAtMs` 仅在非 `ACTIVE` 状态允许赋值

### 2) LifecycleBinding

| Field | Type | Description |
|------|------|-------------|
| ownerType | Enum | `ACTIVITY` / `FRAGMENT` / `PRESENTATION` |
| bindingType | Enum | `OBSERVER` / `RECEIVER` / `TASK` |
| registerPoint | String | 注册位置（类 + 生命周期方法） |
| unregisterPoint | String | 注销位置（类 + 生命周期方法） |
| isSymmetric | Boolean | 注册/注销是否对称 |

**Validation rules**
- `isSymmetric` 必须为 `true`
- `registerPoint` 与 `unregisterPoint` 必须存在且同上下文

### 3) MemoryVerificationRun

| Field | Type | Description |
|------|------|-------------|
| runMode | Enum | `LOCAL` / `CI` |
| scenario | Enum | `COLD_IDLE` / `ENTRY_FLOW` / `REPEAT_SWITCH` / `POST_DESTROY` |
| baselineMb | Double | 基线内存 |
| measuredMb | Double | 实测内存 |
| deltaPercent | Double | 偏移百分比 |
| leakDetected | Boolean | 是否检测到泄漏 |
| pass | Boolean | 是否通过门槛 |

**Validation rules**
- `deltaPercent <= 10`（对应 spec SC-002/SC-005）
- `POST_DESTROY` 场景 `leakDetected == false`
- `LOCAL` 与 `CI` 使用同一阈值

## Relationships

- `EntryFlowSession` 1:N `LifecycleBinding`
- `MemoryVerificationRun` 针对 `EntryFlowSession` 场景集执行

## State Transitions

### EntryFlowSession

`ACTIVE -> FINISHED | CANCELLED | FAILED`

约束：
- 禁止终态回退到 `ACTIVE`
- 宿主销毁时不得存在悬挂 `TASK` 或 `RECEIVER` 绑定

## Scope Notes

- 本数据模型仅服务于重构设计与测试验收，不变更交易业务字段或主机报文契约。
