# 实施计划： Java 转 Kotlin 迁移

**分支**: `001-kotlin-migration` | **日期**: 2025-03-16（宪章 / 测试 / Lint / 构建脚本 2025-03-25–2026-03-25 修订） | **规格**: [spec.md](spec.md)
**输入**：功能规格说明，来源 `specs/001-kotlin-migration/spec.md`

## 摘要

将 POSLinkUI-Demo 应用内全部 Java 源文件一次性转换为 Kotlin（**类/文件数以 [class-inventory.md](class-inventory.md) 为准**），使用 IntelliJ IDEA 自动转换，再对转换后的代码进行审查，解决编译问题与不恰当写法（**零 `!!`、Flow 化状态、Kotlin 判空惯用法、Java 访问器→属性、构造函数收敛**等见 [spec.md](spec.md) FR-005/FR-008）。**构建侧**：以 **`gradle/libs.versions.toml`** 与 **Kotlin DSL 构建脚本**（`*.gradle.kts`）统一依赖版本与插件声明（**FR-009**、**SC-008**、**tasks.md T027**）。转换与审查须与部门 SonarQube 对齐：本地扫描 + MR/PR 前 CI；仅要求不新增问题，历史遗留可后续迭代修复。

## 技术上下文

**语言/版本**: Kotlin 1.9.22 / 2.1.0（与模块构建脚本一致）；**JVM 字节码目标 11**（`compileOptions` / `kotlinOptions` / `jvmToolchain(11)`，与仓库一致），**非**「源码级 Java 8」表述
**构建**: Gradle 8.x、**Android Gradle Plugin 7.4.x**；依赖与插件版本以 **`gradle/libs.versions.toml`**（Version Catalog）为单一事实来源；脚本形态目标为 **`*.gradle.kts`**（见 **FR-009**）
**主要依赖**: com.paxus.ui:constant 1.03.00T、androidx 系列（具体版本在 catalog 中维护）
**存储**: N/A（本迁移不涉及持久化变更）
**测试**: JUnit 4、AndroidX Test、**`./gradlew :app:testDebugUnitTest` 单元测试门禁**（TDD/红绿，见 spec FR-008）、手动 Entry 流程验证、SonarQube 扫描
**目标平台**: Android minSdk 22, targetSdk 31
**项目类型**: mobile-app（Android 支付 POS 应用）
**性能目标**: 主观上无感知退化；**冷启动与核心 Entry 路径稳定**（不因空安全错误崩溃）；若需量化可在后续迭代加启动/首屏采样阈值
**约束**: Neptune/POSLink 接口契约不变；不新增 SonarQube 问题；支付敏感数据禁止明文日志；**业务状态不新增 LiveData，迁移触及处优先 Flow 族**（spec FR-005）；**生产源码零 `!!`**（SC-006）
**规模/范围**: 以 [class-inventory.md](class-inventory.md) 为准，一次性转换；审查覆盖全部 Kotlin 源文件

## 宪章检查

*门禁：必须在 Phase 0 研究前通过，Phase 1 设计后再次检查。*

对照 **`.specify/memory/constitution.md`（当前版本，如 v3.0.0）** 与本特性 spec 的合规性（摘要对照，全文以宪章为准）：

| 宪章章节 | 本特性如何满足 |
|----------|----------------|
| **§I–III Entry Action / Fragment 模式** | 迁移**不改变** action→Fragment 映射与对外契约；仅语言与实现质量变化 |
| **§IV–VI Kotlin 任务、线程与反馈** | 转换后须保留/补强生命周期内取消与主线程回调约束；不弱化既有超时策略 |
| **§VII Kotlin 惯用法** | spec **FR-005** 明确零 `!!`、**Java 访问器→Kotlin 属性**、判空与 Flow；与宪章一致 |
| **§VIII SonarQube** | **FR-007** / **SC-005**；本地 + CI |
| **§IX–XI 架构与状态** | 状态暴露向 **Flow** 族收敛，与「显式状态模型」一致 |
| **§XII–XIII 输入与线程** | FR-005 生命周期与 fail-fast；不新增主线程阻塞 |
| **§XIV–XVII 支付与设备** | FR-005 支付安全 grep；不直接改设备 SDK |
| **§XVIII–XIX 测试** | **FR-008**、**SC-007**：单元测试 + 失败路径 + `testDebugUnitTest` |

**结论**：已对齐现行宪章；spec 中 **Flow/`!!`/TDD** 等为**本特性在宪章下的具体化**，不弱化 VII、XVIII。

**说明**：历史上若存在「需求 I–V」模板表述，**作废**；一律以仓库内最新 `constitution.md` 为准。

## 项目结构

### 文档（本特性）

```text
specs/001-kotlin-migration/
├── plan.md              # 本文件
├── research.md          # Phase 0 研究结论
├── data-model.md        # Phase 1 数据模型（实体与关系）
├── quickstart.md        # Phase 1 快速开始
├── class-inventory.md   # 类清单（细化产出）
├── modification-patterns.md  # 类类型与修改模式
├── acceptance-per-class.md   # 类级审查完成清单
├── contracts/           # Phase 1 接口契约
│   ├── neptune-poslink-compatibility.md
│   └── base-class-modification.md  # 基类修改约束
├── checklists/
│   ├── requirements.md
│   └── high-risk-classes.md  # 高风险类审查要点
└── tasks.md             # Phase 2 由 /speckit.tasks 生成
```

### 源码（仓库根目录）

```text
gradle/
└── libs.versions.toml    # Version Catalog（FR-009 / SC-008）；迁移完成后为权威版本源
settings.gradle.kts       # 目标形态（迁移前可能仍为 settings.gradle）
build.gradle.kts          # 根构建脚本（迁移前可能仍为 build.gradle）
app/
├── build.gradle.kts      # app 模块（迁移前可能仍为 build.gradle）
├── lint-baseline.xml     # Lint 存量基线（若已启用）
└── src/main/java/com/paxus/pay/poslinkui/demo/
    ├── MainActivity.kt
    ├── entry/
    │   ├── EntryActivity.kt
    │   ├── confirmation/   # 各类 Confirm*Fragment
    │   ├── option/        # 各类 Select*Fragment
    │   ├── poslink/       # ShowItemFragment, ShowTextBoxFragment 等
    │   ├── security/     # ASecurityFragment 等
    │   ├── signature/     # SignatureFragment
    │   ├── text/          # 各类输入 Fragment
    │   └── task/         # ScheduledTask
    ├── status/           # TransCompletedStatusFragment, ToastFragment 等
    └── utils/            # StringUtils, CurrencyUtils, DateUtils 等
```

**结构决策**: 单模块 Android 应用；转换后 Kotlin 源文件仍位于 `app/src/main/java/`，保持相同包结构，仅扩展名变为 `.kt`。**构建脚本**逐步收敛为 **Kotlin DSL + catalog**（与 **FR-009** 一致；未完成迁移时以仓库实际文件名为准）。

**编译错误速查**：[kotlin-migration-compilation SKILL](../../.cursor/skills/kotlin-migration-compilation/SKILL.md)

## Phase 1 产出

- [data-model.md](data-model.md) - 实体与关系（无 schema 变更）
- [contracts/neptune-poslink-compatibility.md](contracts/neptune-poslink-compatibility.md) - Neptune/POSLink 兼容性契约
- [quickstart.md](quickstart.md) - 快速开始步骤

**Agent Context 更新**：执行 `.specify/scripts/bash/update-agent-context.sh cursor-agent`（Windows 下可用 Git Bash 或 WSL）。若脚本不可用，可手动将本 feature 的技术栈（Kotlin、SonarQube、Idea 转换）补充到 agent 上下文中。

## Phase 2 规划

本 plan 止于 Phase 2 规划。下一步由 `/speckit.tasks` 将 plan 拆解为可执行任务（tasks.md）。

## 依赖与执行顺序（类级）

**转换顺序**（避免先改子类再改基类导致返工）：

1. **基类**：BaseEntryFragment、UIFragmentHelper、ATextFragment、AConfirmationFragment、ASecurityFragment、AOptionEntryFragment、AAmountFragment、ANumFragment、ANumTextFragment
2. **工具类**：CurrencyUtils、EntryRequestUtils、BundleMaker、ValuePatternUtils 等（被 Fragment 调用）
3. **EntryActivity、MainActivity**：与 UIFragmentHelper 配合
4. **具象 Fragment**：按 entry/confirmation/、entry/text/、entry/security/、entry/option/、entry/poslink/、entry/information/ 等顺序

**参考**：[class-inventory.md](class-inventory.md) 第 10 节、[tasks.md](tasks.md) Dependencies & Execution Order

## 复杂度跟踪

无 Constitution 违规需说明。

## Risk / Edge Case 处理（引用 spec 细化内容）

- **单文件转换失败**：spec 已定义决策标准（入口/高频调用优先手工转换，否则保留 Java 并标记）
- **循环依赖**：spec 已定义识别方式与处理顺序
- **Neptune/POSLink SDK 互操作**：spec 已定义 @Nullable/@NonNull 处理要求
- **审查标准**：spec **FR-005**、**FR-008**、**SC-006**、**SC-007** 已细化空安全（含零 `!!`）、Flow、TDD/单测、生命周期、支付安全、SonarQube
