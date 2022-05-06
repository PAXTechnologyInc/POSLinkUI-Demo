package com.paxus.pay.poslinkui.demo.entry;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.pax.us.pay.ui.constant.entry.ConfirmationEntry;
import com.pax.us.pay.ui.constant.entry.EntryExtraData;
import com.pax.us.pay.ui.constant.entry.EntryRequest;
import com.pax.us.pay.ui.constant.entry.InformationEntry;
import com.pax.us.pay.ui.constant.entry.OptionEntry;
import com.pax.us.pay.ui.constant.entry.SecurityEntry;
import com.pax.us.pay.ui.constant.entry.SignatureEntry;
import com.pax.us.pay.ui.constant.entry.TextEntry;
import com.pax.us.pay.ui.constant.status.CardStatus;
import com.pax.us.pay.ui.constant.status.InformationStatus;
import com.paxus.pay.poslinkui.demo.R;
import com.paxus.pay.poslinkui.demo.entry.confirmation.ConfirmationDialogFragment;
import com.paxus.pay.poslinkui.demo.entry.information.DisplayTransInfoFragment;
import com.paxus.pay.poslinkui.demo.entry.option.OptionsDialogFragment;
import com.paxus.pay.poslinkui.demo.entry.security.InputAccountFragment;
import com.paxus.pay.poslinkui.demo.entry.security.PINFragment;
import com.paxus.pay.poslinkui.demo.entry.security.SecurityFragment;
import com.paxus.pay.poslinkui.demo.entry.signature.SignatureFragment;
import com.paxus.pay.poslinkui.demo.entry.text.AVSFragment;
import com.paxus.pay.poslinkui.demo.entry.text.AmountFragment;
import com.paxus.pay.poslinkui.demo.entry.text.CashbackFragment;
import com.paxus.pay.poslinkui.demo.entry.text.ExpiryFragment;
import com.paxus.pay.poslinkui.demo.entry.text.NumFragment;
import com.paxus.pay.poslinkui.demo.entry.text.NumTextFragment;
import com.paxus.pay.poslinkui.demo.entry.text.TextFragment;
import com.paxus.pay.poslinkui.demo.entry.text.TipFragment;
import com.paxus.pay.poslinkui.demo.entry.text.TotalAmountFragment;
import com.paxus.pay.poslinkui.demo.event.EntryAbortEvent;
import com.paxus.pay.poslinkui.demo.event.InformationStatusEvent;
import com.paxus.pay.poslinkui.demo.event.TransCompletedEvent;
import com.paxus.pay.poslinkui.demo.status.InformationDialogFragment;
import com.paxus.pay.poslinkui.demo.status.TransCompletedDialogFragment;
import com.paxus.pay.poslinkui.demo.utils.EntryRequestUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Set;

public class EntryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);
        Log.d("EntryActivity","onCreate");

        EventBus.getDefault().register(this);
        loadView(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d("EntryActivity","onNewIntent");

        loadView(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("EntryActivity","onResume");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("EntryActivity","onDestroy");

        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        EventBus.getDefault().post(new EntryAbortEvent());
    }

    private void loadView(Intent intent){

        Fragment fragment = createFragment(intent);
        if(fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_placeholder, fragment);
            ft.commit();
        }else {
            DialogFragment dialogFragment = createDialogFragment(intent);
            if(dialogFragment != null) {
                dialogFragment.show(getSupportFragmentManager(), "EntryDialog");
            }else if(InformationEntry.ACTION_DISPLAY_APPROVE_MESSAGE.equals(intent.getAction())) {
                //TODO if you want play your online approve sound or animation, build your fragment.
                Toast.makeText(this, "Online Approved", Toast.LENGTH_SHORT).show();
                EntryRequestUtils.sendNext(this,intent.getStringExtra(EntryExtraData.PARAM_PACKAGE), InformationEntry.ACTION_DISPLAY_APPROVE_MESSAGE);
            }else {
                Toast.makeText(this, "NOT FOUND:" + intent.getAction(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTransactionCompleted(TransCompletedEvent event) {

        if(event.code == -3){//Transaction Cancelled
            finish();
        }else {

            DialogFragment dialogFragment = TransCompletedDialogFragment.newInstance(
                    event.code,
                    event.message,
                    event.timeout
            );

            dialogFragment.show(getSupportFragmentManager(), "TransCompleteDialog");
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onInformationEvent(InformationStatusEvent event) {
        switch (event.action){
            case InformationStatus.TRANS_ONLINE_STARTED:
                showInfoDialog(event.action,"trans_online");
                break;
            case InformationStatus.TRANS_ONLINE_FINISHED:
                closeDialog("trans_online");
                break;
            case InformationStatus.EMV_TRANS_ONLINE_STARTED:
                showInfoDialog(event.action,"emv_trans_online");
                break;
            case InformationStatus.EMV_TRANS_ONLINE_FINISHED:
                closeDialog("emv_trans_online");
                break;
            case InformationStatus.RKI_STARTED:
                showInfoDialog(event.action,"rki_process");
                break;
            case InformationStatus.RKI_FINISHED:
                closeDialog("rki_process");
                break;
            case InformationStatus.DCC_ONLINE_STARTED:
                showInfoDialog(event.action,"dcc_online");
                break;
            case InformationStatus.DCC_ONLINE_FINISHED:
                closeDialog("dcc_online");
                break;
            case InformationStatus.PINPAD_CONNECTION_STARTED:
                showInfoDialog(event.action,"pin_pad_connection");
                break;
            case InformationStatus.PINPAD_CONNECTION_FINISHED:
                closeDialog("pin_pad_connection");
                break;
            case CardStatus.CARD_REMOVAL_REQUIRED:
                showInfoDialog(event.action,"remove_card");
                break;
            case CardStatus.CARD_REMOVED:
                closeDialog("remove_card");
                break;
            case CardStatus.CARD_PROCESS_STARTED:
                showInfoDialog(event.action,"card_process");
                break;
            case CardStatus.CARD_PROCESS_COMPLETED:
                closeDialog("card_process");
                break;
            default:
                break;
        }
    }

    private void showInfoDialog(String action, String tag){
        DialogFragment dialogFragment = InformationDialogFragment.newInstance(
                action
        );

        dialogFragment.show(getSupportFragmentManager(), tag);
    }

    private void closeDialog(String tag){
        Fragment prev = getSupportFragmentManager().findFragmentByTag(tag);
        if (prev != null) {
            DialogFragment df = (DialogFragment) prev;
            df.dismiss();
        }
    }

    private Fragment createFragment(Intent intent){
        String action = intent.getAction();
        Set<String> categories = intent.getCategories();
        if(action != null){
            if(categories.contains(TextEntry.CATEGORY)) {
                switch (action) {
                    case TextEntry.ACTION_ENTER_AMOUNT:
                    case TextEntry.ACTION_ENTER_FUEL_AMOUNT:
                    case TextEntry.ACTION_ENTER_TAX_AMOUNT:
                        return AmountFragment.newInstance(intent);
                    case TextEntry.ACTION_ENTER_CASH_BACK:
                        return CashbackFragment.newInstance(intent);
                    case TextEntry.ACTION_ENTER_TIP:
                        return TipFragment.newInstance(intent);
                    case TextEntry.ACTION_ENTER_TOTAL_AMOUNT:
                        return TotalAmountFragment.newInstance(intent);
                    case TextEntry.ACTION_ENTER_CLERK_ID:
                    case TextEntry.ACTION_ENTER_SERVER_ID:
                    case TextEntry.ACTION_ENTER_TABLE_NUMBER:
                    case TextEntry.ACTION_ENTER_CS_PHONE_NUMBER:
                    case TextEntry.ACTION_ENTER_PHONE_NUMBER:
                    case TextEntry.ACTION_ENTER_GUEST_NUMBER:
                    case TextEntry.ACTION_ENTER_MERCHANT_TAX_ID:
                    case TextEntry.ACTION_ENTER_PROMPT_RESTRICTION_CODE:
                    case TextEntry.ACTION_ENTER_TRANS_NUMBER:
                        return NumFragment.newInstance(intent);
                    case TextEntry.ACTION_ENTER_ADDRESS:
                    case TextEntry.ACTION_ENTER_AUTH:
                    case TextEntry.ACTION_ENTER_CUSTOMER_CODE:
                    case TextEntry.ACTION_ENTER_ORDER_NUMBER:
                    case TextEntry.ACTION_ENTER_PO_NUMBER:
                    case TextEntry.ACTION_ENTER_PROD_DESC:
                        return TextFragment.newInstance(intent);
                    case TextEntry.ACTION_ENTER_ZIPCODE:
                    case TextEntry.ACTION_ENTER_DEST_ZIPCODE:
                    case TextEntry.ACTION_ENTER_INVOICE_NUMBER:
                    case TextEntry.ACTION_ENTER_VOUCHER_DATA:
                    case TextEntry.ACTION_ENTER_REFERENCE_NUMBER:
                    case TextEntry.ACTION_ENTER_MERCHANT_REFERENCE_NUMBER:
                    case TextEntry.ACTION_ENTER_OCT_REFERENCE_NUMBER:
                        return NumTextFragment.newInstance(intent);
                    case TextEntry.ACTION_ENTER_AVS_DATA:
                        return AVSFragment.newInstance(intent);
                    case TextEntry.ACTION_ENTER_EXPIRY_DATE:
                        return ExpiryFragment.newInstance(intent);
                    default:
                        return null;
                }
            }
            else if(categories.contains(SecurityEntry.CATEGORY)){
                switch (action) {
                    case SecurityEntry.ACTION_INPUT_ACCOUNT:
                    case SecurityEntry.ACTION_MANAGE_INPUT_ACCOUNT:
                        return InputAccountFragment.newInstance(intent);
                    case SecurityEntry.ACTION_ENTER_VCODE:
                    case SecurityEntry.ACTION_ENTER_CARD_LAST_4_DIGITS:
                    case SecurityEntry.ACTION_ENTER_CARD_ALL_DIGITS:
                        return SecurityFragment.newInstance(intent);
                    case SecurityEntry.ACTION_ENTER_PIN:
                        return PINFragment.newInstance(intent);
                    default:
                        return null;
                }
            }else if(categories.contains(InformationEntry.CATEGORY)){
                if(InformationEntry.ACTION_DISPLAY_TRANS_INFORMATION.equals(action)){
                    return DisplayTransInfoFragment.newInstance(intent);
                }

            }else if(categories.contains(SignatureEntry.CATEGORY)){
                if(SignatureEntry.ACTION_SIGNATURE.equals(action)){
                    return SignatureFragment.newInstance(intent);
                }

            }
        }

        return null;
    }

    private DialogFragment createDialogFragment(Intent intent){
        String action = intent.getAction();
        Set<String> categories = intent.getCategories();
        if(action != null) {
            if (categories.contains(OptionEntry.CATEGORY)) {
                if(OptionEntry.ACTION_SELECT_INSTALLMENT_PLAN.equals(action)){
                    //TODO Select Installment Plan
                }else {
                    return OptionsDialogFragment.newInstance(intent);
                }
            } else if(categories.contains(ConfirmationEntry.CATEGORY)){
                if(ConfirmationEntry.ACTION_CONFIRM_UNIFIED_MESSAGE.equals(action)
                        ||ConfirmationEntry.ACTION_CONFIRM_CARD_PROCESS_RESULT.equals(action)){
                    return ConfirmationDialogFragment.newInstance(intent);
                }
            }
        }
        return null;
    }

}