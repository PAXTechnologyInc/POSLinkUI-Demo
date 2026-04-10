# 契约：动画迁移

**特性**：004-optimize-animation-stack | **日期**：2026-03-19

## 1. 视觉保真契约

- 迁移后动画的**时长、方向、缓动、可见元素**必须与改造前一致。
- 验收方式：人工对比或录制对比；关键数值（duration、fromYDelta、toYDelta 等）须在测试或文档中体现。

## 2. 实现替换契约

- 旧实现（`AnimationUtils`、`AlphaAnimation`、重复图片库路径等）须完全移除，不得保留并行路径。
- 新实现统一采用：策略化 XML 过渡 / 可复用 `ObjectAnimator` / 共享 `ImageLoader` 的 Coil GIF / policy-aware Compose 图片过渡。

## 3. 生命周期契约

- 动画在宿主（Fragment/Activity/Compose 页面）销毁时必须取消或释放，不得持有引用导致泄漏。
- 参考实现：`ClssLight` 需在 detach/不可见时停止 blink；`TransactionPresentation` 需释放 GIF request。

## 4. 降级契约

- 不可控或不可替换的动画须有降级策略（降频、简化、关闭）。
- 降级触发时须记录日志，便于排查发热与高负载来源。

## 5. 高风险页面回退策略（FR-007）

| 页面 | 回退策略 |
|------|----------|
| TransactionPresentation | 当 `AnimationPolicy.MINIMAL` 时隐藏边框 GIF；`REDUCED` 时缩小解码尺寸 |
| ApproveMessageScreen | 当 `AnimationPolicy.MINIMAL` 或 GIF 加载失败时，降级到文本审批展示 |
| ReceiptPreviewEntryScreen | 当 `AnimationPolicy` 非 `STANDARD` 时关闭 crossfade，直接显示图片 |
