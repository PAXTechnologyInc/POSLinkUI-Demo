# 敏感屏 Compose 迁移审查要点

**特性**： 002-compose-migration  
**日期**: 2025-03-18  
**目的**: Security 模块及涉及敏感输入的 Fragment 迁移必查项。

---

## 1. PINFragment

### 必查项

- [ ] PIN 输入使用掩码（如 `visualTransformation = PasswordVisualTransformation()` 或等效）
- [ ] 无 `Log.*pin`、`Log.*pan`、`Log.*cvv`、`println.*card` 等敏感明文日志
- [ ] 安全键盘若依赖 SDK，通过 AndroidView 或兼容方式接入，行为与迁移前一致
- [ ] 布局、字体、颜色与 Design Tokens 一致
- [ ] 输入与回传行为与迁移前一致

### 验收

- 进入 PIN 输入流程 → 输入 → 验证掩码显示正确
- 提交后结果正确回传
- grep 敏感关键词无违规

---

## 2. InputAccountFragment

### 必查项

- [ ] 持卡人信息（卡号、姓名等）使用掩码
- [ ] 无敏感明文日志
- [ ] 与 Neptune 的 EntryRequest/EntryResponse 契约不变

### 验收

- 进入持卡人信息输入 → 验证掩码
- 提交后结果正确回传

---

## 3. EnterCardAllDigitsFragment

### 必查项

- [ ] 卡号输入使用掩码（如 `*` 或 `•`）
- [ ] 无敏感明文日志
- [ ] 输入长度、格式校验与迁移前一致

---

## 4. EnterCardLast4DigitsFragment

### 必查项

- [ ] 卡号后四位输入使用掩码或按业务要求展示
- [ ] 无敏感明文日志

---

## 5. AdministratorPasswordFragment

### 必查项

- [ ] 管理员密码使用掩码
- [ ] 无敏感明文日志

---

## 6. ManageInputAccountFragment

### 必查项

- [ ] 账户相关输入使用掩码
- [ ] 无敏感明文日志

---

## 7. EnterVcodeFragment

### 必查项

- [ ] 验证码输入使用掩码（若业务要求）
- [ ] 无敏感明文日志

---

## 8. 通用验收

- [ ] 所有 Security 模块 Fragment 迁移后，执行完整 Entry 流程验证
- [ ] 人工对比设计稿与实现截图，布局与 Design Tokens 一致
- [ ] 符合 constitution 原则：禁止明文日志、展示使用掩码

---

## 9. 审查完成标记

- [ ] PINFragment 已迁移并验收
- [ ] InputAccountFragment 已迁移并验收
- [ ] EnterCardAllDigitsFragment 已迁移并验收
- [ ] EnterCardLast4DigitsFragment 已迁移并验收
- [ ] AdministratorPasswordFragment 已迁移并验收
- [ ] ManageInputAccountFragment 已迁移并验收
- [ ] EnterVcodeFragment 已迁移并验收
- [ ] grep 敏感关键词无违规
