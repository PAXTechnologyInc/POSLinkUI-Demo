# Class Modification Map (Fine-grained)

**特性**： 003-jetpack-refactor  
**目标**: 将重构方案细化到“类 -> 修改步骤 -> 风险 -> 验收”。

## 1) `entry/TransactionPresentation.kt`

- **Step 1**: 将 `Observer` 保存为成员变量（避免局部匿名对象无法解绑）
- **Step 2**: 在 `onCreate` 绑定观察，在 `dismiss`/`onStop` 明确解绑
- **Step 3**: 收敛 `!!` 到可空安全分支，避免展示销毁后的 NPE
- **Risk**: 二屏状态更新丢失
- **Verify**: 二屏显示一致；关闭后二屏不再收更新；LeakCanary 无残留

## 2) `entry/EntryActivity.kt`

- **Step 1**: 提取 `cleanupResources()`，固定销毁顺序（receiver -> scheduler -> presentation）
- **Step 2**: `scheduler!!` 改 `scheduler?.`，并在必要路径 fail-safe
- **Step 3**: `register/unregisterUIReceiver` 增加注册状态标记防重复
- **Risk**: status 流程被提前清理
- **Verify**: onNewIntent 与 onDestroy 链路行为一致，无 IllegalArgumentException（receiver）

## 3) `utils/TaskScheduler.kt`

- **Step 1**: `TASK?` / `ScheduledTask?` 改为非空类型
- **Step 2**: 以显式工厂替代反射创建 `ScheduledTask`
- **Step 3**: schedule 失败路径统一日志并安全返回
- **Risk**: 超时任务未触发
- **Verify**: TIMEOUT/FINISH 与当前行为一致；销毁后任务全部取消

## 4) `entry/BaseEntryFragment.kt`

- **Step 1**: 抽取 listener 注册/清理模板方法
- **Step 2**: `onEntryAccepted/onEntryDeclined` 保持扩展点不变
- **Step 3**: 统一 key event 分发逻辑
- **Risk**: 子类无法接收广播结果
- **Verify**: 任意子类（文本输入、确认页）accepted/declined/keycode 行为不变

## 5) `viewmodel/SecondScreenInfoViewModel.kt`

- **Step 1**: 拆分组合函数为纯函数 `buildScreenInfo(...)`
- **Step 2**: `updateAllData` 复用统一组合路径
- **Step 3**: 新增单元测试入口（不改对外 API）
- **Risk**: 二屏字段组合顺序变更
- **Verify**: title/amount/msg/status 显示与当前一致

## 6) `entry/security/PINFragment.kt`

- **Step 1**: receiver 生命周期与 UI 生命周期对齐（`onStart/onStop` 或 guarded unregister）
- **Step 2**: Compose 回调中加“仅首次发送布局”守卫
- **Step 3**: 保持 `sendSetPinKeyLayout/sendSecurityArea` 时机不变
- **Risk**: PIN 面板位置上送时机变化
- **Verify**: 联机/脱机 PIN 输入、清除、取消、完成状态均正常

## 7) `entry/security/InputAccountFragment.kt`

- **Step 1**: 提取 receiver 注册与释放函数
- **Step 2**: `ClssLightsViewStatusManager` 释放动作标准化
- **Step 3**: 二屏更新逻辑集中到单入口，减少散点更新
- **Risk**: Contactless 灯状态错乱
- **Verify**: 插卡/挥卡/刷卡/手输提示一致，destroy 后无回调

## 8) `entry/UIFragmentHelper.kt`

- **Step 1**: 按模块拆 map（Text/Security/Option/Confirmation/Info/POSLink）
- **Step 2**: 建立统一 `resolveFragment(action)` 入口
- **Step 3**: 未知 action 输出结构化日志并回空
- **Risk**: action 路由遗漏
- **Verify**: 关键 action 回归通过；未知 action 行为保持“提示 NOT FOUND”

---

## Execution guardrails

- 每个类改造后立即执行一次该类相关主流程回归，再进入下一类
- 优先改造低耦合类（`TransactionPresentation`、`TaskScheduler`），最后改高耦合类（`UIFragmentHelper`）
- 对外行为（功能、UI、Intent 路由）必须保持与改造前一致

---

## US5 增补：`utils/` 与注入边界对齐（T042）

### 分类对齐（与 spec 清单一致）

- **可注入共享服务**：`TaskScheduler`、`TaskFactory`、`ThreadPoolManager`、`BackgroundTaskRunner`（经 `AppExecutionCoordinator` 统一注入入口）、`MainThreadRunner`（经 `AppExecutionCoordinator` 统一入口）、`FileLogAdapter`、`InterfaceHistory`、`EntryActionFilterManager`
- **DataStore 迁移候选**：`interfacefilter/EntryActionFilterManager`（原 SharedPreferences）
- **静态仓储（可注入适配）**：`interfacefilter/EntryActionAndCategoryRepository`（通过 `StaticEntryActionCatalogRepository` 适配注入，不强制改写原常量表）
- **纯工具/数据容器（保持）**：`StringUtils`、`DateUtils`、`CurrencyUtils`、`ValuePatternUtils`、`EntryRequestUtils`、`DeviceUtils`、`QrBitmapEncoder`、`TaskGroup`、`BundleMaker`、`PrintData*`、`EntryAction`、`EntryCategory`、`SelectOptionContent`
- **UI 辅助（保持）**：`EntryActivityActionBar`、`Toast`、`ViewUtils`、`AnimationSupport`

### 关键实现落点

- `data/DataStoreEntryActionStateStore.kt`：EntryAction 配置迁移到 DataStore
- `utils/interfacefilter/EntryActionFilterManager.kt`：从静态全局对象迁为可注入服务
- `utils/interfacefilter/EntryActionCatalogRepository.kt` + `StaticEntryActionCatalogRepository.kt`：建立可替换仓储边界
- `di/EntryInfrastructureModule.kt`：Hilt 绑定 DataStore 状态存储与仓储适配层
- `settings/*Filter*Fragment.kt` + `MainActivity.kt`：调用方改为注入消费路径
