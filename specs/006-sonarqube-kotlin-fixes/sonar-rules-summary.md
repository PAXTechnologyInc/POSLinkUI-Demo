# SonarQube 规则汇总

**来源**：http://172.16.3.60:9090  
**获取时间**：2025-03-20  
**方式**：SonarQube MCP（已登录）  
**特性**：006-sonarqube-kotlin-fixes

---

## 1. 质量门禁（Quality Gates）

| 名称 | 默认 | 说明 |
|------|------|------|
| PAX Sonar way | ✅ 是 | 部门默认质量门禁 |
| PAX Sonar way + CI_Temp_Whitelist | 否 | |
| PAX Sonar way + HK_LUSO_EDC_A | 否 | |
| PAX Sonar way + Worldline | 否 | |
| PAX Sonar way + whitelist | 否 | |
| PAX_CN Sonar way | 否 | |
| Sonar way | 否 | 内置 |

---

## 2. POSLinkUI-Demo 质量门禁状态

**状态**：✅ **OK**（通过）

| 指标 | 阈值 | 实际值 | 状态 |
|------|------|--------|------|
| duplicated_lines_density | 20 | 9.4 | OK |
| comment_lines_density | 5 | 7.3 | OK |
| software_quality_blocker_issues | 10 | 5 | OK |
| new_violations | 0 | 0 | OK |
| 其他 | - | - | OK |

---

## 3. Kotlin 相关规则（与 006 规格相关）

### kotlin:S6518 - 索引访问应使用下标运算符
- **严重度**：MAJOR
- **说明**：`list.get(1)` → `list[1]`，`map.get("b")` → `map["b"]`

### kotlin:S6519 - 结构相等应使用 == 或 !=
- **严重度**：MAJOR
- **说明**：`a.equals(b)` → `a == b`，`!a.equals(b)` → `a != b`

### kotlin:S1128 - 移除未使用的 import
### kotlin:S1125 - 避免冗余的布尔字面量
### kotlin:S1481 - 移除未使用的局部变量
### kotlin:S6517 - 单函数接口应声明为 fun interface
### kotlin:S6516 - 函数式接口实现应使用 lambda

---

## 4. Java 常见规则（POSLinkUI-Demo 中触发较多）

| 规则键 | 名称 | 严重度 |
|--------|------|--------|
| java:S103 | 行过长（>120 字符） | MAJOR |
| java:S121 | 缺少花括号 | MINOR |
| java:S113 | 文件末尾缺少换行 | MINOR |
| java:S122 | 缩进不一致 | MINOR |
| java:S3599 | 换一种方式初始化实例 | MINOR |
| java:S3776 | 认知复杂度过高 | CRITICAL |
| java:S2039 | 成员可见性 | MAJOR |
| java:S1117 | 局部变量遮蔽字段 | MAJOR |
| java:S1118 | 工具类应有私有构造 | MINOR |
| java:S1171 | 私有静态方法应移除 | MINOR |
| java:S1066 | 条件运算符过多 | MAJOR |

---

## 5. 关于「getter 改为属性访问」

- `obj.getProperty()` → `obj.property`
- `obj.isEnabled()` → `obj.isEnabled`（或 `obj.enabled` 视属性名而定）

SonarQube Kotlin 插件中可能对应不同规则键。IntelliJ 对应检查：`UsePropertyAccessSyntax`。

---

## 6. 后续建议

1. 按 006 规格修复 POSLinkUI-Demo 的 Sonar 问题
2. 优先处理 CRITICAL、BLOCKER，再处理 MAJOR
3. Kotlin 文件重点：S6518、S6519、S1128、S1125、S1481，以及 getter→属性访问
4. Java 遗留文件：S103（行长）、S121（花括号）、S3776（复杂度）等
