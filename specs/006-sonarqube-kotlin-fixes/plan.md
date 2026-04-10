# 实施计划： SonarQube 集成、规则获取与 Kotlin 代码质量

**分支**: `006-sonarqube-kotlin-fixes` | **日期**: 2025-03-20 | **规格**: [spec.md](./spec.md)  
**输入**：功能规格说明，来源 `/specs/006-sonarqube-kotlin-fixes/spec.md`  
**合并说明**：已融合原 007（连接测试与规则获取）内容。

## 摘要

1. **连接与规则（原 007）**：实现 SonarQube 连接验证与全量规则获取脚本；支持环境变量/配置文件凭据、自动分页、简单重试，输出 JSON。
2. **集成与修复（原 006）**：配置项目连接 SonarQube、解决全部 Sonar 问题、Kotlin 属性访问与空安全改进。

## 技术上下文

**主项目（Android/Kotlin）**  
**语言/版本**: Kotlin 1.9.22 / 2.1.0，JVM target 1.8  
**主要依赖**: AndroidX Fragment/AppCompat/Lifecycle、Compose、Logger、Coil、POSLink constant  
**存储**: N/A  
**测试**: 单元测试、仪器测试；SonarQube 分析  
**目标平台**: Android  
**项目类型**: Android 应用  

**脚本工具（连接/规则）**  
**语言/版本**: PowerShell 5.1+ / PowerShell Core 7+  
**主要依赖**: 无外部依赖（Invoke-WebRequest、ConvertFrom-Json）  
**存储**: N/A（JSON 文件输出）  
**目标平台**: Windows / 跨平台  
**项目类型**: CLI / 脚本  
**约束**: 超时 30s/请求；重试最多 2 次，间隔 3–5 秒  

## 宪章检查

- **I. 需求可测试性**：✅
- **II. 需求无歧义**：✅
- **III. 需求与技术无关**：✅
- **IV. 需求范围约束**：✅
- **V. 合规性需求**：N/A（支付相关约束已由项目规则覆盖）

无违规。

## 项目结构

### 文档（本特性）

```text
specs/006-sonarqube-kotlin-fixes/
├── plan.md
├── spec.md
├── research.md
├── data-model.md
├── quickstart.md
├── sonar-rules-summary.md
├── contracts/
├── checklists/
└── tasks.md
```

### Source Code

```text
app/                          # Android 主项目
├── src/main/...
└── build.gradle

scripts/                      # SonarQube 脚本（原 007）
├── fetch-sonar-rules.ps1
└── test-sonar-connection.ps1
```

## 复杂度跟踪

无违规需记录。
