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
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.pax.us.pay.ui.constant.entry.EntryExtraData;
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
import com.paxus.pay.poslinkui.demo.view.ClssLight;
import com.paxus.pay.poslinkui.demo.view.ClssLightsView;

/**
 * Implement security entry actions {@link SecurityEntry#ACTION_INPUT_ACCOUNT}
 * <p>
 * UI Tips:
 * 1.When input box layout ready, send secure area location (Done on ViewTreeObserver.OnGlobalLayoutListener)
 * 2.When confirm button clicked, sendNext
 * 3.Update contactless light when received ClssLightStatus
 * 4.Update amount when received InformationStatus.TRANS_AMOUNT_CHANGED_IN_CARD_PROCESSING
 * 5.Update confirm button status when received SecurityStatus
 * 6.Update entry mode when received CardStatus.CARD_INSERT_REQUIRED, CardStatus.CARD_TAP_REQUIRED,CardStatus.CARD_SWIPE_REQUIRED:
 * 7.Update view according keyboard location if you need
 * </p>
 */

public class InputAccountFragment extends BaseEntryFragment {

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
    protected String merchantName;
    protected String currencyType;

    protected ClssLightsView clssLightsView;
    protected TextView amountTv;

    protected BroadcastReceiver receiver;
    protected View rootView;
    protected Button confirmButton;
    protected int panLength = 0;

    private View mContentView;
    private int mOrigWidth;
    private int mOrigHeight;

    //1.When input box layout ready, send secure area location
    // For A35, need delay 100ms (Ticket: BPOSANDJAX-325)
    private void onPanInputBoxLayoutReady(TextView inputTextView) {
        Runnable securityAreaRequest = () -> sendSecurityArea(inputTextView, "Card Number");
        //Change hint and font color if you want
        if (Build.MODEL.equals("A35")) {
            new Handler(Looper.myLooper()).postDelayed(securityAreaRequest, 100);
        } else {
            securityAreaRequest.run();
        }
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
        return R.layout.fragment_input_account;
    }

    @Override
    protected void loadArgument(@NonNull Bundle bundle) {
        timeOut = bundle.getLong(EntryExtraData.PARAM_TIMEOUT, 30000);
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

        merchantName = bundle.getString(EntryExtraData.PARAM_MERCHANT_NAME);
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
                    onPanInputBoxLayoutReady(panInputBox);
                    mContentView = requireActivity().getWindow().findViewById(Window.ID_ANDROID_CONTENT);
                    if ("Q10A".equals(Build.MODEL)) {
                        panInputBox.addOnLayoutChangeListener((view, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {
                            Logger.d("Security EditText onLayoutChange:" + left + "," + top + "," + right + "," + bottom + "," + oldLeft + "," + oldTop + "," + oldRight + "," + oldBottom);
                            if (right != oldRight) {
                                onPanInputBoxLayoutReady(panInputBox);
                            }
                        });
                    }
                }
            });
        } else {
            rootView.findViewById(R.id.layout_manual).setVisibility(View.GONE);
        }
        clssLightsView = rootView.findViewById(R.id.clss_light);
        clssLightsView.setVisibility((enableContactlessLight && enableTap) ? View.VISIBLE : View.GONE);

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

        ImageView insert = rootView.findViewById(R.id.insert);
        ImageView tap = rootView.findViewById(R.id.tap);
        ImageView swipe = rootView.findViewById(R.id.swipe);

        insert.setImageResource(insertId);
        tap.setImageResource(tapId);
        swipe.setImageResource(swipeId);

        insert.setEnabled(enableInsert);
        tap.setEnabled(enableTap);
        swipe.setEnabled(enableSwipe);

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
        receiver = new InputAccountFragment.Receiver();
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

        TextView merchantNameTv = rootView.findViewById(R.id.merchantName);

        if (!TextUtils.isEmpty(merchantName)) {
            merchantNameTv.setText(merchantName);
        } else {
            rootView.findViewById(R.id.ly_merchant).setVisibility(View.GONE);
        }

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
            Logger.i("receive Status Action \"" + intent.getAction() + "\"");
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
                    if ("Q10A".equals(Build.MODEL)) {
                        //4.Update view according keyboard location if you need
                        Bundle extra = intent.getExtras();
                        if (extra != null) {
                            int x = extra.getInt(StatusData.PARAM_X);
                            int y = extra.getInt(StatusData.PARAM_Y);
                            int width = extra.getInt(StatusData.PARAM_WIDTH);
                            int height = extra.getInt(StatusData.PARAM_HEIGHT);

                            Logger.d("SECURITY_KEYBOARD_LOCATION:" + x + "," + y + "," + width + "," + height);
                            if (x > 0) {
                                ViewGroup.LayoutParams params = mContentView.getLayoutParams();
                                mOrigWidth = mContentView.getMeasuredWidth();
                                mOrigHeight = mContentView.getMeasuredHeight();

                                params.height = mOrigHeight;
                                params.width = mOrigWidth - width;
                                mContentView.setLayoutParams(params);
                                mContentView.requestLayout();
                            } else {
                                ViewGroup.LayoutParams params = mContentView.getLayoutParams();
                                params.height = mOrigHeight;
                                params.width = mOrigWidth;
                                mContentView.setLayoutParams(params);
                                mContentView.requestLayout();
                            }
                        }
                        break;
                    }
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
