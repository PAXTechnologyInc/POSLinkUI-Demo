---
name: compose-orientation-parity-golive
description: >-
  针对 Compose 重构页面执行 golive 横竖屏 1:1 对齐：以 res/layout 与 res/layout-land（及对应限定符资源）为双权威源，避免只对齐竖屏导致横屏机型回归。用户提到横屏机型、landscape、横竖屏适配、键盘比例、文本换行或点击区域异常时优先使用。
---

# Compose 横竖屏 1:1 对齐（golive 资源分流版）

## 何时启用

- 用户反馈“竖屏正常、横屏异常”。
- 涉及任意横屏机型、`screenWidthDp > screenHeightDp`、`layout-land` 或其他方向限定符资源。
- 典型症状：文本被挤换行、键盘比例不对、点击区域错位、同一 action 在横屏机型回归失败。

## 基线原则（必须执行）

1. **双基线对照**：同时读取 golive 的 `res/layout/*.xml` 与 `res/layout-land/*.xml`，禁止只看一个方向。
2. **资源优先级**：`layout/layout-land` -> `values/dimens + drawables` -> 旧版 Java/Fragment 事件链。
3. **Compose 分支对齐**：在 Compose 中显式区分横竖屏分支，不能把横屏修复直接覆盖竖屏默认行为。
4. **最小改动**：仅修改目标 action 对应页面，避免牵连全局 `DesignTokens`。
5. **设备无关**：规则对所有横屏设备生效，禁止写死单一机型型号、分辨率或品牌。

## 实施流程（按顺序）

1. **定位页面**
   - 找当前 action/category 对应 Compose 页面与路由。
   - 找 golive 对应 Fragment 与 XML（含 `layout-land` 版本）。

2. **建立横竖屏映射表**
   - 左右分栏/上下结构
   - 关键控件宽高、间距、weight、minHeight
   - 文案行数与 ellipsize
   - 触摸/按键事件链（如 secure area、pin key layout）

3. **Compose 实现**
   - 用 `LocalConfiguration` 或等价条件判断方向。
   - 允许叠加设备档案（如 `DeviceProfileRegistry`）做微调，但方向分支始终优先。
   - 横屏分支按 `layout-land` 复刻；竖屏分支保留 `layout` 对齐值。
   - 避免“同一套尺寸公式同时作用横竖屏”导致竖屏回归。

4. **事件链路对齐（高优先级）**
   - 不只看 UI 外观，必须对齐 golive 交互链。
   - 对安全输入页重点核对：
     - `ACTION_SECURITY_AREA`（ready/bounds）
     - `ACTION_SET_PIN_KEY_LAYOUT`（按键矩形坐标）
   - 若 golive 点击后页面应锁定，Compose 必须做到“不可再交互”，不是仅“事件去重”。

5. **验证**
   - 编译至少执行：`:app:compileDebugKotlin`。
   - 使用对应 action 的 adb 命令在目标机型复测横屏与竖屏。
   - 明确回归结果：横屏问题已修复且竖屏无新增差异。

## 强制检查清单

- [ ] 已对照 golive 的 `layout` 与 `layout-land` 两套 XML。
- [ ] 文案是否按 golive 保持单行/多行策略一致（无意外换行）。
- [ ] 控件尺寸公式是否按方向分支处理（横屏、竖屏互不误伤）。
- [ ] 点击区域与视觉区域一致（无错位、无误触）。
- [ ] 关键广播/回调链路完整（尤其安全键盘与坐标上报）。
- [ ] 编译通过并有目标机型 adb 复测结论。

## 输出要求（给用户的结论）

- 本次对齐了哪些 golive 文件（XML/Java）；
- Compose 改动点分别属于“横屏专属”还是“横竖共用”；
- 为什么不会影响竖屏（给出条件分支依据）；
- 提供可复测 adb 命令与预期表现。

## 相关参考

- `./.cursor/skills/compose-ui-parity-golive-posuinew/SKILL.md`
- `specs/002-compose-migration/ui-parity-from-golive-xml.md`
