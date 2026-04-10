# 功能规格：UI 转 Compose 迁移

**特性分支**: `002-compose-migration`  
**创建日期**: 2025-03-16  
**最近修改**: 2026-03-28
**状态**: 草案（依赖 001-kotlin-migration 合并后启动）  
**母版对齐**：本特性在工程边界、宿主集成、Design Tokens 权威关系上与 [`specs/000-poslinkui-project-master/spec.md`](../000-poslinkui-project-master/spec.md) 一致；若与母版冲突，应修订母版并注明原因。母版中的 **FR-M001–M005、SC-M001–M003** 仍适用于本迁移（Intent 路由、广播契约、敏感数据、资源释放）。  
**输入**：将 **Entry 相关 UI** 转为 Jetpack Compose 实现，按 POSLinkUI-Design_V1.03.00 设计规范还原。迁移顺序：Text → Security → Confirmation → POSLink → Option → Information。**补充要求**：页面还原度第一；通过 `AndroidManifest.xml` 中 Activity / alias 配置能正常调起；图片使用恰当；主题与可访问性、屏幕兼容、性能、导航等需符合规范。

**目标态（2026-03-26 优化）**：在收尾阶段达成 **布局层去 XML**（见下文 FR-005 与边界说明）、**减少仅为参数差异而存在的 Fragment 子类层级**、**可复用 Compose 组件与统一入口 API**、**机型/副屏布局规格**（[`device-profiles.md`](device-profiles.md)，**SC-011 / FR-017 / FR-018**），以及 **尽可能少的 Compose 宿主 + Navigation Compose `NavHost`**（**FR-019**，少嵌套 `ComposeView`）。

## 澄清说明

### 会话 2025-03-16

- Q: Compose 与 XML 共存策略为何？ → A: 历史结论为「Fragment 内嵌 ComposeView 逐步替换」；**2026-03-26 起目标态改为布局层去 XML**，详见下节会话。
- Q: 设计规范中是否定义主题/深色模式？ → A: 否，设计规范未定义深色模式，本迁移不强制实现深色模式
- Q: 页面还原度如何验收？ → A: 人工对比设计稿与实现截图，逐页/逐模块验收
- Q: 本迁移是否必须实现可访问性（contentDescription）？ → A: 否，本迁移不强制实现可访问性，可后续迭代
- Q: 本迁移是否要求支持横竖屏切换？ → A: 否，本迁移不强制支持横竖屏，可后续迭代

### 会话 2026-03-26（架构与交付优化）

- Q: 是否允许长期保留 `res/layout` 与 XML 混用？ → A: **否（目标态）**。Entry 流程 UI 以 Compose 为唯一布局实现；**过渡期内**可保留 ComposeView 桥接，但每个模块迁移完成后应删除已无引用的 layout 文件及对 XML inflater 的依赖。**保留**：`AndroidManifest.xml`、必要的 `res/values`（如直至全量迁移完成前仍被系统或非 UI 引用的项）、以及母版所述宿主集成所需配置。
- Q: Fragment 继承过深、仅少数字段不同怎么办？ → A: **优先**用单一 Fragment（或少量按安全/生命周期硬边界划分的 Fragment）+ **参数化 Screen Composable**（`action`、标题、校验规则、回调等经参数区分），避免仅为文案或校验差异新增子类；**继承仅保留**在确有模板方法差异或安全隔离需要时。
- Q: 如何避免每个页面重复拉 Theme / 零散引用 token？ → A: 在 `ui` 包下提供 **全局可复用层**：如统一根 Composable（自动套 `PosLinkTheme`）、标准间距/标题/主按钮/列表项等 **预制组件**，以及 **单点读取 Design Tokens** 的访问方式（例如集中 `object` 或 `CompositionLocal`），业务 Screen 主要组合这些 API，而非每次从零拼 Theme。
- Q: 不同 PAX/渠道机型差异如何处理？ → A: 见 **2026-03-27** 会话；已录入参考表于 [`device-profiles.md`](device-profiles.md)，通过 **配置层 profile + `DeviceLayoutSpec`** 消费，禁止业务 Composable 散落机型 `if` 与 magic dp。

### 会话 2026-03-27（P0 机型、副屏、间距/dp 策略）

- Q: 首批必须适配哪些机型？ → A: **P0**：**A3700**、**A920MAX**、**A920Pro（PCI7，含副屏）**、**A80 Android 10（规格上优先按 A80S 档验收，Build 映射见 device-profiles）**、**A35**；全量参考表见 [`device-profiles.md`](device-profiles.md)。  
- Q: 间距、按钮高度等 dp 能否写死？ → A: **否**。语义尺寸（边距、按钮最小高度、列表行高、圆角等）须经 **FR-016 封装** 与 **FR-017 规格映射**（按 `sw` / 档位 / 窗口尺寸），可来自 `DesignTokens` 的**语义档位**，不得在各 Screen 直接堆字面量 `NN.dp` 作为全局唯一值。  
- Q: A920Pro 副屏？ → A: **须单独验收**（FR-018）：`Presentation` / 第二显示设备上的内容与主屏一致走 Token + 布局规格扩展，生命周期与现有 `EntryActivity`、`TransactionPresentation`、`SecondScreenInfoViewModel` 对齐。

### 会话 2026-03-28（Navigation Compose / 少 ComposeView）

- Q: Entry 里还要每个页面一个 `ComposeView` 吗？ → A: **尽量不**。在 **单一 Compose 宿主边界**（`Activity.setContent` 或 **一个** 宿主 `Fragment` 内 **一个** `ComposeView`）内，**优先**使用 **`NavHost(navController, startDestination) { composable(route) { … } }`**（Navigation Compose）组织多屏；路由建议类型安全（如 `sealed class` / `enum`，示例概念 **`TransactionRoute`**）。**Intent / action → route** 须在单点注册表或文档中可追踪，满足母版 **FR-M001** 的「正确界面」等价性。`**onNewIntent**`、返回栈与取消/超时终态仍须符合状态机与项目支付规则。

## 用户场景与测试 *（必填）*

### 用户故事 1 - Text 模块 Compose 迁移（优先级：P1）

将 Text 类 Entry 的 UI 实现从 XML/View 转为 Jetpack Compose，按 POSLinkUI-Design_V1.03.00 设计规范还原布局、字体、颜色、间距。

**优先级理由**：Text 输入是 Entry 流程的基础能力，金额、持卡人信息、参考号等均依赖 Text 模块；优先迁移可建立 Compose 基础组件与主题规范。

**独立测试**：Text 类 Entry（如 AmountFragment、ReferenceNumberFragment、ZipcodeFragment 等）在 Compose 实现下可正常输入与校验；布局、字体、颜色、间距与设计规范一致；交互状态（聚焦、禁用、错误提示）与设计一致。

**验收场景**：

1. **假如** Text 模块已迁移为 Compose，**当** 进入金额输入、参考号输入等 Entry 流程，**那么** 输入框可输入、校验规则生效、结果正确回传
2. **假如** Design Tokens 已提取，**当** 人工对比设计稿与实现截图验收，**那么** 布局（关键元素相对位置、对齐、边距）、字体（字号、字重、行高）、颜色（hex）、交互状态（按下/禁用/聚焦/错误）与 Design Tokens 一致
3. **假如** 存在聚焦、禁用、错误等交互状态，**当** 触发，**那么** 视觉与 Design Tokens 及设计稿一致
4. **假如** manifest.xml 中已配置 Activity，**当** 通过 Intent 或宿主应用调起，**那么** Compose 页面可启动并显示
5. **假如** 页面使用图片资源，**当** 加载与展示，**那么** 图片来自本地 res/drawable*、尺寸与设计一致、无远程 URL 引用

---

### 用户故事 2 - Security 模块 Compose 迁移（优先级：P2）

将 Security 类 Entry（PIN、持卡人信息输入、管理员密码等）的 UI 实现转为 Compose，按设计规范还原。

**优先级理由**：Security 涉及敏感输入，需确保输入掩码、安全键盘、生命周期与设计规范一致。

**独立测试**：PIN、持卡人信息、管理员密码等 Entry 在 Compose 下可正常输入；敏感数据不泄露；布局与设计规范一致。

**验收场景**：

1. **假如** Security 模块已迁移为 Compose，**当** 进入 PIN 输入、持卡人信息输入等流程，**那么** 输入正常、掩码正确、结果正确回传
2. **假如** Design Tokens 已提取，**当** 人工对比设计稿与实现截图验收，**那么** 布局、字体、颜色、交互状态与 Design Tokens 一致；Security 特有要求（如安全键盘、掩码样式）与设计稿一致
3. **假如** 支付敏感数据，**当** 输入与展示，**那么** 符合 constitution 原则（禁止明文日志、展示使用掩码）

---

### 用户故事 3 - Confirmation 模块 Compose 迁移（优先级：P3）

将 Confirmation 类 Entry（各类 Confirm*Fragment）的 UI 实现转为 Compose，按设计规范还原。

**优先级理由**：确认页是交易流程的关键节点，需确保用户可清晰理解待确认内容并完成操作。

**独立测试**：各类确认 Entry 在 Compose 下可正常展示、按钮可点击、结果正确回传；布局与设计规范一致。

**验收场景**：

1. **假如** Confirmation 模块已迁移为 Compose，**当** 进入金额确认、持卡人确认等流程，**那么** 内容展示正确、确认/取消操作生效
2. **假如** Design Tokens 已提取，**当** 人工对比设计稿与实现截图验收，**那么** 布局、字体、颜色、间距与 Design Tokens 一致

---

### 用户故事 4 - POSLink 模块 Compose 迁移（优先级：P4）

将 POSLink 类 Entry（ShowItemFragment、ShowTextBoxFragment、ShowDialogFragment 等）的 UI 实现转为 Compose，按设计规范还原。

**优先级理由**：POSLink 负责展示与对话框，是 Entry 流程中的展示层核心。

**独立测试**：ShowItem、ShowTextBox、ShowDialog 等 Entry 在 Compose 下可正常展示与交互；布局与设计规范一致。

**验收场景**：

1. **假如** POSLink 模块已迁移为 Compose，**当** 进入展示或对话框 Entry，**那么** 内容正确展示、交互正常
2. **假如** Design Tokens 已提取，**当** 人工对比设计稿与实现截图验收，**那么** 布局、字体、颜色与 Design Tokens 一致

---

### 用户故事 5 - Option 模块 Compose 迁移（优先级：P5）

将 Option 类 Entry（各类 Select*Fragment）的 UI 实现转为 Compose，按设计规范还原。

**优先级理由**：Option 负责选项选择，是配置与流程分支的入口。

**独立测试**：Select* 类 Entry 在 Compose 下可正常展示选项、选择生效、结果正确回传；布局与设计规范一致。

**验收场景**：

1. **假如** Option 模块已迁移为 Compose，**当** 进入交易类型、货币、商户等选择流程，**那么** 选项正确展示、选择生效
2. **假如** Design Tokens 已提取，**当** 人工对比设计稿与实现截图验收，**那么** 布局、字体、颜色与 Design Tokens 一致

---

### 用户故事 6 - Information 模块 Compose 迁移（优先级：P6）

将 Information 类 Entry（DisplayTransInfoFragment、DisplayApproveMessageFragment 等）的 UI 实现转为 Compose，按设计规范还原。

**优先级理由**：Information 负责信息展示，是流程收尾与结果展示。

**独立测试**：Display* 类 Entry 在 Compose 下可正常展示交易信息、审批信息等；布局与设计规范一致。

**验收场景**：

1. **假如** Information 模块已迁移为 Compose，**当** 进入交易信息展示、审批信息展示等流程，**那么** 内容正确展示
2. **假如** Design Tokens 已提取，**当** 人工对比设计稿与实现截图验收，**那么** 布局、字体、颜色与 Design Tokens 一致

---

### 边界情况

- **页面还原度第一**：当实现便利性与还原度冲突时，优先保证还原度；不得为简化实现而牺牲布局、字体、颜色、间距的准确性
- **Activity 调起**：迁移后 Compose 页面须通过 manifest 中声明的 Activity 正常启动；宿主应用通过 Intent 调起时，launchMode、taskAffinity、singleTask 等配置须与 XML/View 版本一致
- **图片资源**：切图须落本地；多密度（hdpi/xhdpi/xxhdpi）按设计规范配置；大图考虑内存与加载性能
- **设计文档无法被 AI 解析**：POSLinkUI-Design_V1.03.00.docx 为 .docx 格式，AI 无法直接解析；布局、字体、颜色、间距等关键数值需从文档中手工提取并记录于 spec 或 design-tokens 中
- **蓝湖等设计工具**：若使用蓝湖，资源须落本地，禁止运行时代码引用远程 URL；参考 lanhu-mcp-workflow 规则
- **Neptune/POSLink 接口不变**：Compose 仅替换 UI 层，与 Neptune/POSLink 的 EntryRequest/EntryResponse 契约保持不变
- **渐进式迁移与去 XML**：每个模块迁移后须可独立测试。**过渡期**允许 **少量 Compose 宿主**（见 **FR-019**）：优先 **单宿主 + `NavHost`**，而非「每 Entry 一个 `ComposeView`」；若短期仍多 Fragment，应有收敛至 **单宿主导航** 的里程碑。**模块收尾**须移除该模块已无引用的 `res/layout` 与相关 XML inflate 路径。**全量收尾**须完成 Entry 相关布局 XML 清零盘点（允许 manifest / 非 UI 必需 values 等保留，见 FR-005）。`EntryActivity`、Intent、广播与母版 §7 一致，调起方式不变；**界面承载**可从「多 Fragment」演进为 **Nav 图**，但 **action→界面** 映射须等价（FR-M001）
- **状态机合规**：页面跳转须经状态机合法迁移；超时、取消、失败须有确定终态与回首页策略
- **机型与副屏适配**：**录入源**为 [`device-profiles.md`](device-profiles.md)。须：**(1)** `DeviceLayoutSpec`（或等价）+ `CompositionLocal`/Provider 注入；**(2)** **配置层**可固化机型/profile 键 → 规格映射（Kotlin 数据、资源 JSON 等均可），**禁止**在业务 Composable 内写 `if (Build.MODEL == …)` + 散落 `NN.dp`；**(3)** **P0** 五档（见 device-profiles §1）须达到 **SC-011**；**(4)** **副屏**见 **FR-018**，与 `TransactionPresentation` 等现有路径一致并释放资源

## 需求 *（必填）*

### 功能需求

- **FR-001**：**页面还原度第一**；Compose 实现必须按 Design Tokens 与 POSLinkUI-Design_V1.03.00 设计规范还原；布局、字体、颜色、间距与 Design Tokens 一致；还原度优先于实现便利性
- **FR-002**：通过 AndroidManifest.xml 中 Activity 的配置，Compose 页面必须能被正常调起；Intent 路由、launchMode、taskAffinity 等配置与迁移前行为一致
- **FR-003**：图片须落本地（res/drawable 或 res/drawable-nodpi）；禁止运行时代码引用远程 URL；图片尺寸、缩放、占位与 Design Tokens/设计稿一致；必要时使用语义化命名（如 `detect_` 前缀）
- **FR-004**：验收须包含：布局还原、主题一致性（与 Design Tokens 一致）、交互状态（按下/禁用/聚焦/错误）与设计稿一致。**还原度验收方式**：人工对比设计稿与实现截图，逐页/逐模块验收
- **FR-005（修订）**：**目标态为 Entry UI 布局层去 XML**：以 Compose 实现各 Entry 界面；**不得**在新增或维护的 Entry 路径上依赖 `res/layout` 承载页面结构（`setContentView(R.layout.*)`、`inflate` 布局根等）。**过渡期**：允许 **每窗口极少数** `ComposeView` / `setContent` 根，**其内**用 **`NavHost` + `composable`** 承载多屏（**FR-019**），避免为每个 action 再嵌一层 `ComposeView`；若仍暂存多 `Fragment`，每 Fragment 至多一个 Compose 根并计划收敛。每完成一模块须删除已无引用的 layout 资源。**保留** `AndroidManifest.xml` 及母版要求的宿主集成配置；不新建替代 `EntryActivity` 的宿主入口 Activity，除非母版/宿主契约另有书面变更。**收尾**须产出「已删除 layout / 仍保留 XML 及原因」清单备查
- **FR-006**：迁移顺序为 Text → Security → Confirmation → POSLink → Option → Information；每个模块迁移后须可独立测试
- **FR-007**：Neptune/POSLink 接口契约不变；EntryRequest、EntryResponse、EntryExtraData 等调用方式保持不变
- **FR-008**：支付敏感数据（PAN、PIN block、CVV 等）禁止明文日志；展示须使用掩码；符合 constitution 原则
- **FR-009**：设计文档无法被 AI 解析时，关键数值（布局、字体、颜色、间距）须手工提取并记录
- **FR-010**：若使用蓝湖等设计工具，切图与资源须落本地，禁止运行时代码引用远程 URL
- **FR-011**：每个模块迁移后，Entry 流程可独立验证且行为与迁移前一致
- **FR-012**：主题：Compose 主题须与 Design Tokens 一致；设计规范未定义深色模式，本迁移不强制实现深色模式
- **FR-013**：屏幕兼容：不同屏幕尺寸与密度下布局不崩溃、关键内容可见。横竖屏切换本迁移不强制支持，可后续迭代
- **FR-014**：性能：页面首次渲染可接受为人工主观验收（与 XML/View 版本响应相当，无肉眼可感知的明显卡顿）。可访问性（contentDescription 等）本迁移不强制实现，可后续迭代
- **FR-015**：**Fragment 结构扁平化**：若多屏仅差标题、提示文案、校验规则、按钮文案等，应通过 **参数化 Screen** 或共享 ViewModel/配置对象实现，**避免**仅为差异新增 `Fragment` 子类；确需子类时须在 PR/任务中注明理由（如安全隔离、完全不同的交互范式）
- **FR-016**：**Compose 复用与统一入口**：提供项目级可复用 UI 构造块（如标准顶栏、主按钮、列表行、表单行、对话框骨架）及 **统一根组合**（自动应用主题与常用 Modifier 约定）；业务 Screen 优先通过这些 API 组合实现，Design Tokens 的读取应经 **集中封装**（避免每个 Screen 直接散落原始 dp/sp 与重复 `MaterialTheme` 拼装）
- **FR-017**：**机型/屏幕适配（分阶段）**：**(1)** 提供 `DeviceLayoutSpec`（或等价）与注入点；**(2)** 在 **配置层**维护 profile（键 → 边距阶梯、按钮最小高度、可选双列阈值等），数据可与 [`device-profiles.md`](device-profiles.md) 对齐并版本化；**(3)** **禁止**业务 Screen 写死全局 dp/sp；**允许**仅在 **T030 提供的预制件 / Token 映射函数** 内出现由规格推导的 `Dp`；**(4)** **P0** 机型须可切换策略而**不复制整屏**；**(5)** 设计稿 **基准机型** 用于像素级还原（FR-001），P0 其余机型满足 **SC-011** 功能与触控布局要求  
- **FR-018**：**副屏（第二显示设备）**：在 **A920Pro（PCI7）** 等双屏设备上，副屏 UI 须使用与主屏一致的 **主题与规格管线**（可扩展 `SecondaryDisplaySpec` 或 `DeviceLayoutSpec` 子结构）；**禁止**在 `Presentation` 内独立硬编码一套与主屏无关的 magic 尺寸；须验收副屏展示、与主屏状态同步及 **dismiss/泄漏** 与 `EntryActivity` 现有行为一致  
- **FR-019**：**Navigation Compose 优先（最小化 `ComposeView` 宿主）**：Entry 流程须在 **尽可能少的 Compose 根** 上完成屏间切换——**优先**采用 **`NavHost(navController, startDestination) { composable(route) { … } }`**（Jetpack Navigation Compose）。路由须 **稳定、可枚举、可测试**（推荐使用 **`sealed class` / `enum` / `object` 常量** 表示 route，示例名 **`TransactionRoute`** 仅作说明，实际类名以仓库为准）。**须**建立并维护 **`Intent` action（`com.pax.us.pay.action.*` 等）及 Extras 键 → `route` / 导航参数** 的**单点映射**（代码注册表 + 必要时文档），使母版 **FR-M001**（每条已注册 action 落到正确界面）在「Nav 承载」形态下仍成立；`**onNewIntent**`（`singleTop`）须能导航至正确 destination，且 **返回栈、系统返回、取消、超时、失败终态** 与 **状态机**、`.cursor/rules/payment-project-hard-problems.mdc` 一致。**禁止**在无架构理由时堆叠多个并列 `ComposeView` 各跑独立树；副屏（FR-018）可单独树，但应复用 Theme/规格 API

### 关键实体

- **Text 模块**：AmountFragment、ReferenceNumberFragment、ZipcodeFragment、ATextFragment、ANumTextFragment 等。**完整类清单**见 [class-inventory.md](class-inventory.md)。
- **Security 模块**：PINFragment、InputAccountFragment、EnterCardAllDigitsFragment、AdministratorPasswordFragment 等；敏感屏审查见 [checklists/security-sensitive-screens.md](checklists/security-sensitive-screens.md)。
- **Confirmation 模块**：AConfirmationFragment、ConfirmTotalAmountFragment、ConfirmCardProcessResultFragment 等
- **POSLink 模块**：ShowItemFragment、ShowTextBoxFragment、ShowDialogFragment、ShowMessageFragment 等
- **Option 模块**：AOptionEntryFragment、SelectTransTypeFragment、SelectCurrencyFragment 等
- **Information 模块**：DisplayTransInfoFragment、DisplayApproveMessageFragment 等
- **设计规范**：POSLinkUI-Design_V1.03.00.docx；为权威 UI 来源
- **AndroidManifest.xml**：Activity 声明、Intent Filter、launchMode、taskAffinity 等；迁移后须保证 Compose 页面可被正常调起
- **图片资源**：res/drawable* 下切图；须落本地、语义化命名、多密度适配

### Spec 细化说明（类级可执行）

本 spec 已按 [doc/SPEC-REFINEMENT-STEPS.md](../../doc/SPEC-REFINEMENT-STEPS.md) 细化到类级，产出如下：

| 产出 | 路径 | 用途 |
|------|------|------|
| 类清单 | [class-inventory.md](class-inventory.md) | 六类模块完整 Fragment 列表，按迁移顺序分组 |
| 基类 Compose 策略 | [contracts/base-class-compose-strategy.md](contracts/base-class-compose-strategy.md) | 宿主边界、**`NavHost`（FR-019）**、过渡 ComposeView、与 **T035** 一致 |
| action→route 映射 | [contracts/entry-navigation-routes.md](contracts/entry-navigation-routes.md) | **FR-019** / **T035**；母版 FR-M001 在 Nav 形态下的对照表 |
| 修改模式 | [modification-patterns.md](modification-patterns.md) | 按类类型定义迁移方式；**§0 导航优先** |
| 敏感屏审查 | [checklists/security-sensitive-screens.md](checklists/security-sensitive-screens.md) | PINFragment、InputAccountFragment 等 7 个必查项 |
| 类级验收 | [acceptance-per-class.md](acceptance-per-class.md) | 逐类布局还原、行为正确、掩码验收勾选 |
| 设备与 P0 列表 | [device-profiles.md](device-profiles.md) | 机型参数、P0、副屏说明、与 Token 映射关系 |

**自检**：见 [doc/SPEC-REFINEMENT-CHECKLIST.md](../../doc/SPEC-REFINEMENT-CHECKLIST.md)

## Design Tokens（设计规范关键数值）

**来源**：POSLinkUI-Design_V1.03.00.docx（.docx 格式无法被 AI 解析，需由人工从设计文档提取后填入）

### 待提取项清单

以下 token 需从设计文档手工提取并记录于 `specs/002-compose-migration/design-tokens.md` 或 `app/src/main/res/values/` 下，供 Compose Theme 与验收引用：

| Token 名称 | 说明 | 单位/格式 | 提取后填入 |
|------------|------|-----------|------------|
| PrimaryColor | 主色 | hex | |
| SecondaryColor | 次色 | hex | |
| BackgroundColor | 背景色 | hex | |
| TitleTextSize | 标题字号 | sp | |
| BodyTextSize | 正文字号 | sp | |
| CaptionTextSize | 辅助文字字号 | sp | |
| CardPadding | 卡片内边距 | dp | |
| ScreenPadding | 屏幕边距 | dp | |
| CornerRadius | 圆角半径 | dp | |
| ButtonHeight | 按钮高度 | dp | |

### 引用约定

- 验收时「与设计规范一致」指与上述 token 在 **基准设计机型** 上的数值一致（见 FR-001）；其他 P0 机型通过 **FR-017 映射** 做阶梯调整，验收见 **SC-011**  
- 实现时：**语义 token**（如 `ScreenPadding`、`ButtonHeight`）优先定义为 **档位或函数**（由 `DeviceLayoutSpec` / `sw` 解析），避免全工程单一固定 `dp` 与窄屏/横屏事实冲突  
- 若设计文档暂未提供某 token，可标注「待补充」；不得为简化实现而猜测数值

## 模块验收清单

每类模块迁移完成后，须按以下清单逐项验收；tasks.md 中的 T010、T014、T017、T021、T024、T026 可直接引用本清单。**类级验收勾选**见 [acceptance-per-class.md](acceptance-per-class.md)。

| 模块 | 必须验收的 Fragment 列表 | 独立测试步骤 |
|------|--------------------------|--------------|
| Text | AmountFragment、ATextFragment、ANumTextFragment、ReferenceNumberFragment、ZipcodeFragment、TipFragment、TableNumberFragment、ExpiryFragment 等 | 进入金额输入 → 输入有效值 → 提交 → 验证回传结果；进入参考号/邮编等 Entry → 输入 → 提交 → 验证 |
| Security | PINFragment、InputAccountFragment、EnterCardAllDigitsFragment、EnterCardLast4DigitsFragment、AdministratorPasswordFragment、ManageInputAccountFragment 等 | 进入 PIN 输入 → 输入 → 验证掩码与回传；进入持卡人信息输入 → 验证掩码；验证无明文日志 |
| Confirmation | AConfirmationFragment、ConfirmTotalAmountFragment、ConfirmCardProcessResultFragment、ConfirmBalanceFragment、ConfirmPrintFpsFragment、ConfirmBatchCloseFragment 等 | 进入金额确认 → 确认/取消 → 验证回传；进入持卡人确认等流程 |
| POSLink | ShowItemFragment、ShowTextBoxFragment、ShowDialogFragment、ShowMessageFragment、ShowInputTextBoxFragment、ShowDialogFormFragment、ShowSignatureBoxFragment、ShowThankYouFragment 等 | 进入展示 Entry → 验证内容正确；进入对话框 Entry → 交互 → 验证回传 |
| Option | AOptionEntryFragment、SelectTransTypeFragment、SelectCurrencyFragment、SelectMerchantFragment、SelectTaxReasonFragment、SelectEdcTypeFragment、SelectLanguageFragment 等 | 进入交易类型/货币/商户选择 → 选择选项 → 验证回传 |
| Information | DisplayTransInfoFragment、DisplayApproveMessageFragment 等 | 进入交易信息展示、审批信息展示 → 验证内容正确展示 |

**验收方式**：人工对比设计稿与实现截图，逐页/逐模块；执行 Entry 流程验证行为与迁移前一致。Security 模块须额外参考 [checklists/security-sensitive-screens.md](checklists/security-sensitive-screens.md)。

## 范围外

- **Java→Kotlin 迁移**：由 [001-kotlin-migration](../001-kotlin-migration/spec.md) 负责；本迁移假定业务逻辑层已为 Kotlin 或兼容
- **Neptune/POSLink SDK 变更**：不修改 SDK 接口或契约

## 假设前提

- 设计规范 POSLinkUI-Design_V1.03.00.docx 存在且可被人工查阅；关键数值可手工提取；设计规范未定义深色模式，本迁移不强制实现深色模式
- 项目已具备 Jetpack Compose 依赖；可渐进式引入 Compose 与 XML/View 共存
- **分支与合并**：001 与 002 为两个独立 feature 分支。**推荐**先合并 001 到 main，再从 main 创建 002 分支（002 在 Kotlin 代码上做 Compose 迁移，避免与 001 的 Java→Kotlin 变更冲突）。若并行开发，002 应基于 `001-kotlin-migration` 分支创建，而非 main。详见 [doc/SPEC-KIT-使用步骤.md](../../doc/SPEC-KIT-使用步骤.md) 第 5.5 节。
- Neptune/POSLink 接口保持不变；仅 UI 层从 XML/View 替换为 Compose
- **Compose 与去 XML**：过渡期内可在 Fragment 中托管 `ComposeView`；目标态为 Entry 布局不依赖 `res/layout`（见 FR-005）；不新建替代 `EntryActivity` 的宿主入口 Activity；manifest 与调起方式与母版一致
- 页面还原度为最高优先级；与实现便利性冲突时优先还原度
- AndroidManifest.xml 中 Activity 配置在迁移时需同步更新，确保 Compose 页面可被 Intent 正常调起
- 若本 spec 的 **FR-015（Fragment 扁平化）** 与 `.specify/memory/constitution.md` 中既有「Fragment 实现模式」表述冲突，须在实施前通过 **宪章 MINOR 修订** 或 **书面豁免** 对齐，避免执行期口径不一  
- 若采用 **FR-019（`NavHost`）** 减少 `Fragment` 数量，**不豁免**母版 **FR-M001**：仍须保证每条 manifest 注册的 action 进入 **正确的 Nav destination**；若母版字面仅写「Fragment」，以 **界面等价 + 映射表** 为准，并视需要更新母版措辞

## 成功标准 *（必填）*

### 可衡量成果

- **SC-001**：所有六类模块（Text、Security、Confirmation、POSLink、Option、Information）的 Entry 流程可独立验证且行为与迁移前一致
- **SC-002**：**页面还原度第一**；布局、字体、颜色、间距与 Design Tokens 及 POSLinkUI-Design 设计规范一致；验收通过人工对比设计稿与实现截图、逐页/逐模块验收
- **SC-003**：通过 manifest.xml 中 Activity 配置，Compose 页面可被正常调起；Intent 路由与迁移前一致
- **SC-004**：图片资源落本地；无远程 URL 引用；尺寸与 Design Tokens/设计稿一致
- **SC-005**：交互状态（按下、禁用、聚焦、错误）与 Design Tokens/设计稿一致
- **SC-006**：支付敏感数据符合 constitution 原则；无明文日志、展示使用掩码
- **SC-007**：Neptune/POSLink 接口契约不变；Entry 流程与宿主应用交互正常
- **SC-008**：Entry 相关 **已无引用的** `res/layout` 及 XML 视图层次已移除；保留的 XML（manifest 等）有文档说明（可与收尾任务或 `quickstart.md` 附录同步）
- **SC-009**：可观察到 **统一 Compose UI 入口**（FR-016）：新增典型 Screen 主要组合共享组件而非从零搭建；关键间距/色型来自 Token 封装层
- **SC-010**：**设备适配注入点**（FR-017）已实现：**配置层**可加载/注册 profile（与 [`device-profiles.md`](device-profiles.md) 一致），默认行为可测；在不推翻 Screen 结构前提下可增删机型键  
- **SC-011**：在 **P0** 五类机型（见 `device-profiles.md` §1：`A3700`、`A920MAX`、`A920Pro` 含副屏、`A80S` 档、`A35`）上，主屏 Entry 流程 **无截断、无关键控件重叠、主要按钮可达**（人工 + 截图）；间距/控件尺寸均来自 **规格 API**，抽查无「业务文件内大量字面量 dp」  
- **SC-012**：**A920Pro（PCI7）副屏**：有第二显示设备时，`Presentation` 路径展示正确、与主屏协同合理，**无**明显错位与泄漏（与 `EntryActivity` dismiss 行为一致）  
- **SC-013**：**导航等价性（FR-019）**：在采用 `NavHost` 后，典型 **singleTop + `onNewIntent`** 多步进件、返回与取消路径下，**界面与业务结果**与迁移前 **语义一致**（人工场景 + 可追溯 `route`/日志）；**不存在**「每屏一个无协调的 `ComposeView` 根」作为长期目标态
