# 研究：动画库改造与性能降载

**特性**： 004-optimize-animation-stack | **日期**: 2026-03-19

## 1. 项目动画资产清单（Phase 0 产出）

| 位点 ID | 位置 | 类型 | 实现方式 | 可控 | visualSpec（时长/方向/缓动） | 备注 |
|---------|------|------|----------|------|----------------------------|------|
| A1 | `MainActivity` | Fragment 切换 | `setCustomAnimations(anim_enter_from_bottom, anim_exit_to_bottom)` + policy 降级 | 是 | 150ms, 底部进出, bounce；`REDUCED` 降为 fade；`MINIMAL` 关闭过渡 | 主入口筛选页 |
| A2 | `Toast.kt` | DialogFragment | `setCustomAnimations(anim_enter_from_right, fade_out)` + policy 降级 | 是 | 150ms/80ms, 右进/淡入淡出；`REDUCED` 降为 fade；`MINIMAL` 关闭过渡 | Toast 弹窗 |
| A3 | `ClssLightsView` / `ClssLight` | CLSS 指示灯 | 可复用 `ObjectAnimator` + 生命周期停启 | 是 | `STANDARD` 500ms alpha 1↔0；`REDUCED` 800ms；`MINIMAL` steady on | 长时运行热点 |
| A4 | `TransactionPresentation` | 副屏整屏展示（含边框 GIF） | 根 `ComposeView` + Compose 状态树 + Coil `AsyncImage` + 共享 `ImageLoader` | 是 | 默认展示边框循环 GIF 与文字信息；`REDUCED` 缩小解码尺寸；`MINIMAL` 隐藏边框 | 双屏场景 |
| A5 | `ApproveMessageScreen` | 审批动画 | Coil Compose GIF + 文本 fallback | 是 | MasterCard GIF 循环；`REDUCED` 缩小尺寸/缩短展示；`MINIMAL` 文本回退 | 已替代 `DisplayApprovalUtils` |
| A6 | `ReceiptPreviewEntryScreen` | 图片载入过渡 | Coil `crossfade(true/false)` | 是 | `STANDARD` 开启 crossfade；`REDUCED/MINIMAL` 关闭 | 收据预览非关键动效 |

**活跃 XML 动画资源**：`anim_enter_from_bottom`, `anim_exit_to_bottom`, `anim_enter_from_right`

**Raw 资源**：`border_animated.gif`

**遗留资源（已删除）**：旧 `anim_exit_to_left`、`receipt_out` 与 `second_screen_layout.xml` 已从主路径清理

## 2. 技术选型与迁移路径

### 2.1 轻量化方案对比

| 方案 | 内存 | CPU | 适用场景 | 视觉保真 |
|------|------|-----|----------|----------|
| 保留 XML + 优化 | 中 | 中 | 过渡动画 | 完全一致 |
| ViewPropertyAnimator | 低 | 低 | 单 View 属性动画 | 需手工复刻 |
| ObjectAnimator | 低 | 低 | 单 View 多属性 | 需手工复刻 |
| AndroidX Transition | 中 | 中 | 场景切换 | 可复刻 |
| Compose animate* | 低 | 低 | 已 Compose 页面 | 需复刻 |

**实施状态**：A1/A2 保留 XML 过渡并接入策略档位；A3 已改为可复用 `ObjectAnimator`；A4 已迁到整屏 `ComposeView`；A5/A6 继续走 Compose 承载层上的 Coil 图片栈；FR-007/FR-008 已实现。

**混合策略**（按 spec 约定）：

- **Fragment/Toast 过渡**：保留 XML 动画资源，但由策略档位统一决定标准/淡入淡出/无过渡。
- **CLSS 闪烁**：使用可复用 `ObjectAnimator`，并在可见性/窗口生命周期变化时停启，避免长时空转。
- **GIF**：统一使用 Coil，但仅通过 `ComposeView` / Compose `AsyncImage` 承载；通过共享 `ImageLoader`、`size()` 限制解码尺寸、失败回退与策略档位降低内存与发热。
- **Compose 图片过渡**：收据预览只在 `STANDARD` 启用 crossfade，避免非关键过渡长期占用合成资源。

### 2.2 性能优化要点

- **避免**：`AnimationSet` 内多子动画同时运行、`INFINITE` 重复创建、重复创建 `ImageLoader`、双图片库并存。
- **优先**：`ObjectAnimator` 复用、共享 `ImageLoader`、GIF 解码尺寸限制、按策略关闭非关键 crossfade。
- **降级**：低内存设备或高温时，可切换到极简模式（缩短时长、减少动画数量）。

### 2.3 库级差异（与 spec「实现族」映射）

| 库 / API | 归属实现族 | 与同类差异要点 | 跨机型（不出图）要点 |
|----------|------------|----------------|----------------------|
| **Fragment `setCustomAnimations` + `res/anim` XML** | 过渡类 | 系统按资源解析，与旧 `android.view.animation` 视图动画体系一致；改 XML 即可调时长曲线 | 动画多用 `fromXDelta` 百分比、dp，一般不依赖多套密度切图 |
| **`ObjectAnimator` / `ViewPropertyAnimator`** | 视图属性类 | 走属性动画管线，取消/生命周期对齐更清晰；通常比老 `Animation` 少一层包装 | 平移/缩放应对 **测量后高度/宽度** 或 **百分比 of parent**，避免写死 px；配合 ConstraintLayout 可减少重出图 |
| **Coil / `coil-gif` / `coil-compose`** | 栅格序列类（本项目中 A4-A6） | Kotlin 友好、通过 Compose `AsyncImage` 复用同一 `ImageLoader`；GIF 可用 `ImageDecoder`（P+）或 `GifDecoder`；`size()` 限制解码降内存 | **单套 raw/drawable 资源 + `size(w,h)`** 适配不同屏，无需按分辨率再导出多套 GIF |
| **Compose 承载层** | 视图属性类（声明式） | Compose 页面或整屏 `ComposeView` 承载 AsyncImage、文本 fallback 与副屏展示；策略仍统一由共享动画支持层控制 | 用 `dp`、`fillMaxSize()` 等可避免按 px 出图；避免在 View 层继续保留 `ImageView + Coil` |

**单图片栈结论**：A4/A5/A6 已统一为 Compose 承载层上的 Coil。相比继续保留 Glide，单栈可减少缓存、依赖和重复配置成本，同时仍满足 GIF 尺寸控制与失败降级需求。

### 2.4 与 FR-009 / SC-007 的落地

- 在 `quickstart.md` 或单独附件维护 **DeviceMatrixEntry** 列表（机型名、分辨率、是否第二屏）。
- 栅格类动效验收时检查：是否仅依赖 **一套源文件 + 尺寸上限**，而非要求设计新增多密度批次。

## 3. 自测策略

- **视觉回归**：每个动画位点录制改造前后对比（手动或截图对比），或通过 UI 测试断言关键 View 的 visible/alpha/translation。
- **性能基线**：改造前采集 CPU、内存、温升；改造后对比，满足 SC-002、SC-003、SC-004。
- **可执行**：本地 `./gradlew` 任务 + 可选 UI 测试；CI 可跑性能门禁（若设备可用）。

## 4. 风险与依赖

- **Coil**：已覆盖当前全部动图位点，无需新增动画库；GIF 优化通过现有 API 即可。

## 5. RiskExceptionRecord（T014）

| 位点 | 影响范围 | 临时策略 | 复核 |
|------|----------|----------|------|
| 无 | — | — | 可控位点 A1–A6 均已替换或优化 |
- **Compose 页面**：部分页面已 Compose，可统一用 `animate*`；当前动画以 View 为主，优先迁移 View 动画。
- **Neptune/POSLink**：不涉及，动画为纯 UI 层。
