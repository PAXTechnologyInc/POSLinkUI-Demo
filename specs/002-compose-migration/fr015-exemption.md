# FR-015：参数化合并与豁免说明

**日期**：2026-03-27

## 已完成的参数化（示例组）

- **Text 模块**：以 `TextEntryResponseParams` + `GenericStringEntryScreen` / `AmountScreen` / `Avs` / `Fsa` 替代数十个 per-action Fragment，按 `Intent.action` 映射 `EntryRequest` 输出键。
- **Option 模块**：以 `OptionListEntryRoute` 统一 `OptionEntry.ACTION_SELECT_*`，输出 `EntryRequest.PARAM_INDEX`（0-based）。
- **POSLink 模块**：以 `PoslinkEntryRoute` 内 `when (action)` 覆盖 `SHOW_DIALOG`、`INPUT_TEXT`、`SHOW_MESSAGE` 等主要路径。

## 书面说明

上述合并**不改变**与 BroadPOS 之间的 **action / category / Bundle 契约**；若产品要求保留独立 Fragment 类名以便堆栈分析，可在发布分支回退为多个 Composable 文件，但契约层映射表应保留单点维护。

若需引用项目宪法中「合并 Fragment」条款，请以本文档 + `tasks.md` 附录 A 为 **豁免与等价实现** 依据。
