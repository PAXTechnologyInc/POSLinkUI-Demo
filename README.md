
# POSLinkUI-Demo

POSLinkUI 的简单演示工程。

## EntryActivity
使用 Fragment 实现全部 UI（Activity 与 Dialog）。
1. `exported` 应为 `true`，以便通过 Intent 启动该 Activity。
2. `launchMode` 设为 `singleTop`：若 Activity 已在栈顶则不会新建实例，会收到 `onNewIntent`。
3. 展示类似 `ConfirmationEntry.ACTION_CONFIRM_BATCH_CLOSE` 的对话框时，主题设为无 ActionBar 且透明。
4. 避免在最近任务中出现两个应用图标造成困惑，启用自动从最近任务移除。
```
    <activity
        android:name=".entry.EntryActivity"
        android:exported="true"
        android:launchMode="singleTop"
        android:theme="@style/Theme.POSLinkUI"
        android:autoRemoveFromRecents="true">
```

## UI 列表
只需关注目标 BroadPOS 应用**所需**的 Action，不必实现 POSLinkUI 中定义的**全部** UI Action。
可参考的 AndroidManifest：
1. [全部](./app/src/main/AndroidManifest.xml)
2. [Shift4](./app/src/Shift4/AndroidManifest.xml)
3. [FDRCNV](./app/src/FDRCNV/AndroidManifest.xml)

## POSLinkUI 交易流程
下面日志有助于理解做一笔交易时 POSLinkUI 如何运行。

### 阅读日志的简单说明（按步骤）

- BroadPOS `startActivity`：`"com.pax.us.pay.action.ENTER_AMOUNT"`
- POSLinkUIDemo 按 Intent 创建 Activity
    > 启动 Entry Action `"com.pax.us.pay.action.ENTER_AMOUNT"`
- 用户点击确认后，POSLinkUIDemo 向 BroadPOS 发送带结果的下一跳广播
    > 发送请求广播 `ACTION_NEXT`，来自 action `"com.pax.us.pay.action.ENTER_AMOUNT"`
- BroadPOS 返回 entry 响应，告知结果可被接受或拒绝
- 若拒绝，表示输入不合法，用户需重试；一般 POSLinkUIDemo 会弹出 Toast。
    > 收到响应广播 `ACTION_DECLINED`，对应 action `"com.pax.us.pay.action.ENTER_AMOUNT"`（-32-Please Input Amount）
- 用户重新输入金额并确认后，POSLinkUIDemo 再次请求下一跳。
    > 发送请求广播 `ACTION_NEXT`，来自 action `"com.pax.us.pay.action.ENTER_AMOUNT"`
- 最终 `ACTION_NEXT` 被接受，`ENTER_AMOUNT` 结束。
    > 收到响应广播 `ACTION_ACCEPTED`，对应 action `"com.pax.us.pay.action.ENTER_AMOUNT"`
- 随后 BroadPOS 一般会进入下一步，例如启动小费输入 Activity。
    > 启动 Entry Action `"com.pax.us.pay.action.ENTER_TIP"`


1. 芯片卡交易示例日志：

    EntryActivity onCreate
    start Entry Action "com.pax.us.pay.action.ENTER_AMOUNT"
    AmountFragment onCreateView
    AmountTextWatcher afterTextChanged:$0.01
    AmountTextWatcher afterTextChanged:$0.10
    AmountTextWatcher afterTextChanged:$1.00
    Send Request Broadcast ACTION_NEXT from action  "com.pax.us.pay.action.ENTER_AMOUNT"
    Receive Response Broadcast ACTION_ACCEPTED for action "com.pax.us.pay.action.ENTER_AMOUNT"
    EntryActivity onNewIntent
    start Entry Action "com.pax.us.pay.action.ENTER_TIP"
    TipFragment onCreateView
    AmountFragment onDestroy
    Send Request Broadcast ACTION_NEXT from action  "com.pax.us.pay.action.ENTER_TIP"
    Receive Response Broadcast ACTION_ACCEPTED for action "com.pax.us.pay.action.ENTER_TIP"
    EntryActivity onNewIntent
    start Entry Action "com.pax.us.pay.action.INPUT_ACCOUNT"
    InputAccountFragment onCreateView
    TipFragment onDestroy
    Send Request Broadcast ACTION_SECURITY_AREA for action "com.pax.us.pay.action.INPUT_ACCOUNT"
    receive Status Action "com.pax.us.pay.CLSS_LIGHT_READY_FOR_TXN"
    receive Status Action "com.pax.us.pay.CLSS_LIGHT_READY_FOR_TXN"
    receive Status Action "com.pax.us.pay.CARD_PROCESS_STARTED"
    receive Status Action "com.pax.us.pay.CARD_PROCESS_COMPLETED"
    Receive Response Broadcast ACTION_ACCEPTED for action "com.pax.us.pay.action.INPUT_ACCOUNT"
    EntryActivity onNewIntent
    start Entry Action "com.pax.us.pay.action.SELECT_AID"
    OptionsDialogFragment onCreateView
    Send Request Broadcast ACTION_NEXT from action  "com.pax.us.pay.action.SELECT_AID"
    Receive Response Broadcast ACTION_ACCEPTED for action "com.pax.us.pay.action.SELECT_AID"
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
    Send Request Broadcast ACTION_SET_PIN_KEY_LAYOUT for action "com.pax.us.pay.action.ENTER_PIN"
    Send Request Broadcast ACTION_SECURITY_AREA for action "com.pax.us.pay.action.ENTER_PIN"
    receive Status Action "com.pax.us.pay.PIN_ENTERING"
    receive Status Action "com.pax.us.pay.PIN_ENTERING"
    receive Status Action "com.pax.us.pay.PIN_ENTERING"
    receive Status Action "com.pax.us.pay.PIN_ENTERING"
    Receive Response Broadcast ACTION_ACCEPTED for action "com.pax.us.pay.action.ENTER_PIN"
    receive Status Action "com.pax.us.pay.CARD_REMOVAL_REQUIRED"
    receive Status Action "com.pax.us.pay.CARD_REMOVED"
    EntryActivity onCreate
    start Entry Action "com.pax.us.pay.action.GET_SIGNATURE"
    SignatureFragment onCreateView
    EntryActivity onSaveInstanceState
    Send Request Broadcast ACTION_NEXT from action  "com.pax.us.pay.action.GET_SIGNATURE"
    Receive Response Broadcast ACTION_ACCEPTED for action "com.pax.us.pay.action.GET_SIGNATURE"
    EntryActivity onNewIntent
    start Entry Action "com.pax.us.pay.action.CONFIRM_RECEIPT_VIEW"
    ConfirmReceiptViewFragment onCreateView
    SignatureFragment onDestroy
    Send Request Broadcast ACTION_NEXT from action  "com.pax.us.pay.action.CONFIRM_RECEIPT_VIEW"
    Receive Response Broadcast ACTION_ACCEPTED for action "com.pax.us.pay.action.CONFIRM_RECEIPT_VIEW"
    receive Status Action "com.pax.us.pay.PRINT_STARTED"
    receive Status Action "com.pax.us.pay.PRINT_COMPLETED"
    EntryActivity onNewIntent
    start Entry Action "com.pax.us.pay.action.CONFIRM_PRINT_CUSTOMER_COPY"
    ConfirmationDialogFragment onCreateView
    PINFragment onDestroy
    EntryActivity onDestroy
    ConfirmationDialogFragment onDismiss
    Send Request Broadcast ACTION_NEXT from action  "com.pax.us.pay.action.CONFIRM_PRINT_CUSTOMER_COPY"
    receive Status Action "com.pax.us.pay.PRINT_STARTED"
    receive Status Action "com.pax.us.pay.PRINT_COMPLETED"
    receive Status Action "com.pax.us.pay.PRINT_COMPLETED"
    receive Status Action "com.pax.us.pay.CARD_REMOVAL_REQUIRED"
    receive Status Action "com.pax.us.pay.CARD_REMOVED"
    receive Status Action "com.pax.us.pay.TRANS_COMPLETED"
    TRANS_COMPLETED:0,CREDIT SALE SUCCESS
    ConfirmReceiptViewFragment onDestroy
    EntryActivity onDestroy

## 如何集成 POSLinkUI 库
POSLinkUI 库发布在 GitHub Maven，可按本 Demo 工程方式集成。

### 1. 配置 Maven
在工程根目录 `build.gradle` 中加入 POSLinkUI 的 Maven 仓库。
```
Boolean mavenLocalFirst = gradleProps.getProperty("maven.local.first").toBoolean()
println("maven.local.first: $mavenLocalFirst")
allprojects {
    repositories {
        if(mavenLocalFirst) {
            // Project local maven repo
            maven {
                url new File(project.rootDir, "maven")
            }
        } else {
            maven {
                url 'https://maven.pkg.github.com/PAXTechnologyInc/POSLink-UI'
                credentials {
                    username = GITHUB_USER_NAME
                    password = GITHUB_USER_TOKEN
                }
            }
        }
        google()
        mavenCentral()
        mavenLocal()
    }

```

可在 `gradle.properties` 中修改 `maven.local.first`。默认值为 `true`，工程从本地 Maven 取 POSLinkUI；若要从远程拉取，将该值设为 `false`。

```
maven.local.first=true
```

若 `maven.local.first=false`，需配置 GitHub 凭据。

可通过两种方式登录 GitHub：

1. Settings → Version Control → Github → Log In using token  
2. 在 `local.properties` 中写入用户名与 token，例如：
```
    gpr.key=XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
    gpr.user=XXXXXXXXXXX@XXXXXX
```

**注意**：创建 token 时请勾选 **read:packages** 权限。

如何生成 GitHub token，参见 [Github Personal Access Token](https://docs.github.com/en/authentication/keeping-your-account-and-data-secure/creating-a-personal-access-token)。

### 2. 添加依赖
在 `app/build.gradle` 的 `dependencies` 中加入：
```
    dependencies {
        implementation "com.paxus.ui:constant:1.01.00"
    }
```


## 构建常见问题

### Q1：构建失败，ArtifactResolveException
原因示例：`org.gradle.api.internal.artifacts.ivyservice.DefaultLenientConfiguration&ArtifactResolveException: Could not resolve all files for configuration ':app:debugRuntimeClasspath'`。

**处理**：使用 GitHub token 登录，并确认 token 具有 **read:packages** 权限。
