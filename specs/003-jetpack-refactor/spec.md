# 功能规格：Jetpack 可读性与内存优化重构

**特性分支**: `003-jetpack-refactor`  
**创建日期**: 2025-03-19  
**状态**: 已补充 Hilt/DataStore/utils 清单（待实现与 tasks 对齐）  
**输入**：用户描述： "新增需求，我需要进行代码重构和技术重构，以寻求代码可读性和内存方面的优化，在不改变代码功能和ui的情况下，请帮我向jetpack方向靠齐，或者你觉得更好的实现方式，请浏览下此项目代码，给出你觉得最好的优化方式"

## 澄清说明

### 会话 2026-03-19

- Q: 内存测试的最终验收口径选哪种？ → A: 本地可执行 + CI 门禁
- Q: 在 plan 阶段希望优先哪种 DI 方案？ → A: Hilt 优先

## 用户场景与测试 *（必填）*

### 用户故事 1 - 可读性提升（优先级：P1）

开发者能更快理解 Entry 流程、状态传递与职责边界，降低维护成本与新人上手时间。

**优先级理由**： 可读性是后续重构与功能扩展的基础；100+ Fragment 的 Entry 流程若缺乏清晰边界，将增加修改风险。

**独立测试**： 代码审查通过；新成员可在 4 小时内理解核心 Entry 流程（从 Intent 到 Fragment 到结果回传）的职责划分与数据流。

**验收场景**：

1. **假如** 重构完成，**当** 进行代码审查，**那么** 职责边界清晰、无明显的逻辑与 UI 耦合
2. **假如** 新成员阅读 Entry 流程相关代码，**当** 4 小时内，**那么** 能描述核心流程与状态传递路径
3. **假如** 存在 BaseEntryFragment 与子类，**当** 查看继承关系，**那么** 可复用逻辑已抽取、重复代码减少

---

### 用户故事 2 - 内存安全（优先级：P1）

应用在长时间运行、多流程切换后无内存泄漏或异常增长，保证设备稳定性。

**优先级理由**： 支付类应用常长时间运行；内存泄漏会导致 OOM、卡顿，影响交易完成率。

**独立测试**： LeakCanary 在典型流程（金额输入→确认→完成）下无泄漏报告；连续执行 20 次 Entry 流程切换后，内存占用增长不超过基线 10%。

**验收场景**：

1. **假如** 典型 Entry 流程（金额输入→确认→完成），**当** 执行并退出，**那么** LeakCanary 无泄漏报告
2. **假如** 应用已启动，**当** 连续执行 20 次 Entry 流程切换（含第二屏展示），**那么** 内存占用增长不超过基线 10%
3. **假如** 状态观察者、广播接收器、异步任务，**当** 宿主（Activity/Fragment）销毁，**那么** 所有观察者已移除、广播已注销、任务已取消
4. **假如** 内存验证流程已配置，**当** 执行本地脚本或 CI 流水线，**那么** 可自动完成泄漏检测与内存增长校验，无需人工逐次操作

---

### 用户故事 3 - 架构一致性（优先级：P2）

状态管理与生命周期遵循统一模式，便于维护与测试。

**优先级理由**： 统一模式降低认知负担，便于编写自动化测试与排查问题。

**独立测试**： 关键路径有单元或集成测试覆盖；状态管理逻辑可独立于 UI 验证。

**验收场景**：

1. **假如** 核心状态管理逻辑，**当** 执行自动化测试，**那么** 测试通过且覆盖关键路径
2. **假如** 异步任务与超时逻辑，**当** 宿主销毁，**那么** 任务自动取消，无回调执行于已销毁对象
3. **假如** 状态传递与结果回传，**当** 查阅文档或代码，**那么** 有明确契约（输入、输出、生命周期）

---

### 用户故事 4 - 任务与广播生命周期（优先级：P2）

超时、取消、广播等异步逻辑在 Activity/Fragment 销毁时正确清理，无泄漏、无崩溃、无回调执行于已销毁对象。

**优先级理由**： 支付流程依赖超时与广播；若未正确清理，会导致崩溃或重复回调。

**独立测试**： 无泄漏、无崩溃、无回调执行于已销毁对象；LeakCanary 与 Profiler 验证。

**验收场景**：

1. **假如** 已注册的广播接收器，**当** 宿主销毁，**那么** 在同一上下文中正确注销
2. **假如** 已调度的超时/定时任务，**当** 宿主销毁，**那么** 任务已取消，不再执行
3. **假如** 已订阅的状态观察者，**当** 宿主销毁，**那么** 观察者已移除，不再接收更新

---

### 用户故事 5 - Jetpack 基础设施对齐（优先级：P1）

开发者能够使用 Hilt 管理共享依赖，并将轻量配置存储逐步迁移到 Jetpack DataStore，在不改变现有功能和 UI 的前提下，减少手动单例、静态状态和 Context 透传带来的维护成本与测试难度。

**优先级理由**： 项目已具备 Hilt 依赖与插件，但尚未形成实际注入闭环；`utils/` 中部分共享能力与 `SharedPreferences` 配置未统一到 Jetpack 推荐模式，依赖边界不清晰。

**独立测试**： 纳入范围的共享服务类可通过 Hilt 注入创建；配置读写通过统一的 DataStore 访问层完成；关键 Entry 流程行为与重构前一致。

**验收场景**：

1. **假如** 某共享组件承担线程调度、配置读写、仓储或状态协作职责，**当** 该组件被重构后，**那么** 它应通过 Hilt 提供并在调用方以注入方式使用
2. **假如** `utils/` 下存在纯格式化、纯转换、无外部依赖的工具类，**当** 评估其职责后，**那么** 它可以保留为纯工具而不被强制改造成注入对象
3. **假如** 项目中存在 `SharedPreferences` 轻量配置入口，**当** 完成迁移后，**那么** 新旧字段语义、默认值与外部行为保持兼容
4. **假如** EntryActivity、Fragment、ViewModel 或其他消费方使用共享服务，**当** 完成迁移后，**那么** 调用方不再直接依赖具体实现构造，而是通过注入或抽象边界获取依赖

---

### 边界情况

- **第二屏 Presentation 与 ViewModel 观察**：当第二屏展示时，观察者必须随 Presentation 销毁而移除，否则持有 ViewModel 与 Activity 引用导致泄漏
- **Fragment 替换与 FragmentResultListener**：Fragment 替换时，旧 Fragment 的 ResultListener 应由 FragmentManager 自动清理；若使用 `observeForever`，须手动移除
- **TaskScheduler 与 Activity 生命周期**：Activity 销毁时 TaskScheduler 须 shutdown，确保无任务持有 Activity 引用
- **多 Fragment 共享 ViewModel**：Activity 级 ViewModel 被多 Fragment 共享时，须确保观察者随各自 Fragment 生命周期移除

## 需求 *（必填）*

### 功能需求

- **FR-001**: 状态观察者必须在宿主销毁时移除，不得持有已销毁对象的引用
- **FR-002**: 异步任务（超时、定时）必须在宿主销毁时取消
- **FR-003**: 广播接收器必须在注册的同一上下文中注销
- **FR-004**: 业务逻辑与 UI 展示须有清晰边界，便于单测与复用
- **FR-005**: Entry 流程的状态传递与结果回传须有明确契约
- **FR-006**: 重构不得改变现有功能与 UI 表现；Neptune/POSLink 接口不变
- **FR-007**: 必须建立可重复执行的内存验证流程，覆盖冷启动、典型交易流程、连续流程切换、页面退出回收四类场景
- **FR-008**: 内存验证必须支持本地执行与 CI 门禁两种模式，且两者使用同一组验收阈值
- **FR-009**: 依赖管理方案必须支持后续替换与渐进迁移，不将业务层与单一依赖注入框架强绑定
- **FR-010**: 对于具有线程调度、系统服务访问、配置读写、有状态协作或共享仓储职责的 `utils/` 组件，必须优先采用 Hilt 注入管理；纯工具类按本 spec「utils 目录组件分类清单」保留，不强制注入
- **FR-011**: `utils/` 下各类必须完成职责分类（纯工具 / 可注入共享服务 / UI 辅助 / DataStore 候选），分类结果与上表不一致时须在 `class-modification-map.md` 中说明
- **FR-012**: 已识别的 `SharedPreferences` 轻量配置入口必须迁移到 Jetpack DataStore，并保持 key、默认值与行为兼容；本清单中 **DataStore 迁移候选** 以 `EntryActionFilterManager` 为首要对象
- **FR-013**: Hilt 与 DataStore 的引入不得改变现有 Entry 流程、POSLink/Neptune 契约、页面跳转、结果回传与 UI 表现

### 关键实体

- **Entry 流程**：从 Intent 进入 EntryActivity，经 UIFragmentHelper 映射到 Fragment，用户操作后通过 FragmentResultListener 或 Broadcast 回传结果；涉及 BaseEntryFragment、各类 Confirm/Text/Security/Option Fragment
- **状态管理**：SecondScreenInfoViewModel 聚合 title、amount、msg、status 等，供主屏与第二屏展示；观察者须随生命周期清理
- **任务调度**：TaskScheduler 管理超时与 Finish 任务；持有 Activity 引用，须在 onDestroy 时 shutdown
- **广播**：StatusBroadcastReceiver、ResponseBroadcastReceiver 等，在 EntryActivity 与部分 Security Fragment 中注册；须在 onDestroy 时 unregister

### utils 目录组件分类清单（Jetpack 对齐实施基准）

本清单基于 `app/src/main/java/com/paxus/pay/poslinkui/demo/utils/` 当前文件划分职责与迁移优先级。**不强制**将纯工具类改为 Hilt 注入对象；**优先**迁移可注入共享服务与配置存储（SharedPreferences → DataStore）。

#### 分类说明

| 分类 | 含义 | 迁移策略 |
|------|------|----------|
| **纯工具类** | 无状态、无 Context、无外部副作用，或仅格式化/转换/常量表 | 保持 `object` / 数据类 / 顶层函数，不强制 Hilt |
| **可注入共享服务** | 线程池、调度、日志适配、有状态协作对象，或持有 Context / SharedPreferences | Hilt 提供；调用方通过注入或抽象边界获取 |
| **UI 辅助类** | 强依赖 Activity、Fragment、View、FragmentTransaction、Coil 等 UI 或动画 | 随入口层注入或按需构造，避免不当全局单例 |
| **DataStore 迁移候选** | 当前使用 `SharedPreferences` 的轻量配置 | 迁移到 Jetpack DataStore，保持 key 与语义兼容 |

#### 按文件分类（相对路径以 `.../demo/utils/` 为根）

| 文件 | 建议分类 | 备注 |
|------|----------|------|
| `TaskScheduler.kt` | 可注入共享服务 | 与 Activity 生命周期绑定；配合 `TaskFactory` |
| `TaskFactory.kt` | 可注入共享服务 | 已使用 `@Inject` |
| `TaskGroup.kt` | 纯工具类 | 任务/常量分组 |
| `ThreadPoolManager.kt` | 可注入共享服务 | 线程池单例 |
| `BackgroundTaskRunner.kt` | 可注入共享服务 | 后台任务入口 |
| `MainThreadRunner.kt` | 可注入共享服务 | 主线程派发 |
| `Logger.kt` | 纯工具类（薄封装） | 委托 Orhanobut；可保留 object，或通过 Hilt module 统一装配日志适配 |
| `FileLogAdapter.kt` | 可注入共享服务 | 文件 IO、线程池、异常处理；宜由入口装配而非散落 new |
| `InterfaceHistory.kt` | 可注入共享服务 | 有状态栈；宜按流程/作用域注入 |
| `interfacefilter/EntryActionFilterManager.kt` | 可注入共享服务 + **DataStore 迁移候选** | 使用 `SharedPreferences`（`ENTRY_ACTION_FILTER_PREFS`） |
| `interfacefilter/EntryActionAndCategoryRepository.kt` | 纯工具类 / 静态仓储 | 内存常量表与 action 映射，**非** SharedPreferences；不纳入 DataStore，仅可按需抽象为可注入仓储 |
| `interfacefilter/EntryCategory.kt`、`EntryAction.kt` | 数据模型 | 不强制 Hilt |
| `StringUtils.kt`、`DateUtils.kt`、`CurrencyUtils.kt` | 纯工具类 | |
| `ValuePatternUtils.kt` | 纯工具类 | |
| `DeviceUtils.kt` | 纯工具类 | |
| `EntryRequestUtils.kt` | 纯工具类 | |
| `QrBitmapEncoder.kt` | 纯工具类 | |
| `format/PrintDataConverter.kt` | 纯工具类 | |
| `format/PrintDataItem.kt`、`PrintDataItemContainer.kt` | 数据/容器 | 不强制 Hilt |
| `BundleMaker.kt` | 纯工具类 | |
| `ViewUtils.kt` | UI 辅助类 | |
| `EntryActivityActionBar.kt` | UI 辅助类 | |
| `Toast.kt`（含 `ToastCenter`） | UI 辅助类 | |
| `AnimationSupport.kt` | UI 辅助类 | 含 Coil `ImageLoader` 等可变状态，与 `AnimationPolicy` 协同 |
| `AnimationPolicy.kt`、`AnimationLogger.kt` | 纯工具类 / 策略与日志 | `currentAnimationPolicy` 为全局可变，迁移时评估是否收敛到可注入配置 |
| `SelectOptionContent.kt` | 纯工具类 | 静态选项映射 |

**实施说明**：上表为实施基准；若某文件在实现阶段职责变化，须在 `class-modification-map.md` 中更新分类与原因。

## 成功标准 *（必填）*

### 可衡量成果

- **SC-001**: LeakCanary 在典型流程（金额输入→确认→完成）下无泄漏报告
- **SC-002**: 连续执行 20 次 Entry 流程切换后，内存占用增长不超过基线 10%
- **SC-003**: 所有 Entry 流程行为与重构前一致（回归测试通过）
- **SC-004**: 核心状态管理逻辑有可执行的自动化测试
- **SC-005**: 冷启动后连续空闲 5 分钟，内存占用波动保持在基线 ±10% 以内
- **SC-006**: 完成页面退出后 30 秒内，已销毁页面不再出现在内存泄漏报告中
- **SC-007**: 每次代码合并前，CI 自动执行内存验证并全部通过；失败时禁止合并
- **SC-008**: 本清单中标注为「可注入共享服务」的类完成 Hilt 接入后，调用方不再直接 `new` 或依赖隐式单例构造（纯工具类除外）
- **SC-009**: `EntryActionFilterManager` 所涉配置读写迁移至 DataStore 后，行为与现有筛选/展示一致，且无新增 SharedPreferences 直接写入
- **SC-010**: `utils/` 分类清单与代码实现一致，或在 `class-modification-map.md` 中有书面偏差说明

## 范围外

- UI 迁移（由 002-compose-migration 负责）
- Neptune/POSLink SDK 接口变更
- 新增业务功能

## 假设前提

- 项目已为 Kotlin；002-compose-migration 与本重构可并行或先后进行
- 实施时优先采用 Jetpack 组件（ViewModel、StateFlow、Lifecycle、Hilt、DataStore）实现上述需求；`utils/` 分类以本 spec 清单为实施基准
- LeakCanary 可集成于 debug 构建用于验证
- 基线内存为重构前单次流程切换后的内存占用
- 依赖注入方案（如 Hilt/Koin/手动 DI）在 `/speckit.plan` 阶段按侵入性、可维护性、迁移成本进行决策
- 当前偏好是 Hilt 优先，若迁移成本或兼容性不满足约束，可降级到 Koin 或手动 DI

## 实现选项（在 Plan 阶段决策）

以下为可选技术方向，仅作为规划输入，不构成功能强制要求：

| 领域 | 可选方案 | 说明 |
|------|----------|------|
| 依赖注入 | Hilt、Koin、手动 DI | 当前偏好 Hilt；最终以不改变功能/UI 与可维护性为准 |
| 状态管理 | LiveData、StateFlow | 可渐进迁移，保证现有行为一致 |
| 任务调度 | 现有 TaskScheduler、协程作用域 | 需满足生命周期自动取消和可测试性 |
| 轻量存储 | DataStore、SharedPreferences | 新代码优先 DataStore；既有 SP 按本 spec 清单迁移 |
| `utils/` 治理 | 纯工具保留、共享服务注入 | 以本 spec「utils 目录组件分类清单」为实施基准 |