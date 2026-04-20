---
summary: "单条 POSUInew SSIM/截图失败的结构化缺陷模板，供测试 Skill 复制填写、开发 Skill 解析。"
read_when:
  - 编写新的 BUG-*.md
  - 实现 posuinew-ssim-gate-bug-report 输出格式
title: "门禁 Bug 报告模板（单条）"
---

# BUG-<序号或简名>

> 由 `posuinew-ssim-gate-bug-report` 生成时请删除本提示行。

## 元数据

| 字段 | 值 |
|------|-----|
| **pytest_node_id** | 例：`test_adb_cases.py::test_transaction_cases[CREDIT_SALE_amount_main]` |
| **failure_class** | A \| B \| C \| D（见 compose-ui-parity-golive-posuinew） |
| **gate_ssim_threshold** | 0.97（或脚本当时配置，须写数字） |
| **gate_ssim_actual** | 例：0.84（须与断言/HTML 一致） |
| **gate_passed** | 否 |

## 视觉对比（强制：嵌入图 + SSIM）

以下 SSIM 与图片为开发 Skill 的主要对照依据；**须**同时保留「嵌入图」与下方「原始路径」。

**本次对比 SSIM**：实际 **gate_ssim_actual** vs 阈值 **gate_ssim_threshold**（未通过则 gate_passed = 否）。

### 嵌入图（推荐：复制到本目录 `assets/<slug>/` 后引用）

| | 预览 |
|---|------|
| **Actual（真实截图）** | ![Actual](./assets/<slug>/actual.png) |
| **Expected（期望截图）** | ![Expected](./assets/<slug>/expected.png) |

生成报告时：将 `<slug>` 换为短标识（如 `CREDIT_SALE_amount_main`），并把 POSUInew 生成的 PNG **复制**到对应 `assets/...` 路径。

## 证据（原始路径，便于复跑）

- **assertion_excerpt**：`AssertionError: ... SSIM=...`
- **actual_png**：`POSUInew/Actual_Result/...`
- **expected_png**：`POSUInew/Expected_Result/...`
- **pytest_html_report**：`POSUInew/reports/....html`

## 业务线索（便于开发定位）

- **action_substring**（若有）：
- **category**（若有）：
- **adb_or_extras_note**（键名是否与 `EntryExtraData` 一致）：

## 推荐下一步

- [ ] **B/D 类**：转 `ui-parity-golive-bugfix-from-report`，对照 `golive/v1.03.00` 布局与路由。
- [ ] **A 类**：确认是否 `--save-expect` 或规格改版重录，**勿**默认改应用糊弄旧图。
- [ ] **C 类**：修 `test_cases.xlsx` / Manifest alias / 转义。

## 开发修复回填（可选）

修复后由开发者或 CI 填写：

- **fix_commit**：
- **verified_node_id**：
- **备注**：
