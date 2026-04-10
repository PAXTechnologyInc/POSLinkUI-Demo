# Neptune/POSLink 接口兼容性契约

**特性**： 001-kotlin-migration  
**日期**: 2025-03-16

## 契约说明

本迁移不修改与 Neptune/POSLink SDK 的调用契约。Kotlin 调用方需保持与 Java 版本相同的：

- 方法签名调用方式（参数类型、顺序、返回值处理）
- 回调接口实现（如 `OnResultListener`、`OnClickListener` 等）
- 常量引用（`com.paxus.ui:constant` 中的键、码值）

## 验证

- 编译通过即表示 Kotlin 与 Java SDK 互操作正确
- 运行时 Entry 流程可走通即表示回调与路由正常
