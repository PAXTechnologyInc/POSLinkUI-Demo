# 实现计划：基于扫描结果的 Sonar 问题分批治理（无功能回退）

**分支**：`009-resolve-sonar-issues` | **日期**：2026-04-10 | **规格**：[spec.md](spec.md)  
**输入**：`specs/009-resolve-sonar-issues/spec.md`

## 摘要

本迭代**不新增业务功能**，在部门 SonarQube工程 **`POSLinkUI-Demo`** 上，以**一次书面固定的 ScanBaseline（OPEN 待处理项集合与计数）**为起点，将治理工作拆为多个可独立验收的 **`Txxx` 任务包**；每包遵循「审查编排 → 最小改动修复 → 复查」闭环，并在标完成前通过 **`:app` 构建与单元测试门禁**，确保不相对基线引入新失败、不无故改变用户可感知交易/进件行为。与 **`008-sonar-module-remediation`** 共用同一 Sonar 工程与交接契约思想；008 侧重 **Gradle 模块批次与提交节奏**，009 侧重 **扫描驱动与任务包粒度**，二者由 `tasks.md` 统一编号并避免对同一文件重复派工。

## 技术上下文

**语言/版本**：Kotlin（Android Gradle Plugin / Kotlin DSL，JVM 版本以根工程 Gradle 为准）  
**主要依赖**：AndroidX、Jetpack Compose（现有 `:app`）、部门 Sonar 实例；可选 **user-SonarQube** MCP 与审查/修复代理（与008 一致）  
**存储**：N/A（质量元数据在 Sonar 侧；本计划要求在仓库内记录 **ScanBaseline** 摘要）  
**测试**：`./gradlew :app:assembleDebug`、`:app:testDebugUnitTest`；批次/任务包收尾可按需 `:app:lintDebug`  
**目标平台**：Android POS（与现有 `app` 的 min/targetSdk 一致）  
**项目类型**：mobile-app（当前 `settings.gradle.kts` 仅 `:app`）  
**性能目标**：治理不得引入可感知 UI 卡顿；拆分复杂度等方法级重构须保持行为等价  
**约束**：`.cursor/rules/sonarqube-quality-gate.mdc`（PAX Sonar way）；项目宪章 VIII Sonar 与 XIV 日志/敏感数据；禁止为消告警而弱化 TLS、证书校验或硬编码密钥  
**规模/范围**：`app/src/main` 下纳入分析的源码；任务包按 **严重度切片、规则族或文件簇** 划分，单包改动量须可评审

**Language/Version**: Kotlin (Android), JVM per root Gradle toolchain  
**Primary Dependencies**: AndroidX, Jetpack Compose, SonarQube (PAX gate), existing Pax SDK consumers in `:app`  
**Storage**: N/A  
**Testing**: Gradle `:app:assembleDebug`, `:app:testDebugUnitTest`  
**Project Type**: mobile-app

## 宪章检查（Constitution Check）

*门禁：Phase 0 研究前通过；Phase 1 设计后再次检查（见文末「Phase 1 后复查」）。*

对照 `.specify/memory/constitution.md`（v3）与 [spec.md](spec.md)：

| 检查项 | 结论 |
|--------|------|
| I–IV. 需求可测试、无歧义、范围清晰 | 通过：FR/SC 与验收场景可追溯；与 008 衔接在 FR-007 明示 |
| VIII. SonarQube 合规 | 通过：对齐部门实例与 Blocker/Critical/Major 策略 |
| VII. Kotlin 惯用法 / 不改变可观测行为 | 通过：spec 用户故事 2 + FR-003 |
| XIV. 敏感数据 / 日志 | 通过：治理不放宽脱敏与日志纪律 |
| 构建验证（流程 §八） | 通过：任务包收尾强制 assemble + testDebugUnitTest |

**复杂度追踪**：无宪章违规豁免项。

## 项目结构

### 文档（本功能）

```text
specs/009-resolve-sonar-issues/
├── plan.md              # 本文件（/speckit.plan）
├── spec.md
├── research.md          # Phase 0
├── data-model.md        # Phase 1
├── quickstart.md        # Phase 1
├── contracts/
│   └── task-package-workflow-contract.md
├── checklists/
│   └── requirements.md
└── tasks.md             # Phase 2：由 /speckit.tasks 生成（本命令不创建）
```

### 源代码（仓库根目录）

```text
app/
├── build.gradle.kts
└── src/
    ├── main/java/          # Sonar 修复主战场
    └── test/java/

settings.gradle.kts
sonar-project.properties    # sonar.projectKey=POSLinkUI-Demo
```

**结构决策**：与 008 相同，质量治理作用于 `:app`；任务包在 `tasks.md` 中映射到子路径或规则批次。

## 实施分阶段

### Phase 0 - 研究与口径收敛

- 固定 **ScanBaseline**：记录分析时间、分支/commit、`POSLinkUI-Demo` 上 **OPEN** 总数及严重度分布（见 [research.md](research.md)）。
- 确认与008 的 **模块**、**文件**、**交接单** 定义一致；009 额外约定 **TaskPackage（Txxx）** 的字段（见 [data-model.md](data-model.md)）。
- 产出：[research.md](research.md)

### Phase 1 - 设计与契约

- 固化 **ScanBaseline / TaskPackage / VerificationRecord** 字段与状态（[data-model.md](data-model.md)）。
- 约定 **任务包级** 交接与验收最小字段（[contracts/task-package-workflow-contract.md](contracts/task-package-workflow-contract.md)）；单文件细节继续兼容008 的 `remediation-workflow-contract.md` 思路。
- 执行入口：[quickstart.md](quickstart.md)
- **Agent 上下文**：在可执行 Bash 的环境下应运行 `.specify/scripts/bash/update-agent-context.sh cursor-agent`；当前若脚本不可用，可稍后补跑以更新 `.cursor/rules/specify-rules.mdc`。

### Phase 2 - 任务拆解原则（供 `/speckit.tasks`，本命令到此为止）

1. 在 `tasks.md` **顶部**用表格或列表固化 **ScanBaseline**（日期、分支、OPEN 合计、BLOCKER/CRITICAL/MAJOR/MINOR/INFO 可选）。
2. 将 `Txxx` 按以下**优先序**切片（可合并同类项，但单包不宜过大）：**BLOCKER → CRITICAL → MAJOR → MINOR → INFO**；或按「高文件密度规则族」聚类。
3. 每个 `Txxx` 必须包含：**范围**（路径/文件列表或规则键前缀）、**目标**（关闭条数或「该文件清零」）、**DoD**（平台复查要点 + Gradle 命令）、与 **008** 模块/文件任务**无重复责任**（见 spec FR-007）。
4. 每包闭环：orchestrator 事实 → 交接单 → minimal-fixer → 复查 → 门禁通过 → 勾选任务（task-completion-archive）。
5. **不得**将 unrelated 重构与消告警混在同一 `Txxx`。

## 架构与质量一致性检查点

- 与 008 相同：禁止以弱化安全、弱哈希、无理由 `@Suppress` 等方式「糊弄」门禁。
- **功能回退**：若修复必须改变可观察行为，须单独书面记录并获批后再合入（spec 用户故事 2）。
- **Waived**：须可指向登记文本（规则、原因、范围、共识）。

## 风险与缓解

| 风险 | 缓解 |
|------|------|
| Baseline 与服务器不同步 | 任务包复查注明分析时间；重大争议重新导出分页 `total` |
| 与 008 `tasks.md` 重复派工 | FR-007：009/008 互链 scope；合并前 diff 路径 |
| 单包过大难以评审 | 按文件或规则族再拆 `Txxx` |
| MCP 不可用 | Sonar Web UI + 本地 SonarLint 绑定同实例；结论以服务器 OPEN 为准 |

## Phase 1 后宪章复查

- Sonar、构建、Kotlin 惯用法与敏感数据条款：仍满足。
- 无新增与宪章冲突的条款。

---

**本命令结束于 Phase 2 规划原则**；`tasks.md` 须由 **`/speckit.tasks`** 生成后再开始实作。
