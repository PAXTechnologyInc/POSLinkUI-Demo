# Spec 文档细化优化步骤

**用途**：指导将 spec 从「模块级」细化到「具体类级」和「具体修改方式」。  
**日期**：2025-03-18  
**关联**：[SPEC-KIT-使用步骤.md](SPEC-KIT-使用步骤.md)（如何生成 spec）→ 本步骤（如何细化 spec）

---

## 一、现状分析

### 1.1 当前 Spec 的粒度


| 文档                        | 当前粒度                              | 缺失内容                                                       |
| ------------------------- | --------------------------------- | ---------------------------------------------------------- |
| **001-kotlin-migration**  | 模块级（entry/、status/、utils/）        | 未列出 177 个 Java 文件的完整清单；未按继承链区分基类/子类；未指定单文件审查重点             |
| **002-compose-migration** | 模块级（Text/Security/Confirmation 等） | 未列出每个模块下的完整 Fragment 列表；未说明基类与子类的修改顺序；未细化 ComposeView 替换模式 |


### 1.2 项目实际结构（已梳理）

**继承关系**：

- `BaseEntryFragment` → 所有 Entry 基类
- `ATextFragment` / `ANumTextFragment` / `AAmountFragment` / `ANumFragment` → Text 子类
- `AConfirmationFragment` → 约 25 个 Confirm*Fragment
- `ASecurityFragment` → PIN、持卡人信息等
- `AOptionEntryFragment` → 约 22 个 Select*Fragment

**非 Fragment 类**：`UIFragmentHelper`、`EntryActivity`、`MainActivity`、`CurrencyUtils` 等工具类/View 类。

---

## 二、细化步骤（共 8 步）

### 步骤 1：建立「模块-类」清单

**目标**：为每个 spec 补充 `specs/NNN-xxx/class-inventory.md`，列出该 feature 涉及的全部类。

**操作**：

1. 从 `app/src/main/java/com/paxus/pay/poslinkui/demo/` 递归扫描
2. 按包/模块分组：entry/（再按 text/security/confirmation/poslink/option/information 分）、status/、utils/、view/、viewmodel/、settings/
3. 标注继承关系：基类（abstract）、子类、独立类
4. 标注依赖：如 UIFragmentHelper 被 EntryActivity 使用

**产出**：`specs/001-kotlin-migration/class-inventory.md`、`specs/002-compose-migration/class-inventory.md`

---

### 步骤 2：为「基类」单独写修改说明

**目标**：基类修改会影响所有子类，需在 `contracts/` 中明确「基类修改约束」。

**操作**：

1. 识别基类：BaseEntryFragment、ATextFragment、AConfirmationFragment、ASecurityFragment、AOptionEntryFragment、AAmountFragment、ANumFragment、ANumTextFragment
2. 001：在 `contracts/base-class-modification.md` 中写明对外契约不可变、Kotlin 转换注意点
3. 002：在 `contracts/base-class-compose-strategy.md` 中写明 ComposeView 共存策略、基类迁移步骤

**产出**：`specs/001-kotlin-migration/contracts/base-class-modification.md`、`specs/002-compose-migration/contracts/base-class-compose-strategy.md`

---

### 步骤 3：按「类类型」定义修改模式

**目标**：不同类有不同的修改模式，spec 需明确「这类类该怎么改」。

**操作**：见下表，产出 `modification-patterns.md`。


| 类类型            | 001-kotlin-migration                | 002-compose-migration                             |
| -------------- | ----------------------------------- | ------------------------------------------------- |
| 抽象基类           | 先转换；abstract、open、@CallSuper → KDoc | 引入 ComposeView 分支；子类 override Composable          |
| 具象 Fragment    | 重点审查 loadView、onCreateView 空安全      | 重写 onCreateView 返回 ComposeView { XxxScreen(...) } |
| 工具类            | 静态方法 → object 或顶层函数                 | 通常不改                                              |
| Adapter/View   | RecyclerView.Adapter 泛型、ViewHolder  | 逐步替换为 LazyColumn 或保留 AndroidView                  |
| Helper/Manager | Neptune 互操作、@Nullable 处理            | 不改，仅调用方变化                                         |


**产出**：`specs/001-kotlin-migration/modification-patterns.md`、`specs/002-compose-migration/modification-patterns.md`

---

### 步骤 4：为 tasks.md 补充「具体文件路径」

**目标**：每个 task 明确到「改哪些文件」。

**操作**：

1. 将审查/迁移任务拆分为按目录或按类的子任务
2. 001：T011 拆为 T011a（基类）、T011b（text/）、T011c（confirmation/）、T011d（security/poslink/option/information/）、T011e（status/utils/view/）
3. 002：T006–T009 等引用 class-inventory.md 中的具体文件列表

**产出**：更新 `tasks.md`，引用 class-inventory.md

---

### 步骤 5：为「高风险类」单独写审查/修改要点

**目标**：支付类应用高风险类需单独列出必查项。

**操作**：

1. 001 高风险类：BaseEntryFragment、UIFragmentHelper、PINFragment、InputAccountFragment、CurrencyUtils 等
2. 002 敏感屏：PINFragment、InputAccountFragment、EnterCardAllDigitsFragment 等
3. 每类列出 3–5 条必查项

**产出**：`specs/001-kotlin-migration/checklists/high-risk-classes.md`、`specs/002-compose-migration/checklists/security-sensitive-screens.md`

---

### 步骤 6：补充「验收清单」到类级

**目标**：将模块验收清单细化为类级验收项。

**操作**：

1. 001：按 class-inventory 逐类确认已审查
2. 002：为每个 Fragment 增加布局还原、行为正确、敏感屏掩码等验收项

**产出**：`specs/001-kotlin-migration/acceptance-per-class.md`、`specs/002-compose-migration/acceptance-per-class.md`

---

### 步骤 7：明确「依赖与执行顺序」

**目标**：避免先改子类再改基类导致返工。

**操作**：

1. 001：基类 → 工具类 → 具象 Fragment；UIFragmentHelper 与 EntryActivity 在 Fragment 之前
2. 002：BaseEntryFragment 引入 ComposeView → 基类 → 各模块具象 Fragment；按 Text → Security → Confirmation → POSLink → Option → Information
3. 在 plan.md 和 tasks.md 的 Dependencies 节说明

**产出**：更新 `plan.md`、`tasks.md` 依赖节

---

### 步骤 8：建立「Spec 自检清单」

**目标**：每次更新 spec 后，用清单自检是否达到可执行粒度。

**自检项**：见 [SPEC-REFINEMENT-CHECKLIST.md](SPEC-REFINEMENT-CHECKLIST.md)

**产出**：`doc/SPEC-REFINEMENT-CHECKLIST.md`

---

## 三、产出物汇总


| 步骤     | 产出文件                                     | 所属 spec  |
| ------ | ---------------------------------------- | -------- |
| 步骤 1 | class-inventory.md                       | 001, 002 |
| 步骤 2 | contracts/base-class-modification.md     | 001      |
| 步骤 2 | contracts/base-class-compose-strategy.md | 002      |
| 步骤 3 | modification-patterns.md                 | 001, 002 |
| 步骤 4 | 更新 tasks.md                              | 001, 002 |
| 步骤 5 | checklists/high-risk-classes.md          | 001      |
| 步骤 5 | checklists/security-sensitive-screens.md | 002      |
| 步骤 6 | acceptance-per-class.md                  | 001, 002 |
| 步骤 7 | 更新 plan.md、tasks.md 依赖节                  | 001, 002 |
| 步骤 8 | doc/SPEC-REFINEMENT-CHECKLIST.md         | 通用       |


---

## 四、执行建议

1. **先做 001-kotlin-migration**：类清单和修改模式相对简单
2. **再做 002-compose-migration**：依赖 001 的类清单，补充 Compose 特有策略
3. **最后统一**：更新 tasks.md、plan.md，并运行步骤 8 自检清单

---

## 五、与现有文档的关系

- **SPEC-KIT-使用步骤.md**：描述如何用 Spec-Kit 命令生成 spec
- **本步骤文档**：描述如何将已生成的 spec 细化到类级、可执行级
- 二者互补：先按 SPEC-KIT 生成，再按本步骤细化

