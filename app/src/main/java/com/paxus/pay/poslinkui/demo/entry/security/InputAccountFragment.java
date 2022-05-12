package com.paxus.pay.poslinkui.demo.entry.security;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.pax.us.pay.ui.constant.entry.EntryExtraData;
import com.pax.us.pay.ui.constant.entry.EntryRequest;
import com.pax.us.pay.ui.constant.entry.SecurityEntry;
import com.pax.us.pay.ui.constant.entry.enumeration.CurrencyType;
import com.pax.us.pay.ui.constant.entry.enumeration.PanStyles;
import com.pax.us.pay.ui.constant.entry.enumeration.TransMode;
import com.pax.us.pay.ui.constant.status.CardStatus;
import com.pax.us.pay.ui.constant.status.ClssLightStatus;
import com.pax.us.pay.ui.constant.status.InformationStatus;
import com.pax.us.pay.ui.constant.status.SecurityStatus;
import com.pax.us.pay.ui.constant.status.StatusData;
import com.paxus.pay.poslinkui.demo.R;
import com.paxus.pay.poslinkui.demo.entry.BaseEntryFragment;
import com.paxus.pay.poslinkui.demo.utils.CurrencyUtils;
import com.paxus.pay.poslinkui.demo.utils.EntryRequestUtils;
import com.paxus.pay.poslinkui.demo.utils.Logger;
import com.paxus.pay.poslinkui.demo.utils.ViewUtils;
import com.paxus.pay.poslinkui.demo.view.ClssLight;
import com.paxus.pay.poslinkui.demo.view.ClssLightsView;

public class InputAccountFragment extends BaseEntryFragment {
    private String transType;
    private long timeOut;
    private int minLength;
    private int maxLength;
    private String hint = "";
    private String transMode;

    private boolean enableInsert;
    private boolean enableTap;
    private boolean enableSwipe;
    private boolean enableManual;

    private boolean supportNFC;
    private boolean supportApplePay;
    private boolean supportSamsungPay;
    private boolean supportGooglePay;

    private Long totalAmount;
    private String merchantName;
    private String currencyType;
    private String amountMessage;

    private ClssLightsView clssLightsView;
    private TextView amountTv;

    private BroadcastReceiver receiver;
    private TextView panInputBox;

    public static InputAccountFragment newInstance(Intent intent){
        InputAccountFragment numFragment = new InputAccountFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EntryRequest.PARAM_ACTION, intent.getAction());
        bundle.putAll(intent.getExtras());
        numFragment.setArguments(bundle);
        return numFragment;
    }


    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_input_account;
    }


    @Override
    protected void loadArgument(@NonNull Bundle bundle) {
        action = bundle.getString(EntryRequest.PARAM_ACTION);
        packageName = bundle.getString(EntryExtraData.PARAM_PACKAGE);
        transType = bundle.getString(EntryExtraData.PARAM_TRANS_TYPE);
        transMode = bundle.getString(EntryExtraData.PARAM_TRANS_MODE);
        timeOut = bundle.getLong(EntryExtraData.PARAM_TIMEOUT,30000);
        enableInsert = bundle.getBoolean(EntryExtraData.PARAM_ENABLE_INSERT);
        enableTap = bundle.getBoolean(EntryExtraData.PARAM_ENABLE_TAP);
        enableSwipe = bundle.getBoolean(EntryExtraData.PARAM_ENABLE_SWIPE);
        enableManual = bundle.getBoolean(EntryExtraData.PARAM_ENABLE_MANUAL);

        supportApplePay = bundle.getBoolean(EntryExtraData.PARAM_ENABLE_APPLEPAY);
        supportGooglePay = bundle.getBoolean(EntryExtraData.PARAM_ENABLE_GOOGLEPAY);
        supportSamsungPay = bundle.getBoolean(EntryExtraData.PARAM_ENABLE_SUMSUNGPAY);
        supportNFC = bundle.getBoolean(EntryExtraData.PARAM_ENABLE_NFCPAY);

        String valuePatten = "";
        if(SecurityEntry.ACTION_INPUT_ACCOUNT.equals(action)){
            valuePatten = bundle.getString(EntryExtraData.PARAM_VALUE_PATTERN,"0-19");
            String panStyle = bundle.getString(EntryExtraData.PARAM_PAN_STYLES, PanStyles.NORMAL);
            if(PanStyles.NEW_PAN.equals(panStyle)) {
                hint = getString(R.string.enter_new_account);
            }else {
                hint = getString(R.string.hint_enter_account);
            }
        } else if(SecurityEntry.ACTION_MANAGE_INPUT_ACCOUNT.equals(action)){
            valuePatten = bundle.getString(EntryExtraData.PARAM_VALUE_PATTERN,"0-19");
            hint = getString(R.string.hint_enter_account);
            amountMessage = bundle.getString(EntryExtraData.PARAM_AMOUNT_MESSAGE);
        }

        if(!TextUtils.isEmpty(valuePatten) && valuePatten.contains("-")){
            String[] tmp = valuePatten.split("-");
            if(tmp.length == 2) {
                minLength = Integer.parseInt(tmp[0]);
                maxLength = Integer.parseInt(tmp[1]);
            }
        }

        merchantName = bundle.getString(EntryExtraData.PARAM_MERCHANT_NAME);
        if(bundle.containsKey(EntryExtraData.PARAM_TOTAL_AMOUNT)){
            totalAmount = bundle.getLong(EntryExtraData.PARAM_TOTAL_AMOUNT);
            currencyType = bundle.getString(EntryExtraData.PARAM_CURRENCY, CurrencyType.USD);
        }

    }

    @Override
    protected void loadView(View rootView) {
        if(!TextUtils.isEmpty(transType) && getActivity() instanceof AppCompatActivity){
            ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
            if(actionBar != null) {
                actionBar.setTitle(transType);
            }
        }

        String mode = null;
        if(!TextUtils.isEmpty(transMode)){
            if(TransMode.DEMO.equals(transMode)){
                mode = getString(R.string.demo_only);
            }else if(TransMode.TEST.equals(transMode)){
                mode = getString(R.string.test_only);
            }else if(TransMode.TEST_AND_DEMO.equals(transMode)){
                mode = getString(R.string.test_and_demo);
            }else {
                mode = "";
            }
        }
        if(!TextUtils.isEmpty(mode)){
            ViewUtils.addWaterMarkView(requireActivity(),mode);
        }else{
            ViewUtils.removeWaterMarkView(requireActivity());
        }

        TextView textView = rootView.findViewById(R.id.manual_message);
        textView.setText(hint);

        panInputBox = rootView.findViewById(R.id.edit_account);

        if(enableManual) {
            panInputBox.setEnabled(true);

            ViewTreeObserver observer = panInputBox.getViewTreeObserver();
            observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    panInputBox.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    if(Build.MODEL.equals("A35")){
                        new Handler().postDelayed(()-> {
                            sendSecureArea(panInputBox);
                        },100);
                    }else{
                        sendSecureArea(panInputBox);
                    }
                }
            });
        }else{
            panInputBox.setEnabled(false);
        }
        clssLightsView = rootView.findViewById(R.id.clss_light);

        int swipeId = R.drawable.selection_swipe_card_a920;
        int insertId = R.drawable.selection_insert_card_a920;
        int tapId = R.drawable.selection_tap_card_a920;

        String deviceModel = Build.MODEL;
        if ("A60".equals(deviceModel)) {
            swipeId = R.drawable.selection_swipe_card_a60;
            insertId = R.drawable.selection_insert_card_a60;
        } else if ("Aries8".equals(deviceModel)) {
            swipeId = R.drawable.selection_swipe_card_ar_x;
            insertId = R.drawable.selection_insert_card_ar_x;
            tapId = R.drawable.selection_tap_card_ar_x;
        } else if ("Aries6".equals(deviceModel)) {
            swipeId = R.drawable.selection_swipe_card_ar6;
            insertId = R.drawable.selection_insert_card_ar6;
            tapId = R.drawable.selection_tap_card_ar6;
        } else if ("A80".equals(deviceModel)) {
            swipeId = R.drawable.selection_swipe_card_a80;
        } else if ("A930".equals(deviceModel)) {
            swipeId = R.drawable.selection_swipe_card_a930;
        } else if ("A77".equals(deviceModel)) {
            swipeId = R.drawable.selection_swipe_card_a77;
            insertId = R.drawable.selection_insert_card_a77;
            tapId = R.drawable.selection_tap_card_a77;
        } else if ("PX7A".equals(deviceModel)) {
            swipeId = R.drawable.selection_swipe_card_px7a;
        } else if ("IM30".equals(deviceModel)) {
            swipeId = R.drawable.selection_swipe_card_im30;
            insertId = R.drawable.selection_insert_card_im30;
            tapId = R.drawable.selection_tap_card_im30;
        } else if ("A30".equals(deviceModel)) {
            swipeId = R.drawable.selection_swipe_card_a30;
            insertId = R.drawable.selection_insert_card_a30;
            tapId = R.drawable.selection_tap_card_a30;
        } else if ("A35".equals(deviceModel)) {
            swipeId = R.drawable.selection_swipe_card_a35;
            insertId = R.drawable.selection_insert_card_a35;
            tapId = R.drawable.selection_tap_card_a35;
        } else if ("A800".equals(deviceModel)) {
            swipeId = R.drawable.selection_swipe_card_a800;
        }

        TextView insert = rootView.findViewById(R.id.insert);
        TextView tap = rootView.findViewById(R.id.tap);
        TextView swipe = rootView.findViewById(R.id.swipe);

        insert.setBackgroundResource(insertId);
        tap.setBackgroundResource(tapId);
        swipe.setBackgroundResource(swipeId);

        insert.setEnabled(enableInsert);
        tap.setEnabled(enableTap);
        swipe.setEnabled(enableSwipe);

        ImageView nfc = rootView.findViewById(R.id.nfc);
        ImageView apple = rootView.findViewById(R.id.apple);
        ImageView google = rootView.findViewById(R.id.google);
        ImageView samsung = rootView.findViewById(R.id.samsung);

        nfc.setVisibility(supportNFC? View.VISIBLE: View.GONE);
        apple.setVisibility(supportApplePay? View.VISIBLE: View.GONE);
        google.setVisibility(supportGooglePay? View.VISIBLE: View.GONE);
        samsung.setVisibility(supportSamsungPay? View.VISIBLE: View.GONE);

        if(enableTap){
            receiver = new Receiver();
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addCategory(ClssLightStatus.CATEGORY);
            intentFilter.addAction(ClssLightStatus.CLSS_LIGHT_IDLE);
            intentFilter.addAction(ClssLightStatus.CLSS_LIGHT_COMPLETED);
            intentFilter.addAction(ClssLightStatus.CLSS_LIGHT_NOT_READY);
            intentFilter.addAction(ClssLightStatus.CLSS_LIGHT_READY_FOR_TXN);
            intentFilter.addAction(ClssLightStatus.CLSS_LIGHT_PROCESSING);
            intentFilter.addAction(ClssLightStatus.CLSS_LIGHT_REMOVE_CARD);
            intentFilter.addAction(ClssLightStatus.CLSS_LIGHT_ERROR);

            intentFilter.addCategory(InformationStatus.CATEGORY);
            intentFilter.addAction(InformationStatus.TRANS_AMOUNT_CHANGED_IN_CARD_PROCESSING);

            intentFilter.addCategory(SecurityStatus.CATEGORY);
            intentFilter.addAction(SecurityStatus.SECURITY_ENTER_CLEARED);
            intentFilter.addAction(SecurityStatus.SECURITY_ENTERING);
            intentFilter.addAction(SecurityStatus.SECURITY_ENTER_DELETE);

            requireContext().registerReceiver(receiver,intentFilter);
        }

        TextView merchantTv = rootView.findViewById(R.id.merchant);
        TextView merchantNameTv = rootView.findViewById(R.id.merchantName);

        if(!TextUtils.isEmpty(merchantName)){
            merchantNameTv.setText(merchantName);
        }else {
            merchantTv.setVisibility(View.GONE);
            merchantNameTv.setVisibility(View.GONE);
        }

        TextView totalAmountTv = rootView.findViewById(R.id.total_amount);
        amountTv = rootView.findViewById(R.id.amount_view);
        if(!TextUtils.isEmpty(amountMessage)){
            totalAmountTv.setText(amountMessage);
        }else if(totalAmount != null){
            String amt = CurrencyUtils.convert(totalAmount, currencyType);
            amountTv.setText(amt);

            if(CurrencyType.POINT.equals(currencyType)){
                totalAmountTv.setText(getString(R.string.total_point));
            }
        }else {
            totalAmountTv.setVisibility(View.GONE);
            amountTv.setVisibility(View.GONE);
        }

    }

    private void sendSecureArea(TextView editText){
        int[] location = new int[2];
        editText.getLocationInWindow(location);
        int x = location[0];
        int y = location[1];
        int barHeight = 0;
        boolean immersiveSticky = (requireActivity().getWindow().getDecorView().getSystemUiVisibility() &
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY) > 0;
        if (!immersiveSticky) {
            //area of application
            Rect outRect1 = new Rect();
            requireActivity().getWindow().getDecorView().getWindowVisibleDisplayFrame(outRect1);
            barHeight = outRect1.top;  //statusBar's height
        }
        TextPaint paint = editText.getPaint();
        int fontSize = (int)(paint.getTextSize()/paint.density);
        EntryRequestUtils.sendSecureArea(requireContext(), packageName, action, x, y - barHeight, editText.getWidth(), editText.getHeight(), fontSize,
                "Card Number",
                "FF9C27B0");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if(receiver != null){
            requireContext().unregisterReceiver(receiver);
        }
    }

    private class Receiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            Logger.i("receive Status Action \""+intent.getAction()+"\"");
            switch (intent.getAction()) {
                case ClssLightStatus.CLSS_LIGHT_COMPLETED:
                case ClssLightStatus.CLSS_LIGHT_NOT_READY: //Fix ANBP-383, ANFDRC-319
                    clssLightsView.setLights(-1, ClssLight.OFF);
                    break;
                case ClssLightStatus.CLSS_LIGHT_ERROR:
                    clssLightsView.setLight(0, ClssLight.OFF);
                    clssLightsView.setLight(1, ClssLight.OFF);
                    clssLightsView.setLight(2, ClssLight.OFF);
                    clssLightsView.setLight(3, ClssLight.ON);
                    break;
                case ClssLightStatus.CLSS_LIGHT_IDLE:
                    clssLightsView.setLights(0, ClssLight.BLINK);
                    break;
                case ClssLightStatus.CLSS_LIGHT_PROCESSING:
                    clssLightsView.setLight(0, ClssLight.ON);
                    clssLightsView.setLight(1, ClssLight.ON);
                    clssLightsView.setLight(2, ClssLight.OFF);
                    clssLightsView.setLight(3, ClssLight.OFF);
                    break;
                case ClssLightStatus.CLSS_LIGHT_READY_FOR_TXN:
                    clssLightsView.setLight(0, ClssLight.ON);
                    clssLightsView.setLight(1, ClssLight.OFF);
                    clssLightsView.setLight(2, ClssLight.OFF);
                    clssLightsView.setLight(3, ClssLight.OFF);
                    break;
                case ClssLightStatus.CLSS_LIGHT_REMOVE_CARD:
                    clssLightsView.setLight(0, ClssLight.ON);
                    clssLightsView.setLight(1, ClssLight.ON);
                    clssLightsView.setLight(2, ClssLight.ON);
                    clssLightsView.setLight(3, ClssLight.OFF);
                    break;
                case InformationStatus.TRANS_AMOUNT_CHANGED_IN_CARD_PROCESSING:
                    totalAmount = intent.getLongExtra(StatusData.PARAM_TOTAL_AMOUNT,totalAmount);
                    amountTv.setText(CurrencyUtils.convert(totalAmount, currencyType));
                    break;
                case CardStatus.CARD_INSERT_REQUIRED:
                    clssLightsView.setLights(0, ClssLight.BLINK);
                    Toast.makeText(requireContext(),getString(R.string.please_insert_chip_card),Toast.LENGTH_LONG).show();
                    break;
                case CardStatus.CARD_TAP_REQUIRED:
                    clssLightsView.setLight(0, ClssLight.ON);
                    clssLightsView.setLight(1, ClssLight.OFF);
                    clssLightsView.setLight(2, ClssLight.OFF);
                    clssLightsView.setLight(3, ClssLight.OFF);
                    Toast.makeText(requireContext(),getString(R.string.please_tap_card),Toast.LENGTH_LONG).show();
                    break;
                case CardStatus.CARD_SWIPE_REQUIRED:
                    clssLightsView.setLights(0, ClssLight.BLINK);
                    Toast.makeText(requireContext(),getString(R.string.please_swipe_card),Toast.LENGTH_LONG).show();
                    break;
                case CardStatus.CARD_QUICK_REMOVAL_REQUIRED:
                    Toast.makeText(requireContext(),getString(R.string.please_remove_card_quickly),Toast.LENGTH_LONG).show();
                    break;
                case SecurityStatus.SECURITY_ENTER_CLEARED:
                case SecurityStatus.SECURITY_ENTERING:
                case SecurityStatus.SECURITY_ENTER_DELETE: {
                    //You can update Confirm Button status according to the 3 actions
                    //Example: if input length>0, enabled confirm button. Else, disable confirm button
                    break;
                }
                default:
                    break;
            }

        }
    }

}
