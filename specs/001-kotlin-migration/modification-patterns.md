# 类类型与修改模式：Java 转 Kotlin 迁移

**特性**： 001-kotlin-migration  
**日期**: 2025-03-18（2025-03-25 与 [spec.md](spec.md) FR-005/FR-008 对齐修订）  
**目的**: 按类类型定义修改模式，指导转换与审查。

**相关**：[kotlin-migration-compilation SKILL](../../.cursor/skills/kotlin-migration-compilation/SKILL.md)（编译错误修复速查、错误位置、执行顺序）

---

## 0. 空安全与 `!!`（必遵守，与 spec **SC-006** 一致）

| 原则 | 说明 |
|------|------|
| **生产源码禁止 `!!`** | `app/.../demo/**/*.kt` 中**不使用** `!!`；避免 Idea 转换后的 NPE 与页面起不来 |
| **优先替代** | `?.`、`?:`、`?.let { }`、`requireNotNull(x) { "msg" }`、`checkNotNull(x)`、early return |
| **Fragment/Activity** | 用 `requireContext()`、`requireActivity()`、`requireArguments()` 替代对可空 getter 的强解 |
| **集合/列表** | `?.getOrNull()`、`list?.get(index) ?: return`、`firstOrNull()` 等 |
| **View** | `findViewById` 用 `?.` 或 `rootView ?: return` |
| **极少数例外** | 仅第三方/SDK 互操作且无法在边界消化时，**PR 中单点清单 + 评审**；默认仍应避免 `!!` |

## 0.1 状态暴露：Flow 优先（与 spec FR-005）

| 原则 | 说明 |
|------|------|
| **不新增业务 `LiveData`** | 迁移中凡改动的 ViewModel/状态，暴露 `StateFlow` / `SharedFlow` / `Flow` |
| **收集** | UI 层使用 `lifecycleScope` + `repeatOnLifecycle`（或项目既有等价方式） |
| **边界** | 纯 Compose 主导界面与 **002-compose-migration** 对齐 |

## 0.2 判空与字符串：Kotlin 标准库优先

| 替代方向 | 说明 |
|----------|------|
| `TextUtils.isEmpty(s)` | `s.isNullOrEmpty()`（Kotlin） |
| 空白语义 | `isNullOrBlank()` |
| `length == 0` | `isEmpty()` / `isBlank()`（字符串）；集合用 `isEmpty()` / `none()` |
| 避免无意义 Java 堆砌 | 可读性与空安全优先，Android API 若更清晰可保留并注释 |

## 0.3 构造函数与可见性

| 原则 | 说明 |
|------|------|
| **主构造优先** | 参数与属性一体化，减少费解的二级构造链 |
| **工厂** | 需要时用 `companion object` / 命名工厂，集中文档与单测 |
| **可见性** | 收紧 `internal`/`private`，避免「全 public 遗留 Java 风格」 |

---

## 1. 抽象基类

| 模式 | 说明 |
|------|------|
| **转换顺序** | 先转换，子类依赖基类 |
| **abstract 方法** | 保持 `abstract`，签名不变 |
| **open 修饰** | 子类可 override 的方法需 `open` |
| **@CallSuper** | 转为 KDoc `/** 子类必须调用 super */` |
| **空安全** | 构造函数参数、成员变量注意 null 处理 |

**涉及类**：BaseEntryFragment、ATextFragment、AConfirmationFragment、ASecurityFragment、AOptionEntryFragment、AAmountFragment、ANumFragment、ANumTextFragment、ScheduledTask

---

## 2. 具象 Fragment

| 模式 | 说明 |
|------|------|
| **转换顺序** | 基类之后 |
| **loadView** | 重点审查 `findViewById` 返回值、`rootView` 可能为 null 的链式调用 |
| **onCreateView** | 审查 `getArguments()`、`requireActivity()` 等 |
| **空安全** | **禁止 `!!`**（见 §0）；优先 `?.`、`?:`、`let`、`also`、`requireNotNull`、`checkNotNull` |
| **when** | 分支完整，无 fall-through 误用 |

**涉及类**：所有 Confirm*、Select*、Show*、Display*、Text 子类等

---

## 3. 工具类

| 模式 | 说明 |
|------|------|
| **静态方法** | 转为 `object` 或顶层函数 |
| **无状态** | 若全静态，优先 `object` |
| **依赖** | 被 Fragment 调用，可先转换 |

**涉及类**：StringUtils、CurrencyUtils、DateUtils、ValuePatternUtils、EntryRequestUtils、BundleMaker、ViewUtils、Logger、Toast 等

---

## 4. Adapter / View

| 模式 | 说明 |
|------|------|
| **RecyclerView.Adapter** | 泛型、ViewHolder 模式保持；注意 `getItemCount()`、`onBindViewHolder` 空安全 |
| **自定义 View** | 继承关系、构造函数保持 |
| **TextWatcher** | 接口实现，lambda 转换 |

**涉及类**：ItemListAdapter、LabelAdapter、MessageItemAdapter、SelectOptionsView、ElectronicSignatureView、ClssLight、ClssLightsView、AmountTextWatcher、FormatTextWatcher

---

## 5. Helper / Manager

| 模式 | 说明 |
|------|------|
| **Neptune 互操作** | 对 `@Nullable` 返回值使用 `?.` 或显式判空 |
| **路由逻辑** | `when` 分支完整，避免遗漏 action |
| **依赖** | UIFragmentHelper 被 EntryActivity 使用，先于或与 Activity 同时转换 |

**涉及类**：UIFragmentHelper、POSLinkStatusManager、ClssLightsViewStatusManager、EntryActionFilterManager、EntryActionAndCategoryRepository

---

## 6. Activity / Application

| 模式 | 说明 |
|------|------|
| **生命周期** | 与 Fragment 交互处注意 `isAdded`、`isDestroyed` |
| **Intent** | `getIntent()`、`getExtras()` 可能为 null |

**涉及类**：MainActivity、EntryActivity、DemoApplication

---

## 7. 数据类 / Wrapper

| 模式 | 说明 |
|------|------|
| **简单 POJO** | 可考虑转为 Kotlin `data class` |
| **序列化** | 若涉及 Parcelable/Serializable，保持兼容 |

**涉及类**：FleetData、MsgInfoWrapper、ItemDetailWrapper、SelectOptionContent、PrintDataItem、PrintDataItemContainer
