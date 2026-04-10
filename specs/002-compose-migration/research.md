# 研究：UI 转 Compose 迁移

**特性**：002-compose-migration  
**日期**: 2025-03-16 | **修订**: 2026-03-28

## 1. Entry UI 宿主：Navigation Compose 优先（spec FR-019）

### Decision（当前）

- **主路径**：在 **单宿主**（`EntryActivity.setContent` 或 **单宿主 Fragment + 唯一 `ComposeView`**）内使用 **Jetpack Navigation Compose**：`NavHost(navController, startDestination) { composable(route) { … } }`。
- **映射**：`Intent` action（`com.pax.us.pay.action.*` 等）→ **route** 在 **单点** 解析；文档/占位见 [contracts/entry-navigation-routes.md](contracts/entry-navigation-routes.md)，任务 **T035**。
- **`onNewIntent`**（`singleTop`）与 `navController` 协同；返回栈与取消/超时终态须符合状态机（**SC-013**）。
- **不新建**替代 `EntryActivity` 的 manifest 宿主 Activity；alias 与 action 不变。

### Rationale

- **少并列 `ComposeView` 根**，降低主题/规格/返回栈分裂风险
- 与 **FR-M001** 在「Nav destination」形态下仍可逐条验收 action→界面
- 与 **FR-016/017** 一致：各 destination 共用 T030/T031

### 过渡策略

- 尚未接入 `NavHost` 的屏，可短期保留 **Fragment `onCreateView` → `ComposeView` → `setContent`**；**新屏**应优先注册为 **composable**，并计划收敛。

### 历史决策（2025-03 初版）

- 曾以「每 Fragment 内嵌 ComposeView」为最低风险渐进路径；随 spec **FR-019** 修订，**目标态**升级为 **单宿主 Nav**；内嵌模式降为 **过渡**。

### Alternatives Considered

- **新建 ComposeActivity 替代 EntryActivity**：manifest/宿主契约变更面大，母版不默认采纳。
- **长期「每 Entry 一个独立 ComposeView 根」**：与 **FR-019** 冲突，易重复 Theme/规格与导航状态。

---

## 2. Jetpack Compose 依赖与版本

### Decision

- 使用 `androidx.compose.bom` 管理版本，与 Kotlin 1.9.22 / 2.1.0 兼容
- 最小依赖：`compose-ui`、`compose-material3`（或 material）、`compose-ui-tooling`（debug）
- 启用 `buildFeatures { compose true }` 与 `kotlinCompilerExtensionVersion`

### Rationale

- BOM 简化版本管理
- 项目已有 Kotlin，Compose 与 Kotlin 2.1.0 兼容良好

---

## 3. 设计数值提取与 design-tokens

### Decision

- 从 POSLinkUI-Design_V1.03.00.docx 手工提取：布局（padding、margin、尺寸）、字体（字号、字重）、颜色（HEX）、间距
- 记录于 `res/values/` 下 XML 或 Compose `Theme`/`Color`/`Typography` 对象；可创建 `design-tokens.kt` 或 `Theme.kt` 集中管理

### Rationale

- 设计文档无法被 AI 解析，需人工提取
- 集中管理便于后续维护与主题一致性

---

## 4. 图片加载与 Compose

### Decision

- 使用 Coil（项目已有）的 `coil-compose` 或 `AsyncImage` 加载本地 drawable；或使用 `painterResource(R.drawable.xxx)` 加载 res 资源
- 切图落 `res/drawable` 或 `res/drawable-nodpi`；禁止远程 URL

### Rationale

- 项目已有 Coil，无需额外依赖
- `painterResource` 适用于静态 drawable

---

## 5. 还原度验收流程

### Decision

- 人工对比：从设计稿（docx 导出或截图）与实现截图对比
