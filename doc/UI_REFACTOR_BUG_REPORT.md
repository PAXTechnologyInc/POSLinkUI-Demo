# POSLink Command UI 重构 Bug 报告

**报告日期**：2026-04-01  
**对比范围**：`expected_result/Poslink_Command/` vs `actual_result/Poslink_Command/`  
**旧版参考**：`d:\gitlabProject\AIRebuildTest\POSLinkUI-Demo`（Java + XML 实现）  
**新版被测**：`d:\gitlabProject\AIRebuildTest\Re-POSLinkUI-Demo`（Kotlin + Compose 重构）  
**关联代码**：`ExtendedEntryRoutes.kt → PoslinkEntryRoute`、`PrintDataItem.java`、`TextShowingUtils.java`

---

## 总览

| Bug ID | 分类 | 受影响页面 | 严重度 |
|--------|------|-----------|--------|
| UI-001 | 全局主题/背景色 | 所有 POSLink 页面 | 🔴 高 |
| UI-002 | Title 格式指令未解析（`\L\R\B\C\1\2\3`） | ShowMessage / ShowDialog / ShowTextBox / ShowDialogForm / ShowItem | 🔴 高 |
| UI-003 | `PARAM_MESSAGE_LIST` 显示原始 JSON | ShowMessage / ClearMessage | 🔴 高 |
| UI-004 | `PARAM_ITEMS`（ShowItem）显示原始 JSON | ShowItem | 🔴 高 |
| UI-005 | ShowMessage/ShowItem 广告图片未渲染 | ShowMessage / ShowItem | 🟡 中 |
| UI-006 | Tax/Total 底部固定布局丢失 | ShowMessage / ShowItem | 🟡 中 |
| UI-007 | ShowDialog 按钮并排布局丢失（1+2 并排 / 3 全宽） | ShowDialog | 🔴 高 |
| UI-008 | ShowTextBox 按钮应固定在底部，现在贴在顶部 | ShowTextBox | 🟡 中 |
| UI-009 | ClearMessage 未正确清屏（仍显示上一屏内容） | ClearMessage | 🔴 高 |
| UI-010 | ShowDialogForm 第一个选项被自动选中（旧版无默认选中） | ShowDialogForm | 🟡 中 |
| UI-011 | 按钮样式差异（圆角 vs 直角 / 颜色） | 所有带按钮的页面 | 🟡 中 |
| UI-012 | InputText 键盘不自动弹出 | InputText / ShowInputTextBox | 🟡 中 |

---

## 详细说明

---

### UI-001：全局主题/背景色不符

**受影响页面**：所有 POSLink Command 页面

| | 截图表现 |
|--|---------|
| **Expected（旧版）** | 深海军蓝背景（`~#0d1b3e`），白色文字 |
| **Actual（新版）** | 白色背景，深色文字 |

**Expected 截图示例**（ShowMessage_None）：  
深蓝全屏，Title 居中白色，底部图片广告区，Tax/Total 白色左右对齐。

**Actual 截图示例**（ShowMessage_None）：  
白底，Title 左对齐深色，无图片，无固定底部区。

**根本原因**：  
新版 `PoslinkEntryRoute` 使用的 `Column + PosLinkText + PosLinkPrimaryButton` 继承的是 Light Theme（白色背景），未应用旧版 `R.style.CBPUITheme`（深蓝主题）。

**修复方向**：  
- 为 POSLink 类页面的外层容器设置深蓝背景色（参考旧版 `background_fragment_base.xml` 或 design token）
- 文字色对应改为白色

---

### UI-002：Title 格式指令（`\L` `\R` `\B` `\C` `\1` `\2` `\3`）原样显示，未解析

**受影响页面**：ShowMessage / ShowDialog / ShowTextBox / ShowDialogForm / ShowItem 等所有读取 `PARAM_TITLE` 并渲染的页面

**协议说明**：  
`PARAM_TITLE` 的值可包含如下格式指令（定义于 `PrintDataItem.java`）：

| 指令 | 常量 | 含义 |
|------|------|------|
| `\L` | `LEFT_ALIGN` | 该段左对齐 |
| `\R` | `RIGHT_ALIGN` | 该段右对齐 |
| `\C` | `CENTER_ALIGN` | 该段居中对齐 |
| `\B` | `BOLD` | 该段加粗 |
| `\1` | `SMALL_FONT` | 小字体（20sp） |
| `\2` | `NORMAL_FONT` | 正常字体（24sp） |
| `\3` | `BIG_FONT` | 大字体（28sp） |
| `\n` | `LINE_SEP` | 换行 |

**对比示例（ShowMessage_L_R_B）**：

| | Title 内容 | 渲染结果 |
|--|-----------|---------|
| **Expected（旧版）** | `"\LLeft\RRight\BBold Word"` | 三段水平布局：`Left`（左）/ `Bold Word`（中粗）/ `Right`（右） |
| **Actual（新版）** | `"\LLeft\RRight\BBold Word"` | **原样输出**：`\LLeft\RRight\BBold Word` |

**对比示例（ShowMessage_C）**：

| | Title 内容 | 渲染结果 |
|--|-----------|---------|
| **Expected** | `"\CLeft"` | `Left`（居中） |
| **Actual** | `"\CLeft"` | `\CLeft`（原样） |

**对比示例（ShowMessage_1_2_3）**：

| | Title 内容 | 渲染结果 |
|--|-----------|---------|
| **Expected** | `"\1Left\2Right\3Bold Word"` | 三段：Left（小字）/ Left（正常）/ Bold Word（大字）分别对齐排布 |
| **Actual** | `"\1Left\2Right\3Bold Word"` | `\1Left\2Right\3Bold Word`（原样） |

**根本原因**：  
旧版通过 `PrintDataConverter.parse()` → `TextShowingUtils.getTitleViewList()` / `getViewList()` 将指令解析为多个带 Gravity 的 `TextView`，并动态 `addView` 到 `LinearLayout`。  
新版 `PoslinkEntryRoute` 直接用 `PosLinkText(text = title)` 渲染原始字符串，**完全未移植**指令解析逻辑。

**修复方向**：  
在新版中实现等价的 Compose 指令解析器，将 `\L\R\B\C\1\2\3\n` 指令转为对应的 `TextStyle`、`Arrangement`、`AnnotatedString`，在单行内实现左/中/右多段布局。参考旧版 `PrintDataConverter.java` + `TextShowingUtils.java`。

---

### UI-003：`PARAM_MESSAGE_LIST` 未解析，显示原始序列化字符串

**受影响页面**：ShowMessage (Single / Multiple) / ClearMessage

**协议说明**：  
`PARAM_MESSAGE_LIST` 是一个序列化的消息列表，每条消息包含 `msg1`（行1）和 `msg2`（行2）字段，需要逐条解析并展示为换行文本列表。

**对比示例（ShowMessage_Single）**：

| | 渲染结果 |
|--|---------|
| **Expected** | `message1`（第1行） / `message12`（第2行），正常列表展示 |
| **Actual** | `[{index:x,MsgInfo:{msg1:message1,msg2:message12}}]`（完整 JSON 串原样显示） |

**对比示例（ShowMessage_Multiple，7 条消息）**：

| | 渲染结果 |
|--|---------|
| **Expected** | `message1 / message12 / message1 / message12 / ...`（按条展示） |
| **Actual** | 7 条 JSON 对象拼成的一长串文字 |

**根本原因**：  
`ExtendedEntryRoutes.kt` 第 384 行：
```kotlin
val list = extras.getString(EntryExtraData.PARAM_MESSAGE_LIST).orEmpty()
...
if (list.isNotBlank()) PosLinkText(text = list, role = PosLinkTextRole.Supporting)
```
直接把序列化字符串当文本渲染，未解析。旧版 `ShowMessageFragment.java` 通过 `MessageItemAdapter` + 自定义数据结构（`MsgInfoWrapper`）完成解析。

**修复方向**：  
实现 `PARAM_MESSAGE_LIST` 的解析逻辑（解析 `msg1`/`msg2` 字段），将结果渲染为多行文本列表。

---

### UI-004：ShowItem 商品列表（`PARAM_ITEMS`）未解析，显示原始 JSON

**受影响页面**：ShowItem

**协议说明**：  
`ShowItem` 命令通过 `PARAM_MESSAGE_LIST`（或 item 专用参数）传入商品列表，每项包含 `productName`、`plUcode`、`price`、`unit`、`unitPrice`、`tax`、`quantity`、`productImgUri`、`productImgDesc` 等字段，应格式化展示为商品清单。

**对比示例（ShowItem）**：

| | 渲染结果 |
|--|---------|
| **Expected** | 编号列表：`1. test`（数量 x99 @$9 / 小计 $123.0）/ `2. test2`（99lb @$9/lb / $999.99）/ `3. test2`（99ft @$9/ft / $999.99） |
| **Actual** | `[ {index : 0,ItemDetail :{productName :test, plUcode : 11,price: 123.00,unit : x,...}...]` 完整 JSON 串 |

**根本原因**：  
`PoslinkEntryRoute` 中 `ShowItem` 与 `ShowMessage` 走同一分支，直接输出 `list` 原始字符串。旧版 `ShowItemFragment.java` 通过 `ItemDetailWrapper`、`ItemListAdapter`、格式化价格与数量拼接完成渲染。

**修复方向**：  
为 `ACTION_SHOW_ITEM` 单独实现商品列表解析与 Compose 渲染（`ItemDetailWrapper` 对应的 Kotlin data class + LazyColumn）。

---

### UI-005：ShowMessage / ShowItem 广告图片未渲染

**受影响页面**：ShowMessage / ShowItem

**协议说明**：  
`PARAM_IMAGE_URL` 指定终端广告图 URL，`PARAM_IMAGE_DESC` 为图片描述，显示在消息内容区中部。

**对比示例（ShowMessage_None expected）**：  
中部有一张宽屏广告图（`mt30_ad0.png`），图片下方有文件名描述。

**Actual**：无任何图片区域。

**根本原因**：  
`ExtendedEntryRoutes.kt` 中 `ShowMessage` 分支未读取 `PARAM_IMAGE_URL` / `PARAM_IMAGE_DESC`，缺少对应的 `PosLinkAsyncImage` 渲染逻辑。

**修复方向**：  
在 `ShowMessage` / `ShowItem` 的 Column 中添加图片加载逻辑（与旧版 `Coil` / `Picasso` 等价，新版已有 `PosLinkAsyncImage`）。

---

### UI-006：ShowMessage / ShowItem Tax / Total 底部固定布局丢失

**受影响页面**：ShowMessage / ShowItem

**协议说明**：  
`PARAM_TAX_LINE` 和 `PARAM_TOTAL_LINE` 应显示在屏幕**底部固定区域**（anchor to bottom），以 `Tax:` / `Total:` 标签 + 数值左右对齐形式呈现。

**对比（ShowMessage_None）**：

| | Tax/Total 显示 |
|--|--------------|
| **Expected** | 底部固定：`Tax: 10`（左标签/右值）`Total: 9999` |
| **Actual** | 顶部普通文本流：`10` / `9999`（无标签，无底部固定） |

**根本原因**：  
```kotlin
// ExtendedEntryRoutes.kt 行 387-388
if (tax.isNotBlank()) PosLinkText(text = tax)
if (total.isNotBlank()) PosLinkText(text = total)
```
旧版将 Tax/Total 渲染在 `LinearLayout` 的底部 weight=0 区域（固定底部）。新版将其直接插入 Column 流式布局顶部。

**修复方向**：  
使用 `Box + Alignment.BottomCenter` 或 `Column + weight(1f) + Spacer + tax/total` 实现底部固定区域；按 `"Tax: " + taxLine` 格式拼接标签。

---

### UI-007：ShowDialog 按钮布局——Button1+Button2 应并排，Button3 应单独全宽

**受影响页面**：ShowDialog（含 L_R_B / 1_2_3 / n 变体）

**对比（ShowDialog_L_R_B）**：

| | Button 布局 |
|--|------------|
| **Expected** | Button1 + Button2 同行（各占 50% 宽度）/ Button3 单独一行（全宽） |
| **Actual** | Button1 / Button2 / Button3 各占一行（全宽垂直列） |

**设计规则（来自 PDF 及旧版代码）**：  
- 只有 1 个按钮：全宽单行  
- 有 2 个按钮：左右各占一行  
- 有 3 个按钮：Button1 + Button2 并排（`LinearLayout horizontal weight=1`），Button3 全宽下方

**根本原因**：  
`PoslinkEntryRoute` ShowDialog 分支：
```kotlin
opts.forEachIndexed { index, label ->
    PosLinkPrimaryButton(...)  // 全部全宽垂直排列
}
```
未实现「前两个并排，第三个全宽」的条件布局逻辑。

**修复方向**：  
根据 `opts.size` 判断布局策略：
- 1 个：全宽  
- 2 个：`Row(Modifier.fillMaxWidth()) { Button(Modifier.weight(1f)); Button(Modifier.weight(1f)) }`  
- 3 个：同上（前两个并排）+ 下面全宽 Button3

---

### UI-008：ShowTextBox 按钮应固定在屏幕底部，新版贴在 Title 下方顶部

**受影响页面**：ShowTextBox（含 L_R_B / 1_2_3 / C / n 变体）

**对比（ShowTextBox_L_R_B）**：

| | Button 位置 |
|--|------------|
| **Expected** | 按钮固定在屏幕底部边缘（bottom anchor），Title 在顶部 |
| **Actual** | 按钮紧跟在 Title 下方（顶部区域），屏幕下半部空白 |

**根本原因**：  
`PoslinkTextBoxButtons` 里 Column 布局不含 `Modifier.fillMaxHeight()` + `Spacer(Modifier.weight(1f))` 来将按钮推到底部。

**修复方向**：  
```kotlin
Column(Modifier.fillMaxSize()) {
    // title / body
    Spacer(Modifier.weight(1f))  // 推送按钮到底部
    // buttons
}
```

---

### UI-009：ClearMessage 未正确清屏（仍显示上一屏内容）

**受影响页面**：ClearMessage（`POSLinkStatus.CLEAR_MESSAGE` 广播触发）

**对比（ClearMessage）**：

| | 渲染结果 |
|--|---------|
| **Expected** | 全屏空白深蓝色背景（状态已清除） |
| **Actual** | 显示"Title / 10 / 9999 / [多条 JSON 消息列表]"——上一次 ShowMessage 的内容残留 |

**根本原因分析**：  
POSLink 协议定义 `POSLinkStatus.CLEAR_MESSAGE = "com.pax.us.pay.POSLINK_CLEAR_MESSAGE"` 广播用于清除当前显示内容。  
新版 `StatusBroadcastReceiver` 需要处理此广播，清除 POSLink 页面状态。但当前实现中：
- 该广播可能未在 filter 中注册（待验证）
- 或注册了但收到后未清除 POSLink 页面的显示状态（`EntryViewModel` 中未对应清屏逻辑）

**修复方向**：  
1. 确认 `StatusBroadcastReceiver` 的 filter 包含 `POSLinkStatus.CLEAR_MESSAGE`
2. 收到此广播后，通知当前 POSLink Activity/Composable 清空显示内容（置空 title / messageList / items），恢复空白深色背景状态

---

### UI-010：ShowDialogForm 第一个 RadioButton 自动选中，旧版应无默认选中

**受影响页面**：ShowDialogForm（ButtonType1 / ButtonType2）

**对比（ShowDialogForm_ButtonType1）**：

| | RadioButton 状态 |
|--|----------------|
| **Expected** | 所有选项均为空心圆（未选中状态） |
| **Actual** | 第一个选项蓝色实心（自动选中） |

**根本原因**：  
```kotlin
var sel by remember(labels) { mutableStateOf(0) }  // 默认 index=0 → 第一项被选中
```
旧版 `ShowDialogFormFragment` 初始不选中任何项，用户必须主动选择后按 CONFIRM 才能提交。

**修复方向**：  
```kotlin
var sel by remember(labels) { mutableStateOf(-1) }  // -1 表示无默认选中
// CONFIRM 按钮仅在 sel >= 0 时可用
PosLinkPrimaryButton(
    enabled = sel >= 0,
    onClick = { if (sel >= 0) viewModel.sendNext(...) }
)
```

---

### UI-011：按钮控件样式与旧版不一致

**受影响页面**：所有包含按钮的 POSLink 页面

| 属性 | Expected（旧版） | Actual（新版） |
|------|----------------|--------------|
| 背景色 | 半透明蓝灰（`#8899BB`）| 亮蓝（Material `primary`）|
| 圆角 | 直角或小圆角（`4dp`）| 大圆角胶囊（`50dp`）|
| 字体大小 | 大写英文（ALL CAPS）| 首字母大写 |
| 分隔线 | 有白色分隔线 | 无分隔线 |

**根本原因**：  
`PosLinkPrimaryButton` 控件沿用了 Material3 默认样式，未针对 POSLink 深色主题定制。

**修复方向**：  
为 `PosLinkPrimaryButton` 增加深色主题变体（或在 POSLink 页面内使用专属按钮组件），对齐旧版 XML `btn_poslink.xml` 样式。

---

### UI-012：InputText / ShowInputTextBox 键盘不自动弹出

**受影响页面**：InputText（inputtype 0–7）/ ShowInputTextBox（inputtype 0–7）

**对比（InputText_inputtype0）**：

| | 键盘状态 |
|--|---------|
| **Expected** | 输入框获焦时软键盘自动弹出 |
| **Actual** | 键盘不自动弹出，需手动点击输入框 |

**keyboard_open vs keyboard_closed 子目录均有对应截图，旧版在 `keyboard_open` 变体中键盘展开。**

**根本原因**：  
`GenericStringEntryScreen` 中 `TextField` 可能未设置 `FocusRequester` + `LaunchedEffect` 自动聚焦，导致软键盘不自动弹出。

**修复方向**：  
```kotlin
val focusRequester = remember { FocusRequester() }
LaunchedEffect(Unit) { focusRequester.requestFocus() }
TextField(modifier = Modifier.focusRequester(focusRequester), ...)
```

---

## 修复优先级建议

| 优先级 | Bug | 说明 |
|--------|-----|------|
| P0（阻塞功能） | UI-002、UI-003、UI-004、UI-009 | 指令未解析/内容显示错误/清屏失效，直接影响用户看到错误内容 |
| P1（功能不完整） | UI-001、UI-007、UI-005、UI-006 | 主题、布局、图片等核心视觉功能未对齐 |
| P2（细节差异） | UI-008、UI-010、UI-011、UI-012 | 布局位置、交互默认值、样式、键盘行为 |

---

## 附：格式指令完整参考（来自旧版 PrintDataItem.java）

```java
public static final String BOLD        = "\\B";   // 加粗
public static final String LEFT_ALIGN  = "\\L";   // 左对齐
public static final String RIGHT_ALIGN = "\\R";   // 右对齐
public static final String CENTER_ALIGN= "\\C";   // 居中
public static final String SMALL_FONT  = "\\1";   // 小字（20sp）
public static final String NORMAL_FONT = "\\2";   // 正常字（24sp）
public static final String BIG_FONT    = "\\3";   // 大字（28sp）
public static final String LINE_SEP    = "\\n";   // 换行符
```

示例输入 → 预期渲染：
- `"Title"` → 居中显示（默认居中）
- `"\LLeft\RRight\BBold Word"` → 行内三段：Left（左）、Right（右）、**Bold Word**（粗体居中）
- `"\CLeft"` → "Left" 居中显示
- `"\1Small\2Normal\3Big"` → 三段按字号分别渲染

---

> 报告由自动对比截图 + 代码分析生成。后续测试发现更多问题可在此文档补充。
