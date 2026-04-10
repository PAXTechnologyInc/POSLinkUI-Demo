# Phase 0 研究：补齐未实现 action/category

## 1. 目标

在不改变交易业务语义的前提下，补齐 `v1.03` 文档定义但当前代码链路未完整实现的 Entry 动作，确保动作可以被识别、可交互提交、并可回写结果。

## 2. 范围基线

- 首轮冻结基线：`ACTION_*` 总数 141，缺口 12。
- 增量策略：若实施中发现文档遗漏项，记为增量缺口并并行处理，不阻塞首轮验收结论。

## 3. 已确定缺口（首批 12 个）

### TextEntry（2）

- `ACTION_ENTER_VISA_INSTALLMENT_PLAN_ACCEPTANCE_ID`
- `ACTION_ENTER_VISA_INSTALLMENT_TRANSACTIONID`

### OptionEntry（4）

- `ACTION_SELECT_CASHBACK_AMOUNT`
- `ACTION_SELECT_INSTALLMENT_PLAN`
- `ACTION_SELECT_TIP_AMOUNT`
- `ACTION_SELECT_TRANS_FOR_ADJUST`

### ConfirmationEntry（5）

- `ACTION_CONFIRM_BATCH_CLOSE_WITH_INCOMPLETE_TRANSACTION`
- `ACTION_CONFIRM_BATCH_FOR_APPLICATION_UPDATE`
- `ACTION_CONFIRM_DEBIT_TRANS`
- `ACTION_CONFIRM_ONLINE_RETRY_OFFLINE`
- `ACTION_CONFIRM_TAX_AMOUNT`

### InformationEntry（1）

- `ACTION_DISPLAY_VISA_INSTALLMENT_TRANSACTION_END`

## 4. 现状观察（代码侧）

- 路由识别基线位于 `entry/EntryActionRegistry.kt`。
- Action 元数据基线位于 `utils/interfacefilter/EntryActionAndCategoryRepository.kt`。
- 某些动作在元数据层存在但在路由层仍未完整接通，需按“路由+交互+回写”三段重新核查。

## 5. 决策与权衡

- 验收策略采用“12 个缺口全量人工 + 每个涉及 category 至少 1 个自动化回归动作”。
- 不追求本次引入大规模测试框架升级，以低风险补齐为主。

## 6. 实施前门禁

- 为每个目标动作建立四段状态：`defined` / `routable` / `submittable` / `writable`。
- 任一动作未达到 `writable` 均不能标记为完成。
- 覆盖统计按 category 输出并纳入最终验收记录。
