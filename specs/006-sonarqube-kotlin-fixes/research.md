# 研究：SonarQube 连接测试与规则获取（已融合至 006）

**特性**： 006-sonarqube-kotlin-fixes  
**日期**: 2025-03-20

## 1. SonarQube REST API 规则获取

**Decision**: 使用 `/api/rules/search` 分页获取，参数 `ps`（pageSize）、`p`（page）。

**Rationale**:
- SonarQube Web API 标准路径，文档明确
- 支持 `ps` 最大 500，分页稳定
- 返回 `total` 与 `rules` 数组，便于循环合并

**Alternatives considered**:
- `/api/qualityprofiles/export`：导出质量配置，非规则列表，不满足「全服务器规则」需求
- MCP `show_rule`：需已知 rule key，无法批量枚举

---

## 2. 凭据读取优先级

**Decision**: 环境变量 `SONAR_TOKEN` 优先；可选从 `sonar-project.properties` 读取 `sonar.token`。

**Rationale**:
- 环境变量适合 CI、不落盘，符合安全实践
- `sonar-project.properties` 与 Sonar 扫描器一致，便于复用
- MCP 已配置 Token，通过 MCP 调用时无需脚本凭据

---

## 3. 重试策略

**Decision**: 简单重试：最多 2 次，间隔 3–5 秒；仅对网络/超时类错误重试，401 等认证错误不重试。

---

## 4. 输出格式

**Decision**: JSON 为主格式；字段至少包含 `key`、`name`、`severity`、`lang`、`type`。

---

## 5. 连接测试实现

**Decision**: 调用 `GET /api/system/status` 验证；返回 200 且 `status=UP` 视为成功。
