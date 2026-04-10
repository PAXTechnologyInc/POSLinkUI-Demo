# 产品母版规格：POSLinkUI-Demo

**编号**：`000-poslinkui-project-master`（母版；以后新增的特性规格可引用本文，本文不依赖其他编号 spec）  
**创建日期**：2026-03-25  
**状态**：基线草稿  
**设计输入**：`POSLinkUI-Design_V1.03.00.docx`（与 UI 常量包 `com.paxus.ui:constant` **1.03.00** 线对齐）

---

## 1. 文档定位

本文描述 **POSLinkUI-Demo** 的工程边界、目录结构、与支付宿主（如 BroadPOS）的集成方式，以及 **POSLinkUI-Design V1.03.00** 在实现中的权威关系。以后编写的特性规格若引用本文，宜对齐本文的**结构域划分**与**集成契约**；若与母版冲突，应修订母版并注明变更原因。

**设计文档说明**：`POSLinkUI-Design_V1.03.00.docx` 在本仓库/当前环境中**未保证可被自动解析**。本文从代码与工程文档归纳行为与结构；**视觉与交互细则**（色板、字号、间距、组件状态等）须从该 docx **人工提取**并落到项目内的 **Design Tokens 表**（如 `app/.../ui/theme/DesignTokens.kt` 及你方在 `doc/` 或 `specs/` 下维护的 Markdown 表）中。若将 docx 放入仓库或粘贴「设计说明摘要」，可在本文 **§9.3** 增补对照表。

---

## 2. 产品概述

### 2.1 是什么

POSLinkUI-Demo 是 **POSLink UI** 的 Android 演示与实现载体：通过 **显式 Intent** 由宿主应用按 `com.pax.us.pay.action.*` 调起 `**EntryActivity`**，以 **Fragment**（及部分 **Jetpack Compose**）呈现进件、确认、选项、展示等界面；通过 **广播** 与宿主交换 **下一跳 / 接受 / 拒绝** 及状态、安全区域等事件。契约字段与枚举来自 `**com.paxus.ui:constant`**（Entry / Status 等）。

### 2.2 不是什么

- 非独立收单核心：不替代宿主的交易状态机与联机逻辑。  
- 非全量 POSLink UI 清单的强制实现：宿主可按需裁剪 manifest 中 `activity-alias`（参见 `README.md` 与 product flavor：`Shift4`、`FDRCNV` 等）。

### 2.3 关键利益相关方


| 角色               | 关注点                                        |
| ---------------- | ------------------------------------------ |
| 宿主应用（BroadPOS 等） | Intent action、Extras、广播协议、包可见性             |
| 终端用户             | 流程可读、输入校验明确、失败可重试                          |
| 维护方              | Kotlin/Compose 演进、与 constant 包版本对齐、安全与日志规范 |


---

## 3. 版本与依赖对齐


| 项                  | 说明                                                                         |
| ------------------ | -------------------------------------------------------------------------- |
| 应用 `versionName`   | `V1.03.00_` + 构建日期（`app/build.gradle`）                                     |
| UI 常量包             | `com.paxus.ui:constant:1.03.00T_*`（与 **Design / POSLink UI 1.03.00** 线一致）  |
| 设计规范               | `POSLinkUI-Design_V1.03.00.docx` 为 **UI 视觉与交互**的权威来源（在代码与 token 表落地前以文档为准） |
| minSdk / targetSdk | 22 / 31（`app/build.gradle`）                                                |


---

## 4. 仓库顶层结构

```text
POSLinkUI-Demo/
├── app/                          # Android 应用模块
│   ├── src/main/                 # 默认源码与资源
│   ├── src/Shift4/               # 产品变体：manifest 等
│   ├── src/FDRCNV/
│   └── build.gradle
├── doc/                          # 工程文档（含 adb Intent 命令等）
├── specs/                        # 功能/母版规格（本文位于 000）
├── .cursor/                      # Cursor 规则与 Skills
├── .specify/                     # Specify 模板
├── gradle/
├── README.md
└── settings.gradle, build.gradle
```

---

## 5. 应用模块结构（`app/src/main`）

### 5.1 Java/Kotlin 包：`com.paxus.pay.poslinkui.demo`


| 路径                        | 职责                                                                       |
| ------------------------- | ------------------------------------------------------------------------ |
| `MainActivity`            | 桌面入口（安装/卸载便利），非交易宿主调起路径                                                  |
| `DemoApplication`         | Application；Hilt 等全局初始化                                                  |
| `entry/EntryActivity`     | **唯一**承载几乎全部 Entry 流程的 Activity：`singleTop`、`onNewIntent` 串联多步 Entry     |
| `entry/BaseEntryFragment` | Entry Fragment 基类：参数加载、Compose/XML 双路径、`EntryRequest`/`EntryResponse` 发送 |
| `entry/text/`             | 文本/金额/数字类进件（amount、tip、reference、fleet、fsa…）                             |
| `entry/security/`         | 安全相关（如持卡人账号、PIN、全卡号等）                                                    |
| `entry/confirmation/`     | 确认类界面（余额、小费调整、签购单、批次等）                                                   |
| `entry/option/`           | 选项类（AID、交易类型、EDC 等 `Select*`）                                            |
| `entry/poslink/`          | POSLink 展示/对话框（ShowMessage、ShowTextBox、表单等）                              |
| `entry/information/`      | 信息展示（与 manifest 中部分 `DETAILS` 类 action 对应）                               |
| `entry/signature/`        | 签名采集                                                                     |
| `entry/task/`             | 与调度相关的任务抽象                                                               |
| `status/`                 | 非 Entry 的状态页（如交易完成状态等）                                                   |
| `view/`                   | 可复用 View 组件（如 `TextField`、`SelectOptionsView`、动画相关等）                     |
| `viewmodel/`              | 如副屏信息 `SecondScreenInfoViewModel`                                        |
| `ui/theme/`               | Compose 主题与 **Design Tokens** 代码化（`DesignTokens.kt`、`Theme.kt`）          |
| `utils/`                  | `EntryRequestUtils`、`TaskScheduler`、`Logger`、`BundleMaker`、接口过滤等         |


### 5.2 资源与配置

- `res/layout`、`res/values`、`res/drawable*`：XML 布局与主题（如 `POSLinkUIPastel`）、本地图片。  
- `AndroidManifest.xml`：**约 100 个** `activity-alias`，均指向 `.entry.EntryActivity`，按 `com.pax.us.pay.action.*` 与 `com.pax.us.pay.ui.category.*` 声明。  
- 详细 adb 调起示例见 `doc/adb-entry-intent-commands.md`。

---

## 6. Manifest 中的 UI 类别与代码域映射

宿主通过 **Intent action** 调起界面；manifest 使用 `**com.pax.us.pay.ui.category.*`** 归类。与实现目录的**典型**对应关系如下（个别 alias 命名与 category 可能交叉，以 manifest 为准）。


| UI category（manifest）  | 代码域（主要包路径）               | 说明                                                                                         |
| ---------------------- | ------------------------ | ------------------------------------------------------------------------------------------ |
| `TEXT`                 | `entry/text/`**          | 金额、小费、参考号、Fleet/FSA 等                                                                      |
| `SECURITY`             | `entry/security/**`      | PIN、账号输入等                                                                                  |
| `CONFIRMATION`         | `entry/confirmation/**`  | 各类 Confirm / Check / Supplement 等                                                          |
| `CONFIRM_UPLOAD_TRANS` | `entry/confirmation/**`  | 上传类确认（独立 category）                                                                         |
| `OPTION`               | `entry/option/**`        | `Select*` 选项页                                                                              |
| `POSLINK`              | `entry/poslink/**`       | 通用展示与对话框                                                                                   |
| `DETAILS`              | `entry/information/**` 等 | 如 `DISPLAY_TRANS_INFORMATION`、`DISPLAY_APPROVE_MESSAGE`（manifest 中 alias 名含 `INFORMATION`） |
| `SIGNATURE`            | `entry/signature/**`     | 签名                                                                                         |


**说明**：若按模块分批改造 UI（如逐步引入 Compose），可采用与上表一致的依赖顺序：**Text → Security → Confirmation → POSLINK → Option → Information（DETAILS）**，以降低组件复用与回归成本；具体是否执行、如何验收由后续特性规格单独约定。

---

## 7. 运行时与集成契约（摘要）

### 7.1 Activity 行为

- `**EntryActivity`**：`exported=true`，`launchMode=singleTop`，`autoRemoveFromRecents=true` 等（见 `README.md` 与 manifest）。  
- 新一步 Entry 通常通过 `**onNewIntent**` 进入；需在回调中取消进行中的任务/协程（代码中已处理 scheduler 与 task group）。

### 7.2 与宿主的对话方式（概念）

- **启动**：宿主 `startActivity`，`action` 为 `com.pax.us.pay.action.<ENTRY_NAME>`，Extras 遵循 `EntryExtraData` / `EntryRequest` 等 constant 定义。  
- **结果与下一跳**：Demo 发送请求类广播（如 **下一跳**、**安全区域**、**PIN 键盘布局** 等）；宿主响应 **接受 / 拒绝**（如 `ACTION_ACCEPTED` / `ACTION_DECLINED`）。拒绝时常以 Toast 提示用户重试。  
- **状态**：宿主可下发状态 action（如检卡灯、PIN 输入中、拔卡等），由 `EntryActivity` 侧接收并驱动 UI 或副屏。

具体常量名与字段以 `**com.paxus.ui:constant`** 为准；调试命令见 `doc/adb-entry-intent-commands.md`。

### 7.3 Fragment 基类约定

`BaseEntryFragment` 约定：从 `EntryRequest.PARAM_ACTION` 等加载参数；用户确认后发送下一跳；收到 **declined** 需提示且可重试；收到 **accepted** 后不再对同一结果发送 abort。支持在 Fragment 内 **ComposeView** 与 XML 并存迁移路径。

---

## 8. 横切需求（全项目适用）

### 8.1 安全与合规

- 禁止日志打印 PAN、PIN block、磁道等敏感明文；展示遵循掩码与项目安全规范（见 `.cursor/rules/payment-project-hard-problems.mdc`）。

### 8.2 可观测性

- 关键路径使用统一日志封装（如 `Logger`），便于按 action / 广播排查（参见 README 中日志阅读顺序）。

### 8.3 线程与生命周期

- UI 更新在主线程；异步结果回到 Fragment/Activity 时做生命周期校验（见 `.cursor/rules/android-core.mdc`）。

### 8.4 文档与构建

- public API 保持 KDoc；文档生成：`./gradlew :app:dokkaGeneratePublicationHtml`。  
- 常规构建：`./gradlew assembleDebug`；Lint：`./gradlew lint`。

---

## 9. 设计规范 V1.03.00（与 docx 的关系）

### 9.1 权威层级

1. **行为与协议**：以 `com.paxus.ui:constant` + 宿主联调为准。
2. **视觉与交互**：以 `**POSLinkUI-Design_V1.03.00.docx`** 为准；实现通过 `**ui/theme/DesignTokens.kt**`（及项目内维护的 token 文档）与之一致。
3. **代码现状**：XML/Compose 混合时，**目标态**仍以设计文档 + tokens 为验收基准。

### 9.2 建议从设计文档提取并落入 spec/代码的条目（检查清单）

- 色板（主色/辅色/背景/错误/成功）  
- 字号、字重、行高（标题/正文/辅助）  
- 间距与圆角（屏幕边距、卡片、列表项、按钮高度）  
- 主要组件状态（默认/聚焦/禁用/错误）  
- 插图与图标规范（本地资源命名、密度）

### 9.3 设计文档摘要（人工维护区）

> 将 `POSLinkUI-Design_V1.03.00.docx` 中的**章节标题与关键结论**粘贴于此表，便于 AI 与新人无需打开 docx 即可对齐范围。


| 设计章节 | 摘要 / 关键数值引用 |
| ---- | ----------- |
| （待填） | （待填）        |


---

## 10. 用户旅程（母版级）

### 10.1 用户故事 P1 - 宿主可完整驱动 Entry 流程

**独立测试**：宿主按文档用 Intent 依次调起多个 action，Demo 能展示对应 Fragment、发送广播并正确处理接受/拒绝。

**验收要点**：

1. **假如** manifest 已注册某 `com.pax.us.pay.action.`*，**当** 宿主以正确 Extras 启动，**那么** 对应界面展示且无崩溃。
2. **假如** 用户完成输入并确认，**当** Demo 发送下一跳，**那么** 宿主可返回接受或拒绝；拒绝时用户可感知原因并重试。
3. **假如** 宿主下发状态广播，**当** 处于相关 Entry，**那么** UI 与副屏行为符合 constant 约定（与当前实现一致）。

### 10.2 用户故事 P2 - 演示工程可独立安装与入口调试

**独立测试**：安装 APK 后可通过 `MainActivity` 管理演示；开发者可用 adb 文档调起各 Entry。

---

## 11. 功能需求（母版级 FR）

- **FR-M001**：系统必须将每个已注册的 Entry `action` 路由到 `EntryActivity` 并由其实例化正确的 Fragment（或与 constant 一致的 UI）。  
- **FR-M002**：系统必须按 POSLink 协议发送/接收广播，且 Extras 与 `com.paxus.ui:constant` 一致。  
- **FR-M003**：实现必须满足 **V1.03.00** 设计规范中已提取到 Design Tokens 的条目；未提取项须在变更说明或对应特性规格中标注「待对齐设计文档」。  
- **FR-M004**：禁止在日志中输出支付敏感明文；敏感界面展示须掩码或安全控件。  
- **FR-M005**：`EntryActivity` 在 `onNewIntent`、销毁等路径上必须释放监听、任务与副屏资源，避免泄漏与重复回调。

---

## 12. 成功标准（母版级 SC）

- **SC-M001**：`./gradlew assembleDebug` 与主要 flavor 构建可通过（与 CI/本地约定一致）。  
- **SC-M002**：README 所述典型交易日志路径（Intent → Fragment → 广播）可复现。  
- **SC-M003**：已登记的 Design Tokens 与 **POSLinkUI-Design V1.03.00** 一致（人工抽检设计稿与截图）。

---

## 13. 术语表


| 术语            | 含义                                         |
| ------------- | ------------------------------------------ |
| Entry         | 宿主发起的一步用户交互（金额、确认、选项等）                     |
| 宿主            | 调起 POSLinkUI 的支付应用（如 BroadPOS）             |
| constant 包    | `com.paxus.ui:constant`，Entry/Status 等协议定义 |
| Design Tokens | 从设计文档提取并代码化的颜色、字号、间距等                      |


---

## 14. 修订记录


| 日期         | 变更                                                                                      |
| ---------- | --------------------------------------------------------------------------------------- |
| 2026-03-25 | 初版：结合仓库代码、manifest、README 与 V1.03.00 设计线对齐；设计 docx 细节待 §9.3 人工补全；母版为自洽基线，不引用其他编号特性 spec |


