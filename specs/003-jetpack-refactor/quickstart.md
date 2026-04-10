# 快速开始：003-jetpack-refactor

**特性**： 003-jetpack-refactor  
**日期**: 2026-03-19

## 前置条件

- 当前分支：`003-jetpack-refactor`
- 可正常构建：`./gradlew assembleDebug`
- 已具备本地调试设备或模拟器

## 1) 基础构建与回归

1. 构建 Debug 包：
   - `./gradlew assembleDebug`
2. 运行现有关键 Entry 流程，记录重构前基线（功能与 UI 表现）。

## 2) 生命周期安全检查（重点文件）

- `app/src/main/java/com/paxus/pay/poslinkui/demo/entry/TransactionPresentation.kt`
- `app/src/main/java/com/paxus/pay/poslinkui/demo/entry/EntryActivity.kt`
- `app/src/main/java/com/paxus/pay/poslinkui/demo/entry/BaseEntryFragment.kt`
- `app/src/main/java/com/paxus/pay/poslinkui/demo/utils/TaskScheduler.kt`

检查目标：
- observer/receiver/task 的注册与释放对称
- 宿主销毁后无回调触达已销毁对象

## 2.1) 类级改造顺序（小颗粒）

按以下顺序逐类改造并回归：

1. `TransactionPresentation.kt`
2. `EntryActivity.kt`
3. `TaskScheduler.kt`
4. `BaseEntryFragment.kt`
5. `SecondScreenInfoViewModel.kt`
6. `PINFragment.kt`
7. `InputAccountFragment.kt`
8. `UIFragmentHelper.kt`

每改完一个类，至少执行该类关联流程一次再继续下一类。详细改造步骤见 [class-modification-map.md](class-modification-map.md)。

## 3) 本地内存验证

至少执行以下场景并记录结果：

1. 冷启动后空闲 5 分钟（内存波动在基线 ±10%）
2. 典型 Entry 流程（金额输入 -> 确认 -> 完成）无泄漏
3. 连续 20 次 Entry 流程切换（增幅 <= 10%）
4. 页面销毁后 30 秒内无残留泄漏对象

## 4) CI 门禁接入要求

- 与本地使用同一阈值与同一组场景
- 任一场景失败则 CI 失败并阻止合并

### 建议命令占位（按仓库实际脚本替换）

- 本地内存验证：`./gradlew connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.paxus.pay.poslinkui.demo.MemoryVerificationTest`
- 循环切换验证：`./gradlew connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.paxus.pay.poslinkui.demo.EntryMemoryLoopTest`
- 销毁回收验证：`./gradlew connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.paxus.pay.poslinkui.demo.PostDestroyLeakTest`

## 4.1) 生命周期核验步骤

1. 启动交易流程并触发 `EntryActivity` 销毁，确认无 receiver 泄漏日志。
2. 进入含二屏流程并退出，确认 `TransactionPresentation` 观察者解绑。
3. 触发超时任务后立即退出页面，确认任务不会继续触发 UI 回调。

## 5) DI 方案落地顺序（Plan 输入）

1. 默认尝试 Hilt-first
2. 如侵入性/兼容性不满足约束，降级 Koin
3. 极小范围可先手动 DI，后续再统一

## 6) Hilt / DataStore 对齐验证（US5）

1. 确认入口注解：
   - `DemoApplication` 使用 `@HiltAndroidApp`
   - 相关入口页（如 `MainActivity`、筛选相关 Fragment）具备 `@AndroidEntryPoint`
2. 运行单元测试验证注入边界：
   - `./gradlew :app:testDebugUnitTest --tests "*DependencyBoundaryTest"`
3. 运行 DataStore 状态存储契约测试：
   - `./gradlew :app:testDebugUnitTest --tests "*EntryActionStateStoreContractTest"`
4. 手工回归筛选页行为：
   - 进入 Entry Action 筛选页面，切换某 action 开关后重进页面，状态应保持一致
   - 已有行为（默认值、可见项）与迁移前一致
5. 全量门禁：
   - `./gradlew :app:assembleDebug :app:testDebugUnitTest`

## 参考

- [spec.md](spec.md)
- [plan.md](plan.md)
- [research.md](research.md)
- [data-model.md](data-model.md)
- [contracts/lifecycle-memory-contract.md](contracts/lifecycle-memory-contract.md)
- [contracts/dependency-injection-strategy.md](contracts/dependency-injection-strategy.md)
