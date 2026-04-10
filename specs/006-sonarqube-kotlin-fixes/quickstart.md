# 快速开始：SonarQube 规则获取

**特性**： 006-sonarqube-kotlin-fixes  
**日期**: 2025-03-20

## 前置条件

- PowerShell 5.1+（Windows）或 PowerShell Core 7+（跨平台）
- 可访问 SonarQube 服务器 http://172.16.3.60:9090
- 已获取 SonarQube Token（My Account → Security → Generate Tokens）

## 快速开始

### 方式 1：环境变量 + 脚本

```powershell
$env:SONAR_TOKEN = "your-token-here"
cd d:\Project\US\POSLinkUI-Demo
.\scripts\fetch-sonar-rules.ps1
```

输出默认写入 `sonar-rules.json`。

### 方式 2：命令行传参

```powershell
.\scripts\fetch-sonar-rules.ps1 -Token "your-token" -OutputFile "my-rules.json"
```

### 方式 3：通过 MCP

若已配置 SonarQube MCP，可直接调用 `show_rule`、`list_quality_gates` 等工具，无需脚本凭据。

## 连接测试

```powershell
.\scripts\test-sonar-connection.ps1
```

或直接请求 API：

```powershell
Invoke-WebRequest -Uri "http://172.16.3.60:9090/api/system/status" -UseBasicParsing
```

返回 200 且 `status: "UP"` 表示连接正常。

## SonarQube 分析（Gradle）

```powershell
# 需配置 sonar.token 或 SONAR_TOKEN
.\gradlew -Psonar sonarQubeAnalyze
```

首次运行会下载 SonarQube 插件；若网络受限，可临时注释 `build.gradle` 中的 `apply from: file('gradle/sonar.gradle')` 相关逻辑。

## 故障排查

| 现象 | 可能原因 | 处理 |
|------|----------|------|
| 401 未授权 | Token 无效或未配置 | 检查 SONAR_TOKEN 或 -Token 参数 |
| 连接超时 | 网络/VPN 问题 | 确认可访问 172.16.3.60:9090 |
| 脚本报错 | PowerShell 版本 | 使用 5.1+ 或 Core 7+ |
