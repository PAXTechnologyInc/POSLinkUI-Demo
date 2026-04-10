# 数据模型：Java 转 Kotlin 迁移

**特性**： 001-kotlin-migration  
**日期**: 2025-03-16

## 概述

本迁移不修改业务数据模型或持久化结构，仅将 Java 源码转换为 Kotlin。以下实体为现有结构，转换后字段与关系保持不变。

## 核心实体

### Entry 流程相关

| 实体 | 说明 | 转换后变化 |
|------|------|------------|
| BaseEntryFragment | 进件基类，定义生命周期与 Neptune 回调 | 仅语言转换，接口不变 |
| EntryActivity | 进件容器 Activity | 仅语言转换 |
| Confirm*Fragment | 各类确认 Fragment（金额、持卡人等） | 仅语言转换 |
| Select*Fragment | 各类选项 Fragment | 仅语言转换 |
| ShowItemFragment, ShowTextBoxFragment | POSLink 展示类 | 仅语言转换 |
| ASecurityFragment | 安全相关基类 | 仅语言转换 |
| SignatureFragment | 签名 | 仅语言转换 |
| Text*Fragment | 各类输入 Fragment | 仅语言转换 |

### 状态与工具

| 实体 | 说明 | 转换后变化 |
|------|------|------------|
| TransCompletedStatusFragment | 交易完成状态页 | 仅语言转换 |
| ToastFragment | 提示 | 仅语言转换 |
| StringUtils, CurrencyUtils, DateUtils | 工具类 | 仅语言转换 |

### 外部依赖

| 实体 | 说明 | 转换后变化 |
|------|------|------------|
| com.paxus.ui:constant | Neptune/POSLink 常量与接口 | 不修改，仅调用方语言变更 |
| UIFragmentHelper | 与 Neptune 交互的 Helper | 仅语言转换 |

## 状态与验证

- 无新增状态机或状态迁移
- 无新增持久化字段
- 验证规则：编译通过、Lint 零错误、SonarQube New Code 无新增问题
