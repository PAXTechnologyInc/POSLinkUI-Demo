# 研究：Java 转 Kotlin 迁移

**特性**： 001-kotlin-migration  
**日期**: 2025-03-16

## 1. IntelliJ IDEA Java to Kotlin 转换

### Decision

使用 IntelliJ IDEA 的 Code → Convert Java File to Kotlin File 一次性转换全部 Java 文件。转换顺序由 Idea 自动处理依赖关系；若遇循环依赖或单文件转换失败，人工介入（保留 Java 或手工转换）。

### Rationale

- Idea 的转换器成熟，可处理大部分常见 Java 模式
- 一次性转换与 spec clarify 结论一致，减少分批带来的混合状态
- 项目已有 kotlin-gradle-plugin，无需额外配置

### Alternatives Considered

- 分批转换：spec 已明确一次性，不采用
- 手工重写：工作量大，Idea 转换 + 审查更高效

---

## 2. SonarQube 与 Kotlin

### Decision

- 引用部门现有质量门禁/规则集
- 本地：使用 SonarQube Scanner 或 Gradle 插件在开发时扫描
- CI：MR/PR 合并前执行 SonarQube 检查
- 基线：使用 SonarQube New Code 概念，仅要求新引入的 Kotlin 代码不新增问题；历史 Java 遗留问题可后续迭代

### Rationale

- 部门已有配置，可直接引用，减少从零配置
- 本地 + CI 双重检查与 spec clarify 一致
- New Code 基线符合「仅要求不新增问题」的约束

### Alternatives Considered

- 全量门禁：会阻塞合并（历史问题未修复），与 spec 不符
- 仅 CI 检查：本地反馈慢，不利于开发时及时修正

---

## 3. Kotlin 代码审查重点

### Decision

审查重点按 android-code-review SKILL 与 payment-project-hard-problems 规则：空安全、命名、控制流、生命周期、支付安全。具体包括：

- 减少 `!!`，优先 `?.`、`?:`、`let`、`also`
- 避免冗余 `?.let { it }` 等 Idea 转换常见冗余
- `when` 分支完整、无 fall-through 误用
- 生命周期：Fragment/Activity 中避免在 onDestroy 后更新 UI
- 支付安全：禁止日志打印 PAN、PIN block、CVV 等

### Rationale

与 spec FR-005 及 doc 审查重点一致；支付类应用需严格遵守安全与合规要求。

---

## 4. SonarQube New Code 配置

### Decision

在 SonarQube 中配置 New Code 周期（如本次 MR 的变更、或最近 N 天），使质量门禁仅针对 New Code。历史 Java 遗留问题不阻塞合并。

### Rationale

与 spec「仅要求不新增问题；历史遗留可后续迭代修复」一致。需在 plan 的 tasks 中明确：配置 SonarQube New Code 周期或引用部门已有配置。
