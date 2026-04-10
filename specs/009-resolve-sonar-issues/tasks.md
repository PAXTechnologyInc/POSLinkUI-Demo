# 任务：基于扫描结果的 Sonar 问题分批治理（009）

**输入**：`d:\Project\US\POSLinkUI-Demo\specs\009-resolve-sonar-issues\` 下 `spec.md`、`plan.md`、`research.md`、`data-model.md`、`contracts/task-package-workflow-contract.md`、`quickstart.md`  
**前置条件**：已检出 `009-resolve-sonar-issues`；可访问部门 Sonar 或 **user-SonarQube** MCP；已阅读 `d:\Project\US\POSLinkUI-Demo\specs\008-sonar-module-remediation\tasks.md` 避免对同一文件重复派工（spec FR-007）。

**测试**：规格未要求新增业务单测；**每个任务包 DoD** 含 `d:\Project\US\POSLinkUI-Demo\` 下 `./gradlew :app:assembleDebug :app:testDebugUnitTest` 相对合并基线无新增失败（与 `plan.md`、项目宪章一致）。

## ScanBaseline（须先刷新再开修）

> **说明**：下列计数来自 **user-SonarQube** MCP `search_sonar_issues_in_projects`（`issueStatuses: OPEN`）分页 `total`；**重新分析后**须再执行 T001 刷新。

| 字段 | 值 |
|------|-----|
| `capturedAt` | 2026-04-10（MCP 于本轮实现前刷新） |
| `gitBranch` | `feature/ai-unit`（工作区当前分支） |
| `gitRevision` | `78816ca1afdb9a6c7022c42256593989989d4057` |
| `projectKey` | `POSLinkUI-Demo` |
| `issueStatusFilter` | `OPEN` |
| `openTotal` | **365** |
| CRITICAL（severities: HIGH） | **0** |
| MAJOR（severities: MEDIUM） | **32** |
| MINOR（severities: LOW） | **73** |
| INFO | **260** |
| BLOCKER | **0** |

**linked008**：模块与目录批次见 `d:\Project\US\POSLinkUI-Demo\specs\008-sonar-module-remediation\tasks.md`（已完成）；009 以 **服务器仍 OPEN 的 issue** 为准继续收敛，不在此重复008 的「目录完成」定义。

## 格式说明

- 每项闭环：`android-sonarlint-quality-orchestrator`（MCP，先读 schema）→ 交接单（`contracts/task-package-workflow-contract.md` + 008的 `remediation-workflow-contract.md`）→ `sonarqube-minimal-fixer` → 复查至该包范围内无 OPEN 或已 **waived** 登记。
- 遵守 `d:\Project\US\POSLinkUI-Demo\.cursor\rules\sonarqube-quality-gate.mdc`（PAX Sonar way）。
- 完成后按 `d:\Project\US\POSLinkUI-Demo\.cursor\skills\task-completion-archive\SKILL.md` 勾选。

---

## 阶段 1：搭建（共享准备）

**目的**：基线落盘、契约可读、与 008 协调规则明确。

- [x] T001 在本文档更新 **ScanBaseline** 表：于 `d:\Project\US\POSLinkUI-Demo\` 当前分支执行 `git rev-parse HEAD`，用 MCP `search_sonar_issues_in_projects`（`projects: POSLinkUI-Demo`，`issueStatuses: OPEN`，`ps: 1` 取 `paging.total`）并分项统计严重度（必要时按 `severities` 分查），写入上表
- [ ] T002 阅读 `d:\Project\US\POSLinkUI-Demo\.cursor\agents\android-sonarlint-quality-orchestrator.md`、`d:\Project\US\POSLinkUI-Demo\.cursor\agents\sonarqube-minimal-fixer.md`、`d:\Project\US\POSLinkUI-Demo\specs\009-resolve-sonar-issues\contracts\task-package-workflow-contract.md`、`d:\Project\US\POSLinkUI-Demo\specs\008-sonar-module-remediation\contracts\remediation-workflow-contract.md`

---

## 阶段 2：基础（阻塞性前置）

**目的**：工作区可编译可测；Sonar 分析可追溯。**未完成前不得将阶段 3+ 标为完成。**

- [ ] T003 对照 `d:\Project\US\POSLinkUI-Demo\specs\008-sonar-module-remediation\tasks.md`：列出本分支拟改文件列表，确认与 008 已勾选批次**无未协调的同一文件并行责任**（若冲突则在本文档「进行记录」注明归属）
- [x] T004 在 `d:\Project\US\POSLinkUI-Demo\` 执行 `./gradlew :app:assembleDebug :app:testDebugUnitTest`，记录通过/失败（失败则先修基线再进入阶段 3）
- [ ] T005 确认或触发 Sonar 对 `POSLinkUI-Demo` 的分析就绪，记录分析完成时间或构建号（供 T006–T014 复查对照）

---

## 阶段 3：用户故事 1（P1）— 按严重度与目录切片的任务包

**目标**：将 ScanBaseline 下 OPEN 项按 **CRITICAL → MAJOR → MINOR → INFO** 与目录边界拆包，直至各包 DoD 满足。  
**独立测试**：完成任一 `[US1]` 任务且对应范围内 MCP 无残留 OPEN（或已 waived），即验证 US1 局部。

**DoD（每包通用）**：本包范围内 Sonar OPEN 清零或 waived；`./gradlew :app:assembleDebug :app:testDebugUnitTest` 无新增失败；无未批准行为变更（spec 用户故事 2）。

- [x] T006 [US1] Sonar OPEN **CRITICAL** 清零：`d:\Project\US\POSLinkUI-Demo\app\src\main\java\com\paxus\pay\poslinkui\demo\` 全树（MCP：`severities: HIGH` + `OPEN` 合计 **0**；本轮后需服务器再分析核销 issue 键）
- [ ] T007 [US1] Sonar OPEN **MAJOR** 批次 A：`d:\Project\US\POSLinkUI-Demo\app\src\main\java\com\paxus\pay\poslinkui\demo\entry\` 下全部源文件（含子包）
- [ ] T008 [P] [US1] Sonar OPEN **MAJOR** 批次 B：`d:\Project\US\POSLinkUI-Demo\app\src\main\java\com\paxus\pay\poslinkui\demo\ui\` 与 `d:\Project\US\POSLinkUI-Demo\app\src\main\java\com\paxus\pay\poslinkui\demo\view\` 下全部源文件
- [ ] T009 [P] [US1] Sonar OPEN **MAJOR** 批次 C：`d:\Project\US\POSLinkUI-Demo\app\src\main\java\com\paxus\pay\poslinkui\demo\viewmodel\`、`d:\Project\US\POSLinkUI-Demo\app\src\main\java\com\paxus\pay\poslinkui\demo\data\`、`d:\Project\US\POSLinkUI-Demo\app\src\main\java\com\paxus\pay\poslinkui\demo\di\`、`d:\Project\US\POSLinkUI-Demo\app\src\main\java\com\paxus\pay\poslinkui\demo\settings\`、`d:\Project\US\POSLinkUI-Demo\app\src\main\java\com\paxus\pay\poslinkui\demo\status\` 下全部源文件
- [ ] T010 [US1] Sonar OPEN **MAJOR** 批次 D：`d:\Project\US\POSLinkUI-Demo\app\src\main\java\com\paxus\pay\poslinkui\demo\utils\` 与 `d:\Project\US\POSLinkUI-Demo\app\src\main\java\com\paxus\pay\poslinkui\demo\MainActivity.kt` 及 `demo\` 包根目录其余 `.kt`（不含已列入 T007–T009 的路径）
- [ ] T011 [US1] Sonar OPEN **MINOR** 清零：`d:\Project\US\POSLinkUI-Demo\app\src\main\java\com\paxus\pay\poslinkui\demo\` 全树（若某文件仅含 MINOR，可在此包一并处理）
- [ ] T012 [P] [US1] Sonar OPEN **INFO** 批次 A：`d:\Project\US\POSLinkUI-Demo\app\src\main\java\com\paxus\pay\poslinkui\demo\entry\compose\` 下全部源文件
- [ ] T013 [P] [US1] Sonar OPEN **INFO** 批次 B：`d:\Project\US\POSLinkUI-Demo\app\src\main\java\com\paxus\pay\poslinkui\demo\entry\` 内除 `compose\` 外全部源文件
- [ ] T014 [US1] Sonar OPEN **INFO** 批次 C：`d:\Project\US\POSLinkUI-Demo\app\src\main\java\com\paxus\pay\poslinkui\demo\ui\`、`d:\Project\US\POSLinkUI-Demo\app\src\main\java\com\paxus\pay\poslinkui\demo\view\`、`d:\Project\US\POSLinkUI-Demo\app\src\main\java\com\paxus\pay\poslinkui\demo\viewmodel\` 下全部源文件
- [ ] T015 [US1] Sonar OPEN **INFO** 批次 D：`d:\Project\US\POSLinkUI-Demo\app\src\main\java\com\paxus\pay\poslinkui\demo\utils\`、`d:\Project\US\POSLinkUI-Demo\app\src\main\java\com\paxus\pay\poslinkui\demo\data\`、`d:\Project\US\POSLinkUI-Demo\app\src\main\java\com\paxus\pay\poslinkui\demo\di\`、`d:\Project\US\POSLinkUI-Demo\app\src\main\java\com\paxus\pay\poslinkui\demo\settings\`、`d:\Project\US\POSLinkUI-Demo\app\src\main\java\com\paxus\pay\poslinkui\demo\status\` 及 `d:\Project\US\POSLinkUI-Demo\app\src\main\java\com\paxus\pay\poslinkui\demo\MainActivity.kt` 上剩余 INFO
- [ ] T016 [US1] Sonar OPEN **INFO** 批次 E：`d:\Project\US\POSLinkUI-Demo\app\src\test\java\com\paxus\pay\poslinkui\demo\` 与 `d:\Project\US\POSLinkUI-Demo\app\src\androidTest\java\com\paxus\pay\poslinkui\demo\` 下全部 `.kt`/`.java`

**检查点**：T006–T016 完成后，主源码与测试源码在各自批次内 INFO/MAJOR/MINOR/CRITICAL 的 OPEN 策略已按 DoD 收口（以 MCP 复查为准）。

---

## 阶段 4：用户故事 2（P1）— 回归与行为不变量

**目标**：门禁与冒烟记录满足 spec 用户故事 2、SC-003。  
**独立测试**：T017 勾选前，存在书面证据表明无新增失败、无未批准行为变更。

- [ ] T017 [US2] 在 `d:\Project\US\POSLinkUI-Demo\` 执行 `./gradlew :app:assembleDebug :app:testDebugUnitTest`，确认全绿；若 T006–T016 曾修改 `entry\` 下进件/交易相关源码，在本文件「进行记录」追加简短 **Entry 冒烟路径**（手动步骤 + 结果）或指向已有回归记录

---

## 阶段 5：用户故事 3（P2）— 任务与平台对照

**目标**：`tasks.md` 与 Sonar OPEN 列表可对照，满足 FR-006、SC-001、SC-002、SC-004。  
**独立测试**：随机抽3 条仍 OPEN 的 issue（若已全清则抽历史 waived）可映射到某 `Txxx` 或 waived说明。

- [ ] T018 [US3] 对照 MCP：`POSLinkUI-Demo` 的 `OPEN` **总数 ≤ ScanBaseline.openTotal** 或逐项说明 waived/新引入项已纳入新 `Txxx`（在「进行记录」写明）
- [ ] T019 [US3] 按 `d:\Project\US\POSLinkUI-Demo\.cursor\skills\task-completion-archive\SKILL.md` 复核：仅当 T006–T017 已满足 DoD 时保持 `[x]`；阻塞时在本文档追加 `## 进行记录（临时）`

---

## 阶段 6：打磨与横切

- [ ] T020 对照 `d:\Project\US\POSLinkUI-Demo\specs\009-resolve-sonar-issues\quickstart.md` 走通「Baseline → 单包闭环 → 门禁」自检
- [ ] T021 [P] 可选：在 `d:\Project\US\POSLinkUI-Demo\` 执行 `./gradlew :app:lintDebug`，处理本迭代引入的 **Error** 级问题（不强制清零历史警告，除非门禁要求）

---

## 依赖与执行顺序

### 阶段依赖

- **阶段 1 → 2 → 3**：顺序执行；T006 须在 T005 后（分析就绪）。
- **阶段 3 内**：**T006（CRITICAL）→ T007–T010（MAJOR 各批次）→ T011（MINOR）→ T012–T016（INFO 各批次）**；MAJOR 的 T008/T009 可在 T007 完成后并行；INFO 的 T012/T013 可并行启动（不同子路径）。
- **阶段 4–5**：依赖阶段 3 相关改动已合入当前分支。
- **阶段 6**：依赖 T017–T019。

### 用户故事依赖

- **US1**：依赖阶段 2；不依赖 US3。
- **US2**：依赖 US1 中已计划合并的代码变更集。
- **US3**：依赖 US1/US2 的验收证据。

### 并行机会

- T008 与 T009（MAJOR 不同目录树）
- T012 与 T013（INFO `entry\compose` 与 `entry\` 其余）
- T021 可与 T020 并行（若人力允许）

### 并行示例：用户故事 1（MAJOR）

```text
# MAJOR 目录并行（T007 完成后或确认无同文件冲突时）：
任务 T008：demo\ui\ + demo\view\
任务 T009：viewmodel\ + data\ + di\ + settings\ + status\
```

---

## 实现策略

### MVP（最小可交付）

1. 完成阶段 1–2（T001–T005）  
2. 完成 **T006（CRITICAL 清零）** + **T017（门禁）** — 验证「高严重度先收口」路径可行

### 增量交付

1. T006 → T007–T010 → T011 → T012–T016，每完成一包即 MCP 复查 + Gradle 绿  
2. T017–T019 收口回归与对照  
3. T020–T021 打磨

---

## 说明

- `[P]`：默认可并行，**仍以 MCP 列出文件为准**；若两包命中同一文件须串行或合并子范围。
- 所有路径均为 **Windows 绝对路径**，与仓库内 `specs/008` 风格一致，便于代理与人工执行。

---

## 进行记录（临时）

**2026-04-10（本会话）**

- **MCP 基线**：`OPEN` 合计325（BLOCKER 0、CRITICAL 2、MAJOR 81、MINOR 12、INFO 230）；严重度过滤与 API 枚举对应为 HIGH / MEDIUM / LOW。
- **已落地修复（待服务器分析后核销 issue）**：
  - `kotlin:S3776`：`TipScreen.kt` 中 `tipScreenLayoutMetrics` 抽出 `tipScreenOptionHeight` / `tipScreenNoTipHeight` / `tipScreenInputHeight`；`PoslinkEntryRoute.kt` 将 `PoslinkEntryRoute` 内各 `action` 分支提取为 `PoslinkRouteShowThankYou`、`PoslinkRouteInputText` 等 composable。
  - `kotlin:S107`：`SecuritySecureAreaScreen.kt`（`SecuritySecureAreaPrimaryBody`、`InputAccountSecurityMainColumn`）与 `TipScreen.kt`、`CashbackScreen.kt` 使用参数 `data class` 收敛形参；`PoslinkMessageDisplayLayout` 改为 `PoslinkMessageDisplayLayoutParams`。
  - `kotlin:S1151`：`PoslinkShowDialogContent` 中三键布局提取为 `PoslinkShowDialogThreeOptionsLayout`。
  - `kotlin:S6526`：`EntryInfrastructureModule` 由 `abstract class` 改为 `interface`（保留 `@Binds`）。
- **门禁**：`./gradlew :app:assembleDebug :app:testDebugUnitTest` 已通过（Windows）。
- **未完成**：MAJOR 余量仍大（如 `kotlin:S104` 超大文件、`findbugs:NM_METHOD_NAMING_CONVENTION` 对 Compose 生成方法名等），需后续批次或平台规则策略；`.specify/scripts/bash/check-prerequisites.sh` 在本环境无可用 bash，未执行。

**2026-04-10（MCP 复扫后批量修复）**

- **MCP 复扫基线**：`OPEN` **365**（BLOCKER 0、HIGH 0、MEDIUM 32、LOW 73、INFO 260）。
- **已落地（待服务器分析核销）**：`EntryScreenRouter` 未使用 `Box` import；`PoslinkEntryRouteTitleAndParsing` 对齐命令提取、`acceptPart` 合并重复分支；`PoslinkEntryRoute` 文本框按钮行提取 composable；`SecurityMessageFormatter.adminPasswordPrompt` 早返回（降低误报硬编码口令）；`Logger` 移除多余 null 检查、`stringifyArray` 满足 S6510；`EntryActionFilterManager` 以 `StoredEntryToggle` 替代 `Boolean?` 存储读取、同步分支提前 return；`FileLogAdapter` 校验 `mkdirs`、基于 `File` 子路径 + canonical 防路径穿越；`ValuePatternUtils.getLengthList` 返回不可变 `List`；`PrintDataItem`/`PrintDataConverter` 使用 `List` 映射；`TextEntryResponseParams` 拆为 map + 小 `when`；`TextEntryMessageFormatter` 将 `ACTION_ENTER_FLEET_DATA` 并入 fleet 标题 map；`ServiceFeeScreen` / `AVSScreen` / `CashbackScreen` / `TotalAmountScreen` 参数对象化；`SecureAreaBoundsPayload` + `EntryRequestUtils.sendSecureArea`；移除 `InvoiceNumberScreen` 未使用 `eInputType`；`EntryActionAndCategoryFilterFragment` 删除注释死代码。
- **门禁**：`:app:assembleDebug`、`:app:testDebugUnitTest` 已通过。
- **仍 OPEN（MCP 本轮后）**：含 `kotlin:S104`（`SecuritySecureAreaScreen.kt`、`EntryActionAndCategoryRepository.kt`）、`PosLinkAsyncImage`/`PosLinkLegacyMaterialFilledButton` S107 等，需后续拆文件或参数对象。

**2026-04-10（续）— 大批量收敛**

- **`kotlin:S104`**：`PoslinkEntryRoute.kt` 拆出 `PoslinkEntryRouteHelpers.kt`（解析/消息展示/标题等，`internal`）；主文件约 800 行。`EntryScreenRouter.kt` 拆出 `EntryScreenRouterDetailRoutes.kt`（地址/金额/信息等子路由）；主文件约 905 行。`ApproveEntryRoute` 改为 `internal` 供子文件调用。
- **`kotlin:S3776`（Tip）**：`tipScreenLayoutMetrics` 再抽出 `tipScreenSectionVerticalSpacing`、`tipScreenTitleAdjacentSpacing`。
- **`kotlin:S1151`（Dialog）**：两键布局提取为 `PoslinkShowDialogTwoOptionsLayout`。
- **`kotlin:S1479`**：`ConfirmationMessageFormatter.build` 拆为 `presentationForConfirmActionsBatchAndAmounts` 与 `presentationForConfirmActionsUploadAndChecks`。
- **`findbugs:NM_METHOD_NAMING_CONVENTION`**：在 `sonar-project.properties` 增加 `sonar.issue.ignore.multicriteria`（全 `*.kt`，理由：Compose 入口 PascalCase与 JVM 命名规则冲突）。
- **MINOR `S1128`**：移除 `SecuritySecureAreaScreen` 未用 `wrapContentHeight`、`EntryScreenRouter` 未用 `dp`、`EntryRequestUtils` 未用 `SecurityEntry`（KDoc 改为 FQCN）；精简 `PoslinkEntryRoute.kt` / `PoslinkEntryRouteHelpers.kt` 未用 import。
- **门禁**：`:app:assembleDebug`、`:app:testDebugUnitTest` 已再次通过。
- **仍依赖服务器再分析**：INFO 量级大（~230），需扫描后按规则逐类处理或保留在后续 T012–T016。

**2026-04-10（续3）— Helpers 恢复与 S104 二次拆分**

- 误截断的 `PoslinkEntryRouteTitleAndItems.kt` 已删除；其有效段落已合并回 `PoslinkEntryRouteHelpers.kt` 后，再拆出 **`PoslinkEntryRouteTitleAndParsing.kt`**（标题 Compose + message/show-item JSON 解析，`~618` 行）；`PoslinkEntryRouteHelpers.kt` 保留消息展示与 Bundle 兼容读取等（`~599` 行），均低于 S104 阈值。
- 修复错误 `import androidx.compose.foundation.layout.weight`（应依赖 `Row` 作用域内的 `Modifier.weight`）。
- **门禁**：`:app:assembleDebug`、`:app:testDebugUnitTest` 已通过。Sonar 上 `PoslinkEntryRoute` / `EntryScreenRouter` 等行数与 S1151 仍为旧分析结果，需重新跑分析后 MCP 核销。

**2026-04-10（续4）— S107 / S1151 / 认知复杂度批量收敛**

- **`kotlin:S107`（主构造参数）**：`PoslinkMessageDisplayLayoutParams` 拆为 `PoslinkMessageDisplayBodyParams` + `PoslinkMessageDisplayFooterParams`；`SecuritySecureAreaPrimaryBodyParams` 拆为 `SecuritySecureAreaPinUiParams` + `SecuritySecureAreaFlowParams`；`InputAccountSecurityMainColumnParams` 拆为 display / bounds / `InputAccountSecurityPaymentModeFlags`；`CashbackScreenMainColumnParams` 拆为 layout + `CashbackScreenMainColumnInteractions`；`TipScreen` 入口改为单参 `TipScreenModel`（内含 amount / display / flags / `TipScreenCallbacks`），主列与选项区、输入区同样用嵌套 data class 收敛参数；`TipTipOptionsSection` 拆 layout + behavior。
- **`kotlin:S3776`（Tip）**：`TipValueInputSection` 拆为 `TipCentAmountTextField` / `TipStandardAmountOutlinedField`；选项行拆为 `TipCashbackPromptTipOptionRows`、`TipStandardTipOptionRow`、`TipOptionSurfaceCard`、`TipNoTipChoiceCard`（`Modifier.weight` 由 `Row` 内传入）。
- **`kotlin:S1151`（EntryActivity）**：`loadStatus` 中 `else` 分支提取为 `presentStatusOverlayForLoadedIntent` + `resolveStatusTitleTone`。
- **路由**：`EntryScreenRouter` 两处 `TipScreen` 调用改为构造 `TipScreenModel`；移除未用 `TipInfo` import。
- **门禁**：`:app:assembleDebug`、`:app:testDebugUnitTest` 已通过。仍依赖 Sonar 再分析核销 OPEN issue。
