# 基类修改约束：Java 转 Kotlin 迁移

**特性**： 001-kotlin-migration  
**日期**: 2025-03-18  
**目的**: 明确基类在 Kotlin 转换过程中的修改约束，确保子类与 Neptune/POSLink 契约不受影响。

---

## 1. 契约原则

- **对外契约不变**：方法签名、回调接口、常量引用与 Java 版本一致
- **子类兼容**：子类 override 的方法签名保持兼容
- **Neptune/POSLink 互操作**：对 `@Nullable`/`@NonNull` 的 SDK 返回值正确处理

---

## 2. BaseEntryFragment

### 不可变契约

| 方法/成员 | 说明 |
|-----------|------|
| `getLayoutResourceId(): Int` | 抽象方法，子类必须实现 |
| `loadArgument(Bundle)` | 子类 override，签名不变 |
| `loadView(View)` | 子类 override，签名不变 |
| `focusableTextFields(): Array<TextField>?` | 子类 override，签名不变 |
| `onConfirmButtonClicked()` | 子类 override，签名不变 |
| `sendNext(Bundle)` | 与 Neptune 交互，调用方式不变 |
| `activate()` / `deactivate()` | 生命周期，行为不变 |

### Kotlin 转换注意

- `appContext = getActivity()` 在 `onCreateView` 时可能为 null → 使用 `requireContext()` 或 `activity?.applicationContext`
- `Handler(Looper.getMainLooper())` 回调中更新 UI 前检查 `isAdded`
- `@Nullable Bundle getArguments()` → 使用 `arguments?.let { }` 或 `requireArguments()`
- 避免在 `onDestroyView` 后访问 View

---

## 3. ATextFragment

### 不可变契约

| 方法/成员 | 说明 |
|-----------|------|
| `getLayoutResourceId(): Int` | 返回 `R.layout.fragment_base_text` |
| `getMaxLength(): Int` | 抽象方法 |
| `formatMessage(): String` | 抽象方法 |
| `getRequestedParamName(): String` | 抽象方法 |
| `loadView(View)` | 绑定 textField、confirmBtn |
| `onConfirmButtonClicked()` | 校验 valuePatten，调用 submit |

### Kotlin 转换注意

- `valuePatten` 可能为 null → 使用 `valuePatten?.let` 或 `valuePatten ?: ""`
- `textField.getText().toString()` → 注意 `textField` 在 `loadView` 中初始化，非 null
- `@CallSuper` → 转换为 KDoc `/** @suppress 子类必须调用 super */`

---

## 4. AConfirmationFragment

### 不可变契约

| 方法/成员 | 说明 |
|-----------|------|
| `getLayoutResourceId(): Int` | 返回 `R.layout.fragment_confirmation` |
| `loadArgument(Bundle)` | 读取 message、options |
| `loadView(View)` | 绑定 messageTv、positiveButton、negativeButton |
| `formatMessage(String): String` | 抽象方法 |
| `getPositiveText(): String` | 非 null 返回 |
| `getNegativeText(): String?` | 可 null |
| `submit(Boolean)` | 调用 sendNext |

### Kotlin 转换注意

- `options` 可能为 null → `options?.getOrNull(0)` 等
- `!TextUtils.isEmpty(negativeText)` → `negativeText?.isNotEmpty() == true`

---

## 5. ASecurityFragment

### 不可变契约

| 方法/成员 | 说明 |
|-----------|------|
| 继承 BaseEntryFragment 的抽象方法 | 签名不变 |
| 安全相关逻辑（掩码、输入限制） | 行为不变 |

### Kotlin 转换注意

- 禁止日志打印 PAN、PIN block、CVV 等敏感明文
- 审查 `Log.*`、`println` 中是否包含敏感关键词

---

## 6. AOptionEntryFragment

### 不可变契约

| 方法/成员 | 说明 |
|-----------|------|
| `getLayoutResourceId(): Int` | 子类实现 |
| 选项列表、选择回调 | 行为不变 |

### Kotlin 转换注意

- `RecyclerView.Adapter` 泛型、ViewHolder 模式保持
- 选项数据可能为 null 或空列表 → 使用 `?.` 或 `orEmpty()`

---

## 7. AAmountFragment、ANumFragment、ANumTextFragment

### 不可变契约

- 与 ATextFragment 类似，继承 BaseEntryFragment 的抽象方法签名不变
- 金额格式化、校验逻辑行为不变

### Kotlin 转换注意

- `CurrencyUtils` 等工具类调用保持
- 金额相关字段使用 `Long` 或 `BigDecimal`，避免精度丢失

---

## 8. UIFragmentHelper

### 不可变契约

| 方法 | 说明 |
|------|------|
| `getFragment(String action, Bundle bundle): Fragment?` | 路由入口，返回对应 Fragment |
| 与 EntryActivity 的配合 | 行为不变 |

### Kotlin 转换注意

- 大量 `when` 分支、Fragment 实例化 → 确保 `when` 分支完整、无 fall-through
- 返回 `Fragment?` 时调用方使用 `?.` 或 `requireNotNull`

---

## 9. 验证

- 编译通过即表示 Kotlin 与 Java SDK 互操作正确
- 运行时 Entry 流程可走通即表示回调与路由正常
- 参考 [neptune-poslink-compatibility.md](neptune-poslink-compatibility.md)
