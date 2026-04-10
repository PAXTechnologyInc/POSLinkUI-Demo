# 类级审查完成清单

**特性**： 001-kotlin-migration  
**日期**: 2025-03-18  
**目的**: 按 [class-inventory.md](class-inventory.md) 逐类确认 Kotlin 转换与审查已完成。

**使用方式**：每完成一个类或一批类的审查后勾选；T011a–e、T012、T013 可直接引用本清单。

---

## 1. 基类（优先）

| 类名 | 路径 | 审查完成 |
|------|------|----------|
| BaseEntryFragment | entry/BaseEntryFragment.kt | [ ] |
| UIFragmentHelper | entry/UIFragmentHelper.kt | [ ] |
| ATextFragment | entry/text/text/ATextFragment.kt | [ ] |
| AConfirmationFragment | entry/confirmation/AConfirmationFragment.kt | [ ] |
| ASecurityFragment | entry/security/ASecurityFragment.kt | [ ] |
| AOptionEntryFragment | entry/option/AOptionEntryFragment.kt | [ ] |
| AAmountFragment | entry/text/amount/AAmountFragment.kt | [ ] |
| ANumFragment | entry/text/number/ANumFragment.kt | [ ] |
| ANumTextFragment | entry/text/numbertext/ANumTextFragment.kt | [ ] |

---

## 2. entry/confirmation/

| 类名 | 审查完成 |
|------|----------|
| CheckCardPresentFragment | [ ] |
| CheckDeactivateWarnFragment | [ ] |
| ConfirmAdjustTipFragment | [ ] |
| ConfirmBalanceFragment | [ ] |
| ConfirmBatchCloseFragment | [ ] |
| ConfirmCardEntryRetryFragment | [ ] |
| ConfirmCardProcessResultFragment | [ ] |
| ConfirmCashPaymentFragment | [ ] |
| ConfirmDCCFragment | [ ] |
| ConfirmDeleteSfFragment | [ ] |
| ConfirmDuplicateTransFragment | [ ] |
| ConfirmMerchantScopeFragment | [ ] |
| ConfirmOnlineRetryFragment | [ ] |
| ConfirmPrintCustomerCopyFragment | [ ] |
| ConfirmPrintFailedTransFragment | [ ] |
| ConfirmPrinterStatusFragment | [ ] |
| ConfirmPrintFpsFragment | [ ] |
| ConfirmReceiptSignatureFragment | [ ] |
| ConfirmReceiptViewFragment | [ ] |
| ConfirmServiceFeeFragment | [ ] |
| ConfirmSignatureMatchFragment | [ ] |
| ConfirmSurchargeFeeFragment | [ ] |
| ConfirmTotalAmountFragment | [ ] |
| ConfirmUnifiedMessageFragment | [ ] |
| ConfirmUntippedFragment | [ ] |
| ConfirmUploadRetryFragment | [ ] |
| ConfirmUploadTransFragment | [ ] |
| DisplayQRCodeReceiptFragment | [ ] |
| ReversePartialApprovalFragment | [ ] |
| StartUIFragment | [ ] |
| SupplementPartialApprovalFragment | [ ] |

---

## 3. entry/text/

见 [class-inventory.md](class-inventory.md) 第 3 节。按 text/amount/、text/number/、text/numbertext/、text/text/、text/ 独立 分组逐类勾选。

**验收项**：空安全、命名、控制流、生命周期（若涉及 UI 回调）、支付安全（无敏感日志）

---

## 4. entry/security/

| 类名 | 审查完成 |
|------|----------|
| PINFragment | [ ] |
| InputAccountFragment | [ ] |
| EnterCardAllDigitsFragment | [ ] |
| EnterCardLast4DigitsFragment | [ ] |
| EnterVcodeFragment | [ ] |
| AdministratorPasswordFragment | [ ] |
| ManageInputAccountFragment | [ ] |

**高危**：参考 [checklists/high-risk-classes.md](checklists/high-risk-classes.md)

---

## 5. entry/option/

见 [class-inventory.md](class-inventory.md) 第 5 节。22 个 Select*Fragment 逐类勾选。

---

## 6. entry/poslink/、entry/information/、entry/signature/、entry/task/

见 [class-inventory.md](class-inventory.md) 第 6–8 节。逐类勾选。

---

## 7. status/、utils/、view/、viewmodel/、settings/、根

| 类名 | 审查完成 |
|------|----------|
| EntryActivity | [ ] |
| MainActivity | [ ] |
| DemoApplication | [ ] |
| StatusFragment | [ ] |
| ToastFragment | [ ] |
| TransCompletedStatusFragment | [ ] |
| CurrencyUtils | [ ] |
| EntryRequestUtils | [ ] |
| UIFragmentHelper | [ ] |
| … | 见 class-inventory 第 9 节 |

---

## 8. 汇总

- **总类数**：约 177
- **已审查**：_____
- **完成率**：_____%
