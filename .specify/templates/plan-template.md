# 实现计划：[FEATURE]

**分支**：`[###-feature-name]` | **日期**：[DATE] | **规格**：[link]  
**输入**：来自 `/specs/[###-feature-name]/spec.md` 的功能规格说明

**说明**：本模板由 `/speckit.plan` 命令填写。执行流程见 `.specify/templates/plan-template.md`（本文件）。

## 摘要

[从功能规格提取：主要需求 + 来自研究的技术路线]

## 技术上下文

<!--
  须执行：将本节替换为项目技术细节。此处结构为建议性，用于引导迭代。
-->

**语言/版本**：[例如 Python 3.11、Swift 5.9、Rust 1.75 或 NEEDS CLARIFICATION]  
**主要依赖**：[例如 FastAPI、UIKit、LLVM 或 NEEDS CLARIFICATION]  
**存储**：[如适用，例如 PostgreSQL、CoreData、文件 或 N/A]  
**测试**：[例如 pytest、XCTest、cargo test 或 NEEDS CLARIFICATION]  
**目标平台**：[例如 Linux 服务器、iOS 15+、WASM 或 NEEDS CLARIFICATION]  
**项目类型**：[例如 library/cli/web-service/mobile-app/compiler/desktop-app 或 NEEDS CLARIFICATION]  
**性能目标**：[领域相关，例如 1000 req/s、10k 行/秒、60 fps 或 NEEDS CLARIFICATION]  
**约束**：[领域相关，例如 p95 <200ms、内存 <100MB、可离线 或 NEEDS CLARIFICATION]  
**规模/范围**：[领域相关，例如 1 万用户、100 万行代码、50 个界面 或 NEEDS CLARIFICATION]

## 宪章检查（Constitution Check）

*门禁：必须在 Phase 0 研究前通过，Phase 1 设计后再次检查。*

验证与 `.specify/memory/constitution.md` 需求约束原则的合规性：
- I. 需求可测试性：FR 可测试、验收场景可执行、成功标准可度量
- II. 需求无歧义：无模糊形容词、验收引用可度量对象、边界情况可执行
- III. 需求与技术无关：spec 不规定实现细节
- IV. 需求范围约束：Out-of-Scope、Assumptions、模块验收清单（如适用）已明确
- V. 合规性需求：支付/迁移相关约束（敏感数据、状态幂等、状态机、设备 SDK、渐进式）已在 spec 中体现

[如有违规，在 Complexity Tracking 中记录并说明理由]

## 项目结构

### 文档（本功能）

```text
specs/[###-feature]/
├── plan.md              # 本文件（/speckit.plan 命令输出）
├── research.md          # Phase 0 输出（/speckit.plan）
├── data-model.md        # Phase 1 输出（/speckit.plan）
├── quickstart.md        # Phase 1 输出（/speckit.plan）
├── contracts/           # Phase 1 输出（/speckit.plan）
└── tasks.md             # Phase 2 输出（/speckit.tasks 命令——非 /speckit.plan 创建）
```

### 源代码（仓库根目录）
<!--
  须执行：将下方占位树替换为本功能的具体布局。
  删除未用选项，并将选定结构展开为真实路径（如 apps/admin、packages/something）。
  交付的计划中不得保留 Option 标签字样。
-->

```text
# [未用则删除] 选项 1：单项目（默认）
src/
├── models/
├── services/
├── cli/
└── lib/

tests/
├── contract/
├── integration/
└── unit/

# [未用则删除] 选项 2：Web 应用（当检测到「前端 + 后端」）
backend/
├── src/
│   ├── models/
│   ├── services/
│   └── api/
└── tests/

frontend/
├── src/
│   ├── components/
│   ├── pages/
│   └── services/
└── tests/

# [未用则删除] 选项 3：移动端 + API（当检测到 iOS/Android）
api/
└── [与上述 backend 相同]

ios/ 或 android/
└── [平台相关结构：功能模块、UI 流程、平台测试]
```

**结构决策**：[记录所选结构并引用上文真实目录]

## 复杂度追踪（Complexity Tracking）

> **仅在宪章检查存在必须说明的违规时填写**

| 违规项 | 为何需要 | 更简单方案被拒绝的原因 |
|--------|----------|------------------------|
| [例如第四个项目] | [当前需求] | [为何三个项目不够] |
| [例如 Repository 模式] | [具体问题] | [为何直接访问 DB 不够] |
