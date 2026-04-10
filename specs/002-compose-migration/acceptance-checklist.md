# 002 全量验收勾选单（T027）

**用途**：发布前人工逐项勾选；与 `tasks.md` **T027** 对应。  
**说明**：自动化测试以 `./gradlew :app:assembleDebug :app:testDebugUnitTest` 为底线；下列项需 **真机 / 联调**。

## SC-009 / SC-010 / SC-013

- [ ] 主流程 Entry 仅 **单 Compose 宿主**（`EntryActivity` → `EntryNavigationHost` → `EntryScreenRouter`），无并列互抢的多个 Compose 根。
- [ ] `PosLinkTheme` + `PosLinkScreenRoot` / Token 在典型屏一致。
- [ ] `onNewIntent` 更新 `EntryViewModel.consumeEntryIntent` 后界面随 `revision` 切换（见 `EntryNavHost` `key`）。

## 按模块（各选 1–2 个代表 action 实测）

- [ ] **Text**：金额、单行文本、AVS、FSA。
- [ ] **Security**：PIN（仅 `SECURITY_AREA` ready）、CVV/卡号占位（bounds）、Continue → `NEXT` 无敏感日志。
- [ ] **Confirmation**：通用确认、收据预览 URI、二维码内容、START_UI 自动前进、Surcharge。
- [ ] **POSLink**：SHOW_DIALOG 索引（1-based）、INPUT_TEXT、SHOW_THANK_YOU、DIALOG_FORM。
- [ ] **Option**：任意 `SELECT_*`，索引 0-based。
- [ ] **Signature**：GET_SIGNATURE（demo stub）、POSLink SHOW_SIGNATURE_BOX。
- [ ] **Information**：交易信息、审批页（含 `AndroidView` 部分若仍存在）。

## P0 机型与副屏（与 T034 重叠）

- [ ] **A3700 / A920MAX / A35 / A80S 档**：主按钮可达、无截断。
- [ ] **A920Pro**：副屏 `TransactionPresentation` 展示与 dismiss，无泄漏日志。

## 签署

| 角色 | 姓名 | 日期 |
|------|------|------|
| 测试 | | |
| 开发 | | |
