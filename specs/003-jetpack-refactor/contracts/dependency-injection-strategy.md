# 契约：依赖注入策略（Hilt 优先、可替换）

**特性**：003-jetpack-refactor  
**目的**：在不改变功能/UI 的前提下，定义依赖管理的可替换策略与迁移边界。

## 1. 策略契约

- 默认策略：Hilt-first（用于新建或重构后的依赖入口）。
- 替代策略：Koin 或手动 DI 可作为降级路径。
- 禁止业务逻辑层与单一 DI 框架强耦合。

## 2. 边界契约

- UI 层仅依赖抽象接口，不直接依赖具体实现细节。
- 设备能力调用通过 adapter/interface 暴露，避免 UI/Domain 直连设备 SDK。
- 依赖图调整不得改变对外行为与 UI 输出。

## 3. 迁移契约

- 允许增量迁移，不要求一次性替换全部注入点。
- 旧实现与新实现可并存，但需保持行为一致。
- 每次迁移必须可回滚，且有对应验证用例。

## 4. 验证契约

- 重构后必须通过：功能回归、生命周期安全、内存门禁。
- 若 DI 方案导致约束不满足（侵入性高/回归风险高），必须切换到备选方案。

## 5. Hilt 优先检查点

- 先在新增或重构组件中接入 Hilt，不一次性替换全量注入点。
- 若 Hilt 迁移阻塞发布节奏，则在同一抽象边界下回退到 Koin/手动 DI。
- 任何 DI 方案切换不得改变 `EntryActivity` 到 `Fragment` 的外部行为与参数契约。

## 6. 本轮落地范围（US5）

- 已落地 Application/Activity/Fragment 注入入口：`DemoApplication`、`MainActivity`、`EntryActivity`、`settings` 下 Filter 相关 Fragment。
- 已落地仓储边界：`EntryActionCatalogRepository` + `StaticEntryActionCatalogRepository`（适配现有静态 action map）。
- 已落地配置存储边界：`EntryActionStateStore` + `DataStoreEntryActionStateStore`（替换 `EntryActionFilterManager` 中 `SharedPreferences`）。
- 已落地模块绑定：`di/EntryInfrastructureModule.kt`。
- 已落地执行入口收敛：`ThreadPoolManager` 可注入；`AppExecutionCoordinator` 统一后台/主线程入口。

## 7. 暂缓项（记录）

- `EntryActionAndCategoryRepository` 仍保留静态常量表实现（理由：体量大、行为稳定、改写风险高），通过仓储适配层进入依赖图，后续可逐步拆分。
- `MainThreadRunner`、`BackgroundTaskRunner` 仍保留原工具实现；当前由 `AppExecutionCoordinator` 提供注入入口，后续再评估完全对象化迁移收益。

## 8. 回退条件与策略

- 若 DataStore 迁移导致 action 开关行为回归（筛选页状态异常、默认值不兼容），在保持接口不变前提下可临时回退到旧存储实现。
- 若 Hilt 注入导致入口链路不稳定（启动异常或 Fragment 注入失败），优先回退新增绑定模块，再保留原调用路径，确保对外行为不变。
- 任一回退必须在 `tasks.md` 与变更记录中写明：触发条件、影响范围、后续替换计划。
