# 实施计划：补齐未实现 action/category

**分支**: `007-complete-entry-actions` | **日期**: 2026-03-31 | **规格**: [spec.md](spec.md)  
**输入**：功能规格说明，来源 `specs/007-complete-entry-actions/spec.md`

## 摘要

本迭代在 `v1.03` 文档范围内补齐 Entry 缺口动作，按“冻结基线 141/12 + 增量缺口并行处理”的策略交付。实现重点不是新增业务规则，而是打通 action 到页面交互与提交回写链路，并建立“12 个缺口全量人工验收 + 每个涉及 category 至少 1 个自动化回归动作”的验收闭环。

## 技术上下文

**语言/版本**: Kotlin（JVM 11，Android Gradle Kotlin DSL）  
**主要依赖**: AndroidX、Navigation Compose、`com.pax.us.ui:constant`（Entry 常量来源）  
**存储**: N/A（不新增持久化）  
**测试**: JUnit4 单测 + 手工交易流验收（按 action 清单）  
**目标平台**: Android POS 设备（minSdk 22, targetSdk 31）  
**项目类型**: mobile-app（单模块 Android 应用）  
**性能目标**: 补齐动作后不引入可感知的交互延迟；单动作触发到提交流程维持现有体验  
**约束**: 不改变 Neptune/POSLink 契约；不记录敏感支付数据；保持现有状态流和回调语义  
**规模/范围**: 首轮缺口 12 个动作，涉及 Text/Option/Confirmation/Information 四类入口

## 宪章检查

*门禁：必须在 Phase 0 研究前通过，Phase 1 设计后再次检查。*

- I. 需求可测试性：通过（FR 与 SC 均可执行、可量化）
- II. 需求无歧义：通过（clarify 已冻结基线、验收强度）
- III. 需求与技术无关：通过（spec 层未锁定实现细节）
- IV. 需求范围约束：通过（基线/增量口径已明确）
- V. 合规性需求：通过（支付敏感日志、流程幂等、失败处理在 plan 中保留门禁）

## 项目结构

### 文档（本特性）

```text
specs/007-complete-entry-actions/
├── plan.md
├── spec.md
├── research.md
├── data-model.md
├── quickstart.md
├── contracts/
│   └── entry-action-coverage-contract.md
├── checklists/
│   └── requirements.md
└── tasks.md             # Phase 2 output (/speckit.tasks)
```

### 源码（仓库根目录）

```text
app/src/main/java/com/paxus/pay/poslinkui/demo/
├── entry/
│   ├── EntryActionRegistry.kt
│   ├── EntryActivity.kt
│   ├── compose/
│   │   ├── EntryScreenRouter.kt
│   │   └── ExtendedEntryRoutes.kt
│   ├── text/
│   ├── option/
│   ├── confirmation/
│   └── information/
├── utils/interfacefilter/
│   └── EntryActionAndCategoryRepository.kt
└── viewmodel/
    └── EntryViewModel.kt

app/src/test/java/com/paxus/pay/poslinkui/demo/entry/
└── UIFragmentHelperTest.kt
```

**结构决策**: 保持既有 Entry 架构，仅补齐 action 映射、交互路由和提交回写，不引入新宿主层或新模块。

## 实施分阶段

### Phase 0 - 研究与清单收敛

- 对齐 `v1.03` 文档动作清单，固定首轮基线（141/12）。
- 在代码侧建立“常量定义 -> 可路由 -> 可提交 -> 可回写”四段核查表。
- 产出: [research.md](research.md)

### Phase 1 - 设计与契约

- 定义覆盖实体、状态与验收记录模型。
- 定义按 category 的最小自动化回归策略与通过判定。
- 明确动作补齐时的失败处理契约（未知 action、取消、非法输入、重复触发）。
- 产出:
  - [data-model.md](data-model.md)
  - [contracts/entry-action-coverage-contract.md](contracts/entry-action-coverage-contract.md)
  - [quickstart.md](quickstart.md)

### Phase 2 - 任务拆解原则（供 /speckit.tasks）

1. 先做清单核对与差异确认，再做代码补齐，最后做验收统计更新。
2. 代码补齐顺序按风险执行：Confirmation -> Option -> Text -> Information。
3. 每补齐一个动作，立即补对应验收记录，避免批量返工。
4. 每个涉及 category 至少补 1 个自动化回归样例。

## 架构一致性检查点

- `EntryActionRegistry` 与入口路由行为保持一致，不出现“可识别但不可交互”动作。
- 回写链路保持统一入口，不新增平行状态源。
- Confirmation 取消分支必须可回退且不悬挂流程。
- 错误反馈不暴露敏感字段（PAN/Track2/PIN/CVV）。

## 风险与缓解

- 文档与代码常量命名不一致：先建立映射表再编码，避免误补。
- 增量缺口持续出现：首轮结论按冻结基线，增量项单独标注状态并并行推进。
- 回归成本偏高：采用“全量人工 + 分类代表自动化”平衡策略。
- 状态回写遗漏：将“可回写”作为动作完成的强门禁。

## 复杂度跟踪

无宪章违规，无额外豁免。
