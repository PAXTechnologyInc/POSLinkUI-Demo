# Information 模块验收说明（T026）

**日期**：2026-03-27

- **交易信息**：`EntryScreenRouter` → `TransInfoRoute` → `InfoScreen`（纯 Compose）。
- **审批信息**：`ApproveEntryRoute` → `ApproveMessageScreen`（纯 Compose + Coil 单图片栈，含 GIF 降级策略）。

验收时对照设计稿检查字号/间距；行为以 host 传入的 `EntryExtraData` 为准。
