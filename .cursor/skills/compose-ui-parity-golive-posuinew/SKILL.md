---
name: compose-ui-parity-golive-posuinew
description: >-
  Compose 页 1:1 还原：以 git 分支 golive/v1.03.00 的 layout/dimens/自定义 View 为权威数值；与 POSUInew（SSIM、Expected_Result、单 case pytest）及 feature-code-generator / test-author 双代理（SDK entry 模块粒度）配合。新会话做 UI 还原 / 修 SSIM 时优先读本 Skill。
---

# Compose 还原 + POSUInew 回归（golive XML 权威源）

## 新会话怎么接上（必读顺序）

1. 读 `specs/002-compose-migration/ui-parity-from-golive-xml.md`（**golive 分支名与金额页映射表**）。
2. 读本 Skill 全文（**POSUInew 通过标准、失败分类、build 节奏**）。
3. 按需读 `@.cursor/agents/feature-code-generator.md`、`@.cursor/agents/test-author.md`（子代理分工与反馈块格式）。
4. 自动提交节奏见 `@.cursor/skills/auto-commit-after-test-author/SKILL.md`（须 test-author 通过后再 commit）。

## 权威尺寸与颜色（优于「从 PNG 量」）

- **Git 分支**：`golive/v1.03.00`（非口语 `golive1.03.00`）。
- **优先级**：对应页面的 **`res/layout/*.xml`** → **`res/values/dimens.xml` / `colors.xml`** → **自定义 View**（如 `TextField.kt` 里的 `minHeight`、背景 drawable）。  
- **常见坑**：金额输入框在 golive 上背景色为 **`pastel_on_background` (#DBD4D9)**；不要误用 **`pastel_accent_base`** 或 Compose 里与之接近的 **`SurfaceMutedColor` (#E4E1E3)** 充当同一控件底色，否则整屏与旧截图差异大。
- **金额页速查**：`fragment_amount.xml` + `TextField.kt`（`minHeight = button_height` = **54dp**）+ `rounded_corner_on_background`（**2dp** 描边 + **8dp** 圆角）。

## POSUInew：什么叫「通过」

- 工程路径：`D:\Project\US\POSUInew`（与 POSLinkUI-Demo 并列时按本机实际路径）。
- **主图对比**：`src/Compare/image_compare.py` 中多通道 **SSIM**，默认阈值 **0.97**；低于阈值则 `AssertionError`（消息含 `Failed:` 与 `SSIM=`）。
- **期望图路径**：脚本使用 **`POSUInew/Expected_Result/{model}_ShotScreen/{trans_type}/...`**（与 Demo 仓库根下 `Expected_Result` 不是同一套目录，勿混用）。
- **缺期望图**：回归模式会 `pytest.fail` 提示运行 **`--save-expect`**；此类失败属 **基线/数据**，不要默认当业务代码 bug。
- **单页回归**：勿用宽泛 `--match` 导致一次跑几十条；应精确到 **pytest node id**，例如：  
  `test_adb_cases.py::test_transaction_cases[CREDIT_SALE_amount_main]`  
  报告在 `POSUInew/reports/*.html`。

### Batch5 / CREDIT_SALE 复测轮次（与报告命名）

- **第 4 轮（r4）** 与 Pin 等并列复测时，**须包含**  
  `test_adb_cases.py::test_transaction_cases[CREDIT_SALE_Swipe_Tap_Insert_Manual_no_nfc_main]`，  
  避免仅记录 r1–r3（`batch5_p1_r{1,2,3}_Swipe_Tap_Insert_Manual_no_nfc_main.html`）而遗漏本 case 的轮次归属。
- **r4 报告文件名示例**：`reports/batch5_p1_r4_Swipe_Tap_Insert_Manual_no_nfc_main.html`（与同 case 的 r1–r3 前缀一致，仅 `_r4_` 区分轮次）。

## 截图状态与 SSIM

- golive 的 `TextField` 会 **聚焦并尝试弹出软键盘**；若 `Expected_Result` 在「键盘已打开」下录制，Compose 侧须 **同一交互时序** 再截图，否则全图 SSIM 会极低。
- **水印**：`transMode` 等导致的水印差异，团队约定可排除时再排除；默认仍以全图 SSIM 为准则须与测试对齐启动参数。

## 失败分析（test-author 强制）

每条失败须区分，**禁止只报 failed 数量**：

| 类型 | 含义 | 典型信号 |
|------|------|----------|
| **A** | 基线/期望图 | 期望 PNG 缺失、`--save-expect` 提示、产品已改版需重录 |
| **B** | 实现/路由/Intent | SSIM 不达标但期望存在、错屏、extras 键名与 `EntryExtraData` 不一致 |
| **C** | 测试数据 | Excel `adb_commands` 与 Manifest alias 不一致、空格未转义 |
| **D** | 环境/时序 | 多设备、动画、截图过早、logcat 为空 |

**输出**：必须包含「失败反馈块」——断言原文、SSIM 数值与阈值、Actual/Expected 路径、pytest node id、报告 HTML 路径、**第一阻塞原因（仅一条）**、建议下一步（改代码 / `--save-expect` / 修 Excel / 固定环境）。

## 与 feature-code-generator 的回炉

- 收到上述反馈块后：**先分类**再改代码；**A** 类由测试侧确认后重录期望图，实现侧不应用改 UI「糊」过旧图。
- **一轮只改一个 `action + category` 页面**；改 action 前须已有 **精确原型图或 golive XML 映射**。

## 构建节奏（与两子代理一致：SDK entry 模块）

以 **`feature-code-generator` / `test-author`** 文档为准：**每完成一个 SDK entry 模块**（`com.pax.us.pay.ui.constant.entry.*`）在交付或交测试前至少 **`assembleDebug`**；触及路由主干/主题时再跑单测。若改动 **全局 Theme、`DesignTokens`、路由主干、公共按钮组件**，可 **提前** 触发 build，不必等模块结束。

## 相关仓库文档链接

- `specs/002-compose-migration/ui-parity-from-golive-xml.md`
- `specs/002-compose-migration/quickstart.md`（附录已链到本 Skill）
- `specs/002-compose-migration/device-profiles.md`（P0 机型与 `DeviceLayoutSpec`）
