# Text 模块验收说明（002 / FR-015）

**日期**：2026-03-27

## 实现形态

- 不再按 `class-inventory.md` 逐 Fragment 文件迁移；**单一数据源**为 `com.paxus.ui:constant` 的 `TextEntry` / `EntryRequest` / `EntryExtraData`。
- **路由**：`EntryScreenRouter` → `TextEntryComposeRoute` → `TextEntryResponseParams.resolveKind` → `GenericStringEntryScreen` / `AmountScreen` / `AvsEntryScreen` / `FsaAmountsEntryScreen`。
- **回传键**：`TextEntryResponseParams` 中 `Intent.action` → `EntryRequest` 字符串键（金额类为 `AmountMinor` + Long 键）。

## 与 constant 对齐的补全项（本仓库）

| 场景 | 说明 |
|------|------|
| `ACTION_ENTER_FLEET_DATA`（已 @Deprecated） | 回传 `EntryRequest.PARAM_FLEET_CUSTOMER_DATA`；提示 `prompt_input_fleet_customer_data`。 |
| `ACTION_ENTER_VISA_INSTALLMENT_TRANSACTIONID` | 回传 `PARAM_VISA_TRANSID`；提示 `pls_input_visa_installmenttransID`。 |
| `ACTION_ENTER_VISA_INSTALLMENT_PLAN_ACCEPTANCE_ID` | 回传 `PARAM_VISA_PLAN_ACCEPTANCE_ID`；提示 `please_input_visa_installment_plan_acceptance_id`。 |

固定文案见 `TextEntryMessageFormatter`；无 per-action 固定串时回退 `PARAM_MESSAGE` / `PARAM_TITLE`。

验收时对照设计稿检查 Typography/间距；行为以 host `EntryExtraData`（如 `PARAM_VALUE_PATTERN`、`PARAM_EINPUT_TYPE`）为准。
