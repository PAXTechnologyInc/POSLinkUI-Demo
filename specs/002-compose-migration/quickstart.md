# 快速开始：UI 转 Compose 迁移

**特性**：002-compose-migration  
**日期**: 2025-03-16 | **修订**: 2026-03-28

## 前置条件

- **分支**：建议在 001-kotlin-migration 合并到 main 后，从 main 创建 002 分支；若并行开发，则从 `001-kotlin-migration` 分支创建 002。见 [doc/SPEC-KIT-使用步骤.md](../../doc/SPEC-KIT-使用步骤.md) 5.5 节
- Android Studio / IntelliJ IDEA
- 项目可正常构建（`./gradlew :app:assembleDebug :app:testDebugUnitTest`，仓库根目录）
- POSLinkUI-Design_V1.03.00.docx 可查阅；关键数值已手工提取
- 已阅读 [spec.md](spec.md) **FR-005、FR-016、FR-017、FR-019** 与 [base-class-compose-strategy.md](contracts/base-class-compose-strategy.md)

## 步骤 1：Compose 与 Navigation 依赖（与仓库一致）

本仓库使用 **Gradle Version Catalog**：[`gradle/libs.versions.toml`](../../gradle/libs.versions.toml)。**当前**（以文件为准）：

| 项 | 版本来源 |
|----|-----------|
| Kotlin | `[versions] kotlin`（如 `1.8.22`） |
| Compose BOM | `[versions] compose-bom`（如 `2023.06.01`） |
| Compose Compiler | `[versions] compose-compiler`（如 `1.4.8`），与 Kotlin 1.8.x 配套 |
| Navigation Compose | `[versions] navigation-compose`（如 `2.6.0`），**FR-019 / T035** |

**`app/build.gradle.kts`** 中与 Compose / Nav 相关的写法应与仓库保持一致，例如：

```kotlin
android {
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.navigation.compose)
    debugImplementation(libs.androidx.compose.ui.tooling)
}
```

**`gradle/libs.versions.toml`** 中需存在（名称以仓库为准）：

```toml
[versions]
compose-compiler = "…"   # 与 Kotlin 版本匹配
compose-bom = "…"
navigation-compose = "…"

[libraries]
androidx-compose-bom = { module = "androidx.compose:compose-bom", version.ref = "compose-bom" }
androidx-compose-ui = { group = "androidx.compose.ui", name = "ui" }
androidx-compose-ui-tooling-preview = { group = "androidx.compose.ui", name = "ui-tooling-preview" }
androidx-compose-material3 = { group = "androidx.compose.material3", name = "material3" }
androidx-compose-ui-tooling = { group = "androidx.compose.ui", name = "ui-tooling" }
androidx-navigation-compose = { module = "androidx.navigation:navigation-compose", version.ref = "navigation-compose" }
```

升级 Kotlin / Compose Compiler / BOM / `navigation-compose` 时，须同步改 **catalog** 并跑通 `./gradlew :app:assembleDebug :app:testDebugUnitTest`。

## 步骤 2：建立 design-tokens / Theme（T003–T004）

从设计文档手工提取布局、字体、颜色、间距，创建 `ui/theme/Theme.kt` 等；详见 [design-tokens.md](design-tokens.md)。

## 步骤 3：Compose 复用层与设备规格（T030–T031）

- **T030（已实现）**
  - 统一根：`ui/PosLinkScreenRoot.kt`（外层须已有 `PosLinkTheme`；应用 `LocalDeviceLayoutSpec` 边距 + `Surface`）。
  - Token：`ui/theme/DesignTokens.kt` 内 `PosLinkDesignTokens`。
  - 预制件：`ui/components/PosLinkTopBar.kt`、`PosLinkPrimaryButton.kt`、`PosLinkListItemRow.kt`。
- **T031（已实现）**
  - `ui/device/DeviceLayoutSpec.kt`、`DeviceProfileRegistry.kt`、`DeviceProfileId.kt`、`LocalDeviceLayoutSpec`。
  - P0 / 全表事实来源：[device-profiles.md](device-profiles.md)；副屏示例消费：`TransactionPresentation` 使用 `secondaryTitleSp` / `secondaryBodySp`。

## 步骤 4：单宿主 + `NavHost` 骨架（T035 / FR-019）

**本仓库落地（2026-03-30）**：`EntryActivity` 直接 `setContent` 承载 `EntryNavigationHost`，不再依赖 `activity_entry.xml` / `fragment_placeholder` 的 Entry 业务容器。

1. **路由与图**：`entry/navigation/EntryNavHost.kt`、`TransactionRoute`、`rememberNavController` + `NavHost`。
2. **业务路由**：`EntryScreenRouter` 以 `EntryViewModel.uiState` 的 `action/category/extras` 做单点分发；旧 `UIFragmentHelper` 映射已移除。
3. **纯 Compose 验证 destination**：`compose_demo`；Intent 增加 `EntryActivity.EXTRA_NAV_COMPOSE_DEMO = true` 即可启动（可无有效 Entry action，用于管道验证）。
4. **单点解析**：`entry/navigation/EntryRouteResolver.kt`；对照表 [contracts/entry-navigation-routes.md](contracts/entry-navigation-routes.md)。
5. **`onNewIntent`（SC-013）**：`EntryActivity.onNewIntent` → `loadEntry` → `EntryViewModel.consumeEntryIntent(intent)`，由 `uiState.revision` 驱动 Compose destination 刷新；后续若增加多条 Nav 回程线，在同一入口集中维护 `pop/navigate` 策略。

## 步骤 5：迁移单个 Entry（推荐路径）

1. 为 action 增加 **route** 与 **`composable { … }`**，Screen 内组合 **T030** 预制件。
2. 删除该路径已无引用的 **`res/layout`**。
3. 按设计规范还原；**dp** 经 **DeviceLayoutSpec / Token API**，勿在 Screen 内写死全局尺寸。

## 步骤 6：当前状态（已完成收敛）

- Entry 六类模块（Text / Security / Confirmation / POSLink / Option / Information）已收敛到 `EntryScreenRouter` + Compose screens。
- 旧 Entry Fragment 与对应 Entry layout XML 已从主路径移除；保留 XML 清单见 [layout-xml-retention.md](layout-xml-retention.md)。

## 步骤 7：验收

1. 人工对比设计稿与实现截图（基准机 + P0 见 [device-profiles.md](device-profiles.md)）
2. Entry 流程与迁移前行为一致；**Nav** 场景满足 **SC-013**
3. manifest 与 `EntryActivity` 调起正常

## 步骤 8：按模块推进

Text → Security → Confirmation → POSLink → Option → Information；每模块完成后独立验收。新增界面 **优先** 走 **步骤 5**，而非步骤 6。

## 常见问题与排查

| 现象 | 可能原因 | 处理建议 |
|------|----------|----------|
| Compose 内联错误 | Kotlin 与 Compose 编译器版本不匹配 | 对齐 `libs.versions.compose.compiler` 与 [官方对照表](https://developer.android.com/jetpack/androidx/releases/compose-kotlin)；本仓当前为 Kotlin **1.8.22** + compiler **1.4.8**（以 `libs.versions.toml` 为准） |
| `onNewIntent` 未切换界面 | 未调用 `navController` | 对照 **T035** 与 **SC-013** |
| 001 未合并即启动 002 | 分支依赖冲突 | 先合并 001 或从 `001-kotlin-migration` 建分支 |
| 设计数值缺失 | .docx 无法被 AI 解析 | 手工填入 [design-tokens.md](design-tokens.md) |
| 多机型布局溢出 | dp 写死 | 使用 **T031** `DeviceLayoutSpec`，见 spec **FR-017** |

## 附录：EntryScreenRouter 全模块（2026-03）

单 Activity Compose 路由入口：`entry/compose/EntryScreenRouter.kt`（由 `EntryNavigationHost` 承载）。

| 分类（category） | 实现要点 |
|------------------|----------|
| `SecurityEntry` | `SecuritySecureAreaScreen` + `EntryViewModel.sendSecurityAreaPinReady` / `sendSecurityAreaBounds` |
| `ConfirmationEntry` | `ConfirmationSpecialRoute`（收据 URI / 二维码 / START_UI / Surcharge）+ 默认 `ConfirmationScreen` |
| `TextEntry` | `TextEntryResponseParams` + 金额/字符串/AVS/FSA |
| `OptionEntry` | `OptionListEntryRoute` → `PARAM_INDEX`（0-based） |
| `PoslinkEntry` | `PoslinkEntryRoute`（Dialog / ThankYou / Input / Message / TextBox / Form / SignatureBox 等） |
| `SignatureEntry` | `SignatureCategoryEntryRoute` + `SignatureDemoScreen`（stub） |
| `InformationEntry` | 交易信息 / 审批消息（既有路径） |

补充文档：[layout-xml-retention.md](layout-xml-retention.md)、[acceptance-checklist.md](acceptance-checklist.md)、[resource-audit-t029.md](resource-audit-t029.md)、[fr015-exemption.md](fr015-exemption.md)、[ui-parity-from-golive-xml.md](ui-parity-from-golive-xml.md)（**Compose 1:1 还原时以 `golive/v1.03.00` 的 XML/dimens/自定义 View 为权威来源**）。

### 新会话：UI 还原与 POSUInew 截图门禁

若任务为「对齐 `Expected_Result` / 修 SSIM / 单 action 回归」，请先让 Agent 阅读 Cursor Skill：**`.cursor/skills/compose-ui-parity-golive-posuinew/SKILL.md`**（内含 POSUInew 通过标准、失败分类、递增 build、与 `feature-code-generator` / `test-author` 配合方式）。

## 参考

- [spec.md](spec.md) — 功能规格（含 **FR-019**）
- [plan.md](plan.md) — 实施计划
- [tasks.md](tasks.md) — **T030、T031、T035**
- [contracts/base-class-compose-strategy.md](contracts/base-class-compose-strategy.md)
- [contracts/entry-navigation-routes.md](contracts/entry-navigation-routes.md) — action→route 映射（**T035** 维护）
- [device-profiles.md](device-profiles.md)
- [design-tokens.md](design-tokens.md)
- [001-kotlin-migration](../001-kotlin-migration/spec.md)
- [ui-parity-from-golive-xml.md](ui-parity-from-golive-xml.md) — golive 分支与金额页 XML→Compose 映射
