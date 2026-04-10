# Entry：`Intent` action → Navigation route 映射

**特性**：002-compose-migration  
**维护任务**：**T035**（spec **FR-019**）  
**状态**：已落地骨架；随各屏迁到纯 Compose 时在本表与代码中同步增行。

## 目的

- 满足母版 **FR-M001**：每条 manifest 注册的 `com.pax.us.pay.action.*` 进入正确界面；在 Nav 形态下为正确 **`composable` destination** 或经 **legacy 槽位** 进入等价 Fragment。
- 与 **SC-013** 验收一致：`onNewIntent`、legacy 槽位重挂行为可对照本表审计。

## Nav route 注册表（代码：`entry/navigation/`）

| Nav `route` | 含义 | 实现要点 |
|-------------|------|----------|
| `legacy_host` | 默认 Entry | `EntryNavHost` 内 `FragmentContainerView` + `UIFragmentHelper.createFragment(activity.intent)`；`onNewIntent` 时 `EntryNavHostFragment.notifyEntryIntentChanged()` 触发重挂 |
| `compose_demo` | 管道验证用纯 Compose 屏 | `EntryRouteResolver` 在 Intent 含 `EntryActivity.EXTRA_NAV_COMPOSE_DEMO=true` 时选此起点 |

## `Intent` → route（单点解析）

| 条件 | 起点 route | 代码位置 |
|------|------------|----------|
| `extras[EntryActivity.EXTRA_NAV_COMPOSE_DEMO] == true` | `compose_demo` | `EntryRouteResolver.resolveStartRoute(Intent)` |
| 否则 | `legacy_host` | 同上 |

## 与 manifest action 的关系（过渡）

- **现状**：具体 `Intent.action` + category 仍由 **`UIFragmentHelper`** 解析为 **Fragment 类**；Nav 层在 **`legacy_host`** 内委托该路径，保证与迁移前一致。
- **目标态**：每类 action 在 `EntryRouteResolver` 中映射到独立 `TransactionRoute` + `composable`，并逐步删除 legacy 委托；映射增量维护时**更新本表**。

## 实现约定

- **单点解析**：在 `EntryRouteResolver`（或后续 `EntryNavCoordinator`）集中解析，禁止在多个 Composable 内重复 `when(action)`。
- **与 `UIFragmentHelper` 关系**：迁移期双轨；权威顺序为 **本表 / Nav 图 → legacy 委托**。

## 参考

- [spec.md](../spec.md) **FR-019**、**SC-013**
- [quickstart.md](../quickstart.md) 步骤 4
- [neptune-poslink-compatibility.md](neptune-poslink-compatibility.md)
