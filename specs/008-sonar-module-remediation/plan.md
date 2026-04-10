# 实现计划：按模块闭环解决 Sonar 质量门禁问题

**分支**：`008-sonar-module-remediation` | **日期**：2026-04-08 | **规格**：[spec.md](spec.md)  
**输入**：`specs/008-sonar-module-remediation/spec.md`

## 摘要

本迭代不新增业务功能，而是在部门 SonarQube（`POSLinkUI-Demo` 项目键）上按 **PAX Sonar way** 口径，以 **Gradle 模块为批次**、**源文件为工作单元**，执行「审查编排 → 最小改动修复 → MCP 复查」闭环；每完成一个模块即独立提交，并在 `tasks.md` 中及时勾选任务。当前仓库仅包含 **`:app`** 单模块，模块批次与「整应用一轮」等价，仍须按文件粒度执行闭环与记录。

## 技术上下文

**语言/版本**：Kotlin（Android Gradle Plugin / Kotlin DSL，JVM 以项目 `build.gradle` 为准）  
**主要依赖**：AndroidX、Compose（按现有 `app` 模块）、部门 Sonar 实例与 MCP `user-SonarQube`（可选）  
**存储**：N/A（本功能不引入新持久化）  
**测试**：`./gradlew :app:testDebugUnitTest`；修复后按 `.cursor/skills/android-post-change-build/SKILL.md` 在批次收尾做组装/单测门禁  
**目标平台**：Android POS（与现有 `app` 的 min/targetSdk 一致）  
**项目类型**：mobile-app（单模块 `:app`）  
**性能目标**：修复不得引入可感知 UI 卡顿；认知复杂度拆分等规则遵守后行为与对外契约不变  
**约束**：遵守 `.cursor/rules/sonarqube-quality-gate.mdc`；禁止硬编码凭据；HTTPS/证书校验等安全规则不因「消告警」而削弱；日志与支付相关约束见项目宪章  
**规模/范围**：`app/src/main` 下 Kotlin/Java 源文件（及纳入 Sonar 分析的配置内范围）；模块列表以 `settings.gradle.kts` 为准，现为仅 `:app`

## 宪章检查（Constitution Check）

*门禁：Phase 0 研究前通过；Phase 1 设计后再次检查（见文末「Phase 1 后复查」）。*

对照 `.specify/memory/constitution.md`（POSLinkUI-Demo 项目宪章 v3）：

| 检查项 | 结论 |
|--------|------|
| VIII. SonarQube 合规 | 通过：本计划显式对齐部门实例与 Blocker/Critical/Major 处理策略 |
| 构建验证（§八 开发流程） | 通过：批次收尾执行 `assembleDebug` / `testDebugUnitTest`（与 android-post-change-build 一致） |
| Kotlin 惯用法（§三 VII） | 通过：修复以规则修复为主，不借机改变可观测业务行为；现有测试须保持通过 |
| 敏感数据 / 日志（§六 XIV） | 通过：修复过程不增加敏感日志或弱化脱敏 |
| 需求可测试性（spec） | 通过：FR/SC 与验收场景可追溯 |

**复杂度追踪**：无宪章违规豁免项。

## 项目结构

### 文档（本功能）

```text
specs/008-sonar-module-remediation/
├── plan.md              # 本文件
├── spec.md
├── research.md          # Phase 0
├── data-model.md        # Phase 1
├── quickstart.md        # Phase 1
├── contracts/
│   └── remediation-workflow-contract.md
├── checklists/
│   └── requirements.md
└── tasks.md             # Phase 2：由 /speckit.tasks 生成（本命令不创建）
```

### 源代码（仓库根目录）

```text
app/
├── build.gradle.kts
└── src/
    ├── main/java/          # 主要 Kotlin/Java 源码（Sonar 修复主战场）
    ├── main/res/
    └── test/java/          # 单测（若规则涉及测试代码一并纳入批次）

settings.gradle.kts         # 当前仅 include(":app")
sonar-project.properties    # sonar.projectKey=POSLinkUI-Demo 等
```

**结构决策**：质量治理作用于现有 `:app` 模块；若将来 `settings.gradle.kts` 增加子模块，在 `tasks.md` 中追加模块顺序与逐模块提交策略，不改变本计划闭环定义。

## 实施分阶段

### Phase 0 - 研究与口径收敛

- 确认 Sonar **project key**、**host** 与质量门禁名称（见 `sonar-project.properties` 与 [research.md](research.md)）。
- 固定「模块 = Gradle 子项目」与「文件 = 仓库相对路径源文件」的操作定义。
- 产出：[research.md](research.md)

### Phase 1 - 设计与契约

- 将 spec 中 **ModuleBatch / FileFinding / RemediationHandoff** 落实为可填写的数据字段与状态（见 [data-model.md](data-model.md)）。
- 约定审查编排与修复方之间的 **交接单最小字段**（见 [contracts/remediation-workflow-contract.md](contracts/remediation-workflow-contract.md)）。
- 编写执行者与代理可照做的步骤入口（见 [quickstart.md](quickstart.md)）。
- 更新 Cursor Specify 上下文（见仓库 `.cursor/rules/specify-rules.mdc`）。

### Phase 2 - 任务拆解原则（供 `/speckit.tasks`，本命令到此为止）

1. 在 `tasks.md` 中列出 **模块顺序**（当前仅 T 序：`app`）及 **文件级或规则批次级** 任务，编号 `Txxx`，与 US 映射可选。
2. 每文件（或每批）：`orchestrator` 出报告与交接单 → `minimal-fixer` 改代码 → `orchestrator` 复查 MCP；未关闭不勾任务。
3. 每模块结束：`git commit` 仅含该模块范围；提交说明包含模块名与摘要（如 `fix(sonar): app - ...`）。
4. 每完成一条可验收任务：按 `.cursor/skills/task-completion-archive/SKILL.md` 勾选 `tasks.md`。
5. 批次收尾：按 android-post-change-build 执行 `:app:assembleDebug` 与 `:app:testDebugUnitTest`，失败则回滚或修至绿再提交。

## 架构与质量一致性检查点

- 修复 **不得** 以 `@Suppress`、关闭证书校验、明文 HTTP、弱哈希等方式「糊弄」门禁，除非业务书面例外并登记。
- 拆分高认知复杂度方法时保持 **行为等价**，并补充必要 KDoc 以满足注释密度门禁。
- 同一文件多轮闭环时保留 **规则键 + 行号** 级别的追踪，直至 OPEN issue 清零或登记例外。

## 风险与缓解

| 风险 | 缓解 |
|------|------|
| MCP / 内网 Sonar 不可用 | 使用 Android Studio SonarLint 绑定同实例做本地对照；或待分析完成后再复查 |
| 服务器分析滞后导致复查仍为旧 issue | 触发分析后等待完成；复查注明分析版本/时间 |
| 单模块仓库「模块提交」粒度偏粗 | 仍按 **逻辑子目录或任务批次** 在 `tasks.md` 细分；提交信息写清范围 |
| 修复引入新 issue | 同一文件继续闭环，不勾选完成 |

## Phase 1 后宪章复查

- VIII SonarQube：仍满足（流程强制闭环与门禁口径不变）。
- 构建与测试：Phase 2 执行时强制；计划已约定。
- 无新增与宪章冲突的技术债条款。

---

**本命令结束于 Phase 2 规划原则**；`tasks.md` 须由 `/speckit.tasks` 生成后再开始实作闭环。
