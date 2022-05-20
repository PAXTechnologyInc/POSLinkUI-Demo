package com.paxus.pay.poslinkui.demo.entry.text;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.pax.us.pay.ui.constant.entry.EntryExtraData;
import com.pax.us.pay.ui.constant.entry.EntryRequest;
import com.pax.us.pay.ui.constant.entry.TextEntry;
import com.pax.us.pay.ui.constant.entry.enumeration.CurrencyType;
import com.paxus.pay.poslinkui.demo.R;
import com.paxus.pay.poslinkui.demo.entry.BaseEntryFragment;
import com.paxus.pay.poslinkui.demo.utils.CurrencyUtils;
import com.paxus.pay.poslinkui.demo.utils.EntryRequestUtils;
import com.paxus.pay.poslinkui.demo.utils.ValuePatternUtils;
import com.paxus.pay.poslinkui.demo.view.AmountTextWatcher;

/**
 * Implement text entry action {@value TextEntry#ACTION_ENTER_TOTAL_AMOUNT}<br>
 * <p>
 *     UI Tips:
 *     If noTipEnabled, display no tip button, else hide it.
 *     If click no tip button, send next with base amount
 *     If click confirm button, send next with input amount
 * </p>
 */
public class TotalAmountFragment extends BaseEntryFragment {
    private String transType;
    private String transMode;

    private long timeOut;
    private int minLength;
    private int maxLength;
    private String message = "";

    private String currency = "";
    private long baseAmount;
    private boolean noTipEnabled;
    private String tipName;

    private EditText editText;

    public static Fragment newInstance(Intent intent){
        TotalAmountFragment fragment = new TotalAmountFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EntryRequest.PARAM_ACTION, intent.getAction());
        bundle.putAll(intent.getExtras());
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_total_amount;
    }

    @Override
    protected void loadArgument(@NonNull Bundle bundle) {
        action = bundle.getString(EntryRequest.PARAM_ACTION);
        packageName = bundle.getString(EntryExtraData.PARAM_PACKAGE);
        transType = bundle.getString(EntryExtraData.PARAM_TRANS_TYPE);
        transMode = bundle.getString(EntryExtraData.PARAM_TRANS_MODE);
        timeOut = bundle.getLong(EntryExtraData.PARAM_TIMEOUT,30000);
        currency =  bundle.getString(EntryExtraData.PARAM_CURRENCY, CurrencyType.USD);

        String valuePatten = bundle.getString(EntryExtraData.PARAM_VALUE_PATTERN,"1-12");
        message = getString(R.string.prompt_input_total_amount);

        if(!TextUtils.isEmpty(valuePatten)){
            minLength = ValuePatternUtils.getMinLength(valuePatten);
            maxLength = ValuePatternUtils.getMaxLength(valuePatten);
        }

        baseAmount = bundle.getLong(EntryExtraData.PARAM_BASE_AMOUNT);
        noTipEnabled = bundle.getBoolean(EntryExtraData.PARAM_ENABLE_NO_TIP_SELECTION);
        tipName = bundle.getString(EntryExtraData.PARAM_TIP_NAME);

    }

    @Override
    protected void loadView(View rootView) {
        

        

        TextView textView = rootView.findViewById(R.id.message);
        textView.setText(message);

        editText = rootView.findViewById(R.id.edit_amount);
        editText.setSelected(false);
        editText.setText(CurrencyUtils.convert(0,currency));
        editText.setSelection(editText.getEditableText().length());

        editText.addTextChangedListener(new AmountTextWatcher(maxLength, currency));

        Button confirmBtn = rootView.findViewById(R.id.confirm_button);
        confirmBtn.setOnClickListener(v -> onConfirmButtonClicked());
        TextView baseAmountTv = rootView.findViewById(R.id.base_amount);
        baseAmountTv.setText(CurrencyUtils.convert(baseAmount,currency));

        TextView tipNameTv = rootView.findViewById(R.id.tip_name);
        if(!TextUtils.isEmpty(tipName)){
            tipNameTv.setText(tipName);
        }

        Button noTipButton = rootView.findViewById(R.id.no_tip_button);
        if(noTipEnabled){
            noTipButton.setOnClickListener(v -> onNoTipButtonClicked());
        }else {
            noTipButton.setVisibility(View.GONE);
        }

    }


    private void sendNext(long value){

        String param = EntryRequest.PARAM_TOTAL_AMOUNT;
        EntryRequestUtils.sendNext(requireContext(), packageName, action, param,value);
    }

    //-----------Click Callback for buttons-----------
    //If click no tip button, send next with base amount
    private void onNoTipButtonClicked(){
        sendNext(baseAmount);
    }

    //If click confirm button, send next with input amount
    private void onConfirmButtonClicked(){
        long value = CurrencyUtils.parse(editText.getText().toString());
        if(String.valueOf(value).length() < minLength){
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
        }else {
            sendNext(value);
        }
    }

}
