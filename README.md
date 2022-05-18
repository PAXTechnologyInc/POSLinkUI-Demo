# POSLinkUI-Demo

This is a simple demo for POSLinkUI.

## EntryActivity
Use fragment to implement all UI (Activity and Dialog).
1. exported should be true. Then this activity can be started by Intent.
2. launchMode is set to singleTop. If the activity is at the top of stack, it will not be created. New intent will be called.
3. To show some dialog like ConfirmationEntry.ACTION_CONFIRM_BATCH_CLOSE. The theme is set to No Action Bar and transparent.
4. To not confuse user by seeing 2 app in recents, auto remove from recents
```
    <activity
        android:name=".entry.EntryActivity"
        android:exported="true"
        android:launchMode="singleTop"
        android:theme="@style/Theme.POSLinkUI"
        android:autoRemoveFromRecents="true">
```

## POSLinkUI Transaction flow
The log below would help you understand how POSLinkUI run when do a transaction.

A simple explanation for reading log (Step by Step)
-BroadPOS startActivity "com.pax.us.pay.action.ENTER_AMOUNT"
-POSLinkUIDemo create activity per intent
    >start Entry Action "com.pax.us.pay.action.ENTER_AMOUNT"
-After click confirm button, POSLInkUIDemo send next broadcast with result to BroadPOS
    >send Entry Request ACTION_NEXT from action  "com.pax.us.pay.action.ENTER_AMOUNT"
-BroadPOS return entry response tell POSLinkUIDemo that the result could be accepted or declined
-If declined, that means the input is not valid, user need retry. Generally POSLinkUIDemo will show a toast message.
    >receive Entry Response ACTION_DECLINED for action "com.pax.us.pay.action.ENTER_AMOUNT" (-32-Please Input Amount)
-After user redo input amount, click confirm button, POSLinkUIDemo request next again.
    >send Entry Request ACTION_NEXT from action  "com.pax.us.pay.action.ENTER_AMOUNT"
-Finally, ACTION_NEXT was accepted. The ENTER_AMOUNT action end.
    >receive Entry Response ACTION_ACCEPTED for action "com.pax.us.pay.action.ENTER_AMOUNT"
-Then generally BroadPOS will go to next step. Like startActivity for enter tip.
    >start Entry Action "com.pax.us.pay.action.ENTER_TIP"


1.Example log for a chip transaction:

    start Entry Action "com.pax.us.pay.action.ENTER_AMOUNT"
    send Entry Request ACTION_NEXT from action  "com.pax.us.pay.action.ENTER_AMOUNT"
    receive Entry Response ACTION_ACCEPTED for action "com.pax.us.pay.action.ENTER_AMOUNT"
    start Entry Action "com.pax.us.pay.action.ENTER_TIP"
    send Entry Request ACTION_NEXT from action  "com.pax.us.pay.action.ENTER_TIP"
    receive Entry Response ACTION_ACCEPTED for action "com.pax.us.pay.action.ENTER_TIP"
    start Entry Action "com.pax.us.pay.action.INPUT_ACCOUNT"
    send Entry Request ACTION_SECURITY_AREA for action "com.pax.us.pay.action.INPUT_ACCOUNT"
    receive Status Action "com.pax.us.pay.CLSS_LIGHT_READY_FOR_TXN"
    receive Status Action "com.pax.us.pay.CLSS_LIGHT_READY_FOR_TXN"
    receive Status Action "com.pax.us.pay.CARD_PROCESS_STARTED"
    receive Status Action "com.pax.us.pay.CARD_PROCESS_COMPLETED"
    receive Entry Response ACTION_ACCEPTED for action "com.pax.us.pay.action.INPUT_ACCOUNT"
    start Entry Action "com.pax.us.pay.action.SELECT_AID"
    send Entry Request ACTION_NEXT from action  "com.pax.us.pay.action.SELECT_AID"
    receive Entry Response ACTION_ACCEPTED for action "com.pax.us.pay.action.SELECT_AID"
    receive Status Action "com.pax.us.pay.CARD_PROCESS_STARTED"
    receive Status Action "com.pax.us.pay.CARD_PROCESS_COMPLETED"
    start Entry Action "com.pax.us.pay.action.ENTER_PIN"
    receive Entry Response ACTION_ACCEPTED for action "com.pax.us.pay.action.ENTER_PIN"
    receive Status Action "com.pax.us.pay.EMV_TRANS_ONLINE_STARTED"
    receive Status Action "com.pax.us.pay.EMV_TRANS_ONLINE_FINISHED"
    start Entry Action "com.pax.us.pay.action.GET_SIGNATURE"
    send Entry Request ACTION_NEXT from action  "com.pax.us.pay.action.GET_SIGNATURE"
    receive Entry Response ACTION_ACCEPTED for action "com.pax.us.pay.action.GET_SIGNATURE"
    receive Status Action "com.pax.us.pay.CARD_REMOVAL_REQUIRED"
    receive Status Action "com.pax.us.pay.CARD_REMOVED"
    receive Status Action "com.pax.us.pay.TRANS_COMPLETED"

2.Example log for Entry Response ACTION_DECLINED:

    start Entry Action "com.pax.us.pay.action.ENTER_AMOUNT"
    send Entry Request ACTION_NEXT from action  "com.pax.us.pay.action.ENTER_AMOUNT"
    receive Entry Response ACTION_DECLINED for action "com.pax.us.pay.action.ENTER_AMOUNT" (-32-Please Input Amount)
    send Entry Request ACTION_NEXT from action  "com.pax.us.pay.action.ENTER_AMOUNT"
    receive Entry Response ACTION_ACCEPTED for action "com.pax.us.pay.action.ENTER_AMOUNT"

## TODO List:
1. Implement "com.pax.us.pay.action.ENTER_FLEET_DATA".
2. Implement "com.pax.us.pay.action.ENTER_FSA_DATA"
3. Implement PoslinkEntry