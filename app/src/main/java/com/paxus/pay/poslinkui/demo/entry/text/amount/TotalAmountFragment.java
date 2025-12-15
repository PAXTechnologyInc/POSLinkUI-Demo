package com.paxus.pay.poslinkui.demo.entry.text.amount;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.pax.us.pay.ui.constant.entry.EntryExtraData;
import com.pax.us.pay.ui.constant.entry.EntryRequest;
import com.pax.us.pay.ui.constant.entry.TextEntry;
import com.pax.us.pay.ui.constant.entry.enumeration.CurrencyType;
import com.paxus.pay.poslinkui.demo.R;
import com.paxus.pay.poslinkui.demo.entry.BaseEntryFragment;
import com.paxus.pay.poslinkui.demo.utils.CurrencyUtils;
import com.paxus.pay.poslinkui.demo.utils.Toast;
import com.paxus.pay.poslinkui.demo.utils.ValuePatternUtils;
import com.paxus.pay.poslinkui.demo.view.AmountTextWatcher;
import com.paxus.pay.poslinkui.demo.view.TextField;

import java.util.List;

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
    private long timeOut;
    private int minLength;
    private int maxLength;
    private String message = "";

    private String currency = "";
    private long baseAmount;
    private boolean noTipEnabled;
    private String tipName;

    private TextField textField;
    private String valuePatten;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_total_amount;
    }

    @Override
    protected void loadArgument(@NonNull Bundle bundle) {
        timeOut = bundle.getLong(EntryExtraData.PARAM_TIMEOUT,30000);
        currency =  bundle.getString(EntryExtraData.PARAM_CURRENCY, CurrencyType.USD);

        valuePatten = bundle.getString(EntryExtraData.PARAM_VALUE_PATTERN,"1-12");
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

        textField = rootView.findViewById(R.id.edit_amount);
        textField.setSelected(false);
        textField.setText(CurrencyUtils.convert(0,currency));
        textField.setSelection(textField.getEditableText().length());
        textField.addTextChangedListener(new AmountTextWatcher(maxLength, currency));

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


    private void submit(long value){
        Bundle bundle = new Bundle();
        bundle.putLong(EntryRequest.PARAM_TOTAL_AMOUNT, value);
        sendNext(bundle);
    }

    //-----------Click Callback for buttons-----------
    //If click no tip button, send next with base amount
    private void onNoTipButtonClicked(){
        List<Integer> lengthList = ValuePatternUtils.getLengthList(valuePatten);
        // if value is 0 and pattern does not contains 0, that means it can not be skipped, show error message
        if (baseAmount == 0 && !lengthList.contains(0)) {
            new Toast(getActivity()).show(getString(R.string.prompt_input), Toast.TYPE.FAILURE);
        } else {
            submit(baseAmount);
        }
    }

    @Override
    protected void onConfirmButtonClicked(){
        long value = CurrencyUtils.parse(textField.getText().toString());
        submit(value);
    }

    @Override
    protected TextField[] focusableTextFields() {
        return new TextField[]{textField};
    }
}
