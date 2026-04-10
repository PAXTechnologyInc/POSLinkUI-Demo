# Sonar 规则全量导出（与项目 Quality Profile 一致）

本目录下的 `rules-kotlin.*`、`rules-java.*` **不由 CI 自动生成**；需在**能访问部门 Sonar** 且持有 **User Token** 的机器上执行脚本，以与服务器上 `POSLinkUI-Demo` 当前绑定的 Profile **完全一致**。

## 前置条件

- Sonar：`sonar-project.properties` 中的 `sonar.host.url`（默认 `http://172.16.3.60:9090`）
- Token：SonarQube **用户令牌**（具备查看质量配置 / 分析相关权限即可，勿写入仓库）
- PowerShell 5.1+

## 命令

在仓库根目录（PowerShell；脚本需 **UTF-8 BOM**，否则中文环境下可能解析失败）：

```powershell
$env:SONAR_TOKEN = '<你的_Sonar_User_Token>'
powershell -ExecutionPolicy Bypass -File .\scripts\fetch-sonar-profile-rules.ps1
```

或使用 Python（不依赖 PowerShell 解析，并会额外生成 `doc/sonarqube-rules-summary.md`）：

```powershell
$env:SONAR_TOKEN = '<你的_Sonar_User_Token>'
python .\scripts\fetch_sonar_profile_rules.py
```

可选参数：

```powershell
.\scripts\fetch-sonar-profile-rules.ps1 -SonarHost "http://172.16.3.60:9090" -ProjectKey "POSLinkUI-Demo" -SonarToken $env:SONAR_TOKEN
```

## 生成文件

| 文件 | 说明 |
|------|------|
| `rules-kotlin.md` / `rules-kotlin.tsv` | Kotlin Profile 中**已激活**规则全表 |
| `rules-java.md` / `rules-java.tsv` | Java Profile 中**已激活**规则全表 |
| `EXPORT_META.json` | 导出时间、Profile key/name、条数 |

生成后可将 `.md`/`.tsv` **提交入库**，便于离线检索与让 Cursor 对照修复；**不要**提交 Token。

## 与 `.cursor/rules/sonarqube-quality-gate.mdc` 的关系

- **quality-gate.mdc**：PAX 门禁阈值 + **高频规则摘要**（给 AI/人工快速对齐）。
- **本导出**：服务器上**实际激活**的 Kotlin/Java 规则**完整列表**（条数可与文档中「135 / 1462」对照；若部门改 Profile 以本导出为准）。

## 公开规则索引（非你们 Profile 子集）

与具体 Profile **无关**的 Sonar 官方 Kotlin 规则说明见：<https://rules.sonarsource.com/kotlin/>  
Java：<https://rules.sonarsource.com/java/>  

仅作查阅；**是否启用**以你们实例 Quality Profile 为准。
