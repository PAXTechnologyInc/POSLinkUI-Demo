# 数据模型：UI 转 Compose 迁移

**特性**： 002-compose-migration  
**日期**: 2025-03-16

## 概述

本迁移不修改业务数据模型，仅将 UI 层从 XML/View 替换为 Compose。以下为 UI 相关实体与设计规范映射。

## 核心实体

### 模块与 Fragment 映射

| 模块 | Fragment 示例 | Compose 替换方式 |
|------|----------------|------------------|
| Text | AmountFragment、ReferenceNumberFragment、ZipcodeFragment、ATextFragment | Fragment 内嵌 ComposeView，替换原 View 层级 |
| Security | PINFragment、InputAccountFragment、EnterCardAllDigitsFragment | 同上 |
| Confirmation | AConfirmationFragment、ConfirmTotalAmountFragment | 同上 |
| POSLink | ShowItemFragment、ShowTextBoxFragment、ShowDialogFragment | 同上 |
| Option | AOptionEntryFragment、SelectTransTypeFragment、SelectCurrencyFragment | 同上 |
| Information | DisplayTransInfoFragment、DisplayApproveMessageFragment | 同上 |

### 设计规范实体

| 实体 | 说明 | 来源 |
|------|------|------|
| 布局数值 | padding、margin、尺寸（dp） | POSLinkUI-Design_V1.03.00.docx 手工提取 |
| 字体 | 字号、字重 | 同上 |
| 颜色 | 主色、背景、文字色（HEX） | 同上 |
| 间距 | 组件间距 | 同上 |
| 图片资源 | drawable 资源 ID | res/drawable*，落本地 |

### 状态与验证

- 无新增业务状态机
- 验收：人工对比设计稿与实现截图，逐页/逐模块
