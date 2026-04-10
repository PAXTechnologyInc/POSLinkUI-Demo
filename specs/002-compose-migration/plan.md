# 实施计划： UI 转 Compose 迁移

**分支**: `002-compose-migration` | **日期**: 2025-03-16 | **最近修订**: 2026-03-28 | **规格**: [spec.md](spec.md)  
**母版对齐**：[specs/000-poslinkui-project-master/spec.md](../000-poslinkui-project-master/spec.md)（宿主 Intent、`EntryActivity`、广播、FR-M/SC-M）。

**输入**：功能规格说明，来源 `specs/002-compose-migration/spec.md`

## 摘要

将 POSLinkUI-Demo 的 **Entry 相关 UI** 从 XML/View 转为 Jetpack Compose，按 POSLinkUI-Design_V1.03.00 设计规范还原。**页面还原度第一**（spec FR-001/004）。

**目标态（与 spec 一致）**：

- **布局层去 XML（FR-005）**：Entry 页面不以 `res/layout` 承载；**优先** **极少数 Compose 根**（`EntryActivity.setContent` 或 **单宿主 Fragment 内一个 `ComposeView`**）+ 内部 **`NavHost` + `composable`**（**FR-019**），避免「每 action 一个独立 `ComposeView` 树」；**每模块收尾**删除已无引用的 layout；全量收尾产出清单。**保留** `AndroidManifest.xml`；不新建替代 `EntryActivity` 的宿主入口（除非母版/书面变更）。
- **导航（FR-019）**：Jetpack **Navigation Compose**；**`Intent` action / Extras → route** 单点映射，满足母版 **FR-M001** 的界面等价；`**onNewIntent**`、返回栈与状态机见 spec **SC-013**；任务 **T035**。
- **复用层（FR-016）**：在 `ui` 包提供统一根、`NavHost` 内各 destination 共用的预制件与 Token API。
- **Fragment 扁平化（FR-015）**：少数字段差异优先 **参数化 destination**；若仍暂存多 `Fragment`，应计划收敛至 **单宿主 + Nav**。宪章 §一.II 与 spec 假设前提同前。
- **机型与副屏（FR-017 / FR-018）**：[`device-profiles.md`](device-profiles.md)；**T031**、**T034**。

迁移顺序仍为：Text → Security → Confirmation → POSLink → Option → Information。验收以人工对比设计稿与实现截图为主。

**前置依赖**：[001-kotlin-migration](../001-kotlin-migration/plan.md) 建议先合并；002 在 Kotlin 代码上做 Compose 迁移，避免与 Java→Kotlin 变更冲突。

## 技术上下文

**语言/版本**: Kotlin 1.9.22 / 2.1.0，Java 8 兼容
**主要依赖**: Android Gradle Plugin 7.4.0、Jetpack Compose、**Navigation Compose**（`navigation-compose`，与 FR-019 / **T035** 一致）、com.paxus.ui:constant 1.03.00T、androidx 系列、Coil（已有，可用于 Compose 图片加载）
**存储**: N/A（本迁移不涉及持久化变更）
**测试**: 人工对比设计稿与实现截图、手动 Entry 流程验证
**目标平台**: Android minSdk 22, targetSdk 31
**项目类型**: mobile-app（Android 支付 POS 应用）
**性能目标**: 页面首次渲染无明显卡顿；与 XML/View 版本响应相当
**约束**: Neptune/POSLink 接口契约不变；页面还原度优先于实现便利性；支付敏感数据禁止明文日志；设计规范未定义深色模式、可访问性、横竖屏，本迁移不强制实现
**规模/范围**: 六类模块（Text、Security、Confirmation、POSLink、Option、Information）；约 80+ Fragment；设计文档需手工提取数值

## 宪章检查

*门禁：必须在 Phase 0 研究前通过，Phase 1 设计后再次检查；实施前复核与 spec 修订项。*

对照 **`.specify/memory/constitution.md`（当前仓库版本）** 中与 UI 迁移相关的条款：

| 宪章条款 | 与本特性关系 | 结论 |
|----------|----------------|------|
| **§一.I** Action→界面 映射、`UIFragmentHelper`、Manifest | manifest alias 与 action **不变**；**实现形态**可从「多 Fragment」演进为 **单宿主 + `NavHost` destination**（spec **FR-019**），须维护 **action → route** 等价表，母版 **FR-M001** 仍满足 | **有条件通过**：与 **T035、SC-013** 同步落地 |
| **§一.II** Fragment 实现模式（继承 `AConfirmationFragment` 等） | spec **FR-015** 鼓励参数化 Screen、减少仅为差异存在的子类，可能与「固定继承模式」**张力** | **有条件通过**：实施前须 **宪章 MINOR 修订** 或 **书面豁免**（见 spec 假设前提） |
| **§二.IV–VI** 任务生命周期、线程、用户反馈 | Compose 迁移须保持既有取消/回调策略；不在主线程做 IO | **通过**（不改变业务状态机职责边界） |
| **§三.VII** Kotlin 惯用法、测试不回归 | 迁移中保持可观测行为；既有测试须通过 | **通过** |
| **§四.IX–XI** 分层、状态显式、命名 | Screen 组合 + ViewModel/领域层边界不变 | **通过** |
| **§五–六 XII–XVII** 输入校验、敏感数据、设备 SDK | spec FR-008 与宪章 XIV 一致；不绕过 adapter | **通过** |
| **§七.XVIII–XIX** 测试与高风险场景 | 本特性以人工/Entry 流程为主；敏感屏见 `checklists/security-sensitive-screens.md` | **通过**（按 spec 范围） |
| **§八** 构建验证、KDoc | 变更后 `./gradlew :app:assembleDebug`（及项目门禁下的单测） | **通过** |

**结论**：除 **§一.II 与 FR-015** 需显式闭环外，**§一.I 与 FR-019** 须在「Nav 承载」路径上完成 **action→route** 文档/代码与 **SC-013** 验收；闭环完成前不得将「大规模合并 Fragment / 导航收敛」标为完成。

## 项目结构

### 文档（本特性）

```text
specs/002-compose-migration/
├── plan.md              # 本文件
├── research.md          # Phase 0 研究结论
├── data-model.md        # Phase 1 数据模型
├── quickstart.md        # Phase 1 快速开始
├── class-inventory.md   # 类清单（细化产出）
├── modification-patterns.md  # 类类型与修改模式
├── acceptance-per-class.md   # 类级验收清单
├── contracts/           # Phase 1 接口契约
│   ├── neptune-poslink-compatibility.md
│   ├── base-class-compose-strategy.md  # 宿主 / NavHost / 过渡 ComposeView
│   └── entry-navigation-routes.md      # action → Nav route（T035 维护）
├── checklists/
│   └── security-sensitive-screens.md  # 敏感屏审查要点
├── device-profiles.md   # 机型参数、P0、副屏与 Token 映射说明
└── tasks.md             # Phase 2 由 /speckit.tasks 生成
```

### 源码（仓库根目录）

```text
app/
├── src/main/
│   ├── java/com/paxus/pay/poslinkui/demo/
│   │   ├── entry/
│   │   │   ├── text/          # Text 模块 Fragment
│   │   │   ├── security/      # Security 模块 Fragment
│   │   │   ├── confirmation/  # Confirmation 模块 Fragment
│   │   │   ├── poslink/       # POSLink 模块 Fragment
│   │   │   ├── option/        # Option 模块 Fragment
│   │   │   └── information/   # Information 模块 Fragment
│   │   ├── status/
│   │   ├── ui/                # theme、预制件、统一根、navigation（可选子包）、设备规格（FR-016/017/019）
│   │   └── utils/
│   ├── res/
│   │   ├── drawable/          # 切图落本地
│   │   └── values/            # 主题、颜色等（可新增 design-tokens；layout 目标态清零见 FR-005）
│   └── AndroidManifest.xml
└── build.gradle.kts
```

**结构决策**: 单模块 Android 应用；Gradle 使用 **`app/build.gradle.kts`**。**目标宿主**：**`EntryActivity` `setContent { NavHost(…) }`** 或 **单宿主 `Fragment` + 唯一 `ComposeView` + `NavHost`**（**FR-019**）；屏间切换以 **`composable(route)`** 为主。**过渡**：允许短期多 `Fragment` 各带 Compose 根，须登记技术债并收敛到 **T035**。**目标态**移除 Entry 对 `res/layout` 的依赖（FR-005）；**不**替换 `EntryActivity` 的 manifest 入口角色；强化 **`ui/`**、**设备规格**、**导航注册表**。

**设计数值提取与落地**（引用 spec 新增 Design Tokens 节）：
- 从 POSLinkUI-Design_V1.03.00.docx 手工提取 PrimaryColor、TitleTextSize、BodyTextSize、CardPadding 等 token
- 产出物路径：`specs/002-compose-migration/design-tokens.md` 或 `app/src/main/res/values/`
- 验收策略引用 spec 中「模块验收清单」，逐模块逐 Fragment 验收

## Phase 1 产出

- [data-model.md](data-model.md) - 实体与关系
- [contracts/neptune-poslink-compatibility.md](contracts/neptune-poslink-compatibility.md) - Neptune/POSLink 兼容性契约
- [quickstart.md](quickstart.md) - 快速开始步骤

**Agent Context 更新**：执行 `.specify/scripts/bash/update-agent-context.sh cursor-agent`（Windows 下可用 Git Bash 或 WSL）。若脚本不可用，可手动将 Compose、ComposeView、design-tokens 补充到 agent 上下文中。

## Phase 2 规划

本 plan 止于 Phase 2 规划。下一步由 `/speckit.tasks` 将 plan 拆解为可执行任务（tasks.md）。

## 依赖与执行顺序（类级）

**迁移顺序**（避免先改子类再改基类导致返工）：

1. **阶段 2 扩展（tasks T030–T031、T035）**：**T030** 复用层、**T031** 设备规格、**T035** **Navigation Compose** + **`NavHost` 骨架** + **action→route** 单点解析（与 **FR-019、SC-013** 一致）。
2. **宿主边界**（见 [contracts/base-class-compose-strategy.md](contracts/base-class-compose-strategy.md)）：**优先**单根 `NavHost`；遗留 **BaseEntryFragment + 单 `ComposeView`** 仅作过渡，新屏优先 **注册为 composable destination**。
3. **基类**：ATextFragment、AConfirmationFragment、ASecurityFragment、AOptionEntryFragment、AAmountFragment、ANumFragment、ANumTextFragment —— 在 **FR-015** 与宪章 §一.II 闭环前提下，优先 **参数化 + 共享 Screen**，减少无意义子类。
4. **按模块顺序**：Text → Security → Confirmation → POSLink → Option → Information。
5. **各模块内**：先基类/共享 Screen，再子类；**模块收尾**删除本模块已无引用的 `layout`（汇总至阶段 9 T032）。

**参考**：[class-inventory.md](class-inventory.md) 第 9 节、[tasks.md](tasks.md) Dependencies & Execution Order

## 复杂度跟踪

| 项 | 说明 |
|----|------|
| 宪章 §一.II vs FR-015 | 须在合并 Fragment / 弱化继承前完成修订或豁免 |
| layout 删除与工具类 | `ViewBinding`/inflate 残留需按模块 grep 与清单驱动删除 |
| P0 机型与副屏 | 见 `device-profiles.md`；T031 + T034；副屏对齐 `EntryActivity` |
| Nav 与 `onNewIntent` | `singleTop` 进件与 **pop/back stack** 须与状态机一致；**SC-013**、**T035** |

## Risk / Edge Case 处理（引用 spec 细化内容）

- **设计文档无法解析**：spec 已定义 Design Tokens 待提取项清单，由人工提取后填入
- **验收标准**：spec 已细化验收场景（布局/字体/颜色/交互状态）与模块验收清单，tasks 中验收任务可直接引用
- **去 XML 与构建**：删除 `layout` 前确认无 `R.layout.*` / Binding 引用；保留项写入 T032 清单
- **复用层滞后**：若 T030 未完成即大量写 Screen，易导致重复与返工；**T030 为 Phase 3 硬前置**（见 tasks.md）  
- **dp 写死**：若规格层未就绪即在各 Screen 堆 `NN.dp`，将与 FR-017/FR-016 冲突并导致 P0 机型回归失败  
- **副屏**：仅测主屏会导致 SC-012 缺失；须在 A920Pro（或双屏样机）上验 `Presentation`  
- **导航**：无 **T035** 则 FR-019 悬空；多 `ComposeView` 根与 spec **SC-013** 冲突
