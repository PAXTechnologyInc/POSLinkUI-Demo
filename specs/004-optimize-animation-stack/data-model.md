# 数据模型：动画改造

**特性**： 004-optimize-animation-stack | **日期**: 2026-03-19

## 1. 动画位点清单（AnimationInventoryItem）

| 字段 | 类型 | 说明 |
|------|------|------|
| id | 字符串 | 位点 ID（如 A1-A6） |
| location | 字符串 | 类名 + 方法/行 |
| animType | 枚举 | fragment_transition, view_property, gif, dialog |
| currentImpl | 字符串 | 当前实现方式 |
| targetImpl | 字符串 | 目标实现方式 |
| visualSpec | 对象 | 时长、方向、缓动、可见元素（用于复刻） |
| verified | 布尔 | 是否已通过视觉回归 |

## 2. 动画策略档位（AnimationPolicyProfile）

| 档位 | 触发条件 | 行为 |
|------|----------|------|
| standard | 默认 | 完整动画，性能优先实现 |
| reduced | 低内存 / 用户设置 | 缩短时长、减少并发动画 |
| minimal | 高温 / 极端低性能 | 极简或关闭非关键动画 |

## 3. 性能基线快照（PerformanceBaselineSnapshot）

| 字段 | 说明 |
|------|------|
| scenario | 冷启动 / 主交易流程 / 长时运行 / 前后台切换 |
| cpuPercent | 平均 CPU 占用 |
| memoryMB | 内存峰值 |
| tempDelta | 温升（若可测） |
| timestamp | 采集时间 |

## 4. 契约关系

- 每个动画位点需有 `visualSpec`，迁移后必须满足该规格以通过 FR-004a。
- 每个位点需有自测用例或验收步骤，满足 FR-006a。
