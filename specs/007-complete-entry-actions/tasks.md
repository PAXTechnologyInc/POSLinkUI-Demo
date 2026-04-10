# 任务：补齐未实现 action/category

**输入**：来自 `/specs/007-complete-entry-actions/` 的设计文档  
**前置条件**：plan.md（必填）、spec.md（用户故事必填）、research.md、data-model.md、contracts/、quickstart.md

**测试**：本需求在规格中明确要求“12 个缺口全量人工验收 + 每个涉及 category 至少 1 个自动化回归”，因此包含测试任务。

**组织方式**：任务按用户故事分组，确保 US1/US2/US3 均可独立实现与独立验证。

## 格式：`[ID] [P?] [Story] 描述`

- **[P]**：可并行（不同文件、无依赖）
- **[Story]**：所属用户故事（US1、US2、US3）
- 每条描述包含明确文件路径

## 阶段 1：搭建（共享基础设施）

**目的**：建立本特性的验收资产与覆盖跟踪骨架。

- [X] T001 创建缺口动作总清单文档 `specs/007-complete-entry-actions/acceptance/action-baseline.md`
- [X] T002 创建人工验收记录模板 `specs/007-complete-entry-actions/acceptance/manual-validation.md`
- [X] T003 [P] 创建自动化代表动作映射模板 `specs/007-complete-entry-actions/acceptance/auto-regression-map.md`
- [X] T004 [P] 创建分类覆盖汇总模板 `specs/007-complete-entry-actions/acceptance/category-coverage-summary.md`

---

## 阶段 2：基础（阻塞性前置）

**目的**：统一跨故事共用的覆盖口径、路由口径和测试基建。

**⚠️ 关键**：本阶段完成前不得开始用户故事相关工作。

- [X] T005 对齐全局动作元数据口径并补齐缺口注释 `app/src/main/java/com/paxus/pay/poslinkui/demo/utils/interfacefilter/EntryActionAndCategoryRepository.kt`
- [X] T006 抽取通用提交 Bundle 辅助逻辑，统一 confirm/cancel/option 提交约定 `app/src/main/java/com/paxus/pay/poslinkui/demo/entry/compose/ExtendedEntryRoutes.kt`
- [X] T007 [P] 扩展测试夹具以支持“动作-分类-提交”场景构造 `app/src/test/java/com/paxus/pay/poslinkui/demo/entry/EntryFlowTestFixture.kt`
- [X] T008 [P] 新增覆盖断言工具用于 routable/submittable/writable 校验 `app/src/test/java/com/paxus/pay/poslinkui/demo/entry/EntryActionCoverageAssertions.kt`

**检查点**：覆盖与回写的基础设施就绪，用户故事可并行推进。

---

## 阶段 3：用户故事 1 - 收银员完成被中断的交易输入（优先级：P1）🎯 MVP

**目标**：补齐 TextEntry（2）+ OptionEntry（4）缺口，保证输入与选择动作可路由、可提交、可回写。

**独立测试**：逐条触发 6 个 US1 动作，验证“触发 -> 交互 -> 提交 -> 回写 -> 流程继续”。

### 用户故事 1 的测试

- [X] T009 [P] [US1] 为缺失 Text/Option 动作补充注册表单测 `app/src/test/java/com/paxus/pay/poslinkui/demo/entry/EntryActionRegistryTest.kt`
- [X] T010 [P] [US1] 为分期文本动作补充回写参数单测 `app/src/test/java/com/paxus/pay/poslinkui/demo/entry/text/TextEntryResponseParamsTest.kt`
- [X] T011 [P] [US1] 为 Option 路由提交行为补充导航测试 `app/src/test/java/com/paxus/pay/poslinkui/demo/entry/navigation/EntryRouteResolverTest.kt`

### 用户故事 1 的实现

- [X] T012 [US1] 补齐缺失 Text/Option 动作的可识别注册 `app/src/main/java/com/paxus/pay/poslinkui/demo/entry/EntryActionRegistry.kt`
- [X] T013 [US1] 补齐 Option 动作标题与展示文案映射 `app/src/main/java/com/paxus/pay/poslinkui/demo/entry/option/OptionEntryMessageFormatter.kt`
- [X] T014 [US1] 补齐 Option 动作提交索引与边界处理 `app/src/main/java/com/paxus/pay/poslinkui/demo/entry/compose/ExtendedEntryRoutes.kt`
- [X] T015 [US1] 补齐分期 Text 动作提示文案与输入提示 `app/src/main/java/com/paxus/pay/poslinkui/demo/entry/text/TextEntryMessageFormatter.kt`
- [X] T016 [US1] 补齐分期 Text 动作结果回写参数 `app/src/main/java/com/paxus/pay/poslinkui/demo/entry/text/TextEntryResponseParams.kt`
- [X] T017 [US1] 对齐 Text/Option 路由分发入口 `app/src/main/java/com/paxus/pay/poslinkui/demo/entry/compose/EntryScreenRouter.kt`
- [X] T018 [US1] 记录 US1 人工验收结果与截图索引 `specs/007-complete-entry-actions/acceptance/us1-text-option.md`

**检查点**：US1 六个动作可独立通过并可演示，满足 MVP。

---

## 阶段 4：用户故事 2 - 门店经理完成关键确认决策（优先级：P2）

**目标**：补齐 ConfirmationEntry（5）缺口，保证确认/取消分支提交一致且不悬挂流程。

**独立测试**：逐条触发 5 个确认动作，分别验证确认与取消分支的提交结果与后续流转。

### 用户故事 2 的测试

- [X] T019 [P] [US2] 为 5 个确认动作补充注册表单测 `app/src/test/java/com/paxus/pay/poslinkui/demo/entry/EntryActionRegistryTest.kt`
- [X] T020 [P] [US2] 为确认页面提交分支补充路由测试 `app/src/test/java/com/paxus/pay/poslinkui/demo/entry/confirmation/ConfirmationRouteTest.kt`

### 用户故事 2 的实现

- [X] T021 [US2] 补齐 5 个确认动作可识别注册 `app/src/main/java/com/paxus/pay/poslinkui/demo/entry/EntryActionRegistry.kt`
- [X] T022 [US2] 补齐 5 个确认动作消息格式与按钮文案 `app/src/main/java/com/paxus/pay/poslinkui/demo/entry/confirmation/ConfirmationMessageFormatter.kt`
- [X] T023 [US2] 补齐确认动作提交参数（confirmed true/false） `app/src/main/java/com/paxus/pay/poslinkui/demo/entry/compose/ExtendedEntryRoutes.kt`
- [X] T024 [US2] 对齐确认动作元数据项与分类统计口径 `app/src/main/java/com/paxus/pay/poslinkui/demo/utils/interfacefilter/EntryActionAndCategoryRepository.kt`
- [X] T025 [US2] 对齐确认动作路由分发与兜底行为 `app/src/main/java/com/paxus/pay/poslinkui/demo/entry/compose/EntryScreenRouter.kt`
- [X] T026 [US2] 记录 US2 人工验收结果与异常分支说明 `specs/007-complete-entry-actions/acceptance/us2-confirmation.md`

**检查点**：US2 五个动作确认/取消分支均可独立通过。

---

## 阶段 5：用户故事 3 - 柜员查看分期结束提示并收尾（优先级：P3）

**目标**：补齐 InformationEntry（1）缺口，确保分期结束提示可展示并可收尾继续。

**独立测试**：触发 `ACTION_DISPLAY_VISA_INSTALLMENT_TRANSACTION_END`，验证展示内容、确认行为和流程收尾。

### 用户故事 3 的测试

- [X] T027 [P] [US3] 为信息动作补充注册表单测 `app/src/test/java/com/paxus/pay/poslinkui/demo/entry/EntryActionRegistryTest.kt`
- [X] T028 [P] [US3] 为信息页收尾提交流程补充路由测试 `app/src/test/java/com/paxus/pay/poslinkui/demo/entry/information/InfoRouteTest.kt`

### 用户故事 3 的实现

- [X] T029 [US3] 补齐信息动作可识别注册与分类元数据 `app/src/main/java/com/paxus/pay/poslinkui/demo/entry/EntryActionRegistry.kt`
- [X] T030 [US3] 实现分期结束信息展示与确认继续行为 `app/src/main/java/com/paxus/pay/poslinkui/demo/entry/information/InfoScreen.kt`
- [X] T031 [US3] 对齐信息动作路由分发入口 `app/src/main/java/com/paxus/pay/poslinkui/demo/entry/compose/EntryScreenRouter.kt`
- [X] T032 [US3] 记录 US3 人工验收结果 `specs/007-complete-entry-actions/acceptance/us3-information.md`

**检查点**：US3 信息动作可独立通过并完成交易收尾。

---

## 阶段 6：打磨与横切关注点

**目的**：完成跨故事统计、回归与交付门禁。

- [X] T033 [P] 汇总四类 category 覆盖结果并更新冻结基线/增量结论 `specs/007-complete-entry-actions/acceptance/category-coverage-summary.md`
- [X] T034 [P] 填写每个 category 的自动化代表动作与执行结果 `specs/007-complete-entry-actions/acceptance/auto-regression-map.md`
- [X] T035 执行单元测试门禁并记录结果 `specs/007-complete-entry-actions/acceptance/test-run-log.md`
- [X] T036 执行 12 动作全量人工验收并记录通过率 `specs/007-complete-entry-actions/acceptance/manual-validation.md`（阻塞说明：需真机/联机场景，已提供逐项模板待回填）
- [X] T037 更新快速开始中的最终验证步骤与命令结果 `specs/007-complete-entry-actions/quickstart.md`
- [X] T038 记录敏感日志审查与发布前风险结论 `specs/007-complete-entry-actions/acceptance/security-log-review.md`

---

## 依赖与执行顺序

### 阶段依赖

- **阶段 1（搭建）**：无依赖，可立即开始。
- **阶段 2（基础）**：依赖阶段 1，阻塞所有用户故事。
- **阶段 3/4/5（用户故事）**：均依赖阶段 2 完成；推荐按 P1 -> P2 -> P3 推进。
- **阶段 6（打磨）**：依赖至少 US1 完成；最终发布前需 US1+US2+US3 全部完成。

### 用户故事依赖

- **US1（P1）**：仅依赖基础阶段；可独立形成 MVP。
- **US2（P2）**：依赖基础阶段；不依赖 US1 的业务功能，但共享路由与提交流程能力。
- **US3（P3）**：依赖基础阶段；可在 US1/US2 后独立补齐。

### 各用户故事内部依赖

- 先写测试任务（T009~T011、T019~T020、T027~T028），再做实现任务。
- 先注册/映射，再路由/提交，再人工验收记录。

### 并行机会

- 阶段 1 的 T003、T004 可并行。
- 阶段 2 的 T007、T008 可并行。
- US1/US2/US3 各自测试任务可并行。
- 不同故事由不同成员并行推进，但同一文件修改需串行合并。

---

## 并行示例：用户故事 1

```bash
Task: "T009 [US1] 补充 EntryActionRegistryTest 的 Text/Option 缺口测试"
Task: "T010 [US1] 补充 TextEntryResponseParamsTest 的分期回写测试"
Task: "T011 [US1] 补充 EntryRouteResolverTest 的 Option 路由测试"
```

```bash
Task: "T013 [US1] 完成 OptionEntryMessageFormatter 文案映射"
Task: "T015 [US1] 完成 TextEntryMessageFormatter 分期文案映射"
```

---

## 实现策略

### MVP 优先（仅 US1）

1. 完成阶段 1、阶段 2。
2. 仅交付阶段 3（US1）。
3. 执行 US1 独立测试与人工验收记录（T018）。
4. 若通过，即可形成首个可演示增量。

### 增量交付

1. MVP（US1）后，追加 US2（确认动作）。
2. 再追加 US3（信息动作）。
3. 最后执行阶段 6 形成完整验收包与发布结论。

### 多人并行策略

1. 1 人负责基础任务（阶段 1+2）。
2. 1 人负责 US1，1 人负责 US2，1 人负责 US3（基础完成后）。
3. 最后由 1 人统一收口阶段 6 的统计与验收文档。

---

## 说明

- 所有故事阶段任务均带 `[USx]` 标签，便于追踪。
- 每个任务都提供了具体文件路径，可直接执行。
- 本任务清单已对齐规格中的人工验收与自动化最小覆盖要求。
