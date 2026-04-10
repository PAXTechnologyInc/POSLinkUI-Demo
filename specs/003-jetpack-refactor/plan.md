# 实施计划：Jetpack 可读性与内存优化重构

**分支**: `003-jetpack-refactor` | **日期**: 2026-03-19 | **规格**: [spec.md](spec.md)
**输入**：功能规格说明，来源 `specs/003-jetpack-refactor/spec.md`

## 摘要

在不改变功能与 UI 的前提下，执行 Jetpack 对齐重构，目标是提升代码可读性、收敛共享依赖（**Hilt-first**）、将轻量配置存储迁移到 **Jetpack DataStore**（按 [spec.md](spec.md) 中 `utils` 清单与 FR-010～FR-013），并建立可自动化验证的内存稳定性体系。计划采用「类级小步改造 + 基础设施分层迁移」：优先修复生命周期泄漏风险，再推进 Hilt 注入与 DataStore 收口，同步提升状态管理可测试性与路由/映射可维护性。

## 技术上下文

**语言/版本**: Kotlin 1.9.22 / 2.1.0，JVM target 1.8  
**主要依赖**: AndroidX Fragment/AppCompat/Lifecycle、Compose（已启用）、Hilt、Logger、Coil、POSLink constant  
**存储**: Jetpack DataStore（轻量配置/偏好；**不**引入 Room 或数据库结构变更；与 spec FR-012 对齐）  
**测试**: JUnit4、AndroidX Instrumentation、LeakCanary（debug）、Profiler 基线脚本、CI 内存门禁、Hilt/DataStore 相关单测与回归  
**目标平台**: Android minSdk 22, targetSdk 31  
**项目类型**: mobile-app（Android POS）  
**性能目标**: 20 次 Entry 流程切换内存增长 <= 10%；冷启动空闲 5 分钟内存波动 ±10%  
**约束**: Neptune/POSLink 契约不变；功能/UI 不变；CI 内存验证失败禁止合并  
**规模/范围**: 100+ Fragment；核心重构类为 `EntryActivity`、`BaseEntryFragment`、`TransactionPresentation`、`TaskScheduler`、`SecondScreenInfoViewModel` 及安全类输入 Fragment；`utils/` 按 [spec.md](spec.md) 中分类清单分批迁移（纯工具保留、共享服务注入、DataStore 候选优先）

## 宪章检查

*门禁：必须在 Phase 0 研究前通过，Phase 1 设计后再次检查。*

### 设计前检查

- I. 需求可测试性：通过（FR-001~FR-013 与 SC-001~SC-010 可度量/可执行）
- II. 需求无歧义：通过（内存阈值、回收时限、CI 门禁已量化）
- III. 需求与技术无关：通过（spec 层不绑定实现框架）
- IV. 需求范围约束：通过（Out-of-Scope/Assumptions 明确）
- V. 合规性需求：通过（支付接口契约不变、生命周期安全约束明确）

### 设计后检查

- 通过：类级改造方案与 contracts 一致，未引入宪章违规项

## 项目结构

### 文档（本特性）

```text
specs/003-jetpack-refactor/
├── plan.md
├── research.md
├── data-model.md
├── class-modification-map.md
├── quickstart.md
├── contracts/
│   ├── lifecycle-memory-contract.md
│   └── dependency-injection-strategy.md
└── tasks.md
```

### 源码（仓库根目录）

```text
app/
└── src/main/java/com/paxus/pay/poslinkui/demo/
    ├── entry/
    │   ├── EntryActivity.kt
    │   ├── BaseEntryFragment.kt
    │   ├── TransactionPresentation.kt
    │   ├── UIFragmentHelper.kt
    │   └── security/
    │       ├── PINFragment.kt
    │       └── InputAccountFragment.kt
    ├── utils/
    │   ├── TaskScheduler.kt、TaskFactory.kt、ThreadPoolManager.kt …（完整清单见 spec.md「utils 目录组件分类清单」）
    │   ├── interfacefilter/（含 EntryActionFilterManager 等）
    │   └── format/、…
    ├── data/（规划：DataStore repository，实现时落地）
    └── viewmodel/
        └── SecondScreenInfoViewModel.kt
```

**结构决策**: 保持现有目录结构，仅做类内与小范围类间重构，确保可回滚与低风险。

## 类级改造计划

| Class | 修改颗粒度（小步） | 验收点 |
|---|---|---|
| `entry/TransactionPresentation.kt` | (1) 把 `contentObserver` 提升为类字段；(2) 注册/解绑成对出现（`onCreate` + `dismiss/onStop`）；(3) 收敛 `!!` 高风险点，改为空安全分支 | 关闭二屏后不再更新 UI；LeakCanary 无该类泄漏 |
| `entry/EntryActivity.kt` | (1) 清理顺序固定：先 receiver 再 scheduler 再 presentation；(2) `scheduler!!` 改安全访问；(3) `register/unregisterUIReceiver` 增加幂等守卫 | 多次 onNewIntent 与销毁流程无崩溃/重复注册 |
| `utils/TaskScheduler.kt` | (1) 去掉 `TASK?`/`ScheduledTask?` 可空 map；(2) 替换反射构建为工厂映射；(3) 失败路径统一日志与降级 | TIMEOUT/FINISH 行为一致；销毁后无悬挂任务 |
| `entry/BaseEntryFragment.kt` | (1) 提取 `response/key` listener 注册模板方法；(2) 统一 accepted/declined 处理入口；(3) 子类只覆写业务动作 | 子类行为不变；减少重复监听逻辑 |
| `viewmodel/SecondScreenInfoViewModel.kt` | (1) `combineUserData` 拆分纯函数；(2) 增加构建快照方法；(3) 保持对外 LiveData 接口稳定 | 状态显示一致；可新增单元测试 |
| `entry/security/PINFragment.kt` | (1) receiver 注册时机与释放对称（onStart/onStop 或守卫变量）；(2) 防止 Compose 重组导致重复发送键盘布局 | PIN 输入流程不变；无 receiver 泄漏 |
| `entry/security/InputAccountFragment.kt` | (1) 拆分 `registerSecurityReceivers/releaseSecurityResources`；(2) `ClssLightsViewStatusManager` 生命周期对齐；(3) 二屏状态更新入口聚合 | 刷卡/挥卡/手输提示正常；销毁后无回调 |
| `entry/UIFragmentHelper.kt` | (1) 巨型 action map 拆分为模块 provider；(2) 统一 unknown action fallback 日志；(3) 保留原 action->fragment 路由兼容 | 路由功能完全一致；错误定位更清晰 |

## 依赖与存储迁移策略（与 spec 对齐）

### 总体原则

- **Hilt-first**：共享服务通过 Hilt 提供；业务层不直接依赖具体 DI 框架实现（见 `contracts/dependency-injection-strategy.md`）。
- **DataStore-first（新代码）**：`SharedPreferences` 仅用于迁移期；`EntryActionFilterManager` 为首要 DataStore 迁移对象（spec FR-012）。
- **非机械全量注入**：纯工具类保留纯函数/object；`utils/` 分类以 spec 清单为实施基准。

### 基础设施迁移（摘要）

| 范围 | 分类 | 计划动作 | 验收点 |
|------|------|----------|--------|
| `ThreadPoolManager`、`BackgroundTaskRunner`、`MainThreadRunner` | 可注入共享服务 | 收敛为 Hilt 可注入能力 | 线程语义与行为不变 |
| `EntryActionFilterManager` | DataStore + 可注入 | SP → DataStore，保持 key/语义兼容 | 筛选与展示行为一致 |
| `interfacefilter/EntryActionAndCategoryRepository` | 静态仓储 | 按需抽象为可注入仓储；不强制 DataStore | 查询行为不变 |
| `FileLogAdapter`、`InterfaceHistory` 等 | 可注入/入口装配 | 减少散落 `new` | 日志与历史行为不变 |

## 阶段 0：研究产出

- [research.md](research.md) 完成：DI、内存门禁、生命周期策略、状态管理演进

## 阶段 1：设计与契约产出

- [data-model.md](data-model.md)
- [class-modification-map.md](class-modification-map.md)
- [contracts/lifecycle-memory-contract.md](contracts/lifecycle-memory-contract.md)
- [contracts/dependency-injection-strategy.md](contracts/dependency-injection-strategy.md)
- [quickstart.md](quickstart.md)

## 阶段 1：Agent 上下文更新

- 已执行：`.specify/scripts/bash/update-agent-context.sh cursor-agent`

## 阶段 2 规划截止

本命令止于规划阶段。下一步运行 `/speckit.tasks` 进行任务拆分。

## 架构一致性检查点

- 生命周期资源清理顺序必须一致：receiver -> scheduler -> presentation observer
- 状态管理对外契约保持稳定（`SecondScreenInfoViewModel` 对外仍提供 LiveData）
- 路由改造前后 `UIFragmentHelper` action 解析结果必须一致
- 迁移后的共享服务调用方不得继续直接构造原实现（纯工具类除外）；与 spec SC-008 一致
- 轻量配置存储统一通过 DataStore 访问层收口；与 spec FR-012、SC-009 一致
- `utils/` 分类与 spec 清单一致，或在 `class-modification-map.md` 中书面说明偏差

## 复杂度跟踪

无宪章违规，无例外说明。
