# 类清单：UI 转 Compose 迁移

**特性**： 002-compose-migration  
**日期**: 2025-03-18  
**目的**: 列出该功能特性涉及的全部 Fragment 与 UI 相关类，按模块分组，供 tasks 与验收引用。

**迁移方式**：在现有 Fragment 内嵌 ComposeView，逐步将 View 替换为 Compose。路径保持 `app/src/main/java/...`，扩展名 `.kt`。

---

## 1. 基类（迁移顺序优先）

| 类名 | 路径 | 说明 |
|------|------|------|
| BaseEntryFragment | entry/BaseEntryFragment.java | 需引入 ComposeView 支持，供所有子类使用 |
| ATextFragment | entry/text/text/ATextFragment.java | Text 基类 |
| AConfirmationFragment | entry/confirmation/AConfirmationFragment.java | Confirmation 基类 |
| ASecurityFragment | entry/security/ASecurityFragment.java | Security 基类 |
| AOptionEntryFragment | entry/option/AOptionEntryFragment.java | Option 基类 |
| AAmountFragment | entry/text/amount/AAmountFragment.java | 金额基类 |
| ANumFragment | entry/text/number/ANumFragment.java | 数字基类 |
| ANumTextFragment | entry/text/numbertext/ANumTextFragment.java | 数字文本基类 |

---

## 2. Text 模块（用户故事 1）

### text/amount/

| 类名 | 路径 |
|------|------|
| AmountFragment | entry/text/amount/AmountFragment.java |
| CashbackFragment | entry/text/amount/CashbackFragment.java |
| FuelAmountFragment | entry/text/amount/FuelAmountFragment.java |
| TaxAmountFragment | entry/text/amount/TaxAmountFragment.java |
| TipFragment | entry/text/amount/TipFragment.java |
| TotalAmountFragment | entry/text/amount/TotalAmountFragment.java |

### text/number/

| 类名 | 路径 |
|------|------|
| CsPhoneNumberFragment | entry/text/number/CsPhoneNumberFragment.java |
| EnterTicketNumberFragment | entry/text/number/EnterTicketNumberFragment.java |
| GuestNumberFragment | entry/text/number/GuestNumberFragment.java |
| MerchantTaxIdFragment | entry/text/number/MerchantTaxIdFragment.java |
| PhoneNumberFragment | entry/text/number/PhoneNumberFragment.java |
| PromptRestrictionCodeFragment | entry/text/number/PromptRestrictionCodeFragment.java |
| TableNumberFragment | entry/text/number/TableNumberFragment.java |
| TransNumberFragment | entry/text/number/TransNumberFragment.java |

### text/numbertext/

| 类名 | 路径 |
|------|------|
| ClerkIdFragment | entry/text/numbertext/ClerkIdFragment.java |
| DestZipcodeFragment | entry/text/numbertext/DestZipcodeFragment.java |
| InvoiceNumberFragment | entry/text/numbertext/InvoiceNumberFragment.java |
| MerchantReferenceNumberFragment | entry/text/numbertext/MerchantReferenceNumberFragment.java |
| OctReferenceNumberFragment | entry/text/numbertext/OctReferenceNumberFragment.java |
| ReferenceNumberFragment | entry/text/numbertext/ReferenceNumberFragment.java |
| ServerIdFragment | entry/text/numbertext/ServerIdFragment.java |
| VoucherDataFragment | entry/text/numbertext/VoucherDataFragment.java |
| ZipcodeFragment | entry/text/numbertext/ZipcodeFragment.java |

### text/text/

| 类名 | 路径 |
|------|------|
| AddressFragment | entry/text/text/AddressFragment.java |
| AuthFragment | entry/text/text/AuthFragment.java |
| CustomerCodeFragment | entry/text/text/CustomerCodeFragment.java |
| EnterGlobalUIDFragment | entry/text/text/EnterGlobalUIDFragment.java |
| OrderNumberFragment | entry/text/text/OrderNumberFragment.java |
| OrigTransIdentifierFragment | entry/text/text/OrigTransIdentifierFragment.java |
| PoNumberFragment | entry/text/text/PoNumberFragment.java |
| ProdDescFragment | entry/text/text/ProdDescFragment.java |

### text/ 独立

| 类名 | 路径 |
|------|------|
| AVSFragment | entry/text/AVSFragment.java |
| EnterOrigTransDateFragment | entry/text/EnterOrigTransDateFragment.java |
| ExpiryFragment | entry/text/ExpiryFragment.java |
| FSAAmountFragment | entry/text/fsa/FSAAmountFragment.java |
| FSAFragment | entry/text/fsa/FSAFragment.java |
| FsaOptionsFragment | entry/text/fsa/FsaOptionsFragment.java |
| EnterFleetDataFragment | entry/text/fleet/EnterFleetDataFragment.java |
| NewEnterFleetDataFragment | entry/text/fleet/NewEnterFleetDataFragment.java |

---

## 3. Security 模块（用户故事 2）

| 类名 | 路径 | 敏感说明 |
|------|------|----------|
| PINFragment | entry/security/PINFragment.java | PIN 掩码、安全键盘 |
| InputAccountFragment | entry/security/InputAccountFragment.java | 持卡人信息掩码 |
| EnterCardAllDigitsFragment | entry/security/EnterCardAllDigitsFragment.java | 卡号掩码 |
| EnterCardLast4DigitsFragment | entry/security/EnterCardLast4DigitsFragment.java | 卡号后四位 |
| EnterVcodeFragment | entry/security/EnterVcodeFragment.java | 验证码 |
| AdministratorPasswordFragment | entry/security/AdministratorPasswordFragment.java | 管理员密码 |
| ManageInputAccountFragment | entry/security/ManageInputAccountFragment.java | 账户管理 |

---

## 4. Confirmation 模块（用户故事 3）

| 类名 | 路径 |
|------|------|
| AConfirmationFragment | entry/confirmation/AConfirmationFragment.java |
| CheckCardPresentFragment | entry/confirmation/CheckCardPresentFragment.java |
| CheckDeactivateWarnFragment | entry/confirmation/CheckDeactivateWarnFragment.java |
| ConfirmAdjustTipFragment | entry/confirmation/ConfirmAdjustTipFragment.java |
| ConfirmBalanceFragment | entry/confirmation/ConfirmBalanceFragment.java |
| ConfirmBatchCloseFragment | entry/confirmation/ConfirmBatchCloseFragment.java |
| ConfirmCardEntryRetryFragment | entry/confirmation/ConfirmCardEntryRetryFragment.java |
| ConfirmCardProcessResultFragment | entry/confirmation/ConfirmCardProcessResultFragment.java |
| ConfirmCashPaymentFragment | entry/confirmation/ConfirmCashPaymentFragment.java |
| ConfirmDCCFragment | entry/confirmation/ConfirmDCCFragment.java |
| ConfirmDeleteSfFragment | entry/confirmation/ConfirmDeleteSfFragment.java |
| ConfirmDuplicateTransFragment | entry/confirmation/ConfirmDuplicateTransFragment.java |
| ConfirmMerchantScopeFragment | entry/confirmation/ConfirmMerchantScopeFragment.java |
| ConfirmOnlineRetryFragment | entry/confirmation/ConfirmOnlineRetryFragment.java |
| ConfirmPrintCustomerCopyFragment | entry/confirmation/ConfirmPrintCustomerCopyFragment.java |
| ConfirmPrintFailedTransFragment | entry/confirmation/ConfirmPrintFailedTransFragment.java |
| ConfirmPrinterStatusFragment | entry/confirmation/ConfirmPrinterStatusFragment.java |
| ConfirmPrintFpsFragment | entry/confirmation/ConfirmPrintFpsFragment.java |
| ConfirmReceiptSignatureFragment | entry/confirmation/ConfirmReceiptSignatureFragment.java |
| ConfirmReceiptViewFragment | entry/confirmation/ConfirmReceiptViewFragment.java |
| ConfirmServiceFeeFragment | entry/confirmation/ConfirmServiceFeeFragment.java |
| ConfirmSignatureMatchFragment | entry/confirmation/ConfirmSignatureMatchFragment.java |
| ConfirmSurchargeFeeFragment | entry/confirmation/ConfirmSurchargeFeeFragment.java |
| ConfirmTotalAmountFragment | entry/confirmation/ConfirmTotalAmountFragment.java |
| ConfirmUnifiedMessageFragment | entry/confirmation/ConfirmUnifiedMessageFragment.java |
| ConfirmUntippedFragment | entry/confirmation/ConfirmUntippedFragment.java |
| ConfirmUploadRetryFragment | entry/confirmation/ConfirmUploadRetryFragment.java |
| ConfirmUploadTransFragment | entry/confirmation/ConfirmUploadTransFragment.java |
| DisplayQRCodeReceiptFragment | entry/confirmation/DisplayQRCodeReceiptFragment.java |
| ReversePartialApprovalFragment | entry/confirmation/ReversePartialApprovalFragment.java |
| StartUIFragment | entry/confirmation/StartUIFragment.java |
| SupplementPartialApprovalFragment | entry/confirmation/SupplementPartialApprovalFragment.java |

---

## 5. POSLink 模块（用户故事 4）

| 类名 | 路径 |
|------|------|
| ShowItemFragment | entry/poslink/ShowItemFragment.java |
| ShowTextBoxFragment | entry/poslink/ShowTextBoxFragment.java |
| ShowDialogFragment | entry/poslink/ShowDialogFragment.java |
| ShowMessageFragment | entry/poslink/ShowMessageFragment.java |
| ShowInputTextBoxFragment | entry/poslink/ShowInputTextBoxFragment.java |
| ShowDialogFormFragment | entry/poslink/ShowDialogFormFragment.java |
| ShowDialogFormCheckBoxFragment | entry/poslink/ShowDialogFormCheckBoxFragment.java |
| ShowDialogFormRadioFragment | entry/poslink/ShowDialogFormRadioFragment.java |
| ShowSignatureBoxFragment | entry/poslink/ShowSignatureBoxFragment.java |
| ShowThankYouFragment | entry/poslink/ShowThankYouFragment.java |
| InputTextFragment | entry/poslink/InputTextFragment.java |

---

## 6. Option 模块（用户故事 5）

| 类名 | 路径 |
|------|------|
| AOptionEntryFragment | entry/option/AOptionEntryFragment.java |
| SelectAccountTypeFragment | entry/option/SelectAccountTypeFragment.java |
| SelectAidFragment | entry/option/SelectAidFragment.java |
| SelectBatchReportTypeFragment | entry/option/SelectBatchReportTypeFragment.java |
| SelectBatchTypeFragment | entry/option/SelectBatchTypeFragment.java |
| SelectByPassFragment | entry/option/SelectByPassFragment.java |
| SelectCardTypeFragment | entry/option/SelectCardTypeFragment.java |
| SelectCashDiscountFragment | entry/option/SelectCashDiscountFragment.java |
| SelectCofInitiatorFragment | entry/option/SelectCofInitiatorFragment.java |
| SelectCurrencyFragment | entry/option/SelectCurrencyFragment.java |
| SelectDuplicateOverrideFragment | entry/option/SelectDuplicateOverrideFragment.java |
| SelectEbtTypeFragment | entry/option/SelectEbtTypeFragment.java |
| SelectEdcGroupFragment | entry/option/SelectEdcGroupFragment.java |
| SelectEdcTypeFragment | entry/option/SelectEdcTypeFragment.java |
| SelectLanguageFragment | entry/option/SelectLanguageFragment.java |
| SelectMerchantFragment | entry/option/SelectMerchantFragment.java |
| SelectMotoTypeFragment | entry/option/SelectMotoTypeFragment.java |
| SelectOrigCurrencyFragment | entry/option/SelectOrigCurrencyFragment.java |
| SelectRefundReasonFragment | entry/option/SelectRefundReasonFragment.java |
| SelectReportTypeFragment | entry/option/SelectReportTypeFragment.java |
| SelectSearchCriteriaFragment | entry/option/SelectSearchCriteriaFragment.java |
| SelectSubTransTypeFragment | entry/option/SelectSubTransTypeFragment.java |
| SelectTaxReasonFragment | entry/option/SelectTaxReasonFragment.java |
| SelectTransTypeFragment | entry/option/SelectTransTypeFragment.java |

---

## 7. Information 模块（用户故事 6）

| 类名 | 路径 |
|------|------|
| DisplayTransInfoFragment | entry/information/DisplayTransInfoFragment.java |
| DisplayApproveMessageFragment | entry/information/DisplayApproveMessageFragment.java |

---

## 8. 其他 UI 相关（可选迁移）

| 类名 | 路径 | 说明 |
|------|------|------|
| SignatureFragment | entry/signature/SignatureFragment.java | 签名，可后续迁移 |
| ElectronicSignatureView | entry/signature/ElectronicSignatureView.java | 自定义 View |

---

## 9. 迁移顺序（依赖关系）

1. **BaseEntryFragment** 引入 ComposeView 支持
2. **ATextFragment、AConfirmationFragment、ASecurityFragment、AOptionEntryFragment、AAmountFragment、ANumFragment、ANumTextFragment** 基类
3. 按模块顺序：Text → Security → Confirmation → POSLink → Option → Information
4. 各模块内：先基类，再子类
