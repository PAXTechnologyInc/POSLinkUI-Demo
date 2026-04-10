# Idea 无法转换文件的处理策略

**特性**： 001-kotlin-migration  
**日期**: 2025-03-18  
**Task**: T004

## 决策标准

当 IntelliJ IDEA 无法自动转换某 Java 文件时，按以下优先级处理：

| 优先级 | 条件 | 处理方式 |
|--------|------|----------|
| P1 | 入口类或高频调用（如 BaseEntryFragment、UIFragmentHelper、EntryActivity、MainActivity） | **手工转换**，确保依赖链完整 |
| P2 | 被多个 Fragment/Activity 依赖的工具类（CurrencyUtils、EntryRequestUtils 等） | **手工转换** |
| P3 | 低频调用或独立模块 | 可暂时**保留 Java**，在 `class-inventory.md` 或本文件下标记为「待后续处理」 |

## 参考

- [spec.md](spec.md) Edge Cases - 单文件转换失败
- [research.md](research.md) - Idea 转换决策
- [plan.md](plan.md) - 依赖与执行顺序

## 当前状态

- 本次迁移已通过 Idea 一次性转换全部 Java 文件，无保留的 Java 文件
- 若后续发现遗漏或新增 Java 文件，按上述策略处理
