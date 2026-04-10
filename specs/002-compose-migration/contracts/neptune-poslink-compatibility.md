# Neptune/POSLink 接口兼容性契约

**特性**：002-compose-migration  
**日期**: 2025-03-16 | **修订**: 2026-03-28

## 契约说明

本迁移不修改与 Neptune/POSLink SDK 的调用契约。Compose UI 层须保持与 XML/View 版本相同的：

- 方法签名调用方式（EntryRequest、EntryResponse、EntryExtraData）
- 回调接口实现（如 `OnResultListener`、`OnClickListener` 等）
- 常量引用（`com.paxus.ui:constant` 中的键、码值）
- **生命周期与回调时序**：若 UI 由 **`NavHost` + composable** 承载，须在 **Activity / 单宿主 Fragment / ViewModel** 上保持与原先 **Fragment** 边界等价的订阅与取消（见 spec **FR-019**；路由表 [entry-navigation-routes.md](entry-navigation-routes.md)）

## 验证

- Entry 流程可走通即表示 Neptune/POSLink 交互正常
- 业务逻辑层（ViewModel、Helper）不因 UI 替换而变更
