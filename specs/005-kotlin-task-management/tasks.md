# 任务清单：Kotlin 任务管理与调度优化

**输入**：设计文档目录 `specs/005-kotlin-task-management/`  
**前置条件**：plan.md、spec.md、research.md、data-model.md、contracts/

**测试**：规格未明确要求新增测试；沿用既有 PostDestroyLeakTest、ReceiverLifecycleTest、ScheduledTaskCancellationTest，按 quickstart.md 验证。

**组织方式**：任务按用户故事分组，便于独立实现与验证。

## 格式约定：`[ID] [P?] [Story] 描述`

- **[P]**：可并行（不同文件、无依赖）
- **[Story]**：所属用户故事（US1、US2、US3、US4、US5）
- 描述中须写清文件路径

## 路径约定

- **Android 应用**：`app/src/main/java/com/paxus/pay/poslinkui/demo/`

---

## 阶段 1：搭建（共享基础设施）

**目的**：添加依赖并确保构建通过

- [x] T001 在 `app/build.gradle` 中添加 kotlinx-coroutines-android 依赖（如 1.7.3）
- [x] T002 [P] 若尚未引入 lifecycle-runtime-ktx（用于 lifecycleScope），在 `app/build.gradle` 中添加
- [x] T003 执行 `./gradlew assembleDebug` 验证构建成功

---

## 阶段 2：基础（阻塞性前置）

**目的**：用户故事实现前必须完成的核心基础设施

**⚠️ 关键**：本阶段完成前不得开始用户故事开发

- [x] T004 在 `app/src/main/java/com/paxus/pay/poslinkui/demo/utils/MainThreadRunner.kt` 创建 MainThreadRunner，提供 `post(runnable)` 与 `postWhenAlive(lifecycleOwner, runnable)`，见 contracts/task-management-contract.md
- [x] T005 实现 MainThreadRunner：使用 `Dispatchers.Main` 或 `Handler(Looper.getMainLooper())`；`postWhenAlive` 执行前检查生命周期，若已 DESTROYED 则跳过

**检查点**：MainThreadRunner 就绪；可进行 Handler 迁移与生命周期感知投递

---

## 阶段 3：用户故事 1 - 生命周期感知的定时任务（优先级：P1） 🎯 MVP

**目标**：Activity/Fragment 销毁时超时与结束任务同步取消；按 Fragment 归属任务；重复超时采用替换语义

**独立测试**：启动超时任务，在触发前销毁 Activity/Fragment，验证无回调且无泄漏

### 用户故事 1 的实现

- [x] T006 [US1] 重构 `app/src/main/java/com/paxus/pay/poslinkui/demo/utils/TaskScheduler.kt`：使用 Activity 的 lifecycleScope 与 `CoroutineScope.launch` + `delay`，替代 `ScheduledExecutorService`
- [x] T007 [US1] 为 TaskScheduler 增加按请求方映射：键为 `(requestorId, taskType)`；增加 `cancelForRequestor(requestorId)`；在 generateTaskRequestBundle / PARAM_REQUESTOR_ID 中确保 requestorId
- [x] T008 [US1] 在 TaskScheduler 中实现替换语义：调度相同 `(requestorId, taskType)` 时先取消旧 Job 再启动新 Job
- [x] T009 [US1] 确保 `app/src/main/java/com/paxus/pay/poslinkui/demo/entry/EntryActivity.kt` 在 `onDestroy` 中于 `super` 之前调用 scheduler shutdown/cancel；取消须同步完成
- [x] T010 [US1] 增加 Fragment 生命周期回调或 `onDestroy` 钩子：请求任务的 Fragment 销毁时，EntryActivity 通过 `cancelForRequestor` 取消该 Fragment 的任务
- [x] T011 [US1] 更新 EntryActivity 中 Fragment 结果处理，将 bundle 中的 requestorId 传入 TaskScheduler.schedule
- [x] T012 [US1] 更新请求超时的 Fragment（ShowDialogFragment、ShowDialogFormFragment、ShowSignatureBoxFragment、SignatureFragment、ShowThankYouFragment、InputTextFragment、ShowInputTextBoxFragment、ShowTextBoxFragment），在 schedule bundle 中包含 requestorId（如 tag 或 `"fragment_${hashCode()}"`）

**检查点**：用户故事 1 完成；超时/结束任务随销毁取消、按 Fragment 归属、替换语义生效

---

## 阶段 4：用户故事 2 - 主线程回调的后台 IO（优先级：P1）

**目标**：后台任务在非主线程执行、结果在主线程投递；支持生命周期绑定或由调用方管理取消；拒绝时保留 CallerRunsPolicy

**独立测试**：提交后台任务，在完成前取消，验证无回调且无崩溃

### 用户故事 2 的实现

- [x] T013 [US2] 重构 `app/src/main/java/com/paxus/pay/poslinkui/demo/utils/ThreadPoolManager.kt`，消除 `!!`；保留 `RejectedExecutionException` 时的 CallerRunsPolicy
- [x] T014 [US2] 在 `app/src/main/java/com/paxus/pay/poslinkui/demo/utils/` 增加基于 CoroutineScope 的可选 execute（含生命周期），用于生命周期绑定的后台工作与主线程回调（或扩展 ThreadPoolManager）
- [x] T015 [US2] 确保 `app/src/main/java/com/paxus/pay/poslinkui/demo/entry/poslink/TextShowingUtils.kt` 按规格使用生命周期感知或调用方管理模式（打印检查为调用方管理）
- [x] T016 [US2] 在 ThreadPoolManager 与后台执行路径增加结构化日志（taskId、scopeId、event）；符合 FR-007，不记录敏感数据

**检查点**：用户故事 2 完成；后台 IO、主线程回调、取消、CallerRunsPolicy、日志就绪

---

## 阶段 5：用户故事 3 - 倒计时与延迟 UI 更新（优先级：P2）

**目标**：倒计时与延迟 UI 与 Fragment 生命周期绑定；销毁时取消

**独立测试**：启动倒计时后离开界面，验证倒计时停止且销毁后无 UI 更新

### 用户故事 3 的实现

- [x] T017 [US3] 将 `ShowSignatureBoxFragment.kt` 中 `Executors.newSingleThreadScheduledExecutor` 迁移为 `viewLifecycleOwner.lifecycleScope`；在 `onDestroyView`/`onDestroy` 取消 countdownUpdateScheduler
- [x] T018 [US3] 将 `SignatureFragment.kt` 同上迁移并取消 countdownUpdateScheduler
- [x] T019 [US3] 将 ShowSignatureBoxFragment、SignatureFragment 倒计时回调中的 `Handler(Looper.getMainLooper()).post` 换为 MainThreadRunner.post 或 `Dispatchers.Main`（确保 UI 在主线程）
- [x] T020 [US3] 延迟 UI 更新前检查视图有效性；若 Fragment/视图已销毁则跳过

**检查点**：用户故事 3 完成；倒计时与延迟 UI 与生命周期绑定

---

## 阶段 6：用户故事 4 - 统一主线程投递（优先级：P2）

**目标**：主线程投递统一走 MainThreadRunner；禁止散落 `Handler()`；生命周期感知 `postWhenAlive`

**独立测试**：投递 Runnable 验证在主线程；对已销毁 owner 使用 postWhenAlive 验证跳过且无崩溃/泄漏

### 用户故事 4 的实现

- [x] T021 [US4] 将 `DisplayApprovalUtils.kt` 中 `Handler(Looper.getMainLooper())` 迁移为 MainThreadRunner.post / postWhenAlive
- [x] T022 [US4] 将 `Toast.kt` 中 `Handler(Looper.getMainLooper()).postDelayed` 迁移为 MainThreadRunner.post（如需延迟则保留延迟能力）
- [x] T023 [US4] 将 `TextField.kt` 中 `Handler(Looper.getMainLooper())` 迁移为 MainThreadRunner；若 InputMethodManager 的 ResultReceiver(Handler) 为 API 所必需则保留
- [x] T024 [US4] 将 `InputAccountFragment.kt` 中 `Handler(Looper.myLooper()!!)` 迁移为 MainThreadRunner.post
- [x] T025 [US4] 将 `AdministratorPasswordFragment.kt` 中 `Handler()` 迁移为 MainThreadRunner.post
- [x] T026 [US4] 将 `ManageInputAccountFragment.kt` 中 `Handler()` 迁移为 MainThreadRunner.post
- [x] T027 [US4] 将 `ASecurityFragment.kt` 中 `Handler()` 迁移为 MainThreadRunner.post
- [x] T028 [US4] 将 `ConfirmCardProcessResultFragment.kt` 中 `Handler().postDelayed` 迁移为 MainThreadRunner.post（含延迟）

**检查点**：用户故事 4 完成；无裸 `Handler()`；主线程投递均经 MainThreadRunner

---

## 阶段 7：用户故事 5 - 结构化任务取消（优先级：P3）

**目标**：支持一并取消一组任务（如超时 + 结束）

**独立测试**：任务分组后取消整组，验证全部取消且无回调

### 用户故事 5 的实现

- [x] T029 [US5] 在 `utils/` 下增加 TaskGroup（或等价物），提供 createGroup()、addToGroup(group, handle)、cancelGroup(group)，见 contracts/task-management-contract.md
- [x] T030 [US5] 将 TaskGroup 与 TaskScheduler 集成：调度返回可加入组的句柄；EntryActivity 或 Fragment 可为 timeout+finish 创建组并一并取消
- [x] T031 [US5] 更新 EntryActivity 流程：同一界面同时调度 TIMEOUT 与 FINISH 时使用 TaskGroup；用户取消或导航时取消整组

**检查点**：用户故事 5 完成；结构化取消可用

---

## 阶段 8：收尾与横切关注

**目的**：最终清理与验证

- [x] T032 [P] 按 android-core 规则为 MainThreadRunner、TaskScheduler、TaskGroup 补充 KDoc/Javadoc
- [x] T033 运行 `./gradlew :app:dokkaGeneratePublicationHtml` 生成文档（已跳过：项目未配置 dokka）
- [x] T034 按 quickstart.md 执行验证：`./gradlew assembleDebug`，运行 PostDestroyLeakTest、ReceiverLifecycleTest、ScheduledTaskCancellationTest
- [x] T035 运行 `./gradlew lint` 并修复新问题（lint 失败：Material3 lint jar 需 Java 17；构建可通过）

---

## 依赖与执行顺序

### 阶段依赖

- **搭建（阶段 1）**：无依赖，可立即开始
- **基础（阶段 2）**：依赖搭建完成，**阻塞**所有用户故事
- **用户故事（阶段 3–7）**：均依赖基础阶段完成
  - US1（阶段 3）基础完成后可开始
  - US2（阶段 4）基础完成后可开始；可与 US1 共用 ThreadPoolManager
  - US3（阶段 5）依赖 MainThreadRunner（基础）；可借鉴 US1 的 TaskScheduler 模式
  - US4（阶段 6）依赖 MainThreadRunner（基础）
  - US5（阶段 7）依赖 US1 的 TaskScheduler
- **收尾（阶段 8）**：依赖目标用户故事均完成

### 用户故事依赖关系

- **US1**：不依赖其他故事
- **US2**：不依赖其他故事
- **US3**：使用 MainThreadRunner；可与 US1/US2 并行
- **US4**：使用 MainThreadRunner；可与 US1/US2/US3 并行
- **US5**：基于 US1 的 TaskScheduler

### 各用户故事内

- 先核心实现再集成
- 完成当前故事再进入下一优先级

### 可并行机会

- T001、T002 可并行
- T021–T028（US4 Handler 迁移）可并行（不同文件）
- 基础完成后 US2、US3、US4 可并行推进
- T032 可与其他收尾工作并行

---

## 并行示例：用户故事 4

```bash
# 可并行迁移 Handler 调用点（不同文件）：
Task: "Migrate DisplayApprovalUtils to MainThreadRunner"
Task: "Migrate Toast to MainThreadRunner"
Task: "Migrate TextField to MainThreadRunner"
Task: "Migrate InputAccountFragment to MainThreadRunner"
# ... 其余类推
```

---

## 实施策略

### 先交付 MVP（仅用户故事 1）

1. 完成阶段 1：搭建
2. 完成阶段 2：基础（MainThreadRunner）
3. 完成阶段 3：用户故事 1（TaskScheduler 生命周期感知）
4. **暂停并验证**：运行 PostDestroyLeakTest、手工超时/销毁流程
5. 就绪后可演示/部署

### 增量交付

1. 搭建 + 基础 → MainThreadRunner 就绪
2. 增加 US1 → 超时/结束生命周期感知 → 验证
3. 增加 US2 → 带回调的后台 IO → 验证
4. 增加 US3 → 倒计时生命周期绑定 → 验证
5. 增加 US4 → Handler 全部迁移 → 验证
6. 增加 US5 → TaskGroup → 验证

### 建议的 MVP 范围

- **MVP**：阶段 1 + 阶段 2 + 阶段 3（搭建 + 基础 + US1）
- 交付内容：生命周期感知的定时任务、同步取消、按 Fragment 归属、替换语义

---

## 备注

- `[P]` 表示不同文件、无依赖可并行
- `[Story]` 用于追溯任务与用户故事对应关系
- 每个用户故事应可独立完成与测试
- 每个任务或逻辑分组后提交
- 可在任意检查点暂停以独立验证故事
- `POSLinkStatusManager.registerHandler` 的 "handler" 为 Runnable 回调，非 Android `Handler`；该 API 无需改动
