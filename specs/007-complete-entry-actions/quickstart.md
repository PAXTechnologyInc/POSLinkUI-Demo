# Quickstart：补齐未实现 action/category

## 1. 准备

1. 切换到分支 `007-complete-entry-actions`。
2. 确认规格文件与计划文件存在：
   - `specs/007-complete-entry-actions/spec.md`
   - `specs/007-complete-entry-actions/plan.md`

## 2. 开发顺序建议

1. 先核对动作清单（基线 12 + 增量）。
2. 再补齐路由识别与交互入口。
3. 最后补提交流程与回写链路。
4. 每完成一个动作，立即登记验收状态。

## 3. 验收执行

### 人工验收（必做）

- 对 12 个缺口动作逐条执行“触发 -> 交互 -> 提交 -> 回写”。
- 记录通过/失败与失败原因。

### 自动化回归（最小门禁）

- 每个涉及 category 至少 1 个代表动作纳入自动化回归并通过。

## 4. 本地质量门禁

在仓库根目录执行：

```bash
./gradlew :app:assembleDebug :app:testDebugUnitTest
```

若涉及 lint 配置或相关改动，另执行：

```bash
./gradlew :app:lintDebug
```

## 5. 交付前检查

- 覆盖统计已更新（总数/已实现/未实现）。
- 首轮冻结基线的验收结论已给出。
- 增量缺口状态已单独标注。

## 6. 本次实现结果（2026-03-31）

- 12 个基线缺口动作的代码接入已完成（routable/submittable/writable）。
- 自动化回归门禁通过：`./gradlew.bat :app:assembleDebug :app:testDebugUnitTest`
- 人工验收记录已建档，等待设备侧逐条执行并回填：
  - `acceptance/manual-validation.md`
