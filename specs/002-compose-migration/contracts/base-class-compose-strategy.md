# 基类 Compose 迁移策略

**特性**：002-compose-migration  
**日期**: 2025-03-18 | **修订**: 2026-03-28  
**目的**: 明确 **Compose 宿主**、**Navigation Compose（`NavHost`）** 与基类迁移策略，保证 **渐进交付**、**布局层去 XML**（**FR-005**）、**少并列 `ComposeView`（FR-019）**，以及 **母版** [`specs/000-poslinkui-project-master/spec.md`](../../000-poslinkui-project-master/spec.md) 所述 **Intent / `EntryActivity` / manifest** 与 **FR-M001（action→正确界面）** 等价。

**关联**：spec **FR-015**–**FR-019**；[`device-profiles.md`](../device-profiles.md)；任务 **T030–T035**、[`plan.md`](../plan.md)。

---

## 1. 过渡态 vs 目标态（与 FR-005 一致）

| 维度 | 过渡态（模块迁移中） | 目标态（模块收尾 / 全量 T032） |
|------|----------------------|--------------------------------|
| 导航 / 宿主 | 可短期「每 `Fragment` 一个 `ComposeView`」；**应尽快收敛** | **单宿主**（`EntryActivity.setContent` 或 **一个**宿主 `Fragment` + **一个** `ComposeView`）内 **`NavHost` + `composable(route)`**（**FR-019**，任务 **T035**）；禁止长期「每 Entry 并列多棵无协调 Compose 根」 |
| 页面结构 | 同上 | Entry **不得**依赖 `res/layout` 承载页面根 |
| Activity | **不新建**替代 `EntryActivity` 的宿主入口 Activity | 同左；**action→route→界面** 与母版 **FR-M001** 等价 |
| manifest | `activity-alias`、action、launchMode 等与迁移前一致 | 同左 |
| XML 保留 | 允许仍存在未删 layout，直至该路径已迁完并确认无引用 | 删除已无 `R.layout.*` / ViewBinding 引用；**仍保留**的 XML 记入 **T032** 清单 |

**禁止**：在**新增或维护的 Entry 路径**上继续引入「整页 XML 布局」作为长期方案；过渡期内仅允许尚未迁移完成的旧 layout 暂存。

---

## 2. 宿主边界：`NavHost` 优先（FR-019）

### 2.1 目标形态（推荐）

在 **唯一** Compose 入口内使用 **Jetpack Navigation Compose**：

```kotlin
NavHost(
    navController = navController,
    startDestination = TransactionRoute.AMOUNT.route // 示例：实际 route 以仓库 sealed/enum 为准
) {
    composable(TransactionRoute.HOME.route) { /* HomeScreen */ }
    composable(TransactionRoute.AMOUNT.route) { /* AmountScreen：组合 T030 预制件 */ }
    // …各 Entry 对应 composable(destination)
}
```

- **`navController`** 与 **`EntryActivity.onNewIntent`**（`singleTop`）须协同：**navigate / popBackStack / 清栈** 策略满足 **状态机与支付终态**（spec **SC-013**）。  
- **`com.pax.us.pay.action.*`（或 `PARAM_ACTION`）→ route** 须在 **单点** 解析（任务 **T035**），替代分散的「每 Fragment 硬编码」。  
- **副屏**（FR-018）仍为独立 `Presentation` 树时，**复用 Theme / `DeviceLayoutSpec` API**，避免第三套 magic dp。

### 2.2 过渡：子类 `onCreateView` 返回 `ComposeView`（短期）

在尚未接入 **§2.1** 前，子类可临时返回 `ComposeView { PosLinkEntryRoot { … } }`；**每新增一屏应优先改为 `NavHost` 内 `composable`**，而非永久增加并列根。

### 2.3 过渡：基类「Compose / XML」分支（仅过渡）

在仍有未迁子类时，基类可保留 compose / inflate 分支；**全量 Compose 后删除 inflate**；**最终**应收敛到 **§2.1 单宿主**，而非长期保留「多 Fragment 多 Compose 根」。

### 2.4 执行顺序

- **T035** 落地 `navigation-compose` + `NavHost` 骨架 + action→route。  
- **T005** 视为已验证「Compose 可嵌入宿主」；**后续以 §2.1 为准**。  

---

## 3. Fragment 扁平化（FR-015）与基类

- **优先**：多个屏仅差 **标题、提示、校验、按钮文案、maxLength** 等时，使用 **单一 Fragment + 参数化 `@Composable` Screen**（或共享配置数据类），**避免**仅为差异新增子类。  
- **保留子类/继承**：仅当存在 **安全隔离**、**完全不同的交互**、或 **Neptune/constant 契约强制分类型** 等硬边界时；须在 PR/任务中注明理由。  
- **与宪章关系**：若与 `.specify/memory/constitution.md` **§一.II** 的表述冲突，按 spec **假设前提**执行 **宪章 MINOR 修订**或**书面豁免**后再合并大规模扁平化改动。  

基类（如 `ATextFragment`）可演进为：**薄 Fragment** 只负责 `loadArgument` / `sendNext`，UI 全部进入 **一个或少数几个** 参数化 Screen。

---

## 4. ATextFragment

### 迁移步骤

1. **优先**：在 **`NavHost`** 中为 Text 类 action 增加 **`composable(route) { TextScreen(…) }`**（参数：`message`、`onConfirm`、`maxLength` 等），业务状态可留在 **ViewModel** / 宿主 Activity。  
2. **过渡**：`onCreateView` → 单 `ComposeView` → **统一根** → `TextScreen(...)`（与 §2.2 一致）。  
3. 原 `loadView`、`textField`、`onConfirmButtonClicked` 迁入 Composable；删除 layout 引用  
4. 保持 `loadArgument`、`submit`、`sendNext` 等行为不变  

### 契约不变

- `getRequestedParamName()`、`formatMessage()`、`getMaxLength()` 等可由子类提供或由 **配置对象** 注入（扁平化时）  
- `submit(String)` → `sendNext(bundle)` 不变  

---

## 5. AConfirmationFragment

### 迁移步骤

1. **优先**：`NavHost` 内 **`composable` + `ConfirmationScreen(...)`**  
2. **过渡**：单 `ComposeView` + **统一根** + `ConfirmationScreen(...)`  
3. 删除确认页专用 layout（无引用后）  

### 契约不变

- `formatMessage`、`getPositiveText()`、`getNegativeText()` 等 → 参数或配置注入  
- `submit(Boolean)` 不变  

---

## 6. ASecurityFragment

### 迁移步骤

1. PIN / 账号等：Compose `TextField` 或安全控件；**必须**掩码、无明文日志（spec FR-008 / 宪章 XIV）  
2. 安全键盘若依赖 SDK：通过 `AndroidView` 或适配层嵌入，**设备逻辑不进业务 Composable 深处**（宪章 XVI）  

### 契约不变

- 敏感数据展示与日志要求不变  

---

## 7. AOptionEntryFragment

### 迁移步骤

1. `RecyclerView` + `SelectOptionsView` → `LazyColumn` 等；优先复用 **FR-016** 列表行预制件  
2. 选择结果 → `sendNext(bundle)` 不变  

### 契约不变

- 与 `com.paxus.ui:constant` / Neptune 字段一致  

---

## 8. AAmountFragment、ANumFragment、ANumTextFragment

### 迁移步骤

1. 金额/数字：`TextField` + 校验（`valuePatten`、length 等）在 Composable 或 ViewModel 侧实现  
2. 去除依赖 XML 的 `TextWatcher` 布局后，保留行为等价的状态与校验  

### 契约不变

- `getRequestedParamName()`、`formatMessage()`、校验语义不变  

---

## 9. Design Tokens、统一根与机型规格（FR-016 / FR-017 / FR-018 / FR-019）

- **禁止**：各 **业务** Screen 散落硬编码 dp/sp/色值及 `if (Build.MODEL == …)`；应通过 **T030** 的 **Token/预制件 API** 与 **T031** 的 **`DeviceLayoutSpec`（配置层 profile，见 [`device-profiles.md`](../device-profiles.md)）** 取值。  
- **允许**：在 **单一注册表/配置文件** 内维护机型键 → 规格映射（非「每个 Composable 一套魔术数」）。  
- **副屏（FR-018）**：`TransactionPresentation` 等第二显示路径须复用同一套 **Theme + 规格管线**（可扩展副屏字段），与 `EntryActivity` 的 `dismissPresentation` 生命周期一致。  
- **`NavHost` 内 destination** 同样只通过 **T030/T031** 取样式，不得在 composable  lambda 内堆未映射的 `NN.dp`。  
- 数值权威仍见 [`design-tokens.md`](../design-tokens.md)（语义 + 基准档）；**P0** 见 **SC-011 / SC-012**、**T034**；**导航等价** 见 **SC-013**、**T035**。  

---

## 10. 验证与收尾

- 每模块迁移后 **可独立测试**；manifest / `EntryActivity` 调起与母版一致  
- **模块内**：grep `R.layout`、ViewBinding，删除已无引用 layout  
- **导航**：抽查 **action→route**、`onNewIntent`、返回栈与 **SC-013**  
- **全量**：**T032**、**T034**、**T027**；参见 [neptune-poslink-compatibility.md](neptune-poslink-compatibility.md)  
