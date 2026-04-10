---
name: kotlin-migration-compilation
description: Java 迁 Kotlin 后的编译错误修复与空安全规范。用于解决 valuePatten 智能转换、getMaxLength/override val、内部类 Receiver、滥用 !! 等典型问题。参考 modification-patterns.md 第 0 节。
---

# Kotlin 迁移编译修复技能

## 一、修改总结

### 1. 文档完善
- **modification-patterns.md** 新增第 0 节「空安全与 `!!` 使用规范」
- 明确禁止滥用 `!!`，优先 `?.`、`?:`、`let`、`requireNotNull`、`requireContext()` 等

### 2. 基类与子类模式
| 问题 | 修复方式 |
|------|----------|
| `getMaxLength()` 与基类 `abstract val maxLength` 冲突 | 子类改为 `private var _maxLength` + `override val maxLength get() = _maxLength`，删除 `getMaxLength()` |
| `getCurrency()` 同理 | `override val currency get() = _currency` |
| `getRequestedParamName()` | 改为 `override val requestedParamName` |
| Java 访问器转 Kotlin 属性 | `getContent()` → `content`，`getTouched` → `touched`，`getPathPos` → `pathPos` |

### 3. 空安全与智能转换（smart cast）
| 问题 | 修复方式 |
|------|----------|
| `valuePatten` 可变属性无法做智能转换 | 用 `val p = valuePatten ?: ""` 或直接 `valuePatten ?: ""` 传入 `ValuePatternUtils` |
| `Context?` 需非空 | `getContext()` → `requireContext()` |
| `Activity?` 需非空 | `activityForContext?.finishAndRemoveTask()` 或 `activity ?: return` |
| `MutableList<TextView?>` 与 `MutableList<TextView>` 不匹配 | 接收方改为 `MutableList<TextView?>`，循环内 `val tv = textView ?: continue` |
| `LinearLayout.LayoutParams?` 需非空 | 提前 `val effectiveLp = lp ?: LinearLayout.LayoutParams(...)` 再复用 |

### 4. 内部类与接收器（Receiver）
| 问题 | 修复方式 |
|------|----------|
| 编译提示：内部类 Receiver 须带外部类接收器才能构造 | `InputAccountFragment.Receiver()` → `Receiver()`（在类内）或 `this@OuterClass.Receiver()` |
| `ASecurityFragment` 同理 | `this@ASecurityFragment.Receiver()` |

### 5. `when` 与 `break`
| 问题 | 修复方式 |
|------|----------|
| 编译提示：`break`/`continue` 仅允许出现在循环内 | `when` 分支内误用 `break` 时直接删除 |
| 编译提示：`when` 表达式必须穷尽所有分支 | 补全 `ON_CREATE`/`ON_RESUME` 等分支，或加 `else -> {}` |

### 6. 类型与泛型
| 问题 | 修复方式 |
|------|----------|
| `MutableMap<String?, MutableList<String?>?>` 与 `MutableMap<String, MutableList<String?>?>` | 使用 `hashMapOf(TEXT_SHOWING_LIST to ...)` 明确 key 为 `String` |
| `MutableList<PrintDataItem?>` 与 `filterItems(MutableList<PrintDataItem>?)` | `parsedItems.filterNotNull().toMutableList()` |
| `Option.getValue()` 与 Kotlin 属性 | `option?.value` 替代 `option?.getValue()` |
| `SelectOptionsView.initialize` 需 `MutableList<Option>` | `items?.filterNotNull()?.toMutableList() ?: mutableListOf()` |

### 7. 其他
| 问题 | 修复方式 |
|------|----------|
| `@LayoutRes` 不适用于无幕后字段的属性 | 改为 `@get:LayoutRes` |
| `ViewModelProvider.get<SecondScreenInfoViewModel?>` 类型错误 | 使用 `get(SecondScreenInfoViewModel::class.java)`，后续用 `viewModel?.` |
| `ThreadPoolManager.getInstance()` 返回可空 | `ThreadPoolManager.getInstance()?.execute(...)` |
| `return@setOnKeyListener` 标签未定义 | 使用 `keyListener@{ ... return@keyListener true }` 显式标签 |

---

## 二、出错位置速查

| 文件/模块 | 典型错误 |
|-----------|----------|
| **ATextFragment / ANumFragment / ANumTextFragment 子类** | valuePatten 智能转换问题；getMaxLength 需改为 override val maxLength |
| **AAmountFragment / AmountFragment / FuelAmountFragment / TaxAmountFragment** | getMaxLength/getCurrency → override val；currency 在 loadView 中需 `?: ""` |
| **ASecurityFragment / InputAccountFragment / ManageInputAccountFragment / PINFragment** | Receiver 构造需含 receiver；break 在 when 内非法 |
| **ClssLightsViewStatusManager** | `intent.getAction()` → `intent.action ?: ""`；`when` 需补全所有分支 |
| **SignatureFragment** | getTouched/getPathPos → touched/pathPos；pathPos 为 `MutableList<FloatArray?>` 需 `?.let` |
| **FinishTask / TimeoutTask** | activityForContext 可空，需 `?.` 或提前 return |
| **TextShowingUtils** | parse 的 map 类型；getPrintDataItems 返回含 null 的 List，需 filterNotNull；getContent → content |
| **ShowTextBoxController** | lp 可空，需提前 effectiveLp |
| **ShowThankYouFragment / ShowTextBoxFragment** | setTextView 接收 `MutableList<TextView?>`；view 循环内 `view ?: continue` |
| **EntryActionAndCategoryFilterFragment** | getLifecycle → lifecycle；EntryCategory? 需 getOrNull?.let |
| **EntryActionFilterByCategoryFragment** | getContext → requireContext |
| **TransCompletedStatusFragment** | Context? → Context；message 智能转换改用局部变量 |
| **CashbackFragment / TipFragment / FsaOptionsFragment** | option?.getValue() → option?.value；tipFieldModified 为 Boolean? 需 `== true` |
| **FSAFragment** | fsaAmountOptions 可空，用局部变量 opts 再判空 |
| **NewEnterFleetDataFragment** | FleetData 用 `defaultValuePattern` 等属性替代 getXxx；实现 `override val requestedParamName` |

---

## 三、执行顺序建议

1. **先修基类**：AAmountFragment、ANumFragment、ANumTextFragment、ATextFragment 的 abstract val 与 valuePatten 用法
2. **批量修子类**：所有 getMaxLength/getCurrency 改为 override val，valuePatten 用 `?: ""`
3. **修 security 模块**：Receiver、break、requireContext
4. **修 poslink / TextShowingUtils**：map 类型、filterNotNull、content、LayoutParams
5. **修 settings / status**：lifecycle、requireContext、EntryCategory?
6. **修 amount 相关**：CashbackFragment、TipFragment、FsaOptionsFragment 的 Option.value、append

---

## 四、禁止项（避免再次出错）

- 禁止在 when 分支内使用 `break`（when 非循环）
- 禁止 `getXxx()` 与 `override val xxx` 混用导致编译报「未覆盖任何成员」
- 禁止 `MutableList<T?>` 直接传给需要 `MutableList<T>` 的 API，需 `filterNotNull().toMutableList()`
- 禁止内部类用 `OuterClass.InnerClass()` 却无外部接收器构造，应使用 `Receiver()` 或 `this@Outer.InnerClass()`
- 禁止对可变属性依赖智能转换，需用局部变量或 `?: ""`

---

## 五、参考

- `specs/001-kotlin-migration/modification-patterns.md` 第 0 节
- `specs/001-kotlin-migration/tasks.md`
- payment-project-hard-problems 规则（状态机、幂等、8583、设备 SDK 隔离）
