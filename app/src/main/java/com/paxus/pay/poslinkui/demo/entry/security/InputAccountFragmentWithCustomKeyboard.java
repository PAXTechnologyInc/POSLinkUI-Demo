package com.paxus.pay.poslinkui.demo.entry.security;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.pax.us.pay.ui.constant.entry.EntryExtraData;
import com.pax.us.pay.ui.constant.entry.EntryRequest;
import com.pax.us.pay.ui.constant.entry.SecurityEntry;
import com.pax.us.pay.ui.constant.entry.enumeration.CurrencyType;
import com.pax.us.pay.ui.constant.entry.enumeration.PanStyles;
import com.pax.us.pay.ui.constant.status.CardStatus;
import com.pax.us.pay.ui.constant.status.ClssLightStatus;
import com.pax.us.pay.ui.constant.status.InformationStatus;
import com.pax.us.pay.ui.constant.status.SecurityStatus;
import com.pax.us.pay.ui.constant.status.StatusData;
import com.paxus.pay.poslinkui.demo.R;
import com.paxus.pay.poslinkui.demo.entry.BaseEntryFragment;
import com.paxus.pay.poslinkui.demo.utils.CurrencyUtils;
import com.paxus.pay.poslinkui.demo.utils.Logger;
import com.paxus.pay.poslinkui.demo.utils.ValuePatternUtils;
import com.paxus.pay.poslinkui.demo.utils.ViewUtils;
import com.paxus.pay.poslinkui.demo.view.ClssLight;
import com.paxus.pay.poslinkui.demo.view.ClssLightsView;
import com.paxus.pay.poslinkui.demo.view.SecureKeyboardView;

/**
 * Implement security entry actions
 * {@link SecurityEntry#ACTION_INPUT_ACCOUNT}
 */

public class InputAccountFragmentWithCustomKeyboard extends BaseEntryFragment {

    protected long timeOut;
    protected int minLength;
    protected int maxLength;
    protected String manualMessage = "";

    protected boolean enableInsert;
    protected boolean enableTap;
    protected boolean enableSwipe;
    protected boolean enableManual;

    protected boolean supportNFC;
    protected boolean supportApplePay;
    protected boolean supportSamsungPay;
    protected boolean supportGooglePay;

    protected boolean enableContactlessLight;

    protected Long totalAmount;
    protected String currencyType;

    //
    protected ClssLightsView clssLightsView;
    protected TextView amountTv;

    protected BroadcastReceiver receiver;
    protected View rootView;
    protected Button confirmButton;
    protected int panLength = 0;

    SecureKeyboardView secureKeyboardView;

    private void onPanInputBoxLayoutReady(TextView inputTextView, LinearLayout keyboardLayout) {
        Runnable securityAreaRequest = () -> {
            Bundle bundle = new Bundle();

            bundle.putAll(getInputLocationBundle(inputTextView));
            bundle.putAll(getKeyboardLocationBundle(secureKeyboardView));

            sendRequestBroadcast(EntryRequest.ACTION_SECURITY_AREA, bundle);
        };

        if (Build.MODEL.equals("A35")) {
            new Handler(Looper.myLooper()).postDelayed(securityAreaRequest, 100);
        } else {
            securityAreaRequest.run();
        }
    }

    private Bundle getInputLocationBundle(TextView inputTextView) {
        Bundle bundle = new Bundle();

        if(inputTextView == null) return bundle;

        int[] location = new int[2];
        inputTextView.getLocationInWindow(location);

        int fontSize = (int) (inputTextView.getPaint().getTextSize() / inputTextView.getPaint().density);

        bundle.putInt(EntryRequest.PARAM_X, location[0]);
        bundle.putInt(EntryRequest.PARAM_Y, location[1] - ViewUtils.getBarHeight(getActivity()));
        bundle.putInt(EntryRequest.PARAM_WIDTH, inputTextView.getWidth());
        bundle.putInt(EntryRequest.PARAM_HEIGHT, inputTextView.getHeight());
        bundle.putInt(EntryRequest.PARAM_FONT_SIZE, fontSize);
        bundle.putString(EntryRequest.PARAM_HINT, "Account Number");
        bundle.putString(EntryRequest.PARAM_COLOR, String.format("%X", inputTextView.getCurrentTextColor()));

        return bundle;
    }

    private Bundle getKeyboardLocationBundle(SecureKeyboardView secureKeyboardView) {
        Bundle bundle = new Bundle();
        if(secureKeyboardView == null) return bundle;
        return secureKeyboardView.generateLocationBundle(getActivity());
    }

    @Override
    protected void onConfirmButtonClicked() {
        sendNext(null);
    }

    private void updateCtlessLightStatus(String status) {
        if (clssLightsView.getVisibility() != View.VISIBLE) {
            return;
        }
        switch (status) {
            case ClssLightStatus.CLSS_LIGHT_COMPLETED:
            case ClssLightStatus.CLSS_LIGHT_NOT_READY: //Fix ANBP-383, ANFDRC-319
                clssLightsView.setLights(-1, ClssLight.OFF);
                return;
            case ClssLightStatus.CLSS_LIGHT_ERROR:
                clssLightsView.setLight(0, ClssLight.OFF);
                clssLightsView.setLight(1, ClssLight.OFF);
                clssLightsView.setLight(2, ClssLight.OFF);
                clssLightsView.setLight(3, ClssLight.ON);
                return;
            case ClssLightStatus.CLSS_LIGHT_IDLE:
                clssLightsView.setLights(0, ClssLight.BLINK);
                return;
            case ClssLightStatus.CLSS_LIGHT_PROCESSING:
                clssLightsView.setLight(0, ClssLight.ON);
                clssLightsView.setLight(1, ClssLight.ON);
                clssLightsView.setLight(2, ClssLight.OFF);
                clssLightsView.setLight(3, ClssLight.OFF);
                return;
            case ClssLightStatus.CLSS_LIGHT_READY_FOR_TXN:
                clssLightsView.setLight(0, ClssLight.ON);
                clssLightsView.setLight(1, ClssLight.OFF);
                clssLightsView.setLight(2, ClssLight.OFF);
                clssLightsView.setLight(3, ClssLight.OFF);
                return;
            case ClssLightStatus.CLSS_LIGHT_REMOVE_CARD:
                clssLightsView.setLight(0, ClssLight.ON);
                clssLightsView.setLight(1, ClssLight.ON);
                clssLightsView.setLight(2, ClssLight.ON);
                clssLightsView.setLight(3, ClssLight.OFF);
                return;
            default:
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            requireContext().unregisterReceiver(receiver);
        }
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_input_account_with_custom_keyboard;
    }

    @Override
    protected void loadArgument(@NonNull Bundle bundle) {
        enableInsert = bundle.getBoolean(EntryExtraData.PARAM_ENABLE_INSERT);
        enableTap = bundle.getBoolean(EntryExtraData.PARAM_ENABLE_TAP);
        enableSwipe = bundle.getBoolean(EntryExtraData.PARAM_ENABLE_SWIPE);
        enableManual = bundle.getBoolean(EntryExtraData.PARAM_ENABLE_MANUAL);

        supportApplePay = bundle.getBoolean(EntryExtraData.PARAM_ENABLE_APPLEPAY);
        supportGooglePay = bundle.getBoolean(EntryExtraData.PARAM_ENABLE_GOOGLEPAY);
        supportSamsungPay = bundle.getBoolean(EntryExtraData.PARAM_ENABLE_SAMSUNGPAY);
        supportNFC = bundle.getBoolean(EntryExtraData.PARAM_ENABLE_NFCPAY);

        enableContactlessLight = bundle.getBoolean(EntryExtraData.PARAM_ENABLE_CONTACTLESS_LIGHT);

        String valuePatten = bundle.getString(EntryExtraData.PARAM_VALUE_PATTERN, "0-19");
        String panStyle = bundle.getString(EntryExtraData.PARAM_PAN_STYLES, PanStyles.NORMAL);
        if (PanStyles.NEW_PAN.equals(panStyle)) {
            manualMessage = getString(R.string.enter_new_account);
        } else {
            manualMessage = getString(R.string.hint_enter_account);
        }

        if (!TextUtils.isEmpty(valuePatten)) {
            minLength = ValuePatternUtils.getMinLength(valuePatten);
            maxLength = ValuePatternUtils.getMaxLength(valuePatten);
        }

        if (bundle.containsKey(EntryExtraData.PARAM_TOTAL_AMOUNT)) {
            totalAmount = bundle.getLong(EntryExtraData.PARAM_TOTAL_AMOUNT);
            currencyType = bundle.getString(EntryExtraData.PARAM_CURRENCY, CurrencyType.USD);
        }
    }

    @Override
    protected void loadView(View rootView) {
        this.rootView = rootView;
        TextView textView = rootView.findViewById(R.id.manual_message);
        textView.setText(manualMessage);

        secureKeyboardView = rootView.findViewById(R.id.keyboard_view);

        TextView panInputBox = rootView.findViewById(R.id.edit_account);
        confirmButton = rootView.findViewById(R.id.confirm_button);
        if (enableManual) {
            panInputBox.setEnabled(true);
            confirmButton.setOnClickListener(v -> onConfirmButtonClicked());

            ViewTreeObserver observer = panInputBox.getViewTreeObserver();
            observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    panInputBox.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    secureKeyboardView.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                    onPanInputBoxLayoutReady(panInputBox, secureKeyboardView);

                    if ("Q10A".equals(Build.MODEL)) {
                        panInputBox.addOnLayoutChangeListener((view, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {
                            Logger.d("Security EditText onLayoutChange:" + left + "," + top + "," + right + "," + bottom + "," + oldLeft + "," + oldTop + "," + oldRight + "," + oldBottom);
                            if (right != oldRight) {
                                onPanInputBoxLayoutReady(panInputBox, secureKeyboardView);
                            }
                        });
                    }

                    secureKeyboardView.addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {
                        onPanInputBoxLayoutReady(panInputBox, secureKeyboardView);
                    });
                }
            });
        } else {
            rootView.findViewById(R.id.layout_manual).setVisibility(View.GONE);
        }
        clssLightsView = rootView.findViewById(R.id.clss_light);
        clssLightsView.setVisibility((enableContactlessLight && enableTap) ? View.VISIBLE : View.GONE);

        ImageView nfc = rootView.findViewById(R.id.nfc);
        ImageView apple = rootView.findViewById(R.id.apple);
        ImageView google = rootView.findViewById(R.id.google);
        ImageView samsung = rootView.findViewById(R.id.samsung);

        nfc.setVisibility(supportNFC ? View.VISIBLE : View.GONE);
        apple.setVisibility(supportApplePay ? View.VISIBLE : View.GONE);
        google.setVisibility(supportGooglePay ? View.VISIBLE : View.GONE);
        samsung.setVisibility(supportSamsungPay ? View.VISIBLE : View.GONE);
        rootView.findViewById(R.id.contactless_logo_container)
                .setVisibility(((supportNFC || supportApplePay || supportGooglePay || supportSamsungPay) && enableTap) ? View.VISIBLE : View.GONE);

        if(receiver != null) {
            requireContext().unregisterReceiver(receiver);
        }
        receiver = new InputAccountFragmentWithCustomKeyboard.Receiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addCategory(SecurityStatus.CATEGORY);
        intentFilter.addAction(SecurityStatus.SECURITY_ENTER_CLEARED);
        intentFilter.addAction(SecurityStatus.SECURITY_ENTERING);
        intentFilter.addAction(SecurityStatus.SECURITY_ENTER_DELETE);
        intentFilter.addAction(SecurityStatus.SECURITY_KEYBOARD_LOCATION);

        intentFilter.addCategory(CardStatus.CATEGORY);
        intentFilter.addAction(CardStatus.CARD_INSERT_REQUIRED);
        intentFilter.addAction(CardStatus.CARD_TAP_REQUIRED);
        intentFilter.addAction(CardStatus.CARD_SWIPE_REQUIRED);

        if (enableTap) {
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
        }
        requireContext().registerReceiver(receiver, intentFilter);

        TextView totalAmountTv = rootView.findViewById(R.id.total_amount);
        amountTv = rootView.findViewById(R.id.amount_view);
        if (totalAmount != null) {
            String amt = CurrencyUtils.convert(totalAmount, currencyType);
            amountTv.setText(amt);

            if (CurrencyType.POINT.equals(currencyType)) {
                totalAmountTv.setText(getString(R.string.total_point));
            }
        } else {
            totalAmountTv.setVisibility(View.GONE);
            amountTv.setVisibility(View.GONE);
        }
        onResume();
    }

    private class Receiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Logger.i("Received Status Broadcast " + intent.getAction());
            Logger.intent(intent);

            switch (intent.getAction()) {
                case ClssLightStatus.CLSS_LIGHT_COMPLETED:
                case ClssLightStatus.CLSS_LIGHT_NOT_READY:
                case ClssLightStatus.CLSS_LIGHT_ERROR:
                case ClssLightStatus.CLSS_LIGHT_IDLE:
                case ClssLightStatus.CLSS_LIGHT_PROCESSING:
                case ClssLightStatus.CLSS_LIGHT_READY_FOR_TXN:
                case ClssLightStatus.CLSS_LIGHT_REMOVE_CARD:
                    //3.Update contactless light according status
                    updateCtlessLightStatus(intent.getAction());
                    return;
                case InformationStatus.TRANS_AMOUNT_CHANGED_IN_CARD_PROCESSING:
                    //4.Update amount
                    totalAmount = intent.getLongExtra(StatusData.PARAM_TOTAL_AMOUNT, totalAmount);
                    amountTv.setText(CurrencyUtils.convert(totalAmount, currencyType));
                    return;
                case CardStatus.CARD_INSERT_REQUIRED:
                    enableInsert = true;
                    enableManual = enableSwipe = enableTap = false;
                    loadView(rootView);
                    break;
                case CardStatus.CARD_TAP_REQUIRED:
                    updateCtlessLightStatus(ClssLightStatus.CLSS_LIGHT_READY_FOR_TXN);
                    enableTap = true;
                    enableManual = enableInsert = enableSwipe = false;
                    loadView(rootView);
                    break;
                case CardStatus.CARD_SWIPE_REQUIRED:
                    enableSwipe = true;
                    enableManual = enableInsert = enableTap = false;
                    loadView(rootView);
                    return;
                case SecurityStatus.SECURITY_ENTER_CLEARED: {
                    panLength = 0;
                    break;
                }
                case SecurityStatus.SECURITY_ENTERING: {
                    panLength++;
                    break;
                }
                case SecurityStatus.SECURITY_ENTER_DELETE: {
                    panLength--;
                    break;
                }
                case SecurityStatus.SECURITY_KEYBOARD_LOCATION: {
                    break;
                }
                default:
                    break;
            }

            //5.Update confirm button status when received SecurityStatus
            if (confirmButton != null) {
                confirmButton.setEnabled(panLength > 0);
            }

        }
    }
}
