# 研究：Jetpack 可读性与内存优化重构

**特性**： 003-jetpack-refactor  
**日期**: 2026-03-19

## 1) DI Strategy for Jetpack Alignment

### Decision

采用 **Hilt-first** 作为依赖注入优先路线，同时保留 Koin/手动 DI 的降级路径，不在需求层锁死框架。

### Rationale

- 与 Jetpack `ViewModel`/Lifecycle 生态协同最好，降低样板代码
- 易于统一依赖入口，提升可测试性和可读性
- 通过“可替换契约”避免大规模一次性迁移风险

### Alternatives considered

- **Koin-first**：接入快，但对当前团队与现有架构收益不如 Hilt 稳定
- **Manual DI only**：短期简单，长期在 100+ Fragment 场景下维护成本较高

---

## 2) Memory Validation Gate

### Decision

内存验证采用 **本地可执行 + CI 门禁** 双模式，并共享统一阈值与同一组用例。

### Rationale

- 本地可快速回归，CI 可防止回归漏入主干
- 与 spec 中 SC-007（失败禁止合并）一致
- 适合支付项目的稳定性要求

### Alternatives considered

- **仅本地**：无法形成团队级质量闸门
- **仅人工**：成本高、稳定性差、可复现性不足

---

## 3) Lifecycle-safe Observer/Broadcast/Task Pattern

### Decision

统一生命周期规则：

- UI 观察者仅绑定可感知生命周期的 owner，禁止长期 `observeForever` 未释放
- 广播接收器必须“谁注册谁注销”，并在宿主销毁前清理
- 定时任务必须在宿主结束时主动取消，防止持有 Activity/Fragment

### Rationale

- 直接针对当前泄漏热点（`TransactionPresentation`、`TaskScheduler`、多处 receiver）
- 规则可测试、可代码审查、可自动化验收

### Alternatives considered

- 保留现状并增加文档约束：无法从机制上防回归

---

## 4) State Management Evolution

### Decision

保持现有 `LiveData` 兼容运行，按风险分层逐步引入 `StateFlow`（仅在计划/任务阶段确定具体落点）。

### Rationale

- 符合“功能/UI 不变”和渐进迁移要求
- 能提升测试可控性，不触发全量重构风险

### Alternatives considered

- 一次性全量迁移到 Flow：风险高、回归面过大
- 完全不演进：无法解决可测试性与可读性目标

---

## 5) Baseline & Rollback Notes

### Baseline snapshot

- 基线采集范围：`EntryActivity`/`TransactionPresentation`/`TaskScheduler` 相关流程
- 基线指标：冷启动空闲 5 分钟、20 次 Entry 流程切换、页面销毁后 30 秒泄漏检查
- 记录方式：本地脚本结果 + CI 报告归档（同阈值）

### Rollback strategy

- 每次类级改造保持单文件或单主题提交，失败可快速回退
- 若出现行为偏差，优先回滚最近一个类级步骤，而非整体回滚整个阶段
