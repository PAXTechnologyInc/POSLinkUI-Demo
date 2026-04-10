# 图片与远程资源审计（T029）

**日期**：2026-03-27  
**方法**：在 `app/src/main` 下检索 `http://`、`https://`（Kotlin / XML）。

## 结论

- **业务 Kotlin 代码**：未发现将 `http(s)://` 硬编码为图片/接口 URL 的用法。
- **XML**：匹配主要为 `http://schemas.android.com/...` 的 **XML 命名空间声明**，属正常 Android 资源格式，**非**远程切图引用。
- **Compose / Coil**：`ExtendedEntryRoutes` 中收据预览使用 **`content://` / `file` 等由 host 传入的 URI**（`PARAM_RECEIPT_URI`），不内置远程 URL。

## 维护约定

- 蓝湖/设计切图须落本地 `res/drawable*`（见 Lanhu 工作流规则）。
- 若 host 传入 `PARAM_IMAGE_URL`（POSLink Show Message），须在集成层评审 HTTPS 与白名单；演示工程未默认加载不可信 http URL。
