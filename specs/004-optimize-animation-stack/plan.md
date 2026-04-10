# 实施计划： 全量动画改造与性能降载

**分支**: `004-optimize-animation-stack` | **日期**: 2026-03-19 | **规格**: [spec.md](spec.md)
**输入**：功能规格说明，来源 `specs/004-optimize-animation-stack/spec.md`

## 摘要

在不影响原有动画预览效果的前提下，将项目内所有动画迁移到性能优先的实现方式，降低内存与 CPU 占用，并建立可执行的自测与性能验收流程。采用混合策略：Fragment 过渡保留 XML 但优化解析与窗口范围；CLSS 闪烁与小票滑出迁移到 ViewPropertyAnimator/ObjectAnimator；GIF 通过 Coil/Glide 配置限制解码尺寸与帧缓存；第三方不可控动画提供降级策略。

## 技术上下文

**语言/版本**: Kotlin 1.9.22 / 2.1.0，JVM target 11  
**主要依赖**: AndroidX Fragment/AppCompat、Coil、Glide、Compose（已启用）  
**存储**: N/A  
**测试**: JUnit4、AndroidX Instrumentation、手动视觉回归、Profiler/adb 性能采集  
**目标平台**: Android minSdk 22, targetSdk 31  
**项目类型**: mobile-app（Android POS）  
**性能目标**: CPU 下降 ≥25%、内存峰值下降 ≥20%、30 分钟温升降低 ≥2°C、主流程 90% 交互 <200ms  
**约束**: 动画视觉效果不变；Neptune/POSLink 契约不变；可控动画 100% 替换  
**规模/范围**: 8 个动画位点（A1–A8），涉及 MainActivity、EntryActivity、Toast、ClssLightsView、ConfirmReceiptViewFragment、TransactionPresentation、DisplayApprovalUtils

## 宪章检查

*门禁：必须在 Phase 0 研究前通过，Phase 1 设计后再次检查。*

### 设计前检查

- I. 需求可测试性：通过（FR-001~FR-009、FR-004a、FR-006a 与 SC-001~SC-007 可度量/可执行）
- II. 需求无歧义：通过（CPU/内存/温升/响应时延已量化；US1/US3 验收已替换模糊词为可度量标准）
- III. 需求与技术无关：通过（spec 层不绑定实现框架）
- IV. 需求范围约束：通过（Out-of-Scope、Assumptions、动画位点清单明确）
- V. 合规性需求：通过（渐进式迁移、接口契约不变、自测覆盖）

### 设计后检查

- 通过：research、data-model、contracts 与 plan 一致，未引入宪章违规项

## 项目结构

### 文档（本特性）

```text
specs/004-optimize-animation-stack/
├── plan.md
├── spec.md
├── research.md
├── data-model.md
├── quickstart.md
├── contracts/
│   └── animation-migration-contract.md
├── checklists/
│   └── requirements.md
└── tasks.md             # Phase 2 output (/speckit.tasks command)
```

### 源码（仓库根目录）

```text
app/src/main/java/com/paxus/pay/poslinkui/demo/
├── MainActivity.kt
├── entry/
│   ├── EntryActivity.kt
│   ├── TransactionPresentation.kt
│   ├── confirmation/
│   │   └── ConfirmReceiptViewFragment.kt
│   └── information/
│       └── DisplayApprovalUtils.kt
├── utils/
│   └── Toast.kt
└── view/
    ├── ClssLight.kt
    └── ClssLightsView.kt

app/src/main/res/
├── anim/
│   ├── anim_enter_from_bottom.xml
│   ├── anim_exit_to_bottom.xml
│   ├── anim_enter_from_right.xml
│   ├── anim_exit_to_left.xml
│   └── receipt_out.xml
└── raw/
    └── border_animated.gif
```

**结构决策**: 保持现有目录结构，仅做动画实现替换与资源优化，确保可回滚与低风险。

## 动画改造计划

| 位点 | 类/文件 | 当前实现 | 目标实现 | 视觉保真 |
|------|---------|----------|----------|----------|
| A1 | MainActivity | setCustomAnimations(XML) | 保留 XML 或迁移 Transition | 时长/方向 一致 |
| A2/A3 | EntryActivity | setCustomAnimations(XML) | 同上 | 同上 |
| A4 | Toast.kt | setCustomAnimations(XML) | 同上 | 同上 |
| A5 | ClssLightsView | AlphaAnimation INFINITE | ViewPropertyAnimator 或 ObjectAnimator | 500ms 周期、闪烁 |
| A6 | ConfirmReceiptViewFragment | AnimationUtils.loadAnimation | ViewPropertyAnimator.translationY | 3000ms、-90% |
| A7 | TransactionPresentation | Coil GIF | Coil + 解码尺寸/帧缓存优化 | 边框 GIF 不变 |
| A8 | DisplayApprovalUtils | Glide GIF | Glide + 降级策略 | 审批 GIF 或静态图降级 |

## 阶段 0：研究产出

- [research.md](research.md) 完成：动画清单、技术选型、混合策略、自测策略

## 阶段 1：设计与契约产出

- [data-model.md](data-model.md)
- [contracts/animation-migration-contract.md](contracts/animation-migration-contract.md)
- [quickstart.md](quickstart.md)

## 阶段 1：Agent 上下文更新

- 可选执行：`.specify/scripts/bash/update-agent-context.sh cursor-agent`

## 阶段 2 规划截止

本命令止于规划阶段。下一步运行 `/speckit.tasks` 进行任务拆分。

## 架构一致性检查点

- 动画替换后视觉效果必须与改造前一致（FR-004a）
- 每个动画位点须有自测或验收步骤（FR-006a）
- 生命周期内动画须正确取消，无泄漏
- 高风险页面须有回退策略（FR-007）；动画降级与性能事件须记录日志（FR-008）
- 跨机型兼容须满足 FR-009 / SC-007（机型矩阵书面化 + 不依赖多批次切图）

## 复杂度跟踪

无宪章违规，无例外说明。
