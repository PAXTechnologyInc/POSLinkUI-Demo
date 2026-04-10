# 契约：生命周期与内存安全

**特性**： 003-jetpack-refactor  
**目的**: 约束观察者、广播、任务调度在生命周期内的注册与释放行为，确保无泄漏与无越界回调。

## 1. Observer Contract

- 观察者必须绑定生命周期 owner（Activity/Fragment/Presentation 宿主）。
- 如必须使用长期观察形式，必须定义明确的移除时机并可自动验证。
- 宿主销毁后，观察者不得继续接收回调。

## 2. BroadcastReceiver Contract

- 广播必须遵循“谁注册谁注销”。
- 注册与注销必须在同一上下文完成（同 owner）。
- 宿主 `onDestroy` 结束前必须完成注销，禁止依赖 GC 被动回收。

## 3. Scheduled Task Contract

- 任务调度对象不得跨宿主生命周期持有已销毁引用。
- 宿主结束时必须触发统一取消入口（cancel/shutdown）。
- 超时任务不得在宿主销毁后继续触发 UI 或业务回调。

## 4. Verification Contract

- 本地与 CI 执行同一批内存场景与阈值。
- 必测场景：冷启动空闲、典型 Entry 流程、连续切换、页面销毁后回收。
- 任一场景失败视为质量门禁失败（阻止合并）。

## 5. Lifecycle Verification Steps

- Activity 销毁：先注销 receiver，再取消 scheduler，再释放 presentation observer。
- Fragment 销毁：保证 receiver/observer 的注销动作在同 owner 下完成。
- 任务销毁：`shutdown/cancelTasks` 后不得存在后续 UI 回调。
