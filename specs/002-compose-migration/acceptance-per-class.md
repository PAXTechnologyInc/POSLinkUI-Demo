# 类级验收清单

**特性**： 002-compose-migration  
**日期**: 2025-03-18  
**目的**: 将 spec「模块验收清单」细化为类级验收项，便于逐类勾选。tasks.md 中 T010、T014、T017、T021、T024、T026 可直接引用本清单。

**验收项**：布局还原、输入/选择/确认行为正确、敏感屏掩码与无明文日志、与 Design Tokens 一致。

---

## 1. Text 模块

本仓库按 **FR-015** 以 **参数化 Compose** 覆盖全部 `TextEntry` action，不再保留 per-action Fragment 类文件。验收时以 **constant 契约 + 实际路由** 为准，详见 [text-entry-acceptance-notes.md](text-entry-acceptance-notes.md)。

| 验收维度 | 说明 | 完成 |
|----------|------|------|
| 路由与注册 | `EntryActionRegistry` 含全部 Text action；`TextEntryResponseParams` 与 `EntryRequest` 键一致 | [ ] |
| 金额 / 字符串 / AVS / FSA | `AmountScreen`、`GenericStringEntryScreen`、`AvsEntryScreen`、`FsaAmountsEntryScreen` 行为与 extras 一致 | [ ] |
| 固定提示文案 | `TextEntryMessageFormatter` 与历史 `formatMessage()` 对齐；其余走 host `PARAM_MESSAGE` / `PARAM_TITLE` | [ ] |
| 设计与 Token | 与 Design Tokens / 设计稿一致 | [ ] |

---

## 2. Security 模块

| 类名 | 布局还原 | 掩码正确 | 无明文日志 | 验收完成 |
|------|----------|----------|-------------|----------|
| PINFragment | [ ] | [ ] | [ ] | [ ] |
| InputAccountFragment | [ ] | [ ] | [ ] | [ ] |
| EnterCardAllDigitsFragment | [ ] | [ ] | [ ] | [ ] |
| EnterCardLast4DigitsFragment | [ ] | [ ] | [ ] | [ ] |
| EnterVcodeFragment | [ ] | [ ] | [ ] | [ ] |
| AdministratorPasswordFragment | [ ] | [ ] | [ ] | [ ] |
| ManageInputAccountFragment | [ ] | [ ] | [ ] | [ ] |

---

## 3. Confirmation 模块

| 类名 | 布局还原 | 确认/取消行为正确 | 验收完成 |
|------|----------|-------------------|----------|
| AConfirmationFragment | [ ] | [ ] | [ ] |
| ConfirmTotalAmountFragment | [ ] | [ ] | [ ] |
| ConfirmCardProcessResultFragment | [ ] | [ ] | [ ] |
| ConfirmBalanceFragment | [ ] | [ ] | [ ] |
| ConfirmPrintFpsFragment | [ ] | [ ] | [ ] |
| ConfirmBatchCloseFragment | [ ] | [ ] | [ ] |
| （其余 Confirm* 等） | [ ] | [ ] | [ ] |

---

## 4. POSLink 模块

| 类名 | 布局还原 | 展示/交互正确 | 验收完成 |
|------|----------|---------------|----------|
| ShowItemFragment | [ ] | [ ] | [ ] |
| ShowTextBoxFragment | [ ] | [ ] | [ ] |
| ShowDialogFragment | [ ] | [ ] | [ ] |
| ShowMessageFragment | [ ] | [ ] | [ ] |
| ShowInputTextBoxFragment | [ ] | [ ] | [ ] |
| ShowDialogFormFragment | [ ] | [ ] | [ ] |
| ShowDialogFormCheckBoxFragment | [ ] | [ ] | [ ] |
| ShowDialogFormRadioFragment | [ ] | [ ] | [ ] |
| ShowSignatureBoxFragment | [ ] | [ ] | [ ] |
| ShowThankYouFragment | [ ] | [ ] | [ ] |
| InputTextFragment | [ ] | [ ] | [ ] |

---

## 5. Option 模块

| 类名 | 布局还原 | 选择行为正确 | 验收完成 |
|------|----------|--------------|----------|
| AOptionEntryFragment | [ ] | [ ] | [ ] |
| SelectTransTypeFragment | [ ] | [ ] | [ ] |
| SelectCurrencyFragment | [ ] | [ ] | [ ] |
| SelectMerchantFragment | [ ] | [ ] | [ ] |
| （其余 Select*） | [ ] | [ ] | [ ] |

---

## 6. Information 模块

| 类名 | 布局还原 | 内容正确展示 | 验收完成 |
|------|----------|--------------|----------|
| DisplayTransInfoFragment | [ ] | [ ] | [ ] |
| DisplayApproveMessageFragment | [ ] | [ ] | [ ] |

---

## 7. 验收方式

- **布局还原**：人工对比设计稿与实现截图，与 Design Tokens 一致
- **行为正确**：执行 Entry 流程，输入/选择/确认/回传与迁移前一致
- **敏感屏**：掩码、无明文日志
- 参考 spec 中「模块验收清单」与 [checklists/security-sensitive-screens.md](checklists/security-sensitive-screens.md)
