---
name: ui-parity-golive-bugfix-from-report
description: >-
  读取 specs/posuinew-gate-reports/ 下由 SSIM 门禁生成的 Markdown Bug 报告，仅对未通过门禁且归类为须开发修复的项：定位对应 Compose 页面与路由，并对照 git 分支 golive/v1.03.00 上重构前 layout/Java/XML 排查未对齐点并最小修复。与 posuinew-ssim-gate-bug-report 成对使用。
---

# Golive 对齐：从门禁 Bug 报告到代码修复（开发 Skill）

## 何时使用

- 用户给出 **`specs/posuinew-gate-reports/`** 下已生成的 **`BUG-*.md`** 或 **`gate-run-summary.md`**，要求**按报告修 UI/路由对齐**。
- 缺陷来源为 **POSUInew SSIM** 未通过，且测试侧已标 **failure_class**（优先处理 **B**；**A** 须先确认是否重录期望而非改代码）。

## 输入前提

- 报告中须含：**pytest node id**、**action/category 或 adb 线索**、**SSIM 实际值与阈值（数字）**、**Actual/Expected 原始路径**。  
- **视觉**：优先阅读 **`BUG-*.md` 内嵌的 Actual/Expected 图**（`./assets/.../actual.png` 等）；测试 Skill 要求将图复制进仓库并嵌入，便于在未挂载 POSUInew 时仍能对齐分析。  
- 若仅有路径无图、无 SSIM 数字：先要求补全报告，避免猜测页面。

## 必读依赖

- Golive 权威源与尺寸：`@.cursor/skills/compose-ui-parity-golive-posuinew/SKILL.md`、`specs/002-compose-migration/ui-parity-from-golive-xml.md`
- 路由与 Entry：`@.cursor/agents/feature-code-generator.md`（`EntryActionRegistry`、`EntryScreenRouter`、`TextEntryResponseParams`、`EntryExtraData`）
- Kotlin 编译与空安全：`@.cursor/skills/kotlin-migration-compilation/SKILL.md`

## 工作流程

### 1. 过滤报告

- 打开 **`gate-run-summary.md`**（若存在）与目标 **`BUG-*.md`**。
- **跳过或仅备注**：`failure_class: A` 且结论为「需重录 Expected」、**C** 类（先改 Excel/adb）。
- **默认实施**：**B**（及确认非环境问题后的 **D**）。

### 2. 定位页面（Compose 侧）

1. 从报告中的 **action 子串**、**category**、**node id** 对照 **`EntryActionRegistry`**、**`EntryScreenRouter`**、**`AndroidManifest.xml` activity-alias**。  
2. 确认落到具体 **`*Route.kt` / `*Screen.kt`**（如 `AmountEntryRoute`、`TextEntry*`、`Confirmation*`）。  
3. 核对 **`EntryExtraData`** 与报告中 adb 是否一致（错键名 → 表现像「整页不对」）。

### 3. 对照 golive/v1.03.00（重构前）

权威分支名：**`golive/v1.03.00`**（非口语混用）。

在 **POSLinkUI-Demo** 仓库根目录使用只读对照（示例）：

```bash
git show golive/v1.03.00:app/src/main/res/layout/<对应_fragment>.xml
git show golive/v1.03.00:app/src/main/res/values/dimens.xml
git show golive/v1.03.00:app/src/main/java/.../view/TextField.kt
```

- **尺寸/色值/圆角**：优先以 **golive 的 layout + dimens + colors** 为准，而非凭 Actual 截图猜。  
- **逻辑分支**：旧版 **Fragment/Activity** 内条件与 Compose **State/Route** 是否一致。

### 4. 修改原则

- **最小 diff**：只改该 Bug 对应页面与共享组件中必要部分；禁止借机全局「重设计」。  
- **不误伤全局 Token**：单页像素对齐参见 `ui-parity-from-golive-xml.md`，避免为过一页 SSIM 乱改 `DesignTokens`。  
- 改路由/常量后：按 `@.cursor/skills/android-post-change-build/SKILL.md` 在任务收尾时执行 **`assembleDebug`** / **`testDebugUnitTest`**。

### 5. 闭环

- 修复后在说明中列出：**根因类别**（registry / resolveKind / 路由顺序 / extras / 纯 UI 尺寸）。  
- 建议用户用 **同一 pytest node id** 复跑 POSUInew，或委托 **test-author** 按原命令复测。  
- 若需更新期望图：在回复中明确须 **测试侧 `--save-expect`**，避免实现侧硬凑。

## 禁止

- 未读 golive 对应文件就仅按 PNG 「目测」改 Compose。  
- 将 **A 类（缺图/过期基线）** 当纯代码 Bug 无限改 UI。  
- 忽略 **Entry extras** 与 **Manifest alias** 与 Excel 的一致性（常与 **C** 类交织）。

## 相关

- 生成报告的测试 Skill：`@.cursor/skills/posuinew-ssim-gate-bug-report/SKILL.md`
