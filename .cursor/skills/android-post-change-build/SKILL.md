---
name: android-post-change-build
description: >-
  POSLinkUI-Demo 在会话内**全部任务完成后**执行一次 Android 构建门禁（:app:assembleDebug 与 :app:testDebugUnitTest，必要时 lintDebug）。
  在有多步实现、Todo/Spec 任务批次、或交付前最终验证时使用；**不在**每一次单文件修改或中间步骤后强制跑 Gradle。
---

# Android 变更后构建门禁（批次完成时执行）

## 何时执行（重要）

**不要**在每次编辑、每个文件保存或每个子步骤后都跑构建。

在以下时机 **执行一次** 完整门禁，**直至成功**（不得在未跑通的情况下结束交付、勾选全部任务或声称「本批次已完成」）：

- 当前会话中的 **Todo / 任务清单已全部完成**，或用户给出的 **实现清单已全部做完**
- 用户明确要求「全部做完后编译验证」「最终跑一下 Gradle」「交付前检查」
- 单任务会话且该任务已收尾、准备交付时（等价于「全部任务完成」）

若用户明确说「中间不要跑构建，最后再跑」，仅在该批次末尾执行。

## 仍适用本门禁的变更类型（在批次末一次性验证）

批次内若包含以下任意一类改动，**在全部任务完成后**跑通门禁即可：

- 新增或修改 `app/src/**` 下的 Kotlin / Java / XML / 资源
- 修改 `app/build.gradle.kts`、`build.gradle.kts`、`settings.gradle.kts`、`gradle/libs.versions.toml`、Gradle 插件或依赖版本
- 「生成代码」「实现功能」「修复编译」等产出可运行产物的需求（以批次结束为验证点）

## 命令（项目约定）

在仓库根目录执行（Windows 可用 `gradlew.bat` 等价替换）：

```bash
./gradlew :app:assembleDebug :app:testDebugUnitTest
```

含 **Lint 配置 / `lint-baseline.xml` / 大量资源与 Manifest 变更** 时，另在 **`JAVA_HOME`=JDK 17+** 下执行 `./gradlew :app:lintDebug`（Material3 Lint 探测器需 JDK 17；见根目录 `gradle.properties` 注释）。

## 失败时

1. 记录**首个失败任务**与**根因**（编译错误、测试失败、JDK/AGP 不匹配等）。
2. **修改代码或配置**后**重新执行同一命令**，循环直至成功。
3. 若环境导致无法执行（例如仅有 JDK 8 而 AGP 需 JDK 11+），须在回复中**明确写出阻塞原因**与**本机应使用的 JDK 版本**，并仍尝试在可用环境下跑通；不得静默跳过。

## 快速排查（本项目）

1. **dex 阶段出现 `com.android.tools.r8.kotlin.H`**
   - 怀疑 Kotlin / AGP / Compose 版本不匹配；将 Kotlin、Compose 编译器与 Compose BOM 对齐到已知兼容组合。
   - 若传递依赖导致 Kotlin stdlib 漂移，在根目录 `build.gradle` 的 `resolutionStrategy` 中强制统一 stdlib 版本。
2. **当前编译器不支持的 Kotlin 语法**
   - 对较低 Kotlin 版本构建，替换不兼容写法（例如：`..<` 改为 `until`）。
3. **Kapt 与 JVM 目标不一致**
   - 保持 `compileOptions` 与 `kotlinOptions.jvmTarget` 一致。

## 输出模板（批次验证后）

- 已执行命令：`./gradlew :app:assembleDebug :app:testDebugUnitTest`（及 `lintDebug` 若适用）
- 结果：`成功` / `失败`
- 若已修复：根因、修改的文件、最终验证结果
- 残留风险（若有）

## 与 `android-build-gate` 的关系

- 历史技能 `android-build-gate` 已合并指向本技能；以本文件为准。

## 反例（禁止）

- 在中间步骤每改一个文件就跑完整 Gradle（除非用户要求即时验证）。
- 「代码已写好，请你本地编译」——在**批次收尾**时由代理自行执行并修到通过。
- 仅运行 `assembleDebug` 而**不跑** `testDebugUnitTest` 即视为门禁未完成（除非用户明确临时豁免并在回复中说明）。

## 临时豁免

若用户明确说某次可跳过测试或仅编 debug，须在回复中写明豁免范围与原因。
