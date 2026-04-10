# 类级修改映射

**来源**：SonarQube MCP 问题列表 + 006 tasks.md  
**用途**：将 T015–T022 细化为「按类」的修改任务，便于实现与验收  
**说明**：Sonar 报告引用 `.java` 路径，项目已迁移为 Kotlin，实际文件为 `.kt` 等价路径。

---

## 1. CRITICAL 问题（必须优先修复）

| 类名 | 实际路径 | 规则 | 行号 | 修改建议 |
|------|----------|------|------|----------|
| PrintDataConverter | `utils/format/PrintDataConverter.kt` | java:S3776 | 54 | 认知复杂度 27→20：拆分方法、提取子函数 |
| FormatTextWatcher | `view/FormatTextWatcher.kt` | java:S3776 | - | 降低认知复杂度：提取条件分支为独立方法 |
| ShowInputTextBoxFragment | `entry/poslink/ShowInputTextBoxFragment.kt` | java:S3776 | - | 同上 |
| InputAccountFragment | `entry/security/InputAccountFragment.kt` | java:S3776 | - | 同上；另 S103 行长 L175、L368 |

---

## 2. MAJOR 问题（按类）

| 类名 | 实际路径 | 规则 | 行号 | 修改建议 |
|------|----------|------|------|----------|
| EntryActionAndCategoryRepository | `utils/interfacefilter/EntryActionAndCategoryRepository.kt` | S103 | 131 | 拆分 175 字符行长 |
| ShowMessageFragment | `entry/poslink/ShowMessageFragment.kt` | S103 | 101, 132 | 拆分 185/195 字符行长 |
| ShowThankYouFragment | `entry/poslink/ShowThankYouFragment.kt` | S103 | 64–66 | 拆分 188/191 字符行长 |
| ASecurityFragment | `entry/security/ASecurityFragment.kt` | S103 | 159 | 拆分 166 字符行长 |
| InputAccountFragment | `entry/security/InputAccountFragment.kt` | S103 | 175, 368 | 拆分行长 |
| ManageInputAccountFragment | `entry/security/ManageInputAccountFragment.kt` | S103 | 165, 329 | 拆分行长 |
| InputTextFragment | `entry/poslink/InputTextFragment.kt` | S121 | - | 补全花括号 |
| CashbackFragment | `entry/text/amount/CashbackFragment.kt` | S103 等 | - | 拆分行长、格式 |
| TipFragment | `entry/text/amount/TipFragment.kt` | S103 | - | 拆分行长 |
| TextShowingUtils | `entry/poslink/TextShowingUtils.kt` | S103 等 | - | 拆分行长 |
| ItemListAdapter | `entry/poslink/ItemListAdapter.kt` | S103 等 | - | 拆分行长 |
| ViewUtils | `utils/ViewUtils.kt` | S103 | - | 拆分行长 |
| EntryActivity | `entry/EntryActivity.kt` | S103 | - | 拆分行长 |
| ReversePartialApprovalFragment | `entry/confirmation/ReversePartialApprovalFragment.kt` | S103 | - | 拆分行长 |
| SupplementPartialApprovalFragment | `entry/confirmation/SupplementPartialApprovalFragment.kt` | S103 | - | 拆分行长 |
| DisplayApproveMessageFragment | `entry/information/DisplayApproveMessageFragment.kt` | S103 | - | 拆分行长 |
| StatusFragment | `status/StatusFragment.kt` | S103 | - | 拆分行长 |
| SecondScreenInfoViewModel | `viewmodel/SecondScreenInfoViewModel.kt` | S103 等 | - | 拆分行长 |
| TransactionPresentation | `entry/TransactionPresentation.kt` | S103 等 | 多处 | 拆分行长 |
| DeviceUtils | `utils/DeviceUtils.kt` | S103 | - | 拆分行长 |
| AVSFragment | `entry/text/AVSFragment.kt` | S103 等 | - | 拆分行长 |
| NewEnterFleetDataFragment | `entry/text/fleet/NewEnterFleetDataFragment.kt` | S103 等 | - | 拆分行长 |

---

## 3. MINOR 问题（按类）

| 类名 | 实际路径 | 规则 | 修改建议 |
|------|----------|------|----------|
| UIFragmentHelper | `entry/UIFragmentHelper.kt` | S3599 | 换一种方式初始化实例（L279–305） |
| EntryActionAndCategoryRepository | `utils/interfacefilter/EntryActionAndCategoryRepository.kt` | S113, S3599 | 文件末尾换行；换一种方式初始化（L41–194） |
| DateUtils | `utils/DateUtils.kt` | S103 等 | 拆分行长 |
| BaseEntryFragment | `entry/BaseEntryFragment.kt` | S103 | 拆分行长 |
| ThreadPoolManager | `utils/ThreadPoolManager.kt` | S103 等 | 拆分行长 |

---

## 4. US5 Kotlin 惯用法（按类范围）

**目标**：getter→属性、下标访问、equals→==、null 惯用法

**收益**：缩减代码语句、提升可读性；链式与条件表达更简洁。

| 修改类型 | 示例 | 缩减效果 |
|----------|------|----------|
| getter→属性 | `obj.getProperty()` → `obj.property` | 少 6 字符，语句更短 |
| is-getter→属性 | `obj.isEnabled()` → `obj.isEnabled` | 少 2 字符 |
| get(index)→下标 | `list.get(1)` → `list[1]` | 少 4 字符 |
| set(index)→下标 | `list.set(0, x)` → `list[0] = x` | 更符合 Kotlin 惯用法 |
| equals→== | `a.equals(b)` → `a == b` | 更简洁 |
| Android API getter | `activity.getSupportFragmentManager()` → `activity.supportFragmentManager` | 链式调用更简洁 |
| if-else 链→when | `if (x == A) ... else if (x == B) ...` → `when (x) { A -> ... B -> ... else -> ... }` | 多分支更简洁、利于穷举 |

**典型缩减场景**：
- 链式：`a.getB().getC()` → `a.b.c`
- 条件：`if (obj.getStatus() == X)` → `if (obj.status == X)`
- 空安全：`obj?.getX()?.getY()` → `obj?.x?.y`
- **Android/Fragment**：`activity.getSupportFragmentManager().executePendingTransactions()` → `activity.supportFragmentManager.executePendingTransactions()`
- **if→when**：`if (status == A) ... else if (status == B) ...` → `when (status) { A -> ... B -> ... else -> ... }`；适用 StatusFragment、UIFragmentHelper、TransactionPresentation 等含状态/类型分支的类

| 修改类型 | 适用类范围 | 说明 |
|----------|------------|------|
| getter→属性 | 所有 Kotlin 类 | `obj.getX()` → `obj.x` |
| get/set→下标 | 含 List/Map 的类 | `list.get(1)` → `list[1]` |
| equals→== | 含 equals 调用的类 | `a.equals(b)` → `a == b` |
| if→when | 含多分支 if-else 的类 | `if (x == A) ... else if (x == B) ...` → `when (x) { A -> ... B -> ... else -> ... }` |
| null 惯用法 | 含 null 判断的类 | `?.`、`?:`、`let`、`also` |

**建议优先扫描的类**（业务核心、易出 null 问题）：
- `TransactionPresentation.kt`
- `UIFragmentHelper.kt`
- `BaseEntryFragment.kt`
- `EntryActivity.kt`
- `InputAccountFragment.kt`
- `SecondScreenInfoViewModel.kt`

---

## 5. 任务与类映射（供 tasks.md 引用）

| 任务 | 涉及类（示例） |
|------|----------------|
| T015 Blocker/Critical | PrintDataConverter, FormatTextWatcher, ShowInputTextBoxFragment, InputAccountFragment |
| T016 Major/Minor | 上表 2、3 中全部类 |
| T018 getter→属性 | 全 Kotlin 代码库；优先 TransactionPresentation, UIFragmentHelper, BaseEntryFragment |
| T019 get/set→下标 | 含 List/Map 的 Fragment、Adapter、ViewModel |
| T020 equals→== | 同上 |
| T020a if→when | StatusFragment、UIFragmentHelper、TransactionPresentation、EntryActivity 及含状态/类型分支的 Fragment |
| T021 null 惯用法 | TransactionPresentation, UIFragmentHelper, BaseEntryFragment, EntryActivity, InputAccountFragment, SecondScreenInfoViewModel |
