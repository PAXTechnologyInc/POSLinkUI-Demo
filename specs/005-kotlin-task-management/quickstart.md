# 快速开始：005-kotlin-task-management

**特性**： 005-kotlin-task-management  
**日期**: 2025-03-19

## 前置条件

- 当前分支：`005-kotlin-task-management`
- 可正常构建：`./gradlew assembleDebug`
- 已具备本地调试设备或模拟器

## 1) 依赖添加

在 `app/build.gradle` 的 `dependencies` 中增加：

```groovy
implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3'
```

（版本需与 Kotlin 兼容，可参考项目 Kotlin 版本选择 1.6.x / 1.7.x / 1.8.x）

## 2) 基础构建与回归

1. 构建 Debug 包：`./gradlew assembleDebug`
2. 运行现有关键 Entry 流程（金额输入、确认、签名、超时、完成），记录重构前行为基线

## 3) 改造顺序（建议）

按以下顺序逐块改造并回归：

1. **MainThreadRunner**：新增统一主线程投递工具，替换分散的 `Handler(Looper.getMainLooper())` 调用
2. **TaskScheduler**：迁移到 `lifecycleScope` + 协程 `delay`，支持 per-Fragment 任务映射，在 `onDestroy` 中同步取消
3. **Fragment 内倒计时**：ShowSignatureBoxFragment、SignatureFragment 的 `countdownUpdateScheduler` 迁移到 `viewLifecycleOwner.lifecycleScope`
4. **ThreadPoolManager**：保留 CallerRunsPolicy，消除 `!!`，可选增加协程桥接
5. **Handler 调用点**：DisplayApprovalUtils、Toast、TextField、InputAccountFragment 等迁移到 MainThreadRunner

## 4) 验收检查点

- 启动超时任务后立即退出页面，确认无回调、无崩溃、LeakCanary 无泄漏
- 多个 Fragment 同时有超时时，销毁其一仅取消其任务
- 重复调度超时：新任务替换旧任务，仅一次执行
- 主线程投递到已销毁目标：不崩溃、不泄漏
- 任务队列满时：任务在调用线程执行，不丢失

## 5) 测试命令

- 内存验证：`./gradlew connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.paxus.pay.poslinkui.demo.PostDestroyLeakTest`
- 生命周期：`./gradlew connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.paxus.pay.poslinkui.demo.ReceiverLifecycleTest`
- 任务取消：`./gradlew connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.paxus.pay.poslinkui.demo.ScheduledTaskCancellationTest`

## 参考

- [spec.md](../spec.md)
- [plan.md](../plan.md)
- [research.md](../research.md)
- [data-model.md](../data-model.md)
- [contracts/task-management-contract.md](task-management-contract.md)
