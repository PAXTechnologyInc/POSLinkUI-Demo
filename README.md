
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

## UI List
You just need focus on required actions of specific BroadPOS app and don't have to implement ALL UI actions defined in POSLinkUI.
Specific AndroidManifest.xml for reference:
1. [ALL](./app/src/main/AndroidManifest.xml)
2. [Shift4](./app/src/Shift4/AndroidManifest.xml)
3. [FDRCNV](./app/src/FDRCNV/AndroidManifest.xml)

## POSLinkUI Transaction flow
The log below would help you understand how POSLinkUI run when do a transaction.

A simple explanation for reading log (Step by Step)

- BroadPOS startActivity "com.pax.us.pay.action.ENTER_AMOUNT"
- POSLinkUIDemo create activity per intent
    > start Entry Action "com.pax.us.pay.action.ENTER_AMOUNT"
- After click confirm button, POSLInkUIDemo send next broadcast with result to BroadPOS
    > send Entry Request ACTION_NEXT from action  "com.pax.us.pay.action.ENTER_AMOUNT"
- BroadPOS return entry response tell POSLinkUIDemo that the result could be accepted or declined
- If declined, that means the input is not valid, user need retry. Generally POSLinkUIDemo will show a toast message.
    > receive Entry Response ACTION_DECLINED for action "com.pax.us.pay.action.ENTER_AMOUNT" (-32-Please Input Amount)
- After user redo input amount, click confirm button, POSLinkUIDemo request next again.
    > send Entry Request ACTION_NEXT from action  "com.pax.us.pay.action.ENTER_AMOUNT"
- Finally, ACTION_NEXT was accepted. The ENTER_AMOUNT action end.
    > receive Entry Response ACTION_ACCEPTED for action "com.pax.us.pay.action.ENTER_AMOUNT"
- Then generally BroadPOS will go to next step. Like startActivity for enter tip.
    > start Entry Action "com.pax.us.pay.action.ENTER_TIP"


1.Example log for a chip transaction:

    EntryActivity onCreate
    start Entry Action "com.pax.us.pay.action.ENTER_AMOUNT"
    AmountFragment onCreateView
    AmountTextWatcher afterTextChanged:$0.01
    AmountTextWatcher afterTextChanged:$0.10
    AmountTextWatcher afterTextChanged:$1.00
    send Entry Request ACTION_NEXT from action  "com.pax.us.pay.action.ENTER_AMOUNT"
    receive Entry Response ACTION_ACCEPTED for action "com.pax.us.pay.action.ENTER_AMOUNT"
    EntryActivity onNewIntent
    start Entry Action "com.pax.us.pay.action.ENTER_TIP"
    TipFragment onCreateView
    AmountFragment onDestroy
    send Entry Request ACTION_NEXT from action  "com.pax.us.pay.action.ENTER_TIP"
    receive Entry Response ACTION_ACCEPTED for action "com.pax.us.pay.action.ENTER_TIP"
    EntryActivity onNewIntent
    start Entry Action "com.pax.us.pay.action.INPUT_ACCOUNT"
    InputAccountFragment onCreateView
    TipFragment onDestroy
    send Entry Request ACTION_SECURITY_AREA for action "com.pax.us.pay.action.INPUT_ACCOUNT"
    receive Status Action "com.pax.us.pay.CLSS_LIGHT_READY_FOR_TXN"
    receive Status Action "com.pax.us.pay.CLSS_LIGHT_READY_FOR_TXN"
    receive Status Action "com.pax.us.pay.CARD_PROCESS_STARTED"
    receive Status Action "com.pax.us.pay.CARD_PROCESS_COMPLETED"
    receive Entry Response ACTION_ACCEPTED for action "com.pax.us.pay.action.INPUT_ACCOUNT"
    EntryActivity onNewIntent
    start Entry Action "com.pax.us.pay.action.SELECT_AID"
    OptionsDialogFragment onCreateView
    send Entry Request ACTION_NEXT from action  "com.pax.us.pay.action.SELECT_AID"
    receive Entry Response ACTION_ACCEPTED for action "com.pax.us.pay.action.SELECT_AID"
    OptionsDialogFragment onDismiss
    receive Status Action "com.pax.us.pay.CARD_PROCESS_STARTED"
    receive Status Action "com.pax.us.pay.CARD_PROCESS_COMPLETED"
    EntryActivity onNewIntent
    start Entry Action "com.pax.us.pay.action.ENTER_PIN"
    PINFragment onCreateView
    InputAccountFragment onDestroy
    PIN Layout[key_cancel]:Rect(481, 665 - 712, 804)
    PIN Layout[key_3]:Rect(323, 665 - 465, 804)
    PIN Layout[key_clear]:Rect(481, 820 - 712, 960)
    PIN Layout[key_enter]:Rect(481, 976 - 712, 1272)
    PIN Layout[key_4]:Rect(8, 820 - 150, 960)
    PIN Layout[key_0]:Rect(166, 1132 - 307, 1272)
    PIN Layout[key_7]:Rect(8, 976 - 150, 1116)
    PIN Layout[key_9]:Rect(323, 976 - 465, 1116)
    PIN Layout[key_8]:Rect(166, 976 - 307, 1116)
    PIN Layout[key_6]:Rect(323, 820 - 465, 960)
    PIN Layout[key_5]:Rect(166, 820 - 307, 960)
    PIN Layout[key_2]:Rect(166, 665 - 307, 804)
    PIN Layout[key_1]:Rect(8, 665 - 150, 804)
    send Entry Request ACTION_SET_PIN_KEY_LAYOUT for action "com.pax.us.pay.action.ENTER_PIN"
    send Entry Request ACTION_SECURITY_AREA for action "com.pax.us.pay.action.ENTER_PIN"
    receive Status Action "com.pax.us.pay.PIN_ENTERING"
    receive Status Action "com.pax.us.pay.PIN_ENTERING"
    receive Status Action "com.pax.us.pay.PIN_ENTERING"
    receive Status Action "com.pax.us.pay.PIN_ENTERING"
    receive Entry Response ACTION_ACCEPTED for action "com.pax.us.pay.action.ENTER_PIN"
    receive Status Action "com.pax.us.pay.CARD_REMOVAL_REQUIRED"
    receive Status Action "com.pax.us.pay.CARD_REMOVED"
    EntryActivity onCreate
    start Entry Action "com.pax.us.pay.action.GET_SIGNATURE"
    SignatureFragment onCreateView
    EntryActivity onSaveInstanceState
    send Entry Request ACTION_NEXT from action  "com.pax.us.pay.action.GET_SIGNATURE"
    receive Entry Response ACTION_ACCEPTED for action "com.pax.us.pay.action.GET_SIGNATURE"
    EntryActivity onNewIntent
    start Entry Action "com.pax.us.pay.action.CONFIRM_RECEIPT_VIEW"
    ConfirmReceiptViewFragment onCreateView
    SignatureFragment onDestroy
    send Entry Request ACTION_NEXT from action  "com.pax.us.pay.action.CONFIRM_RECEIPT_VIEW"
    receive Entry Response ACTION_ACCEPTED for action "com.pax.us.pay.action.CONFIRM_RECEIPT_VIEW"
    receive Status Action "com.pax.us.pay.PRINT_STARTED"
    receive Status Action "com.pax.us.pay.PRINT_COMPLETED"
    EntryActivity onNewIntent
    start Entry Action "com.pax.us.pay.action.CONFIRM_PRINT_CUSTOMER_COPY"
    ConfirmationDialogFragment onCreateView
    PINFragment onDestroy
    EntryActivity onDestroy
    ConfirmationDialogFragment onDismiss
    send Entry Request ACTION_NEXT from action  "com.pax.us.pay.action.CONFIRM_PRINT_CUSTOMER_COPY"
    receive Status Action "com.pax.us.pay.PRINT_STARTED"
    receive Status Action "com.pax.us.pay.PRINT_COMPLETED"
    receive Status Action "com.pax.us.pay.PRINT_COMPLETED"
    receive Status Action "com.pax.us.pay.CARD_REMOVAL_REQUIRED"
    receive Status Action "com.pax.us.pay.CARD_REMOVED"
    receive Status Action "com.pax.us.pay.TRANS_COMPLETED"
    TRANS_COMPLETED:0,CREDIT SALE SUCCESS
    ConfirmReceiptViewFragment onDestroy
    EntryActivity onDestroy

## How to build

### 1.Config Maven
In build.gradle of root project, we add maven repository of POSLinkUI.
```
    allprojects {
        repositories {
            google()
            mavenCentral()
            maven {
                url 'https://maven.pkg.github.com/PAXTechnologyInc/POSLink-UI'
                credentials {
                    username = GITHUB_USER_NAME
                    password = GITHUB_USER_TOKEN
                }
            }
            mavenLocal()
        }

    }
```

You can login your github credential by 2 ways:
1. Settings->Version Control->Github->Log In using token
2. Add github user name and token to local.properties. Example:
```
    gpr.key=XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
    gpr.user=XXXXXXXXXXX@XXXXXX
```

About how to generate github token. Please refer [Github Personal Access Token](https://docs.github.com/en/authentication/keeping-your-account-and-data-secure/creating-a-personal-access-token)

