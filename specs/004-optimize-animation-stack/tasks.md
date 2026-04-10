# 任务清单： 全量动画改造与性能降载

**输入**：设计文档目录 `/specs/004-optimize-animation-stack/`
**前置条件**: plan.md, spec.md, research.md, data-model.md, contracts/, quickstart.md

**测试**：包含测试任务（因 spec FR-006a 明确要求可执行自测用例，覆盖各动画位点视觉效果回归与性能基线对比）。

**组织方式**：任务按用户故事分组，便于独立实现与验证。

## 格式约定：`[ID] [P?] [Story] 描述`

- **[P]**：可并行（不同文件、无依赖）
- **[Story]**：用户故事标签（`[US1]`、`[US2]`、`[US3]`）
- 每个任务均包含明确文件路径

## 阶段 1：搭建（共享基础设施）

**目的**: 建立动画改造执行与验收基础

- [x] T001 在 `D:/Project/US/POSLinkUI-Demo/specs/README.md` 更新功能文档索引
- [x] T002 [P] 在 `D:/Project/US/POSLinkUI-Demo/specs/004-optimize-animation-stack/research.md` 增加动画盘点检查清单（当前主路径以 A1–A6 记录 visualSpec）
- [x] T003 在 `D:/Project/US/POSLinkUI-Demo/specs/004-optimize-animation-stack/quickstart.md` 增加基线采集占位说明

---

## 阶段 2：基础（阻塞性前置）

**目的**: 所有用户故事共享的基础能力，必须先完成

- [x] T004 按 quickstart.md 采集迁移前性能基线（CPU、内存）并写入 `D:/Project/US/POSLinkUI-Demo/specs/004-optimize-animation-stack/quickstart.md`
- [x] T005 [P] 在 `D:/Project/US/POSLinkUI-Demo/app/src/androidTest/java/com/paxus/pay/poslinkui/demo/AnimationVisualRegressionTest.kt` 创建动画自测 instrumentation 骨架
- [x] T006 [P] 在 `D:/Project/US/POSLinkUI-Demo/specs/004-optimize-animation-stack/quickstart.md` 补充性能测量步骤与阈值

**检查点**：基础已就绪，可开始用户故事工作。

---

## 阶段 3：用户故事 1 - 交易主流程动画降载（优先级：P1） MVP

**目标**：主交易流程内动画迁移到性能优先实现，保持视觉效果不变，降低内存与 CPU

**独立测试**： 执行主交易流程（启动交易、金额输入、确认、结果展示），验证动画反馈完整、无卡顿、无输入延迟；20 次流程切换无性能衰退

### 用户故事 1 的测试

- [x] T007 [P] [US1] 在 `D:/Project/US/POSLinkUI-Demo/app/src/androidTest/java/com/paxus/pay/poslinkui/demo/AnimationVisualRegressionTest.kt` 为主流程动画位点增加 instrumentation

### 用户故事 1 的实现

- [x] T008 [US1] 在 `MainActivity.kt`、`Toast.kt` 优化 A1/A2 Fragment 过渡动画并接入 policy（保持视觉语义一致）
- [x] T009 [US1] 在 `ClssLightsView.kt`、`ClssLight.kt` 将 A3 长时闪烁收敛为可复用 `ObjectAnimator`（按可见性停启）
- [x] T010 [US1] 在 `ApproveMessageScreen.kt`、`ExtendedEntryRoutes.kt` 为 A5/A6 加入 GIF fallback 与 crossfade 策略控制
- [x] T011 [US1] 在 `TransactionPresentation.kt` 优化 A4 Coil GIF 加载（解码尺寸、共享 ImageLoader、释放 request）

**检查点**：可通过主交易流程验证 US1；动画观感不变，性能提升。

---

## 阶段 4：用户故事 2 - 全量动画一致替换与降级（优先级：P1）

**目标**：所有可控动画 100% 替换，不可控动画有降级策略；移除旧实现并行路径

**独立测试**： 逐项验收 A1–A6，可控位点已替换，不可控位点有生效降级；无 AlphaAnimation/AnimationUtils/Glide 并行路径残留

### 用户故事 2 的实现

- [x] T012 [US2] 在 `ApproveMessageScreen.kt` 优化 A5 审批 GIF 并增加文本降级策略
- [x] T013 [US2] 在 `app/src/main/java/com/paxus/pay/poslinkui/demo/` 移除全部旧动画路径（AlphaAnimation、小票 AnimationUtils）并确认无并行实现
- [x] T014 [US2] 确认动画盘点 100% 完整，并在 `D:/Project/US/POSLinkUI-Demo/specs/004-optimize-animation-stack/research.md` 记录 RiskExceptionRecord
- [x] T015 [US2] 在 `app/src/main/java/com/paxus/pay/poslinkui/demo/` 按契约增加动画策略档位（standard/reduced/minimal）触发逻辑
- [x] T023 [US2] 按 FR-007 在实现代码与 `animation-migration-contract.md` 定义并实现高风险页（TransactionPresentation、ApproveMessageScreen、ReceiptPreviewEntryScreen）降级策略
- [x] T024 [US2] 按 FR-008 在 `app/src/main/java/com/paxus/pay/poslinkui/demo/` 实现动画降级触发与关键性能事件的日志

**检查点**：通过盘点检查清单验证 US2；满足 SC-005。

---

## 阶段 5：用户故事 3 - 长时运行温控稳定（优先级：P2）

**目标**：30 分钟长时运行场景下温升与 CPU 维持在目标范围，无性能衰退

**独立测试**： 连续运行 30 分钟核心页面切换与交互，观测 CPU、内存、温升满足 SC-002、SC-003、SC-004

### 用户故事 3 的实现

- [x] T016 [US3] 在 `D:/Project/US/POSLinkUI-Demo/specs/004-optimize-animation-stack/quickstart.md` 增加长时（30 分钟）性能验证步骤
- [x] T017 [US3] 在 `D:/Project/US/POSLinkUI-Demo/specs/004-optimize-animation-stack/quickstart.md` 增加性能门禁阈值（CPU 降幅 ≥25%、内存 ≥20%、温度 ≥2°C）
- [x] T018 [US3] 执行迁移后基线采集并将结果写入 `D:/Project/US/POSLinkUI-Demo/specs/004-optimize-animation-stack/quickstart.md`

**检查点**：通过 30 分钟运行验证 US3；满足 SC-002/SC-003/SC-004。

---

## 阶段 6：收尾与横切关注

**目的**: 全局收尾与交付前核验

- [x] T019 [P] 在 `D:/Project/US/POSLinkUI-Demo/specs/004-optimize-animation-stack/research.md` 同步最终实现说明
- [x] T020 [P] 在 `D:/Project/US/POSLinkUI-Demo/specs/004-optimize-animation-stack/checklists/requirements.md` 更新检查清单
- [x] T021 端到端校验 quickstart 并调整 `D:/Project/US/POSLinkUI-Demo/specs/004-optimize-animation-stack/quickstart.md` 中的命令
- [x] T022 在 `D:/Project/US/POSLinkUI-Demo/specs/004-optimize-animation-stack/plan.md` 做 plan/spec/tasks 一致性终检

---

## 依赖与执行顺序

### 阶段依赖

- 阶段 1（搭建）：无依赖
- 阶段 2（基础）：依赖阶段 1，阻塞所有用户故事
- 阶段 3（US1）：依赖阶段 2
- 阶段 4（US2）：依赖阶段 2（可在 US1 之后执行以拉齐全量清单）
- 阶段 5（US3）：依赖阶段 2 及 US1/US2 的实现，以便建立有意义的基线
- 阶段 6（收尾）：依赖所选故事阶段全部完成

### 用户故事依赖关系

- **US1**：基础完成后可独立推进；MVP 范围
- **US2**：基础完成后可独立推进；完成全量替换
- **US3**：依赖 US1/US2，用于迁移后的基线对比

### 可并行机会

- 基础阶段 `[P]`：T005、T006
- US1 `[P]`：T007
- US2：T023、T024（US2 内建议顺序执行）
- 收尾 `[P]`：T019、T020

---

## 实施策略

### 先交付 MVP

1. 完成阶段 1 + 阶段 2
2. 交付阶段 3（US1）完成主流程动画迁移
3. 在全量清单（US2）前独立验证

### 增量交付

1. US1（主流程）→ 稳定性能基线
2. US2（全量替换 + 降级）
3. US3（长时运行验证）
4. 阶段 6 收尾

### 备注

- `[P]` 任务针对不同文件，可并发执行
- 每个任务均包含明确文件路径，可直接交给执行模型
- 保持「视觉效果不变」作为每阶段回归门禁（FR-004a）
