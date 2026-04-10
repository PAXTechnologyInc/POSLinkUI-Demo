---
description: 通过处理并执行 tasks.md 中定义的全部任务来执行实现计划。
---

## 用户输入

```text
$ARGUMENTS
```

在继续之前，你**必须**考虑用户输入（若非空）。

## 执行前检查

**扩展钩子（实现前）**：
- 检查项目根目录是否存在 `.specify/extensions.yml`。
- 若存在，读取并在 `hooks.before_implement` 键下查找条目
- 若 YAML 无法解析或无效，静默跳过钩子检查并照常继续
- 仅筛选 `enabled: true` 的钩子
- 对剩余每个钩子，**不要**尝试解释或求值钩子 `condition` 表达式：
  - 若钩子无 `condition` 字段或为 null/空，视为可执行
  - 若钩子定义了非空 `condition`，跳过该钩子，将 condition 求值留给 HookExecutor 实现
- 对每个可执行钩子，根据其 `optional` 标志输出如下：
  - **可选钩子**（`optional: true`）：
    ```
    ## Extension Hooks

    **Optional Pre-Hook**: {extension}
    Command: `/{command}`
    Description: {description}

    Prompt: {prompt}
    To execute: `/{command}`
    ```
  - **必选钩子**（`optional: false`）：
    ```
    ## Extension Hooks

    **Automatic Pre-Hook**: {extension}
    Executing: `/{command}`
    EXECUTE_COMMAND: {command}
    
    Wait for the result of the hook command before proceeding to the Outline.
    ```
- 若未注册钩子或不存在 `.specify/extensions.yml`，静默跳过

## 概要

1. 在仓库根目录运行 `.specify/scripts/bash/check-prerequisites.sh --json --require-tasks --include-tasks`，解析 FEATURE_DIR 与 AVAILABLE_DOCS。所有路径须为绝对路径。参数中含单引号（如 "I'm Groot"）时使用转义：例如 `'I'\''m Groot'`（或尽量用双引号：`"I'm Groot"`）。

2. **检查检查清单状态**（若存在 FEATURE_DIR/checklists/）：
   - 扫描 checklists/ 目录下所有检查清单文件
   - 对每个检查清单统计：
     - 总项：匹配 `- [ ]` 或 `- [X]` 或 `- [x]` 的行
     - 已完成：匹配 `- [X]` 或 `- [x]` 的行
     - 未完成：匹配 `- [ ]` 的行
   - 生成状态表：

     ```text
     | Checklist | Total | Completed | Incomplete | Status |
     |-----------|-------|-----------|------------|--------|
     | ux.md     | 12    | 12        | 0          | ✓ PASS |
     | test.md   | 8     | 5         | 3          | ✗ FAIL |
     | security.md | 6   | 6         | 0          | ✓ PASS |
     ```

   - 计算总体状态：
     - **PASS**：所有检查清单未完成项为 0
     - **FAIL**：至少一个检查清单有未完成项

   - **若任检查清单未完成**：
     - 显示含未完成计数的表格
     - **停止**并询问：「部分检查清单未完成。是否仍要继续实现？（yes/no）」
     - 等待用户回答后再继续
     - 若用户回答 "no" 或 "wait" 或 "stop"，中止执行
     - 若用户回答 "yes" 或 "proceed" 或 "continue"，进入步骤 3

   - **若所有检查清单已完成**：
     - 显示全部通过的表格
     - 自动进入步骤 3

3. 加载并分析实现上下文：
   - **必填**：读取 tasks.md 获取完整任务列表与执行计划
   - **必填**：读取 plan.md 获取技术栈、架构与文件结构
   - **若存在**：读取 data-model.md 获取实体与关系
   - **若存在**：读取 contracts/ 获取 API 规格与测试要求
   - **若存在**：读取 research.md 获取技术决策与约束
   - **若存在**：读取 quickstart.md 获取集成场景

4. **项目搭建校验**：
   - **必填**：根据实际项目创建/校验 ignore 文件：

   **检测与创建逻辑**：
   - 用下列命令判断是否 git 仓库（若是则创建/校验 .gitignore）：

     ```sh
     git rev-parse --git-dir 2>/dev/null
     ```

   - 若存在 Dockerfile* 或 plan.md 含 Docker → 创建/校验 .dockerignore
   - 若存在 .eslintrc* → 创建/校验 .eslintignore
   - 若存在 eslint.config.* → 确保配置的 `ignores` 覆盖所需模式
   - 若存在 .prettierrc* → 创建/校验 .prettierignore
   - 若存在 .npmrc 或 package.json → 创建/校验 .npmignore（若发布）
   - 若存在 terraform 文件（*.tf）→ 创建/校验 .terraformignore
   - 若需要 .helmignore（存在 helm chart）→ 创建/校验 .helmignore

   **若 ignore 已存在**：校验是否含必要模式，仅追加缺失的关键模式  
   **若 ignore 缺失**：按检测到的技术创建完整模式集

   **按技术的常见模式**（来自 plan.md 技术栈）：
   - **Node.js/JavaScript/TypeScript**：`node_modules/`, `dist/`, `build/`, `*.log`, `.env*`
   - **Python**：`__pycache__/`, `*.pyc`, `.venv/`, `venv/`, `dist/`, `*.egg-info/`
   - **Java**：`target/`, `*.class`, `*.jar`, `.gradle/`, `build/`
   - **C#/.NET**：`bin/`, `obj/`, `*.user`, `*.suo`, `packages/`
   - **Go**：`*.exe`, `*.test`, `vendor/`, `*.out`
   - **Ruby**：`.bundle/`, `log/`, `tmp/`, `*.gem`, `vendor/bundle/`
   - **PHP**：`vendor/`, `*.log`, `*.cache`, `*.env`
   - **Rust**：`target/`, `debug/`, `release/`, `*.rs.bk`, `*.rlib`, `*.prof*`, `.idea/`, `*.log`, `.env*`
   - **Kotlin**：`build/`, `out/`, `.gradle/`, `.idea/`, `*.class`, `*.jar`, `*.iml`, `*.log`, `.env*`
   - **C++**：`build/`, `bin/`, `obj/`, `out/`, `*.o`, `*.so`, `*.a`, `*.exe`, `*.dll`, `.idea/`, `*.log`, `.env*`
   - **C**：`build/`, `bin/`, `obj/`, `out/`, `*.o`, `*.a`, `*.so`, `*.exe`, `*.dll`, `autom4te.cache/`, `config.status`, `config.log`, `.idea/`, `*.log`, `.env*`
   - **Swift**：`.build/`, `DerivedData/`, `*.swiftpm/`, `Packages/`
   - **R**：`.Rproj.user/`, `.Rhistory`, `.RData`, `.Ruserdata`, `*.Rproj`, `packrat/`, `renv/`
   - **通用**：`.DS_Store`, `Thumbs.db`, `*.tmp`, `*.swp`, `.vscode/`, `.idea/`

   **工具相关模式**：
   - **Docker**：`node_modules/`, `.git/`, `Dockerfile*`, `.dockerignore`, `*.log*`, `.env*`, `coverage/`
   - **ESLint**：`node_modules/`, `dist/`, `build/`, `coverage/`, `*.min.js`
   - **Prettier**：`node_modules/`, `dist/`, `build/`, `coverage/`, `package-lock.json`, `yarn.lock`, `pnpm-lock.yaml`
   - **Terraform**：`.terraform/`, `*.tfstate*`, `*.tfvars`, `.terraform.lock.hcl`
   - **Kubernetes/k8s**：`*.secret.yaml`, `secrets/`, `.kube/`, `kubeconfig*`, `*.key`, `*.crt`

5. 解析 tasks.md 结构并提取：
   - **任务阶段**：Setup、Tests、Core、Integration、Polish
   - **任务依赖**：顺序 vs 并行规则
   - **任务细节**：ID、描述、文件路径、并行标记 [P]
   - **执行流**：顺序与依赖要求

6. 按任务计划执行实现：
   - **按阶段执行**：完成当前阶段再进入下一阶段
   - **遵守依赖**：顺序任务按序执行，标记 [P] 的可并行
   - **遵循 TDD**：测试任务先于对应实现任务执行
   - **按文件协调**：影响相同文件的任务须顺序执行
   - **验证检查点**：每阶段完成后再继续

7. 实现执行规则：
   - **先搭建**：初始化项目结构、依赖、配置
   - **先测后码**：若需为契约、实体与集成场景编写测试
   - **核心开发**：实现模型、服务、CLI、端点
   - **集成工作**：数据库连接、中间件、日志、外部服务
   - **打磨与验证**：单元测试、性能优化、文档

8. 进度与错误处理：
   - 每完成一项任务后报告进度
   - 任一非并行任务失败则中止执行
   - 并行任务 [P] 中成功的继续，失败的单独报告
   - 提供含上下文的明确错误信息便于调试
   - 若无法继续实现则建议后续步骤
   - **重要**：已完成任务须在 tasks 文件中勾选为 [X]

9. 完成校验：
   - 确认所有必需任务已完成
   - 确认实现与原始规格一致
   - 确认测试通过且覆盖率满足要求
   - 确认实现符合技术计划
   - 报告最终状态与完成工作摘要

**说明**：本命令假定 tasks.md 中已有完整任务分解。若任务不完整或缺失，建议先运行 `/speckit.tasks` 重新生成任务列表。

10. **扩展钩子**：完成校验后，检查项目根目录是否存在 `.specify/extensions.yml`。
    - 若存在，读取并在 `hooks.after_implement` 键下查找条目
    - 若 YAML 无法解析或无效，静默跳过钩子检查并照常继续
    - 仅筛选 `enabled: true` 的钩子
    - 对剩余每个钩子，**不要**尝试解释或求值钩子 `condition` 表达式：
      - 若钩子无 `condition` 字段或为 null/空，视为可执行
      - 若钩子定义了非空 `condition`，跳过该钩子，将 condition 求值留给 HookExecutor 实现
    - 对每个可执行钩子，根据其 `optional` 标志输出如下：
      - **可选钩子**（`optional: true`）：
        ```
        ## Extension Hooks

        **Optional Hook**: {extension}
        Command: `/{command}`
        Description: {description}

        Prompt: {prompt}
        To execute: `/{command}`
        ```
      - **必选钩子**（`optional: false`）：
        ```
        ## Extension Hooks

        **Automatic Hook**: {extension}
        Executing: `/{command}`
        EXECUTE_COMMAND: {command}
        ```
    - 若未注册钩子或不存在 `.specify/extensions.yml`，静默跳过
