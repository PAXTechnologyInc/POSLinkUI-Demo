# 任务清单：Jetpack 可读性与内存优化重构

**输入**：设计文档目录 `/specs/003-jetpack-refactor/`  
**前置条件**：plan.md、spec.md、research.md、data-model.md、contracts/、class-modification-map.md

**测试**：包含测试任务（因 spec 明确要求自动化测试与内存门禁）。  
**组织方式**：任务按用户故事分组，便于独立实现与验证。

## 格式约定：`[ID] [P?] [Story] 描述`

- **[P]**：可并行（不同文件、无依赖）
- **[Story]**：用户故事标签（`[US1]`、`[US2]`、`[US3]`、`[US4]`、`[US5]`）
- 每个任务均包含明确文件路径

## 阶段 1：搭建（共享基础设施）

**目的**：建立重构执行与验证基础

- [X] T001 更新特性文档索引：`D:/Project/US/POSLinkUI-Demo/specs/README.md`
- [X] T002 [P] 在 research.md 中补充重构基线说明：`D:/Project/US/POSLinkUI-Demo/specs/003-jetpack-refactor/research.md`
- [X] T003 [P] 在 class-modification-map.md 中补充类级执行说明：`D:/Project/US/POSLinkUI-Demo/specs/003-jetpack-refactor/class-modification-map.md`
- [X] T004 在 quickstart.md 中定义内存验证命令占位：`D:/Project/US/POSLinkUI-Demo/specs/003-jetpack-refactor/quickstart.md`

---

## 阶段 2：基础（阻塞性前置）

**目的**：所有用户故事共享的基础能力，必须先完成

- [X] T005 创建生命周期绑定工具抽象：`D:/Project/US/POSLinkUI-Demo/app/src/main/java/com/paxus/pay/poslinkui/demo/entry/LifecycleBindingRegistry.kt`
- [X] T006 [P] 创建任务创建工厂：`D:/Project/US/POSLinkUI-Demo/app/src/main/java/com/paxus/pay/poslinkui/demo/utils/TaskFactory.kt`
- [X] T007 [P] 创建内存验证仪器测试骨架：`D:/Project/US/POSLinkUI-Demo/app/src/androidTest/java/com/paxus/pay/poslinkui/demo/MemoryVerificationTest.kt`
- [X] T008 为 Entry 流程状态添加通用测试夹具：`D:/Project/US/POSLinkUI-Demo/app/src/test/java/com/paxus/pay/poslinkui/demo/entry/EntryFlowTestFixture.kt`
- [X] T009 在 CI 工作流中增加内存验证门禁任务定义：`D:/Project/US/POSLinkUI-Demo/.github/workflows/android.yml`

**检查点**：基础就绪，可开始用户故事工作。

---

## 阶段 3：用户故事 1 - 可读性提升（优先级：P1） 🎯 MVP

**目标**：提升核心 Entry 链路代码可读性与职责边界清晰度

**独立测试**：代码审查可通过；新人可在 4 小时内描述核心流程职责与状态传递

### 用户故事 1 的测试

- [X] T010 [P] [US1] 为 UIFragmentHelper 路由解析添加单元测试：`D:/Project/US/POSLinkUI-Demo/app/src/test/java/com/paxus/pay/poslinkui/demo/entry/UIFragmentHelperTest.kt`
- [X] T011 [P] [US1] 为 BaseEntryFragment 监听器分发添加单元测试：`D:/Project/US/POSLinkUI-Demo/app/src/test/java/com/paxus/pay/poslinkui/demo/entry/BaseEntryFragmentListenerTest.kt`

### 用户故事 1 的实现

- [X] T012 [US1] 重构 action 映射提供方：`D:/Project/US/POSLinkUI-Demo/app/src/main/java/com/paxus/pay/poslinkui/demo/entry/UIFragmentHelper.kt`
- [X] T013 [US1] 引入统一的 resolveFragment(action) 路径：`D:/Project/US/POSLinkUI-Demo/app/src/main/java/com/paxus/pay/poslinkui/demo/entry/UIFragmentHelper.kt`
- [X] T014 [US1] 抽取 response/key 监听器注册方法：`D:/Project/US/POSLinkUI-Demo/app/src/main/java/com/paxus/pay/poslinkui/demo/entry/BaseEntryFragment.kt`
- [X] T015 [US1] 规范化 accepted/declined 处理入口：`D:/Project/US/POSLinkUI-Demo/app/src/main/java/com/paxus/pay/poslinkui/demo/entry/BaseEntryFragment.kt`
- [X] T016 [US1] 为 EntryActivity 增加可读的 cleanupResources 流程：`D:/Project/US/POSLinkUI-Demo/app/src/main/java/com/paxus/pay/poslinkui/demo/entry/EntryActivity.kt`
- [X] T017 [US1] 更新 class-modification-map 中的类级重构说明：`D:/Project/US/POSLinkUI-Demo/specs/003-jetpack-refactor/class-modification-map.md`

**检查点**：US1 可独立评审与验证。

---

## 阶段 4：用户故事 2 - 内存安全（优先级：P1）

**目标**：消除生命周期泄漏风险并通过本地 + CI 内存门禁

**独立测试**：LeakCanary 无泄漏；20 次流程切换内存增长 <= 10%

### 用户故事 2 的测试

- [X] T018 [P] [US2] 添加重复 Entry 切换的仪器测试：`D:/Project/US/POSLinkUI-Demo/app/src/androidTest/java/com/paxus/pay/poslinkui/demo/EntryMemoryLoopTest.kt`
- [X] T019 [P] [US2] 添加销毁后泄漏检查的仪器测试：`D:/Project/US/POSLinkUI-Demo/app/src/androidTest/java/com/paxus/pay/poslinkui/demo/PostDestroyLeakTest.kt`
- [X] T020 [P] [US2] 添加调度器生命周期单元测试：`D:/Project/US/POSLinkUI-Demo/app/src/test/java/com/paxus/pay/poslinkui/demo/utils/TaskSchedulerTest.kt`

### 用户故事 2 的实现

- [X] T021 [US2] 重构 TransactionPresentation 观察者绑定/解绑生命周期：`D:/Project/US/POSLinkUI-Demo/app/src/main/java/com/paxus/pay/poslinkui/demo/entry/TransactionPresentation.kt`
- [X] T022 [US2] 减少 TransactionPresentation 中不安全的空断言：`D:/Project/US/POSLinkUI-Demo/app/src/main/java/com/paxus/pay/poslinkui/demo/entry/TransactionPresentation.kt`
- [X] T023 [US2] 将反射创建任务改为使用 TaskFactory：`D:/Project/US/POSLinkUI-Demo/app/src/main/java/com/paxus/pay/poslinkui/demo/utils/TaskScheduler.kt`
- [X] T024 [US2] 强化 EntryActivity 中调度器/资源关闭顺序：`D:/Project/US/POSLinkUI-Demo/app/src/main/java/com/paxus/pay/poslinkui/demo/entry/EntryActivity.kt`
- [X] T025 [US2] 为 PINFragment 增加 receiver 注册防护：`D:/Project/US/POSLinkUI-Demo/app/src/main/java/com/paxus/pay/poslinkui/demo/entry/security/PINFragment.kt`
- [X] T026 [US2] 为 InputAccountFragment 增加 receiver/资源对称释放流程：`D:/Project/US/POSLinkUI-Demo/app/src/main/java/com/paxus/pay/poslinkui/demo/entry/security/InputAccountFragment.kt`
- [X] T027 [US2] 在 quickstart.md 中接入内存门禁运行步骤：`D:/Project/US/POSLinkUI-Demo/specs/003-jetpack-refactor/quickstart.md`

**检查点**：US2 可通过本地与 CI 内存检查验证。

---

## 阶段 5：用户故事 3 - 架构一致性（优先级：P2）

**目标**：统一状态管理和依赖边界，提升可测试性

**独立测试**：核心状态管理逻辑具备自动化测试且行为与现网一致

### 用户故事 3 的测试

- [X] T028 [P] [US3] 添加 ScreenInfo 组合的单元测试：`D:/Project/US/POSLinkUI-Demo/app/src/test/java/com/paxus/pay/poslinkui/demo/viewmodel/SecondScreenInfoViewModelTest.kt`
- [X] T029 [P] [US3] 添加 DI 边界契约测试：`D:/Project/US/POSLinkUI-Demo/app/src/test/java/com/paxus/pay/poslinkui/demo/di/DependencyBoundaryTest.kt`

### 用户故事 3 的实现

- [X] T030 [US3] 重构 SecondScreenInfoViewModel 中屏幕信息组合辅助逻辑：`D:/Project/US/POSLinkUI-Demo/app/src/main/java/com/paxus/pay/poslinkui/demo/viewmodel/SecondScreenInfoViewModel.kt`
- [X] T031 [US3] 在 dependency-injection-strategy.md 中补充 Hilt 优先且可替换的 DI 说明：`D:/Project/US/POSLinkUI-Demo/specs/003-jetpack-refactor/contracts/dependency-injection-strategy.md`
- [X] T032 [US3] 在 plan.md 中补充架构一致性检查点：`D:/Project/US/POSLinkUI-Demo/specs/003-jetpack-refactor/plan.md`

**检查点**：US3 可通过测试与设计评审验证，且不改变 UI 行为。

---

## 阶段 6：用户故事 4 - 任务与广播生命周期（优先级：P2）

**目标**：异步任务与广播在宿主销毁时严格清理，避免越界回调

**独立测试**：无已销毁对象回调、无 receiver 泄漏、无调度残留任务

### 用户故事 4 的测试

- [X] T033 [P] [US4] 添加 receiver 生命周期集成测试：`D:/Project/US/POSLinkUI-Demo/app/src/androidTest/java/com/paxus/pay/poslinkui/demo/ReceiverLifecycleTest.kt`
- [X] T034 [P] [US4] 添加定时任务取消集成测试：`D:/Project/US/POSLinkUI-Demo/app/src/androidTest/java/com/paxus/pay/poslinkui/demo/ScheduledTaskCancellationTest.kt`

### 用户故事 4 的实现

- [X] T035 [US4] 在 EntryActivity 中落实 receiver 归属契约：`D:/Project/US/POSLinkUI-Demo/app/src/main/java/com/paxus/pay/poslinkui/demo/entry/EntryActivity.kt`
- [X] T036 [US4] 更新 lifecycle-memory-contract.md 中的生命周期契约：`D:/Project/US/POSLinkUI-Demo/specs/003-jetpack-refactor/contracts/lifecycle-memory-contract.md`
- [X] T037 [US4] 在 quickstart.md 中记录生命周期验证步骤：`D:/Project/US/POSLinkUI-Demo/specs/003-jetpack-refactor/quickstart.md`

**检查点**：US4 可独立验证生命周期清理正确性。

---

## 阶段 7：用户故事 5 - Jetpack 基础设施对齐（优先级：P1）

**目标**：按 [spec.md](spec.md) 中 FR-010～FR-013、utils 分类清单与 SC-008～SC-010，落地 Hilt 共享依赖与 DataStore 轻量配置迁移；不改变功能与 UI。

**独立测试**：Hilt 可注入、DataStore 读写测试通过；Entry 与筛选等行为与迁移前一致。

**前置说明**：T001～T041 对应既有内存/可读性/架构任务；本阶段为 spec 增补后的**新增**工作项，默认未勾选，直至实现完成。

### 用户故事 5 的测试

- [X] T042 [P] [US5] 在 class-modification-map.md 中记录 `utils/` 分类与 spec 清单对齐（含偏差说明）：`D:/Project/US/POSLinkUI-Demo/specs/003-jetpack-refactor/class-modification-map.md`
- [X] T043 [P] [US5] 扩展 DI 边界/Hilt 可替换契约测试：`D:/Project/US/POSLinkUI-Demo/app/src/test/java/com/paxus/pay/poslinkui/demo/di/DependencyBoundaryTest.kt`
- [X] T044 [P] [US5] 为 DataStore repository 添加单元测试：`D:/Project/US/POSLinkUI-Demo/app/src/test/java/com/paxus/pay/poslinkui/demo/data/`
- [X] T045 [P] [US5] 为迁移后的关键调用方补充回归测试：`D:/Project/US/POSLinkUI-Demo/app/src/test/java/com/paxus/pay/poslinkui/demo/`

### 用户故事 5 的实现

- [X] T046 [US5] 建立 `@HiltAndroidApp` Application 与 Manifest 入口（若已存在则对齐 Hilt 要求）：`D:/Project/US/POSLinkUI-Demo/app/src/main/AndroidManifest.xml`、`D:/Project/US/POSLinkUI-Demo/app/src/main/java/com/paxus/pay/poslinkui/demo/`
- [X] T047 [US5] 新增 `di/` 下 Hilt modules，提供线程调度、仓储等共享绑定：`D:/Project/US/POSLinkUI-Demo/app/src/main/java/com/paxus/pay/poslinkui/demo/di/`
- [X] T048 [US5] 将 `ThreadPoolManager`、`BackgroundTaskRunner`、`MainThreadRunner` 等收敛为可注入能力：`D:/Project/US/POSLinkUI-Demo/app/src/main/java/com/paxus/pay/poslinkui/demo/utils/`
- [X] T049 [US5] 将 `interfacefilter/EntryActionFilterManager` 配置从 SharedPreferences 迁移至 DataStore，并保持可注入：`D:/Project/US/POSLinkUI-Demo/app/src/main/java/com/paxus/pay/poslinkui/demo/utils/interfacefilter/`
- [X] T050 [US5] 按需将 `EntryActionAndCategoryRepository` 抽象为可注入仓储（不改变对外查询语义）：`D:/Project/US/POSLinkUI-Demo/app/src/main/java/com/paxus/pay/poslinkui/demo/utils/interfacefilter/`
- [X] T051 [US5] 更新 `EntryActivity`、相关 Fragment/ViewModel 等对共享服务的获取方式（注入或显式边界）：`D:/Project/US/POSLinkUI-Demo/app/src/main/java/com/paxus/pay/poslinkui/demo/entry/` 等
- [X] T052 [US5] 更新 `contracts/dependency-injection-strategy.md`：记录已落地范围、暂缓项与回退条件：`D:/Project/US/POSLinkUI-Demo/specs/003-jetpack-refactor/contracts/dependency-injection-strategy.md`
- [X] T053 [US5] 更新 `quickstart.md`：补充 Hilt/DataStore 验证与回归步骤：`D:/Project/US/POSLinkUI-Demo/specs/003-jetpack-refactor/quickstart.md`

### 用户故事 5 的交付门禁

- [X] T054 [US5] 执行 `./gradlew :app:assembleDebug :app:testDebugUnitTest` 直至通过：`D:/Project/US/POSLinkUI-Demo`
- [X] T055 [P] [US5] 若涉及 lint 基线或大量资源变更，执行 `./gradlew :app:lintDebug`（JDK 17+）并处理新增问题：`D:/Project/US/POSLinkUI-Demo`（已执行；失败项为既有基线外资源问题，未由本次改动引入）
- [X] T056 [US5] 为新增 public API 补充 KDoc 并执行 `./gradlew :app:dokkaGeneratePublicationHtml`：`D:/Project/US/POSLinkUI-Demo`
- [X] T057 [US5] US5 完成后执行 plan/spec/tasks 一致性终检（增补任务）：`D:/Project/US/POSLinkUI-Demo/specs/003-jetpack-refactor/{plan.md,spec.md,tasks.md}`

**检查点**：US5 满足 spec FR-010～FR-013 与 SC-008～SC-010 的可验证口径。

---

## 阶段 8：收尾与横切关注

**目的**：全局收尾与交付前核验

- [X] T038 [P] 全面更新 checklists/requirements.md 回归清单：`D:/Project/US/POSLinkUI-Demo/specs/003-jetpack-refactor/checklists/requirements.md`
- [X] T039 [P] 同步 research.md 最终实现说明：`D:/Project/US/POSLinkUI-Demo/specs/003-jetpack-refactor/research.md`
- [X] T040 端到端校验 quickstart 并调整命令：`D:/Project/US/POSLinkUI-Demo/specs/003-jetpack-refactor/quickstart.md`
- [X] T041 对 plan.md 做 plan/spec/tasks 一致性终检：`D:/Project/US/POSLinkUI-Demo/specs/003-jetpack-refactor/plan.md`

---

## 依赖与执行顺序

### 阶段依赖

- 阶段 1（搭建）：无依赖
- 阶段 2（基础）：依赖阶段 1，阻塞所有用户故事
- 阶段 3（US1）：依赖阶段 2
- 阶段 4（US2）：依赖阶段 2（基础完成后可与 US1 并行，建议在 US1 可读性整理后再做）
- 阶段 5（US3）：依赖阶段 2，部分依赖 US1（`BaseEntryFragment` 可读性基础）
- 阶段 6（US4）：依赖阶段 2 与 US2 的生命周期实现
- 阶段 7（US5）：依赖阶段 2；建议与 US3 并行或在其后（共享 DI 边界）；实现 FR-010～FR-013 时须与 spec `utils` 清单一致
- 阶段 8（收尾）：依赖所选故事阶段全部完成（含 US5 若纳入本轮交付）

### 用户故事依赖关系

- **US1**：基础完成后可独立推进，建议作为 MVP
- **US2**：基础完成后可独立推进；引用生命周期抽象
- **US3**：基础完成后可独立推进；引用状态/DI 契约
- **US4**：依赖 US2 的生命周期实现以做最终验证
- **US5**：依赖基础能力；与 US3 在 DI 文档/测试上可协同；DataStore 迁移建议在单测就绪后推进

### 可并行机会

- 基础 `[P]`：T006、T007、T008、T009
- US1 `[P]`：T010、T011
- US2 `[P]`：T018、T019、T020
- US3 `[P]`：T028、T029
- US4 `[P]`：T033、T034
- 收尾 `[P]`：T038、T039
- US5 `[P]`：T042、T043、T044、T045；T055 可与 T054 错开（lint 仅在有变更时）

---

## 并行示例：用户故事 2

```bash
# 可并行测试
Task: "T018 [US2] Entry 流程重复切换仪器测试"
Task: "T019 [US2] 销毁后泄漏检查仪器测试"
Task: "T020 [US2] TaskScheduler 生命周期单元测试"

# 随后实现顺序
Task: "T021 [US2] 重构 TransactionPresentation 观察者生命周期"
Task: "T023 [US2] 以工厂替代 TaskScheduler 反射创建"
Task: "T024 [US2] 强化 EntryActivity 关闭顺序"
```

---

## 实施策略

### 先交付 MVP

1. 完成阶段 1 + 阶段 2
2. 交付阶段 3（US1）建立可读性基线
3. 在内存风险改动前独立验证

### 增量交付

1. US1（可读性）→ 稳定评审基线
2. US2（内存安全 + 门禁）
3. US3（架构一致性）
4. US4（生命周期收尾）
5. US5（Hilt + DataStore + `utils` 清单落地，见 spec FR-010～FR-013）
6. 阶段 8 收尾

### 备注

- `[P]` 任务针对不同文件，可并发执行
- 每个任务均包含明确文件路径，可直接交给执行模型
- 保持「功能与 UI 不变」作为每阶段回归门禁
