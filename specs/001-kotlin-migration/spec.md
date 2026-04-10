# 功能规格：Java 转 Kotlin 迁移

**特性分支**: `001-kotlin-migration`  
**创建日期**: 2025-03-16  
**最近修改**: 2026-03-25  
**状态**: 进行中  
**输入**：功能一：将 Java 转为 Kotlin：先用 Idea 自动转换，再对转换后的代码进行审查，解决编译问题和不恰当的代码。需注意编译问题，代码格式化问题，前后功能逻辑问题，已经需要考虑会不会影响到其他应用的调用。在转换的过程中，需要时刻注意与部门的 SonarQube 对齐。

## 澄清说明

### 会话 2025-03-16

- Q: 本次 Kotlin 迁移的代码审查应覆盖哪些文件？ → A: 全部转换后的 Kotlin 源文件
- Q: 不恰当代码的审查重点是否包含生命周期与支付安全？ → A: 是，包含生命周期与支付安全（参考 android-code-review SKILL）
- Q: 如何验证其他应用对本模块的调用不受影响？ → A: 本迁移不涉及对外调用，可跳过
- Q: Java→Kotlin 转换是一次性还是分批进行？ → A: 一次性转换全部 Java 文件
- Q: 是否允许构建/Lint 存在已批准的豁免？ → A: 构建零错误，Lint 警告可暂时豁免
- Q: SonarQube 未通过项是否允许豁免？ → A: 不允许豁免，必须全部通过质量门禁
- Q: SonarQube 检查时机为何？ → A: 本地 + CI 双重检查（本地开发时扫，合并前 CI 再扫）
- Q: 部门 SonarQube 是否有现成配置可引用？ → A: 有，部门已有质量门禁/规则集，可直接引用
- Q: 转换前 Java 代码已有 SonarQube 问题时，转换后如何处理？ → A: 仅要求不新增问题；历史遗留可后续迭代修复

### 会话 2025-03-25（Kotlin 惯用法与测试门禁）

- Q: `!!` 与 NPE 导致页面无法启动如何处理？ → A: **禁止**在 `app/.../demo/` 生产 Kotlin 源码中使用 `!!`；一律改为 `?.`/`?:`/early return/`checkNotNull`（调试断言）等；**零例外**为默认，若与第三方 SDK 互操作极少数不可避免须在 PR 中单点说明并评审。
- Q: 状态暴露是否继续用 LiveData？ → A: **不新增**业务层 `LiveData`；迁移中凡改动的 ViewModel/状态暴露优先改为 **`Flow` / `StateFlow` / `SharedFlow`**，UI 侧用 `repeatOnLifecycle` 等收集；与 **002-compose-migration** 的纯 Compose 界面以该 spec 为准。
- Q: 判空是否沿用 `TextUtils` 等 Java 写法？ → A: 优先 **Kotlin 标准库**（如 `isNullOrEmpty()`、`isNullOrBlank()`、`orEmpty()`）；避免仅为习惯而保留 `TextUtils`；Android 专用 API 若更清晰可保留但须在审查中注明。
- Q: 构造函数「很奇怪」如何收敛？ → A: 优先 **主构造函数 + 明确可见性**；避免无意义的多层次构造；需要工厂场景用 `companion object`/`factory` 并写清契约；详见 [modification-patterns.md](modification-patterns.md) 增补节（与 spec 同步）。
- Q: `length == 0` 等判断？ → A: 字符串/集合优先 **`isEmpty()` / `isBlank()`**（或 `none()` 等）替代手写 `length == 0`。
- Q: 如何落实 TDD？ → A: 与项目宪章 **§XVIII** 对齐：**每批行为相关改动**先补或调整**失败用例**再改实现；关键工具类、校验、ViewModel 状态逻辑须有单测；合并前 `./gradlew :app:testDebugUnitTest`（或 CI 等价任务）通过。

### 会话 2026-03-25（构建脚本、Version Catalog、Lint 语义）

- Q: 依赖版本与 Gradle 脚本用何种形态管理？ → A: 使用 **Gradle Version Catalog**（仓库约定路径 **`gradle/libs.versions.toml`**）集中声明插件与依赖的版本及坐标别名；将 **`settings.gradle`、根目录 `build.gradle`、`app/build.gradle`** 迁移为 **Kotlin DSL**（对应 **`*.gradle.kts`**）。验收见 **FR-009**、**SC-008**；执行任务见 **tasks.md T027**。
- Q: Kotlin 中 Java 风格 `getX()` / `isY()` 与属性语法？ → A: 与宪章 **§VII** 一致：在互操作允许处优先写作 **`x` / `y`**，不在 Kotlin 侧无谓保留 JavaBeans 式调用；第三方 Java SDK 以其实际 API 为准，须在审查中区分「可属性化」与「须保留方法调用」。详见 **FR-005** 子项。
- Q: Lint「警告可豁免」与「无高优先级问题」如何并存？ → A: **Error 级** Lint 不得无故遗留；已采用 **`lint-baseline.xml`** 时，baseline **仅覆盖存量**，**合并默认不得新增 baseline 条目**（须修代码或经评审后有意批量 `updateLintBaseline`）。**Warning** 可按 **FR-002** 分批消化；**SC-004** 所指「高优先级」优先对齐 **error / fatal** 及团队对 warning 严重度的约定。

## 用户场景与测试 *（必填）*

### 用户故事 1 - Idea 自动转换与基础审查（优先级：P1）

使用 IntelliJ IDEA 的「Convert Java File to Kotlin File」完成 Java 到 Kotlin 的自动转换，并解决转换后产生的编译错误，使项目能够成功构建。

**优先级理由**：编译通过是后续审查和优化的前提，无法构建则无法验证功能。

**独立测试**：执行构建命令，项目编译成功、无编译错误。应用可正常启动，核心 Entry 流程可走通；**冷启动与核心路径不因空安全（如误用 `!!`）崩溃**。

**验收场景**：

1. **假如** 转换完成，**当** 执行构建，**那么** 无编译错误、构建成功
2. **假如** 应用已构建，**当** 启动应用并执行 Entry 流程，**那么** 路由与页面加载正常、**无未捕获 NPE 导致的中断**
3. **假如** 存在 Idea 无法自动转换的 Java 文件，**当** 人工介入，**那么** 有明确处理策略（保留或手工转换）

---

### 用户故事 2 - 代码质量与格式审查（优先级：P2）

对转换后的 Kotlin 代码进行格式化和质量审查，修正不恰当的写法，包括：过度使用 `!!`、冗余的 `?.let`、不当的 `when` 分支、命名风格、空安全处理、生命周期、支付安全等（参考 android-code-review SKILL）。

**优先级理由**：Idea 自动转换常产生可读性差或不符合 Kotlin 惯用法的代码，需在功能验证前完成基础质量修正。

**独立测试**：代码符合项目 Kotlin 风格规范；**`app/.../demo/` 下生产源码不含 `!!`**（见 FR-005）；**业务状态暴露不新增 `LiveData`**，以 **Flow** 族为主（见 FR-005）；判空与集合判断以 Kotlin 标准库惯用法为主；审查含生命周期、支付安全；与部门 SonarQube 规则对齐。**单元测试**：对本轮修改模块执行 TDD/红绿重构，**`./gradlew :app:testDebugUnitTest` 通过**，且关键逻辑含至少一条失败路径用例（对齐宪章 XVIII）。

**验收场景**：

1. **假如** 转换后的 Kotlin 代码，**当** 执行格式化、Lint 与 SonarQube 检查，**那么** 符合项目规范与部门 SonarQube 规则、无高优先级告警
2. **假如** 存在 Idea 转换产生的不恰当代码，**当** 审查并修正，**那么** 空安全、命名、控制流、`Flow`/判空/构造函数形状等符合本 spec 与 [modification-patterns.md](modification-patterns.md)
3. **假如** 一次性转换完成，**当** 审查全部 Kotlin 文件，**那么** 无循环依赖或编译错误

---

### 用户故事 3 - 功能逻辑验证（优先级：P3）

确保转换前后功能逻辑一致，无行为回归。

**优先级理由**：支付类应用对逻辑正确性要求高，必须在交付前验证。

**独立测试**：所有 Entry 流程可独立验证且行为与转换前一致。本迁移不涉及对外调用验证。

**验收场景**：

1. **假如** 转换完成，**当** 执行完整 Entry 流程（金额输入、确认、状态等），**那么** 行为与 Java 版本一致
2. **假如** 存在超时、取消、失败等边界流程，**当** 执行，**那么** 状态迁移与回首页行为与转换前一致

---

### 边界情况

- **单文件转换失败**：当 Idea 无法转换某 Java 文件时，采取「保留 Java 或手工转换」；决策标准：若该文件为入口或高频调用，优先手工转换；否则可暂时保留 Java 并标记为后续处理
- **循环依赖**：识别方式为构建报错（如 A 依赖 B、B 依赖 A）；处理顺序：先转换无依赖或依赖少的文件，再转换依赖方；若无法消除，需人工拆分或引入接口隔离
- **Neptune/POSLink SDK 互操作**：SDK 为 Java 时，Kotlin 调用方需正确处理 `@Nullable`/`@NonNull`；对可能为 null 的返回值使用 `?.` 或显式判空；**禁止 `!!` 强解**可能为 null 的 SDK 返回值（与本特性「零 `!!`」一致；极少数边界须在 PR 中单点论证）
- **支付敏感数据**：转换过程中不得引入日志打印 PAN、PIN block 等敏感明文（符合 constitution 原则）
- **SonarQube 对齐**：转换与审查过程中须持续关注 SonarQube 报告；仅要求不新增问题；历史遗留可后续迭代修复

## 需求 *（必填）*

### 功能需求

- **FR-001**：转换后项目必须能成功编译，无编译错误。
- **FR-002**：代码格式符合项目 Kotlin 风格规范。**Android Lint**：**error 级问题不得无故遗留**；已使用 **`lint-baseline.xml`** 时，baseline 仅固化**存量**问题，**默认不得在 MR 中新增 baseline 记录**（须修代码或经评审后批量更新 baseline）。**warning** 可暂时欠账并逐步清理，**高优先级 warning** 须优先修复。与 **SC-001**、**SC-004** 一致。
- **FR-003**：转换前后功能逻辑一致，无行为回归。
- **FR-004**：本迁移不涉及对外宿主/模块调用验证，相关项跳过（成功标准见 SC-003）。
- **FR-005**：修正 Idea 转换产生的不恰当代码，**去 Java 化**并符合 Kotlin 惯用法。审查重点具体化：
  - **空安全（`!!`）**：**默认禁止**在生产 Kotlin 源码中使用 `!!`**；**消除因 `!!` 导致的运行时 NPE**；优先 `?.`、`?:`、`let`、`also`、early return；与 SDK 互操作同样禁止强解可空值
  - **异步状态与 UI**：**不新增 `LiveData` 承载业务状态**；本次迁移中凡改动的状态暴露改为 **`StateFlow`/`SharedFlow`/`Flow`**，收集侧遵循生命周期（如 `repeatOnLifecycle`）；纯 Compose 屏幕与 **002-compose-migration** 对齐
  - **判空与字符串/集合**：优先 Kotlin 标准库（`isNullOrEmpty()`、`isNullOrBlank()`、`orEmpty()` 等）；**避免**仅为习惯保留 `TextUtils`/`String.valueOf` 等 Java 式堆砌；**字符串/集合长度判断**优先 `isEmpty()`/`isBlank()`/`none()` 等，替代 `length == 0` 等写法
  - **Java 访问器 → Kotlin 属性**（对齐 `.specify/memory/constitution.md` **§VII**）：在 Kotlin 调用侧，对可由 Kotlin 互操作映射的 JavaBean 风格访问器，优先使用属性语法（如 `getFoo()`/`isBar()` → **`foo`** / **`isBar`**），避免无谓的 `getX()` 链；**Neptune/Java SDK** 若仅有方法形态或语义非属性，则保留原方法调用并在审查中注明
  - **构造函数与 API 形状**：主构造优先、可见性明确；避免费解的构造重载链；工厂逻辑集中、可测；详见 [modification-patterns.md](modification-patterns.md)
  - **精简代码**：在**不改变可观测行为**前提下消除重复与样板（提取小函数/扩展函数等），并与 Sonar 重复度规则一致
  - **`?.let` 等**：审查冗余 `?.let`、`lateinit` 滥用
  - **生命周期**：Fragment 回调中更新 UI 前须检查 `isAdded`；避免在 `onDestroyView` 后访问 View
  - **支付安全**：禁止日志打印 PAN、PIN block、CVV 等敏感明文；审查时 grep 关键词：`Log.*pan`、`Log.*pin`、`Log.*cvv`、`println.*card` 等
  - **SonarQube**：引用部门规则集/质量门禁；验证方式为本地扫描 + CI 报告；仅要求不新增问题
  - 参考 android-code-review SKILL、[payment-project-hard-problems](../../.cursor/rules/payment-project-hard-problems.mdc)、[checklists/high-risk-classes.md](checklists/high-risk-classes.md)
- **FR-006**：审查范围覆盖全部转换后的 Kotlin 源文件。
- **FR-007**：转换与审查过程中须与部门 SonarQube 规则对齐；代码提交前必须全部通过 SonarQube 质量门禁，不允许豁免。检查时机：本地开发时执行 SonarQube 扫描，MR/PR 合并前由 CI 再次执行检查。基线策略：仅要求不新增问题；转换前 Java 代码的历史遗留问题可后续迭代修复。
- **FR-008**：**测试与 TDD**：与 `.specify/memory/constitution.md` **§XVIII** 对齐。对本轮**行为相关**改动采用红绿重构：**先**补充或调整**失败用例**（至少一条失败路径），**再**实现；关键工具、校验、ViewModel 状态逻辑须具备单元测试；合并前 **`./gradlew :app:testDebugUnitTest`（或 CI 等价）通过**。
- **FR-009**：**构建脚本与依赖版本管理**：使用 **Gradle Version Catalog**（`gradle/libs.versions.toml`）集中管理依赖版本及库别名（**`app` 模块 `dependencies` 一律 `libs.*`**）；将 **`settings.gradle`、项目根 `build.gradle`、`app/build.gradle`** 迁移为 **`settings.gradle.kts`、根 `build.gradle.kts`、`app/build.gradle.kts`**。根项目可在 **`buildscript` classpath** 中解析 Android/Kotlin/Hilt 插件（版本与 catalog **[versions]** 保持同步注释），以兼容部分 Maven 镜像无法解析 **Gradle 插件 marker** 的情况；**不**与 `buildSrc` 重复维护同一坐标（本仓库以 catalog 为库版本单一事实来源，除非团队书面例外）。迁移完成后 **`./gradlew :app:assembleDebug`**、**`./gradlew :app:testDebugUnitTest`** 须通过；若工程已启用 Lint 门禁，**`./gradlew :app:lintDebug`** 在 **JDK 17+** 下须通过（与既有 Lint/Material3 探测器要求一致）。CI、文档、脚本中硬编码旧 `.gradle` 路径的须同步更新。

### 关键实体

- **Entry 相关类**：BaseEntryFragment、UIFragmentHelper、EntryActivity、MainActivity 等；转换后保持原有职责与调用关系。**完整类清单**见 [class-inventory.md](class-inventory.md)。
- **Neptune/POSLink 接口**：EntryRequest、EntryResponse、EntryExtraData 等；契约不变，仅实现语言从 Java 变为 Kotlin。
- **对外暴露的 API**：见 **FR-004**（本迁移不验证宿主调用；Intent/广播契约不因语言改写而改变行为）。

### Spec 细化说明（类级可执行）

本 spec 已按 [doc/SPEC-REFINEMENT-STEPS.md](../../doc/SPEC-REFINEMENT-STEPS.md) 细化到类级，产出如下：


| 产出     | 路径                                                                           | 用途                                               |
| ------ | ---------------------------------------------------------------------------- | ------------------------------------------------ |
| 类清单    | [class-inventory.md](class-inventory.md)                                     | 完整类列表（数量以该文件统计为准，约 177），按模块与继承关系分组                         |
| 基类修改约束 | [contracts/base-class-modification.md](contracts/base-class-modification.md) | 8 个基类的 Kotlin 转换契约与注意点                           |
| 修改模式   | [modification-patterns.md](modification-patterns.md)                         | 按类类型（抽象基类、具象 Fragment、工具类等）定义修改方式                |
| 高风险类审查 | [checklists/high-risk-classes.md](checklists/high-risk-classes.md)           | BaseEntryFragment、PINFragment、CurrencyUtils 等必查项 |
| 类级验收   | [acceptance-per-class.md](acceptance-per-class.md)                           | 逐类审查完成勾选清单                                       |


**编译错误速查**：[kotlin-migration-compilation SKILL](../../.cursor/skills/kotlin-migration-compilation/SKILL.md)（修改模式、错误位置、执行顺序）

**自检**：见 [doc/SPEC-REFINEMENT-CHECKLIST.md](../../doc/SPEC-REFINEMENT-CHECKLIST.md)

## 范围外

- **对外调用验证**：本迁移不涉及宿主应用或其他模块对本应用的调用。
- **UI→Compose 迁移**：由独立 spec（002-compose-migration）负责。

## 假设前提

- IntelliJ IDEA 的 Java to Kotlin 转换功能可用；转换范围覆盖项目内 Java 源文件。
- Neptune/POSLink SDK（com.pax.us.pay.ui.constant）为 Java，迁移不修改 SDK 接口或契约。
- 项目已有或可配置 Kotlin 风格与 Lint 规则。
- 转换策略：一次性转换全部 Java 文件；审查可分批进行。
- 本迁移不涉及宿主应用或其他模块对本应用的调用，故对外调用验证（FR-004、SC-003）可跳过。
- 部门 SonarQube 规则为质量门禁依据；部门已有质量门禁/规则集，可直接引用。转换与审查过程中须持续对齐，避免引入新的质量问题。采用本地 + CI 双重检查：本地开发时扫描，MR/PR 合并前 CI 再扫。

## 成功标准 *（必填）*

### 可衡量成果

- **SC-001**：构建零错误。**Lint**：**error** 不得无故遗留；存量 **error** 仅允许已被 **`lint-baseline.xml`** 覆盖且 **MR 不新增** baseline 条目；**warning** 可暂时豁免并逐步清理（与 **FR-002** 一致）。
- **SC-002**：所有 Entry 流程可独立验证且无功能回归。
- **SC-003**：本迁移不涉及对外调用，SC-003 不适用。
- **SC-004**：转换后的 Kotlin 与资源通过项目 Lint 与格式检查：**无未处理的 Lint error**（含 baseline 策略：**New Code 不新增 baseline 记录**）；**高优先级 warning** 按团队约定清零或排期（与 **FR-002** 一致）。
- **SC-005**：转换后的代码不新增 SonarQube 问题；历史遗留可后续迭代修复。新引入的代码须通过部门 SonarQube 质量门禁，不允许豁免。
- **SC-006**：`app/src/main/java/com/paxus/pay/poslinkui/demo/` 下 `.kt` **生产源码中无 `!!`**（全仓库检索或 detekt/静态规则）；若有经评审的极少数例外须在 PR 中清单化并附原因。
- **SC-007**：**单元测试门禁**：`./gradlew :app:testDebugUnitTest`（或 CI 等价）通过；且本轮合并涉及的行为逻辑包含 **至少一条失败路径** 用例（见 FR-008）。
- **SC-008**：**Version Catalog + Kotlin DSL**：存在有效的 **`gradle/libs.versions.toml`**；**`settings.gradle.kts`、根 `build.gradle.kts`、`app/build.gradle.kts`** 为构建入口（无未迁移且仍承担主构建职责的 Groovy 脚本，除非团队书面豁免）；**FR-009** 所列 Gradle 命令通过。
