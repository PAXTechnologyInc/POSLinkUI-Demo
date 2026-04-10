# Android 规则与技能指南

本指南说明项目内 Android 相关的 Cursor 规则与技能。

## 规则

### android-core.mdc
- 作用范围：始终生效
- 用途：基础编码规范、空安全、线程与生命周期、日志规范
- 价值：保证日常 Android 修改安全可读

### android-architecture.mdc
- 作用范围：`app/src/main/java/**/*.{java,kt}`
- 用途：Fragment/ViewModel 职责、状态建模、边界契约
- 价值：避免业务逻辑堆在 UI 层

### android-payment-safety.mdc
- 作用范围：`app/src/main/java/**/*.{java,kt}`
- 用途：交易状态机、回调幂等、敏感信息脱敏、失败与恢复
- 价值：降低支付流程的合规与资金风险

### android-testing.mdc
- 作用范围：`app/src/main/java/**/*.{java,kt}`
- 用途：最小测试要求、支付场景回归保护
- 价值：减少高风险路径的重复缺陷

## 技能

### android-feature-delivery
- 触发场景：实现需求、新增流程、重构 Android 功能
- 输出重点：改动范围、行为变化、安全点、测试

### android-payment-troubleshooting
- 触发场景：重复结果、卡在处理中、超时、状态不一致
- 输出重点：现象、根因、风险、修复方案、回归用例

### android-code-review
- 触发场景：PR 评审、代码质量/安全审查
- 输出重点：按风险排序的结论与具体修复建议

### code-gen-subagent / code-verify-subagent（委派子代理）
- 技能路径：`.cursor/skills/code-gen-subagent/SKILL.md`、`.cursor/skills/code-verify-subagent/SKILL.md`。
- **推荐顺序（先写后测）**：父会话发**两条消息**——先启动 **code-gen-subagent**（`generalPurpose` + 按 SKILL 执行），待结束且代码落盘后，再启动 **code-verify-subagent**，测试与审查针对上一阶段写好的代码。Cursor 内置 **Task 子代理类型不可自定义**，任务说明中写明「按对应 SKILL 执行」。
- **并行**：仅当两任务**修改文件不重叠**时考虑；同一功能默认 **不要** 与验证并行。
- **code-gen-subagent**：对齐 android-feature-delivery，**不负责**批次末全量 Gradle。
- **code-verify-subagent**：对齐 android-code-review + android-post-change-build；跑 **`:app:testDebugUnitTest`** 及父任务给出的**脚本/命令测试**，批次末执行 **assemble + test** 门禁。

### 子代理：实现 + 脚本测试交接（Entry / POSUInew）

- **实现**：`.cursor/agents/feature-code-generator.md` — 按规格改 Kotlin/Android，产出变更说明供测试侧使用。
- **测试**：`.cursor/agents/test-author.md` — Gradle 单测/仪器化；涉及 **Intent action、ui.category、Manifest、Entry 路由** 时，在 **POSUInew** 跑真机 pytest（`run_case_by_intent.py`、报告在 `reports/`）。脚本失败时须输出「失败反馈块」转交实现代理。
- **用户可复制指令**：`.cursor/agents/script-test-handoff-user-prompts.md` — 含「实现后委托 test-author」「失败转交 feature-code-generator 再改」「复跑」等段落，主对话中替换路径与设备 SN 即可。
- **推荐顺序**：`feature-code-generator` 落盘 → `test-author` 跑 POSUInew → 失败则把反馈块交给 `feature-code-generator` 分析并重改 → `test-author` 再跑至通过或确认需更新 `Expected_Result`。

## 推荐用法

1. 做功能开发时使用 **android-feature-delivery**
2. 合并前使用 **android-code-review**
3. 交易流程出问题时使用 **android-payment-troubleshooting**
4. 建议与上述规则一起启用，形成统一质量门禁
5. 改动 Entry 起页链路时，结合 **子代理与 POSUInew 脚本交接** 一节与 `script-test-handoff-user-prompts.md` 做真机回归

## 维护说明

- 架构或支付流程契约变更时，同步更新对应规则/技能
- 保持规则与技能内容简短、可执行
- 仅在有明确、重复的团队痛点时再新增规则或技能
