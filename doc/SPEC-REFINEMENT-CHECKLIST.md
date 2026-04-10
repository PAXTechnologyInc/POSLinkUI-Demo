# Spec 细化自检清单

**用途**：每次更新 spec 后，用本清单自检是否达到「可执行」粒度。  
**日期**：2025-03-18

---

## 自检项

- [x] **类清单**：是否列出该 feature 涉及的全部类（或可推导的过滤条件）？→ 001、002 均有 [class-inventory.md](../specs/001-kotlin-migration/class-inventory.md)
- [x] **基类约束**：基类修改约束是否明确？→ 001 [base-class-modification.md](../specs/001-kotlin-migration/contracts/base-class-modification.md)、002 [base-class-compose-strategy.md](../specs/002-compose-migration/contracts/base-class-compose-strategy.md)
- [x] **修改模式**：是否按类类型定义了修改模式？→ 001、002 均有 [modification-patterns.md](../specs/001-kotlin-migration/modification-patterns.md)
- [x] **任务与文件**：每个 task 是否对应到具体文件路径？→ tasks.md 已拆分 T011a–e 并引用 class-inventory
- [x] **高风险类**：高风险类是否有单独审查/修改要点？→ 001 [high-risk-classes.md](../specs/001-kotlin-migration/checklists/high-risk-classes.md)、002 [security-sensitive-screens.md](../specs/002-compose-migration/checklists/security-sensitive-screens.md)
- [x] **验收粒度**：验收是否可落到类级？→ 001、002 均有 [acceptance-per-class.md](../specs/001-kotlin-migration/acceptance-per-class.md)
- [x] **依赖与顺序**：依赖与执行顺序是否明确？→ plan.md、tasks.md 均已补充 Class-Level Execution Order

---

## 引用

- 完整步骤见 [SPEC-REFINEMENT-STEPS.md](SPEC-REFINEMENT-STEPS.md)
- 001-kotlin-migration 产出：class-inventory.md、base-class-modification.md、modification-patterns.md、high-risk-classes.md、acceptance-per-class.md
- 002-compose-migration 产出：class-inventory.md、base-class-compose-strategy.md、modification-patterns.md、security-sensitive-screens.md、acceptance-per-class.md
