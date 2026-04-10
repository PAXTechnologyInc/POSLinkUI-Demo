# 数据模型：SonarQube 连接测试与规则获取（已融合至 006）

**特性**： 006-sonarqube-kotlin-fixes  
**日期**: 2025-03-20

## 实体

### SonarQube 服务器 (Server)

| 属性 | 类型 | 说明 |
|------|------|------|
| url | string | 基础 URL，如 http://172.16.3.60:9090 |
| version | string | 从 /api/system/status 获取 |
| status | enum | UP / DOWN / 未知 |

---

### 规则 (Rule)

| 属性 | 类型 | 说明 |
|------|------|------|
| key | string | 规则键，如 kotlin:S6518 |
| name | string | 规则名称 |
| severity | string | BLOCKER / CRITICAL / MAJOR / MINOR / INFO |
| type | string | BUG / VULNERABILITY / CODE_SMELL 等 |
| lang | string | 语言键，如 kotlin、java |
| langName | string | 语言显示名 |
| htmlDesc | string | 可选，规则描述 HTML |

---

### 凭据 (Credentials)

| 属性 | 类型 | 说明 |
|------|------|------|
| token | string | SonarQube Token |
| source | enum | env / config_file / mcp |

---

## 状态转换

- **连接测试**: 未测试 → 成功(UP) / 失败(超时/错误)
- **规则获取**: 未开始 → 获取中(分页) → 完成 / 失败(认证/网络)

---

## 验证规则

- URL 必须以 `http://` 或 `https://` 开头
- Token 非空（通过脚本获取时）
- 输出文件路径可写
