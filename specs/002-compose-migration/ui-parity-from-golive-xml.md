# Compose 还原度：以 golive XML 为权威尺寸来源

**特性**：002-compose-migration  
**用途**：当仅靠 PNG 难以量出精确 dp/sp/色值时，以 **迁移前已上线的 View 布局与自定义控件** 为单一事实来源，避免在 Compose 里「扫图猜数」来回拧。

## 基线分支（Git）

- **分支名**：`golive/v1.03.00`（远程：`origin/golive/v1.03.00`）。勿与 `golive1.03.00` 等口语写法混淆。
- **查看方式示例**（在 POSLinkUI-Demo 根目录）：
  - `git show golive/v1.03.00:app/src/main/res/layout/fragment_amount.xml`
  - `git show golive/v1.03.00:app/src/main/res/values/dimens.xml`
  - `git show golive/v1.03.00:app/src/main/java/com/paxus/pay/poslinkui/demo/view/TextField.kt`

## 金额页（ENTER_AMOUNT）映射示例

| 层级 | golive 路径 | 说明 |
|------|-------------|------|
| Fragment 布局 | `res/layout/fragment_amount.xml` | `ScrollView` + 垂直 `LinearLayout`；`message` 用 `text_size_title` + `space_between_textview`；确认按钮 `button_height` |
| 尺寸表 | `res/values/dimens.xml` | 如 `space_between_textview=12dp`、`text_size_title=24sp`、`button_height=54dp`、`corner_radius=8dp` |
| 输入框外观与最小高度 | `view/TextField.kt` | `minHeight = button_height`（54dp）；背景 `rounded_corner_on_background`；文字色 `pastel_text_color_on_light`；`gravity` 居中 |
| 输入框 drawable | `res/drawable/rounded_corner_on_background.xml` | 填充与描边均为 `pastel_on_background`，描边 **2dp**，圆角 `@dimen/corner_radius` |
| 颜色 | `res/values/colors.xml` | 输入区 **`pastel_on_background` (#DBD4D9)**，勿与 **`pastel_accent_base` / Compose `SurfaceMutedColor` (#E4E1E3)** 混用 |

其他 Text 类页面：在 golive 上找到对应 `fragment_*.xml` 与是否使用 `TextField` / 主题 style，再映射到 Compose。

## 与当前 Compose Token 的关系

- `PosLinkDesignTokens` 对齐的是设计文档与全站语义；**单页若与 golive 像素级一致**，应以 **该页 XML + 自定义 View** 为准做局部 Composable，避免为「过一页 SSIM」误改全局 Token。
- 若 golive 与 `design-tokens.md` 冲突，应在 PR/任务中写明采纳哪一侧（通常：**截图门禁以 golive+录制 Expected 为准** 或 **以设计稿修订为准**）。

## 自动化与下一会话

- POSUInew 判定、递增 build、`test-author` 失败分析等 **操作流程** 见 Cursor Skill：  
  `@.cursor/skills/compose-ui-parity-golive-posuinew/SKILL.md`
