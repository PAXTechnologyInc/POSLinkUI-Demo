<!--
  版本变更: 2.0.0 → 3.0.0
  变更类型: MAJOR - 重心从需求约束转为当前开发实践
  修改原则: 以 Entry Action 实现、任务管理、代码质量为核心
  参考: UIFragmentHelper.kt、EntryActionAndCategoryRepository.kt、AndroidManifest.xml
-->

# POSLinkUI-Demo 项目宪章

本宪章以**当前开发实践**为核心，规定 Entry Action 实现、任务管理、代码质量等必须遵守的规则。

## 一、Entry Action 实现（核心）

### I. Action → Fragment 映射

- 每个新实现的 Entry Action 必须在 `UIFragmentHelper.kt` 对应 `*_FRAGMENT_MAP` 中增加 action → Fragment 映射。
- 映射来源：`com.pax.us.pay.ui.constant.entry`（constant 库）、`EntryActionAndCategoryRepository.kt`。
- 若 constant 库中已有 action 但 Manifest 未声明，需在 `AndroidManifest.xml` 中补充 activity-alias。

### II. Fragment 实现模式

- **Confirmation 类**：继承 `AConfirmationFragment`，重写 `formatMessage()`，提供 Yes/No 按钮逻辑。参考：`ConfirmBatchCloseFragment`、`ConfirmOnlineRetryFragment`。
- **Option 类**：继承 `BaseOptionFragment` 或参考 `SelectCurrencyFragment`，实现选项列表与选择回调。参考：`SelectRefundReasonFragment`。
- **Text 类**：继承 `ANumTextFragment` 或 `ATextFragment`，配置输入类型与校验规则。参考：`OrigTransIdentifierFragment`、`ReferenceNumberFragment`。

### III. 未实现 Action 优先级

| 优先级 | Action | 理由 |
|--------|--------|------|
| 高 | `ACTION_CONFIRM_BATCH_CLOSE_WITH_INCOMPLETE_TRANSACTION` | 批结安全，避免未完成交易被误结 |
| 高 | `ACTION_CONFIRM_ONLINE_RETRY_OFFLINE` | 联机失败降级，影响交易成功率 |
| 中 | `ACTION_CONFIRM_BATCH_FOR_APPLICATION_UPDATE` | 应用更新前数据一致性 |
| 中 | `ACTION_SELECT_TIP_AMOUNT`、`ACTION_SELECT_CASHBACK_AMOUNT` | 常见餐饮/零售场景 |
| 低 | `ACTION_SELECT_INSTALLMENT_PLAN`、`ACTION_ENTER_VISA_INSTALLMENT_*` | 分期专用，场景较少 |

## 二、Kotlin 任务管理

### IV. 生命周期绑定

- 超时、finish、countdown 等 scheduled task 必须在关联 Activity/Fragment 销毁时**同步取消**（onDestroy 内完成）。
- 同一 Fragment 或 flow 重复调度超时时，新任务**替换**旧任务，旧任务取消。
- 回调不得在目标组件销毁后执行；销毁前必须完成取消。

### V. 后台任务与主线程回调

- 后台 IO/计算必须在非主线程执行，结果回调必须在主线程。
- 支持 lifecycle-bound（自动取消）或 caller-managed（显式取消）。
- 队列满或 executor 拒绝时，fallback 到调用线程执行，保证任务不丢失。

### VI. 用户反馈策略

- **用户主动取消**：可显示 toast/dialog 反馈。
- **系统取消/失败**：仅打日志，不打扰用户。

## 三、代码质量

### VII. Kotlin 惯用法

- 使用属性访问替代 getter 调用：`obj.getX()` → `obj.x`。
- null 判断使用安全调用（`?.`）、Elvis（`?:`）、`let`/`also`；多分支用 `when` 替代 `if-else` 链。
- 变更不得改变可观测行为；现有测试必须通过。

### VIII. SonarQube 合规

- 项目连接部门 SonarQube：http://172.16.3.60:9090。
- Blocker、Critical 必须解决或明确接受并文档化。
- Major、Minor 尽量解决；例外须文档化。

## 四、架构与分层

### IX. 职责边界

- Fragment/Activity 只负责 UI 渲染与导航编排；业务决策、状态迁移放在 ViewModel 或 domain 层。
- 方法职责单一；同时做校验、映射、IO 时须拆分。
- 类内聚；可复用逻辑从 Fragment 抽到 utility 或 ViewModel。

### X. 状态与契约

- 屏幕状态显式建模（`loading`、`ready`、`error`、`completed`），避免散落 boolean。
- 可复用组件须有清晰输入/输出契约（request in、result out）。
- Bundle key 集中校验，避免多处重复字符串字面量。

### XI. 命名

- 使用业务语义命名（如 `TransactionResult`、`EntryRequestBuilder`），避免 `data`、`obj` 等泛化名。

## 五、输入、线程与异常

### XII. 输入校验与 Fail Fast

- 外部输入（Bundle、Intent extras、SDK 回调、网络 payload）须尽早校验。
- 校验失败时 fail fast，打有意义日志；禁止静默吞异常。
- 金额、币种等字段的 fallback 默认值须业务安全。

### XIII. 线程与生命周期

- UI 更新必须在主线程；禁止在主线程做文件 IO、解析、长计算。
- 异步回调执行前须检查 lifecycle 状态，再触碰 Fragment/UI。

## 六、支付与合规（通用）

### XIV. 敏感数据保护

- 禁止日志打印 PAN、track2、PIN block、CVV、密钥；展示须掩码。
- 日志须包含：`txnId/state/event/errorCode/costMs` 等结构化字段。

### XV. 状态与幂等

- 页面跳转须经状态机合法迁移；超时、取消、失败须有确定终态（FAILED/CANCELLED）与回首页策略。
- 联机上送须绑定唯一业务流水；重试前检查是否已成功。

### XVI. 设备 SDK 隔离

- 设备 SDK（检卡/PED/打印）通过 adapter 访问；UI/Domain 不得直接调用设备 API。
- 设备不可用时允许 mock 降级，须打日志且可一键关闭。

### XVII. 8583 与响应码

- 8583 字段须先定义契约（必填域、格式、长度、编码），再实现 pack/unpack。
- 响应码映射须中心化，禁止在 UI 层散落硬编码。

## 七、测试策略

### XVIII. 覆盖要求

- 新变更须覆盖正面路径及至少一个失败路径。
- 涉及金额/币种/输入约束时须有校验测试。
- 支付 bug 修复须加回归测试，否则在 PR 中说明原因与后续计划。

### XIX. 高风险场景

- 超时 + 重试（无重复终态）、重复回调（幂等完成）、lifecycle 中断（旋转/后台）、错误映射一致性。
- 关键验收至少覆盖：成功流、超时、取消、主机拒绝、打印缺纸。

## 八、开发流程

- **构建验证**：变更后执行 `./gradlew assembleDebug`，确保编译通过。
- **文档生成**：public API 须有 KDoc；执行 `./gradlew :app:dokkaGeneratePublicationHtml` 输出至 `doc/javadoc/`。
- **参考规则**：`.cursor/rules/`（android-core、android-architecture、android-testing、payment-project-hard-problems、specify-rules）、`.cursor/skills/`（android-feature-delivery、kotlin-migration-compilation、android-payment-troubleshooting）。

## 治理

- 本宪章优先于项目内其他实践，除非有书面例外。
- 修订须具备：书面理由、批准、受影响 spec 的迁移说明。
- 版本：MAJOR 不兼容变更；MINOR 新增原则；PATCH 澄清与措辞修正。

**版本**：3.0.0 | **批准**：2025-03-20 | **最后修订**：2025-03-20
