# Layout XML 保留清单（T032 / SC-008）

**日期**：2026-03-30  
**范围**：`app/src/main/res/layout`、`app/src/main/res/layout-land`

## 本轮结论（与代码一致）

- Entry 业务主路径已不再依赖 `activity_entry.xml` 与各模块 `fragment_*.xml`（Text/Security/Confirmation/POSLink/Option/Information/Signature）。
- `entry/**` 下旧 Fragment 继承与实现已移除，`EntryActivity` 使用 Compose `setContent + EntryNavigationHost`。
- `layout-land` 下 `fragment_pin.xml`、`fragment_input_account.xml` 等 Entry 相关布局已删除。

## 仍有 `R.layout` 引用的文件（当前保留）

| 布局文件 | 引用位置 |
|----------|----------|
| `activity_main.xml` | `MainActivity` |
| `action_bar.xml` | `EntryActivityActionBar` |
| `layout_watermark.xml` | `ViewUtils` |
| `layout_clss_light_default.xml` | `ClssLightsView` |
| `fragment_status.xml` | `StatusFragment` |
| `fragment_interface_action_filter.xml` | `EntryActionAndCategoryFilterFragment` |
| `fragment_interface_action_by_category.xml` | `EntryActionFilterByCategoryFragment` |
| `layout_item_interface_action.xml` | `EntryActionFilterByCategoryFragment` |
| `layout_select_option_item.xml` | `SelectOptionsView` |

## 当前未见 Java/Kotlin 直接引用的 layout（非 Entry 核心，待后续评估）

- `fragment_double_line_key_value.xml`
- `item_show_item.xml`
- `item_layout_msg_list.xml`
- `json_option_list_item.xml`
- `option_list_item.xml`
- `item_cashback_option.xml`
- `layout_show_dialog_form_item.xml`

上述文件不属于本轮 Entry Compose 收敛阻塞项；若后续确认全仓无引用，可单独清理。
