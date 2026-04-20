---
name: posuinew-ssim-gate-bug-report
description: >-
  在 POSUInew 工程跑完 pytest 截图/SSIM 回归后，解析 HTML 报告与断言，按门禁（SSIM≥0.97、失败分类 A/B/C/D）生成结构化 Markdown Bug 报告并落盘到 specs/posuinew-gate-reports/。用于「跑完脚本 → 出可交接的测试缺陷单」；与 ui-parity-golive-bugfix-from-report 成对使用。
---

# POSUInew SSIM 门禁与 Bug 报告（测试 Skill）

## 何时使用

- 用户要求跑完 POSUInew 真机/pytest 截图对比，并**产出测试侧缺陷文档**（`.md`）。
- 需要将 **pytest HTML 报告**中的失败项转化为**可开发消费的 Bug 单**（含门禁判定、证据路径、分类）。

## 必读依赖

- 流程与通过标准：`@.cursor/skills/compose-ui-parity-golive-posuinew/SKILL.md`
- 反馈块字段与 Excel/adb：`@.cursor/agents/test-author.md`

## 门禁定义（与 POSUInew 脚本一致）

| 项 | 说明 |
|----|------|
| **主图 SSIM** | `src/Compare/image_compare.py` 多通道对比，默认阈值 **0.97**；低于即 **未通过门禁**（实现类问题需修代码；基线类见下）。 |
| **期望图** | 路径在 **`POSUInew/Expected_Result/{model}_ShotScreen/{trans_type}/...`**（勿与 Demo 仓库根下其它 `Expected_Result` 混淆）。 |
| **缺期望图** | 提示 `--save-expect` → 分类 **A（基线/数据）**，不默认记为「应用逻辑 Bug」。 |
| **单 case 范围** | 优先精确 **pytest node id**，避免宽泛 `--match` 一次扫全表。 |

### 失败分类（写入每条 Bug 的 `failure_class`）

| 类 | 含义 | 门禁是否记「须开发修」 |
|----|------|------------------------|
| **A** | 基线/期望图缺失或须重录 | 否（先确认是否 `--save-expect` 或规格改版） |
| **B** | 实现/路由/Intent/Extras 与期望不符 | **是**（默认） |
| **C** | 测试数据（Excel adb 与 Manifest、空格转义等） | 视团队分工，可记「测试数据任务」 |
| **D** | 环境/时序（截图过早、多机、键盘状态不一致） | 先稳定环境；仍不达标再归 **B** |

## 工作流程

1. **确认 POSUInew 根目录**  
   以用户本机为准（Skill `compose-ui-parity-golive-posuinew` 中的示例路径仅作参考）；在已激活 `.venv` 下执行。

2. **跑完回归**  
   按用户给定的 `--match` / node id / `run_case_by_intent.py` 参数执行，**直至 pytest 结束**（通过或失败均生成报告）。报告通常在 **`POSUInew/reports/*.html`**。

3. **解析报告**  
   - 从 HTML 或控制台日志提取：**断言原文**、**SSIM 与阈值**、**Actual / Expected PNG 路径**、**pytest node id**、**报告 HTML 文件名**。  
   - 若有多条失败：**逐条**处理，禁止只汇总「Failed N」。

4. **应用门禁**  
   - SSIM &lt; 0.97（或脚本配置的阈值）→ 记 **未通过主图门禁**。  
   - 结合分类 A/B/C/D，判断是否进入「开发待修」列表（通常 **B** 与确认非基线后的 **D**）。

5. **落盘 Markdown**  
   - 目录：**`specs/posuinew-gate-reports/<YYYY-MM-DD>/`**（或用户指定子目录）。  
   - **汇总文件**：`gate-run-summary.md`（一次运行的元数据、总通过/失败、报告路径列表）。  
   - **单条缺陷**：`BUG-<序号或case简名>.md`，内容遵循仓库内模板 **`specs/posuinew-gate-reports/_template-bug-report.md`**。  
   - 每个 `.md` 文件须含 YAML frontmatter（`summary`、`read_when`、`title`），见模板。

### 5b. 视觉与 SSIM（**强制**，供开发 Skill 直接对照）

每条 **`BUG-*.md`** 必须同时包含以下内容，**不可仅写路径而不给图与分数**：

1. **SSIM 数值**  
   - 写明 **`gate_ssim_threshold`**（如 0.97）与脚本断言中的 **`gate_ssim_actual`**（失败时的实际 SSIM）。  
   - 若报告/HTML 中还有 per-channel 或附加说明，可一并摘要到 **`assertion_excerpt`**。

2. **真实截图与预期截图**  
   - **推荐（默认）**：将 POSUInew 产出的 **Actual**、**Expected** PNG **复制**到本目录下，例如：  
     `specs/posuinew-gate-reports/<YYYY-MM-DD>/assets/<bug-slug>/actual.png`  
     `specs/posuinew-gate-reports/<YYYY-MM-DD>/assets/<bug-slug>/expected.png`  
   - 在 Markdown 正文中用**相对路径嵌入**，便于仓库内预览与 Agent 读取：  
     `![Actual](./assets/<bug-slug>/actual.png)`  
     `![Expected](./assets/<bug-slug>/expected.png)`  
   - **仍须保留** POSUInew 侧的原始绝对/工程内路径于「证据」小节，便于复盘与复跑。

3. **例外**  
   - 若环境禁止拷贝二进制入仓库：须在报告中说明原因，并改为**可访问的绝对路径**嵌入（同一工作区内）或附件清单；**禁止**省略图片与 SSIM 仅留文字描述。

6. **输出告知用户**  
   - 汇总路径、单条 Bug 文件列表、**明确标出「须开发修复」**的条目（引用 `ui-parity-golive-bugfix-from-report` Skill）。

## Bug 报告最小字段（单条 `.md`）

- `title` / `summary` / `read_when`（frontmatter）
- **pytest node id**
- **failure_class**（A|B|C|D）
- **gate**: **SSIM 实际值、阈值**、是否通过（数字必须写出）
- **assertion_excerpt**（断言原文摘要）
- **paths**: Actual PNG、Expected PNG、HTML 报告（原始路径）
- **嵌入式对比图**：Actual / Expected 各至少一张（见上文 **5b**，复制到 `assets/` 并 `![...](...)`）
- **adb_or_match_hint**（若可从 Excel/日志还原）
- **recommended_next**（改代码 / 重录期望 / 修 Excel / 固定环境）

## 禁止

- 不把脚本失败**一律**写成「应用必改」；**A 类**须单独说明。
- 不省略 **Actual/Expected 路径**、**node id**、**SSIM 数值**与**嵌入图**（开发 Skill 依赖可视化对比）。

## 相关

- 开发侧根据 `.md` 修 Bug：`@.cursor/skills/ui-parity-golive-bugfix-from-report/SKILL.md`
