# 高风险类审查要点

**特性**： 001-kotlin-migration  
**日期**: 2025-03-18（2025-03-25 增补：`!!`/Flow/判空横切项，对齐 [spec.md](../spec.md) FR-005、SC-006、SC-007）  
**目的**: 支付类应用高风险类必查项，审查时须逐项确认。

---

## 0. 横切：Kotlin 惯用法（全模块，优先于单类走查）

与 [modification-patterns.md](../modification-patterns.md) §0–§0.3、[spec.md](../spec.md) **FR-005**、**SC-006**、**SC-007** 一致。

### 必查项

- [ ] **零 `!!`**：`app/src/main/java/com/paxus/pay/poslinkui/demo/**/*.kt` 中无 `!!`（含基类/Fragment/ViewModel/utils）；例外须在 PR 中单点清单 + 评审
- [ ] **Flow 优先**：本次迁移**触及**的 ViewModel / 状态暴露已改为 `StateFlow` / `SharedFlow` / `Flow`，UI 侧 `repeatOnLifecycle`（或项目等价）收集；**不新增**业务层 `LiveData`
- [ ] **Kotlin 判空**：避免仅为习惯保留 `TextUtils.isEmpty` 等；优先 `isNullOrEmpty()`、`isNullOrBlank()`、`orEmpty()`
- [ ] **`length == 0` 等**：字符串/集合优先 `isEmpty()`、`isBlank()`、`none()` 等（按语义）
- [ ] **构造函数 / API 形状**：高风险路径（基类、`UIFragmentHelper`、工具单例）主构造清晰，无费解二级构造链；`getX()` → 属性访问在互操作允许范围内
- [ ] **TDD / 单测**：本轮改动配套单测，`./gradlew :app:testDebugUnitTest` 通过，且含至少一条**失败路径**用例（**FR-008**）

---

## 1. BaseEntryFragment

### 必查项

- [x] 回调中更新 UI 前检查 `isAdded`（如 Handler、异步回调、Neptune 回调）
- [ ] `onDestroyView` 后不访问 View；避免在 `view?.findViewById` 等已 detach 后调用
- [x] `getArguments()`、`requireActivity()` 等可能为 null 的调用使用 `?.` 或 `requireArguments()`/`requireActivity()`
- [x] `appContext = getActivity()` 在 `onCreateView` 时可能为 null → 已移除未使用的 appContext
- [ ] 无 `!!` 强解可能为 null 的 SDK 返回值（目标：**零 `!!`**，见 §0）

### 参考

- [contracts/base-class-modification.md](../contracts/base-class-modification.md)
- [payment-project-hard-problems](../../.cursor/rules/payment-project-hard-problems.mdc)

---

## 2. UIFragmentHelper

### 必查项

- [ ] `when` 分支完整，无遗漏 action 导致返回 null 或错误 Fragment
- [ ] 返回 `Fragment?` 时调用方（EntryActivity）正确使用 `?.` 或 `requireNotNull`
- [ ] 与 Neptune/POSLink 的 action 常量引用正确，无硬编码错误

### 参考

- [contracts/base-class-modification.md](../contracts/base-class-modification.md)

---

## 3. PINFragment

### 必查项

- [ ] 无 `Log.*pin`、`Log.*pan`、`Log.*cvv`、`println.*card` 等敏感明文日志
- [ ] PIN 输入使用掩码展示
- [ ] 无将 PIN block、PAN 等写入日志或调试输出

### 参考

- [payment-project-hard-problems](../../.cursor/rules/payment-project-hard-problems.mdc) 安全与合规节

---

## 4. InputAccountFragment

### 必查项

- [ ] 持卡人信息（卡号、姓名等）使用掩码
- [ ] 无敏感明文日志
- [ ] 与 Neptune 交互的 Bundle 键值正确

---

## 5. EnterCardAllDigitsFragment / EnterCardLast4DigitsFragment

### 必查项

- [ ] 卡号输入使用掩码
- [ ] 无敏感明文日志
- [ ] 与 ASecurityFragment 基类一致的安全处理

---

## 6. CurrencyUtils

### 必查项

- [ ] 金额计算无精度丢失（使用 Long 或 BigDecimal）
- [ ] 格式化输出与业务需求一致
- [ ] 静态方法转为 Kotlin `object` 或顶层函数时行为不变

---

## 7. 通用 grep 关键词

审查时执行以下检索（PowerShell / Git Bash / ripgrep 任选），排除**注释、字符串字面量、文档**误报后确认无违规：

```bash
# 敏感数据日志（禁止）
grep -r "Log.*pan\|Log.*pin\|Log.*cvv\|Log.*card\|println.*card" --include="*.kt" app/

# 非空断言（目标：demo 包内生产代码零命中；见 SC-006）
rg "\!\!" app/src/main/java/com/paxus/pay/poslinkui/demo --glob "*.kt"

# TextUtils 判空（应逐步收敛到 Kotlin 标准库；新改代码优先清零）
rg "TextUtils\.(isEmpty|isWhitespace)" app/src/main/java/com/paxus/pay/poslinkui/demo --glob "*.kt"

# 可读性：手写 length 判空（按需改为 isEmpty/isBlank）
rg "\.length\s*==\s*0|0\s*==\s*.+\.length" app/src/main/java/com/paxus/pay/poslinkui/demo --glob "*.kt"

# 业务 LiveData 增量（不新增；迁移触及处应改为 Flow 族）
rg "MutableLiveData|LiveData<" app/src/main/java/com/paxus/pay/poslinkui/demo/viewmodel --glob "*.kt"
```

**说明**：`LiveData` 行仅辅助审计「本次是否仍新增/应迁未迁」；历史遗留可与 **T022** 任务清单对照销项。

---

## 8. 审查完成标记

- [ ] **§0 横切项**（`!!` / Flow / 判空 / isEmpty / 构造 / 单测）已逐项确认
- [x] BaseEntryFragment 已审查
- [ ] UIFragmentHelper 已审查
- [ ] PINFragment、InputAccountFragment、EnterCardAllDigitsFragment、EnterCardLast4DigitsFragment 已审查
- [ ] CurrencyUtils 已审查
- [x] grep 敏感关键词无违规
- [ ] **§7 扩展 grep**（`!!`、TextUtils、length、viewmodel LiveData）已执行并处理误报
