# Auto Regression Map

## Category Representative Actions

| Category | Representative Action | Automated Coverage |
|---|---|---|
| TextEntry | `ACTION_ENTER_VISA_INSTALLMENT_TRANSACTIONID` | `TextEntryResponseParamsTest` |
| OptionEntry | `ACTION_SELECT_INSTALLMENT_PLAN` | `EntryActionRegistryTest` |
| ConfirmationEntry | `ACTION_CONFIRM_DEBIT_TRANS` | `EntryActionRegistryTest` / `ConfirmationRouteTest` |
| InformationEntry | `ACTION_DISPLAY_VISA_INSTALLMENT_TRANSACTION_END` | `EntryActionRegistryTest` / `InfoRouteTest` |

## Execution Result

- Last local run: pass
- Command: `./gradlew.bat :app:assembleDebug :app:testDebugUnitTest`
