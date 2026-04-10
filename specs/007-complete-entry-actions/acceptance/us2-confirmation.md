# US2 Validation - Confirmation

## Covered Gap Actions

- `ACTION_CONFIRM_BATCH_CLOSE_WITH_INCOMPLETE_TRANSACTION`
- `ACTION_CONFIRM_BATCH_FOR_APPLICATION_UPDATE`
- `ACTION_CONFIRM_DEBIT_TRANS`
- `ACTION_CONFIRM_ONLINE_RETRY_OFFLINE`
- `ACTION_CONFIRM_TAX_AMOUNT`

## Implementation Evidence

- Route registry: updated with all five actions
- Message/button mapping: updated in `ConfirmationMessageFormatter`
- Submit payload: unified via confirmation submit helper (`PARAM_CONFIRMED=true/false`)
- Unit tests: `EntryActionRegistryTest`, `ConfirmationRouteTest`

## Exception Branch Notes

- Confirm branch emits `PARAM_CONFIRMED=true`
- Cancel branch emits `PARAM_CONFIRMED=false`
