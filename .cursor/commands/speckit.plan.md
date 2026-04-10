---
description: 使用计划模板执行实现规划工作流并生成设计产物。
handoffs: 
  - label: 创建任务
    agent: speckit.tasks
    prompt: 将计划拆分为任务
    send: true
  - label: 创建检查清单
    agent: speckit.checklist
    prompt: 为以下领域创建检查清单……
---

## 用户输入

```text
$ARGUMENTS
```

在继续之前，你**必须**考虑用户输入（若非空）。

## 概要

1. **搭建**：在仓库根目录运行 `.specify/scripts/bash/setup-plan.sh --json`，解析 JSON 得到 FEATURE_SPEC、IMPL_PLAN、SPECS_DIR、BRANCH。参数中含单引号（如 "I'm Groot"）时使用转义：例如 `'I'\''m Groot'`（或尽量用双引号：`"I'm Groot"`）。

2. **加载上下文**：读取 FEATURE_SPEC 与 `.specify/memory/constitution.md`。加载 IMPL_PLAN 模板（已复制）。

3. **执行计划工作流**：按 IMPL_PLAN 模板结构：
   - 填写 Technical Context（未知标为 "NEEDS CLARIFICATION"）
   - 从宪章填写 Constitution Check 节
   - 评估门禁（若无理由的违规则 ERROR）
   - Phase 0：生成 research.md（解决全部 NEEDS CLARIFICATION）
   - Phase 1：生成 data-model.md、contracts/、quickstart.md
   - Phase 1：通过运行 agent 脚本更新 agent 上下文
   - 设计后再次评估 Constitution Check

4. **停止并报告**：命令在 Phase 2 规划处结束。报告分支、IMPL_PLAN 路径及已生成产物。

## 阶段

### Phase 0：大纲与研究

1. **从上方 Technical Context 提取未知项**：
   - 每个 NEEDS CLARIFICATION → 研究任务
   - 每个依赖 → 最佳实践任务
   - 每个集成 → 模式任务

2. **生成并分派研究代理**：

   ```text
   对 Technical Context 中每个未知项：
     任务："在 {feature context} 下研究 {unknown}"
   对每项技术选型：
     任务："查找 {domain} 中 {tech} 的最佳实践"
   ```

3. **在 `research.md` 中汇总**，格式：
   - 决策：[所选方案]
   - 理由：[选择原因]
   - 备选方案：[还评估了哪些]

**输出**：research.md，所有 NEEDS CLARIFICATION 已解决

### Phase 1：设计与契约

**前置条件：** `research.md` 已完成

1. **从功能规格提取实体** → `data-model.md`：
   - 实体名、字段、关系
   - 来自需求的校验规则
   - 若适用则含状态迁移

2. **定义接口契约**（若项目有对外接口）→ `/contracts/`：
   - 识别项目向用户或其他系统暴露的接口
   - 按项目类型记录合适的契约格式
   - 示例：库的公开 API、CLI 的命令 schema、Web 服务的端点、解析器的文法、应用的 UI 契约
   - 若项目纯内部（构建脚本、一次性工具等）可跳过

3. **Agent 上下文更新**：
   - 运行 `.specify/scripts/bash/update-agent-context.sh cursor-agent`
   - 脚本检测当前使用的 AI agent
   - 更新对应的 agent 专用上下文文件
   - 仅添加当前计划中的新技术
   - 保留标记之间的手工增补

**输出**：data-model.md、/contracts/*、quickstart.md、agent 专用文件

## 关键规则

- 使用绝对路径
- 门禁失败或未解决澄清时报 ERROR
