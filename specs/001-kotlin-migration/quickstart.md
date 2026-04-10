# 快速开始：Java 转 Kotlin 迁移

**特性**： 001-kotlin-migration  
**日期**: 2025-03-16

## 前置条件

- IntelliJ IDEA（推荐 2023.x 及以上）或 Android Studio
- **JDK 11+**（Android Gradle Plugin 7.4.0 要求；JDK 8 会导致构建失败）
- 项目可正常构建（`./gradlew assembleDebug`）

## 步骤 1：执行 Idea 转换

1. 在 IDEA 中打开项目
2. 选中 `app/src/main/java/com/paxus/pay/poslinkui/demo/` 目录
3. 右键 → **Refactor** → **Convert Java File(s) to Kotlin File(s)**
4. 或对单个文件：**Code** → **Convert Java File to Kotlin File**
5. 一次性转换全部文件时，可多选全部 Java 文件后执行批量转换

## 步骤 2：解决编译错误

1. 执行 `./gradlew assembleDebug`
2. 根据编译错误逐项修复（常见：类型推断、互操作、平台类型）
3. 若遇 Idea 无法转换的文件，按 plan 策略处理（保留 Java 或手工转换）

## 步骤 3：格式化与 Lint

1. 格式化：**Code** → **Reformat Code** 或 `Ctrl+Alt+L`
2. 执行 Lint：`./gradlew lint`（零错误为目标；警告可暂时豁免）

## 步骤 4：SonarQube 检查

1. 配置 SonarQube Scanner 或 Gradle 插件（引用部门配置）
2. 本地执行扫描，确认 New Code 无新增问题
3. 提交 MR/PR 前，CI 将再次执行 SonarQube 检查

## 步骤 5：功能验证

1. 安装应用：`./gradlew installDebug`
2. 手动执行 Entry 流程（金额输入、确认、状态页等）
3. 验证超时、取消、失败等边界流程

## 构建环境（JDK 11+）

Android Gradle Plugin 7.4.0 要求 JDK 11 及以上。若当前为 JDK 8，可在项目根目录 `gradle.properties` 中指定：

```properties
# 取消注释并填入本机 JDK 11+ 路径
# org.gradle.java.home=C:/Program Files/Java/jdk-11
```

或在系统环境变量中设置 `JAVA_HOME` 指向 JDK 11+。

## 常见编译问题与排查

| 现象 | 可能原因 | 处理建议 |
|------|----------|----------|
| `Unresolved reference` / 类型推断失败 | Idea 转换后平台类型、互操作 | 见 [kotlin-migration-compilation SKILL](../../.cursor/skills/kotlin-migration-compilation/SKILL.md) |
| `!!` 导致 NPE 或编译警告 | 滥用非空断言 | 优先 `?.`、`?:`、`requireNotNull`、`requireContext()`；见 [modification-patterns.md](modification-patterns.md) §0 |
| `getXxx()` 需改为属性 | Kotlin 属性语法 | `getMaxLength()`→`override val maxLength`；`getCurrency()`→`override val currency` |
| `when` 不完整 / 非法 `break` | Java 控制流差异 | 补全 `else` 分支；`break` 改为 `return` 或 `else` |
| D8 dexing / kotlin-stdlib 版本冲突 | Gradle/JDK 版本 | 确认 JDK 11+；检查 `kotlin-stdlib` 与 Kotlin 版本一致 |
| Compose 内联错误（若已启用 Compose） | Kotlin/Compose 版本不匹配 | 见 [002-compose-migration/quickstart.md](../002-compose-migration/quickstart.md) 依赖版本 |

**速查**：编译错误修复模式与执行顺序见 [kotlin-migration-compilation SKILL](../../.cursor/skills/kotlin-migration-compilation/SKILL.md)。

## 参考

- [spec.md](spec.md) - 功能规格
- [plan.md](plan.md) - 实施计划
- [modification-patterns.md](modification-patterns.md) - 修改模式与空安全规范
- [research.md](research.md) - 研究结论
- [kotlin-migration-compilation SKILL](../../.cursor/skills/kotlin-migration-compilation/SKILL.md) - 编译错误修复速查
