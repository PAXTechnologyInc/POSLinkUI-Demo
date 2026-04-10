# 类清单：Java 转 Kotlin 迁移

**特性**： 001-kotlin-migration  
**日期**: 2025-03-18  
**目的**: 列出该功能特性涉及的全部 Java 类，按模块与继承关系分组，供 tasks 与审查引用。

**转换后**：同路径下扩展名变为 `.kt`，包结构不变。

---

## 1. entry/ 根与基类（转换顺序优先）

### 基类（abstract，影响所有子类）

| 类名 | 路径 | 说明 |
|------|------|------|
| BaseEntryFragment | entry/BaseEntryFragment.java | 所有 Entry 基类，定义生命周期与 Neptune 回调 |
| UIFragmentHelper | entry/UIFragmentHelper.java | 与 Neptune 路由交互，被 EntryActivity 使用 |
| EntryActivity | entry/EntryActivity.java | 进件容器 Activity |
| EmptyFragment | entry/EmptyFragment.java | 空占位 Fragment |
| TransactionPresentation | entry/TransactionPresentation.java | 交易展示 |

---

## 2. entry/confirmation/（Confirmation 模块）

### 基类

| 类名 | 路径 | 说明 |
|------|------|------|
| AConfirmationFragment | entry/confirmation/AConfirmationFragment.java | 确认类基类 |

### 子类（extends AConfirmationFragment）

| 类名 | 路径 |
|------|------|
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
| ConfirmServiceFeeFragment | entry/confirmation/ConfirmServiceFeeFragment.java |
| ConfirmSignatureMatchFragment | entry/confirmation/ConfirmSignatureMatchFragment.java |
| ConfirmTotalAmountFragment | entry/confirmation/ConfirmTotalAmountFragment.java |
| ConfirmUnifiedMessageFragment | entry/confirmation/ConfirmUnifiedMessageFragment.java |
| ConfirmUntippedFragment | entry/confirmation/ConfirmUntippedFragment.java |
| ConfirmUploadRetryFragment | entry/confirmation/ConfirmUploadRetryFragment.java |
| ConfirmUploadTransFragment | entry/confirmation/ConfirmUploadTransFragment.java |
| ReversePartialApprovalFragment | entry/confirmation/ReversePartialApprovalFragment.java |
| SupplementPartialApprovalFragment | entry/confirmation/SupplementPartialApprovalFragment.java |

### 独立类（extends BaseEntryFragment 或 Fragment）

| 类名 | 路径 | 说明 |
|------|------|------|
| ConfirmReceiptViewFragment | entry/confirmation/ConfirmReceiptViewFragment.java | extends BaseEntryFragment |
| ConfirmSurchargeFeeFragment | entry/confirmation/ConfirmSurchargeFeeFragment.java | extends BaseEntryFragment |
| DisplayQRCodeReceiptFragment | entry/confirmation/DisplayQRCodeReceiptFragment.java | extends BaseEntryFragment |
| StartUIFragment | entry/confirmation/StartUIFragment.java | extends BaseEntryFragment |

---

## 3. entry/text/（Text 模块）

### 基类

| 类名 | 路径 | 说明 |
|------|------|------|
| AAmountFragment | entry/text/amount/AAmountFragment.java | 金额类基类 |
| ANumFragment | entry/text/number/ANumFragment.java | 数字类基类 |
| ANumTextFragment | entry/text/numbertext/ANumTextFragment.java | 数字文本类基类 |
| ATextFragment | entry/text/text/ATextFragment.java | 文本类基类 |

### text/amount/ 子类

| 类名 | 路径 |
|------|------|
| AmountFragment | entry/text/amount/AmountFragment.java |
| CashbackFragment | entry/text/amount/CashbackFragment.java |
| FuelAmountFragment | entry/text/amount/FuelAmountFragment.java |
| TaxAmountFragment | entry/text/amount/TaxAmountFragment.java |
| TipFragment | entry/text/amount/TipFragment.java |
| TotalAmountFragment | entry/text/amount/TotalAmountFragment.java |

### text/number/ 子类

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

### text/numbertext/ 子类

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

### text/text/ 子类

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

### text/ 独立类

| 类名 | 路径 |
|------|------|
| AVSFragment | entry/text/AVSFragment.java |
| EnterOrigTransDateFragment | entry/text/EnterOrigTransDateFragment.java |
| ExpiryFragment | entry/text/ExpiryFragment.java |
| FSAAmountFragment | entry/text/fsa/FSAAmountFragment.java |
| FSAFragment | entry/text/fsa/FSAFragment.java |
| FsaOptionsFragment | entry/text/fsa/FsaOptionsFragment.java |
| EnterFleetDataFragment | entry/text/fleet/EnterFleetDataFragment.java |
| FleetData | entry/text/fleet/FleetData.java |
| NewEnterFleetDataFragment | entry/text/fleet/NewEnterFleetDataFragment.java |

---

## 4. entry/security/（Security 模块）

### 基类

| 类名 | 路径 | 说明 |
|------|------|------|
| ASecurityFragment | entry/security/ASecurityFragment.java | 安全类基类 |

### 子类与独立类

| 类名 | 路径 | 说明 |
|------|------|------|
| AdministratorPasswordFragment | entry/security/AdministratorPasswordFragment.java | extends BaseEntryFragment |
| ClssLightsViewStatusManager | entry/security/ClssLightsViewStatusManager.java | 独立类 |
| EnterCardAllDigitsFragment | entry/security/EnterCardAllDigitsFragment.java | extends ASecurityFragment |
| EnterCardLast4DigitsFragment | entry/security/EnterCardLast4DigitsFragment.java | extends ASecurityFragment |
| EnterVcodeFragment | entry/security/EnterVcodeFragment.java | extends ASecurityFragment |
| InputAccountFragment | entry/security/InputAccountFragment.java | extends BaseEntryFragment |
| ManageInputAccountFragment | entry/security/ManageInputAccountFragment.java | extends BaseEntryFragment |
| PINFragment | entry/security/PINFragment.java | extends BaseEntryFragment |

---

## 5. entry/option/（Option 模块）

### 基类

| 类名 | 路径 | 说明 |
|------|------|------|
| AOptionEntryFragment | entry/option/AOptionEntryFragment.java | 选项类基类 |

### 子类

| 类名 | 路径 |
|------|------|
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

## 6. entry/poslink/（POSLink 模块）

| 类名 | 路径 | 说明 |
|------|------|------|
| InputTextFragment | entry/poslink/InputTextFragment.java | 独立类 |
| ItemDetailWrapper | entry/poslink/ItemDetailWrapper.java | 数据类 |
| ItemListAdapter | entry/poslink/ItemListAdapter.java | RecyclerView.Adapter |
| LabelAdapter | entry/poslink/LabelAdapter.java | RecyclerView.Adapter |
| MessageItemAdapter | entry/poslink/MessageItemAdapter.java | ArrayAdapter |
| MsgInfoWrapper | entry/poslink/MsgInfoWrapper.java | 数据类 |
| POSLinkStatusManager | entry/poslink/POSLinkStatusManager.java | BroadcastReceiver |
| ShowDialogFormCheckBoxFragment | entry/poslink/ShowDialogFormCheckBoxFragment.java | Fragment |
| ShowDialogFormFragment | entry/poslink/ShowDialogFormFragment.java | BaseEntryFragment |
| ShowDialogFormRadioFragment | entry/poslink/ShowDialogFormRadioFragment.java | Fragment |
| ShowDialogFragment | entry/poslink/ShowDialogFragment.java | BaseEntryFragment |
| ShowInputTextBoxFragment | entry/poslink/ShowInputTextBoxFragment.java | BaseEntryFragment |
| ShowItemFragment | entry/poslink/ShowItemFragment.java | BaseEntryFragment |
| ShowMessageFragment | entry/poslink/ShowMessageFragment.java | BaseEntryFragment |
| ShowSignatureBoxFragment | entry/poslink/ShowSignatureBoxFragment.java | BaseEntryFragment |
| ShowTextBoxController | entry/poslink/ShowTextBoxController.java | 控制器 |
| ShowTextBoxFragment | entry/poslink/ShowTextBoxFragment.java | BaseEntryFragment |
| ShowThankYouFragment | entry/poslink/ShowThankYouFragment.java | BaseEntryFragment |
| TextShowingUtils | entry/poslink/TextShowingUtils.java | 工具类 |

---

## 7. entry/information/（Information 模块）

| 类名 | 路径 | 说明 |
|------|------|------|
| DisplayApprovalUtils | entry/information/DisplayApprovalUtils.java | 工具类 |
| DisplayApproveMessageFragment | entry/information/DisplayApproveMessageFragment.java | BaseEntryFragment |
| DisplayTransInfoFragment | entry/information/DisplayTransInfoFragment.java | BaseEntryFragment |

---

## 8. entry/signature/、entry/task/

| 类名 | 路径 | 说明 |
|------|------|------|
| ElectronicSignatureView | entry/signature/ElectronicSignatureView.java | extends View |
| SignatureFragment | entry/signature/SignatureFragment.java | BaseEntryFragment |
| FinishTask | entry/task/FinishTask.java | extends ScheduledTask |
| ScheduledTask | entry/task/ScheduledTask.java | 抽象类 |
| TimeoutTask | entry/task/TimeoutTask.java | extends ScheduledTask |

---

## 9. status/、utils/、view/、viewmodel/、settings/

### status/

| 类名 | 路径 |
|------|------|
| StatusFragment | status/StatusFragment.java |
| ToastFragment | status/ToastFragment.java |
| TransCompletedStatusFragment | status/TransCompletedStatusFragment.java |

### utils/

| 类名 | 路径 |
|------|------|
| BundleMaker | utils/BundleMaker.java |
| CurrencyUtils | utils/CurrencyUtils.java |
| DateUtils | utils/DateUtils.java |
| DeviceUtils | utils/DeviceUtils.java |
| EntryActivityActionBar | utils/EntryActivityActionBar.java |
| EntryRequestUtils | utils/EntryRequestUtils.java |
| FileLogAdapter | utils/FileLogAdapter.java |
| InterfaceHistory | utils/InterfaceHistory.java |
| Logger | utils/Logger.java |
| SelectOptionContent | utils/SelectOptionContent.java |
| StringUtils | utils/StringUtils.java |
| TaskScheduler | utils/TaskScheduler.java |
| ThreadPoolManager | utils/ThreadPoolManager.java |
| Toast | utils/Toast.java |
| ValuePatternUtils | utils/ValuePatternUtils.java |
| ViewUtils | utils/ViewUtils.java |
| PrintDataConverter | utils/format/PrintDataConverter.java |
| PrintDataItem | utils/format/PrintDataItem.java |
| PrintDataItemContainer | utils/format/PrintDataItemContainer.java |
| EntryAction | utils/interfacefilter/EntryAction.java |
| EntryActionAndCategoryRepository | utils/interfacefilter/EntryActionAndCategoryRepository.java |
| EntryActionFilterManager | utils/interfacefilter/EntryActionFilterManager.java |
| EntryCategory | utils/interfacefilter/EntryCategory.java |

### view/

| 类名 | 路径 | 说明 |
|------|------|------|
| AmountTextWatcher | view/AmountTextWatcher.java | TextWatcher |
| ClssLight | view/ClssLight.java | AppCompatImageView |
| ClssLightsView | view/ClssLightsView.java | View |
| FormatTextWatcher | view/FormatTextWatcher.java | TextWatcher |
| SelectOptionsView | view/SelectOptionsView.java | RecyclerView |
| TextField | view/TextField.java | 自定义 View（若存在） |

### viewmodel/、settings/、根

| 类名 | 路径 |
|------|------|
| SecondScreenInfoViewModel | viewmodel/SecondScreenInfoViewModel.java |
| EntryActionAndCategoryFilterFragment | settings/EntryActionAndCategoryFilterFragment.java |
| EntryActionFilterByCategoryFragment | settings/EntryActionFilterByCategoryFragment.java |
| DemoApplication | DemoApplication.java |
| MainActivity | MainActivity.java |

---

## 10. 依赖关系（转换顺序参考）

- **UIFragmentHelper** 被 EntryActivity 使用 → 先于 EntryActivity 或与 EntryActivity 同时转换
- **BaseEntryFragment** 被所有 Entry 使用 → 最先转换
- **ATextFragment、AConfirmationFragment、ASecurityFragment、AOptionEntryFragment、AAmountFragment、ANumFragment、ANumTextFragment** → 在 BaseEntryFragment 之后、具象子类之前
- **工具类**（CurrencyUtils、EntryRequestUtils 等）→ 被 Fragment 调用，可先转换

---

## 11. 统计

- **总类数**：约 177 个 Java 文件
- **基类**：9 个（BaseEntryFragment、UIFragmentHelper、AConfirmationFragment、ATextFragment、AAmountFragment、ANumFragment、ANumTextFragment、ASecurityFragment、AOptionEntryFragment、ScheduledTask）
- **Fragment 子类**：约 100+ 个
