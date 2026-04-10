# CLI 契约：fetch-sonar-rules.ps1

**特性**： 006-sonarqube-kotlin-fixes  
**日期**: 2025-03-20

## 命令

```text
.\fetch-sonar-rules.ps1 [-BaseUrl <string>] [-Token <string>] [-OutputFile <string>]
```

## 参数

| 参数 | 类型 | 默认值 | 必填 | 说明 |
|------|------|--------|------|------|
| BaseUrl | string | http://172.16.3.60:9090 | 否 | SonarQube 服务器 URL |
| Token | string | $env:SONAR_TOKEN | 否 | 认证 Token；未提供时尝试 sonar-project.properties |
| OutputFile | string | sonar-rules.json | 否 | 输出 JSON 文件路径 |

## 环境变量

| 变量 | 说明 |
|------|------|
| SONAR_TOKEN | 认证 Token；优先于配置文件 |

## 退出码

| 码 | 含义 |
|----|------|
| 0 | 成功 |
| 1 | 失败（凭据缺失、认证失败、网络错误等） |

## 输出

- 成功：JSON 文件写入 OutputFile；控制台输出规则数量
- 失败：控制台输出错误信息，退出码 1

## 行为

1. 解析凭据（env > config）
2. 可选：先调用 /api/system/status 验证连接
3. 循环请求 /api/rules/search 分页，合并结果
4. 网络/超时失败时重试（最多 2 次，间隔 3–5 秒）
5. 将合并后的规则数组写入 JSON 文件
