# Specs 总览

**用途**：功能规格与实施文档索引，供开发与 AI Agent 快速定位。

---

## 母版（全项目基线）

| 文档 | 用途 |
|------|------|
| [spec.md](000-poslinkui-project-master/spec.md) | **产品母版规格**：仓库结构、Entry 集成契约、manifest 类别与代码域映射、V1.03.00 设计文档关系（自洽基线，不绑定其他编号 spec） |

---

## 功能列表

| 功能 | 分支 | 状态 | 说明 |
|---------|------|------|------|
| **000-poslinkui-project-master** | （文档基线） | 基线 | 母版规格；非独立开发分支 |
| **001-kotlin-migration** | `001-kotlin-migration` | 进行中 | Java→Kotlin 迁移；Idea 转换 + 编译修复 + 质量审查 |
| **002-compose-migration** | `002-compose-migration` | 待启动 | UI→Compose 迁移；Fragment 内嵌 ComposeView；页面还原度第一 |
| **003-jetpack-refactor** | `003-jetpack-refactor` | 草案 | 代码重构；可读性、内存优化、Jetpack 架构对齐；不改变功能与 UI |
| **004-optimize-animation-stack** | `004-optimize-animation-stack` | 草案 | 动画库改造；预览效果不变、降内存/CPU、自测覆盖 |

**依赖关系**：002 建议基于 001 合并后的 main 创建；若并行开发，002 从 `001-kotlin-migration` 分支创建。003 与 002 可并行，均不涉及 UI 替换。004 可与 003 并行，聚焦动画实现替换。

---

## 文档结构（按功能特性）

### 001-kotlin-migration

| 文档 | 用途 |
|------|------|
| [spec.md](001-kotlin-migration/spec.md) | 功能规格、用户故事、FR、验收场景 |
| [plan.md](001-kotlin-migration/plan.md) | 实施计划、技术上下文、Phase 规划 |
| [tasks.md](001-kotlin-migration/tasks.md) | 可执行任务、依赖顺序、Checkpoint |
| [quickstart.md](001-kotlin-migration/quickstart.md) | 快速开始步骤、构建环境 |
| [modification-patterns.md](001-kotlin-migration/modification-patterns.md) | 类类型与修改模式、**空安全与 !! 规范** |
| [class-inventory.md](001-kotlin-migration/class-inventory.md) | 类清单、按模块分组 |
| [research.md](001-kotlin-migration/research.md) | Phase 0 研究结论 |
| [data-model.md](001-kotlin-migration/data-model.md) | 数据模型（本迁移无 schema 变更） |
| [contracts/](001-kotlin-migration/contracts/) | 基类修改约束、Neptune 兼容性 |
| [checklists/](001-kotlin-migration/checklists/) | 高风险类、需求检查清单 |

**相关 Skill**：[kotlin-migration-compilation](../.cursor/skills/kotlin-migration-compilation/SKILL.md)（编译错误修复速查）

---

### 002-compose-migration

| 文档 | 用途 |
|------|------|
| [spec.md](002-compose-migration/spec.md) | 功能规格、六模块用户故事、Design Tokens |
| [plan.md](002-compose-migration/plan.md) | 实施计划、Compose 共存策略 |
| [tasks.md](002-compose-migration/tasks.md) | 可执行任务 |
| [quickstart.md](002-compose-migration/quickstart.md) | 快速开始、Compose 依赖 |
| [modification-patterns.md](002-compose-migration/modification-patterns.md) | 按模块的 Compose 迁移方式 |
| [design-tokens.md](002-compose-migration/design-tokens.md) | 设计规范关键数值 |
| [class-inventory.md](002-compose-migration/class-inventory.md) | 六类模块 Fragment 清单 |
| [contracts/](002-compose-migration/contracts/) | 基类 Compose 策略 |
| [checklists/](002-compose-migration/checklists/) | 敏感屏审查 |

**相关 Skill**：[lanhu-mcp-troubleshooting](../.cursor/skills/lanhu-mcp-troubleshooting/SKILL.md)（蓝湖设计还原）

---

### 003-jetpack-refactor

| 文档 | 用途 |
|------|------|
| [spec.md](003-jetpack-refactor/spec.md) | 功能规格、可读性/内存/架构用户故事、FR、验收场景 |
| [plan.md](003-jetpack-refactor/plan.md) | 类级实施计划、依赖顺序、门禁检查 |
| [tasks.md](003-jetpack-refactor/tasks.md) | 可执行任务分解（按 US 分阶段） |
| [class-modification-map.md](003-jetpack-refactor/class-modification-map.md) | 细粒度类改造步骤、风险与验收 |
| [research.md](003-jetpack-refactor/research.md) | DI 与内存门禁决策记录 |
| [data-model.md](003-jetpack-refactor/data-model.md) | 架构/内存治理实体与状态关系 |
| [contracts/](003-jetpack-refactor/contracts/) | 生命周期与 DI 契约 |
| [quickstart.md](003-jetpack-refactor/quickstart.md) | 本地与 CI 验证流程 |
| [checklists/](003-jetpack-refactor/checklists/) | 需求质量检查清单 |

**相关 Skill**：[android-payment-troubleshooting](../.cursor/skills/android-payment-troubleshooting/SKILL.md)、[payment-project-difficulty-playbook](../.cursor/skills/payment-project-difficulty-playbook/SKILL.md)

---

### 004-optimize-animation-stack

| 文档 | 用途 |
|------|------|
| [spec.md](004-optimize-animation-stack/spec.md) | 功能规格、动画降载用户故事、FR-004a 视觉保真、FR-006a 自测 |
| [plan.md](004-optimize-animation-stack/plan.md) | 实施计划、动画位点清单、混合迁移策略 |
| [research.md](004-optimize-animation-stack/research.md) | 动画资产清单、技术选型、自测策略 |
| [data-model.md](004-optimize-animation-stack/data-model.md) | 动画位点、策略档位、性能基线 |
| [contracts/](004-optimize-animation-stack/contracts/) | 动画迁移契约、视觉保真、生命周期 |
| [quickstart.md](004-optimize-animation-stack/quickstart.md) | 视觉回归、性能基线、验收门禁 |
| [tasks.md](004-optimize-animation-stack/tasks.md) | 可执行任务分解（按 US 分阶段） |
| [checklists/](004-optimize-animation-stack/checklists/) | 需求质量检查清单 |

---

## 通用规则引用

- [payment-project-hard-problems](../.cursor/rules/payment-project-hard-problems.mdc)：状态机、幂等、8583、设备 SDK、安全合规
- [android-core](../.cursor/rules/android-core.mdc)：命名、空安全、线程、日志、KDoc
- [specify-rules](../.cursor/rules/specify-rules.mdc)：项目技术栈与结构

---

## 命令速查

| 命令 | 说明 |
|------|------|
| `./gradlew assembleDebug` | 构建 |
| `./gradlew lint` | Lint 检查 |
| `./gradlew installDebug` | 安装到设备 |
| `./gradlew :app:dokkaGeneratePublicationHtml` | 生成 KDoc，输出至 `doc/javadoc/` |
