# 类类型与修改模式：UI 转 Compose 迁移

**特性**：002-compose-migration  
**日期**: 2025-03-18 | **修订**: 2026-03-28  
**目的**: 按类类型定义 Compose 迁移模式，与 [spec.md](spec.md) **FR-019**（**`NavHost` + `composable` 优先**）、**FR-016**、**FR-017** 及 [base-class-compose-strategy.md](contracts/base-class-compose-strategy.md) 一致。

---

## 0. 导航优先（所有类型的默认策略）

| 项 | 说明 |
|----|------|
| **主路径** | 在 **单宿主**（`EntryActivity.setContent` 或 **单宿主 Fragment + 唯一 `ComposeView`**）内使用 **`NavHost { composable(route) { … } }`**；每个 Entry 动作对应 **一个 destination**（或带参数的同一 destination） |
| **路由** | 类型安全 route（`sealed class` / `enum` 等；spec 示例名 `TransactionRoute`）；**`Intent` action → route** 见 [contracts/entry-navigation-routes.md](contracts/entry-navigation-routes.md)（**T035** 落地） |
| **过渡** | 尚未接入 `NavHost` 前，可短期 **`onCreateView` → `ComposeView` → Screen**；**新屏禁止**长期新增并列无协调的 Compose 根 |
| **任务** | **T035**（骨架 + `onNewIntent`）、**T030**（destination 内共用预制件） |

---

## 1. 抽象基类

| 模式 | 说明 |
|------|------|
| **迁移顺序** | **T035** `NavHost` 骨架 → 各模块将 UI 迁为 **composable destination**；基类可退化为 **导航参数 + ViewModel** 或薄壳 |
| **策略** | 以 [base-class-compose-strategy.md](contracts/base-class-compose-strategy.md) **§2.1** 为准；过渡用 **§2.2 / §2.3** |
| **契约** | **业务契约**（`sendNext`、Bundle、constant）不变；**承载**从多 Fragment 收敛到 Nav 时保持 **FR-M001** 等价 |

**涉及类**：BaseEntryFragment、ATextFragment、AConfirmationFragment、ASecurityFragment、AOptionEntryFragment、AAmountFragment、ANumFragment、ANumTextFragment（随收敛，部分逻辑迁入共享 ViewModel / destination 工厂）

---

## 2. 具象 Fragment（Text / Amount / Num / NumText）

| 模式 | 说明 |
|------|------|
| **优先** | 在 **`NavHost`** 中 **`composable(route) { XxxScreen(…) }`**；`XxxScreen` 为 `@Composable`，参数化 message、onConfirm、maxLength 等 |
| **过渡** | `onCreateView` 返回 **`ComposeView { PosLinkEntryRoot { XxxScreen(…) } }`**（单根，计划迁到 Nav） |
| **布局** | 经 **FR-016 / T030** 与 **FR-017** 取间距与控件尺寸，禁止业务文件散落 magic `dp` |
| **校验** | valuePatten、length 等在 Composable / ViewModel 内实现 |

**涉及类**：AmountFragment、ReferenceNumberFragment、ZipcodeFragment、TipFragment、TableNumberFragment、ExpiryFragment 等（**类名可保留**作路由壳，直至完全去掉 Fragment）

---

## 3. 具象 Fragment（Security）

| 模式 | 说明 |
|------|------|
| **优先** | **`composable` + 安全 Screen**（PIN、账号等） |
| **敏感输入** | 掩码；禁止明文日志 |
| **安全键盘** | SDK 通过 `AndroidView` 或适配层嵌入（宪章 SDK 隔离） |
| **验收** | 必须验证掩码、无 Log 敏感字段 |

**涉及类**：PINFragment、InputAccountFragment、EnterCardAllDigitsFragment、EnterCardLast4DigitsFragment、AdministratorPasswordFragment、ManageInputAccountFragment

---

## 4. 具象 Fragment（Confirmation）

| 模式 | 说明 |
|------|------|
| **优先** | **`NavHost` + `ConfirmationScreen`** |
| **交互** | 按钮 → `submit(true/false)` / `sendNext(bundle)` 行为不变 |
| **formatMessage** | 经参数或配置注入 Composable |

**涉及类**：ConfirmTotalAmountFragment、ConfirmCardProcessResultFragment、ConfirmBalanceFragment 等

---

## 5. 具象 Fragment（POSLink）

| 模式 | 说明 |
|------|------|
| **优先** | 各 Show* / Dialog 对应 **独立或参数化 composable destination** |
| **展示类** | `LazyColumn`、`Text` 等 |
| **对话框** | Compose `Dialog` / `AlertDialog` |
| **列表** | `RecyclerView` + Adapter → `LazyColumn` + items |
| **互操作** | 必要时 `AndroidView`；签名等自定义 View 同 [§8](#8-工具类--adapter--view) |

**涉及类**：ShowItemFragment、ShowTextBoxFragment、ShowDialogFragment、ShowMessageFragment、ShowInputTextBoxFragment、ShowDialogFormCheckBoxFragment、ShowDialogFormRadioFragment、ShowSignatureBoxFragment、ShowThankYouFragment

---

## 6. 具象 Fragment（Option）

| 模式 | 说明 |
|------|------|
| **优先** | **`composable` + `LazyColumn` / 可选列表** |
| **选择回调** | `sendNext(bundle)` 不变 |

**涉及类**：SelectTransTypeFragment、SelectCurrencyFragment、SelectMerchantFragment 等

---

## 7. 具象 Fragment（Information）

| 模式 | 说明 |
|------|------|
| **优先** | **`composable` + 信息布局** |
| **布局** | Design Tokens + **DeviceLayoutSpec** |

**涉及类**：DisplayTransInfoFragment、DisplayApproveMessageFragment

---

## 8. 工具类 / Adapter / View

| 模式 | 说明 |
|------|------|
| **工具类** | 通常不改，仅被 Compose / ViewModel 调用 |
| **Adapter** | 逐步替换为 `LazyColumn`；或 `AndroidView` 互操作 |
| **自定义 View** | `ElectronicSignatureView` 等 → `AndroidView` 嵌入 **destination** 内 Composable |

---

## 9. 通用要求

- **页面还原度第一**：与 Design Tokens、设计稿一致（基准机像素级 + P0 见 **SC-011**）
- **manifest / `EntryActivity`**：alias 与 action 不变；**界面**由 **Nav** 或过渡 Fragment 承载须 **等价**（**SC-013**）
- **图片**：落本地，无远程 URL
- **Neptune/POSLink**：EntryRequest、EntryResponse 契约不变
