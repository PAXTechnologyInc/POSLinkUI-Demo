package com.paxus.pay.poslinkui.demo.entry.text.numbertext;

import android.os.Bundle;
import android.text.InputFilter;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.pax.us.pay.ui.constant.entry.EntryExtraData;
import com.pax.us.pay.ui.constant.entry.TextEntry;
import com.pax.us.pay.ui.constant.entry.enumeration.InputType;
import com.paxus.pay.poslinkui.demo.R;
import com.paxus.pay.poslinkui.demo.entry.BaseEntryFragment;
import com.paxus.pay.poslinkui.demo.utils.Toast;
import com.paxus.pay.poslinkui.demo.utils.ValuePatternUtils;
import com.paxus.pay.poslinkui.demo.view.TextField;

import java.util.Arrays;
import java.util.List;

/**
 * Implement text entry actions:<br>
 * {@value TextEntry#ACTION_ENTER_ZIPCODE}<br>
 * {@value TextEntry#ACTION_ENTER_DEST_ZIPCODE}<br>
 * {@value TextEntry#ACTION_ENTER_INVOICE_NUMBER}<br>
 * {@value TextEntry#ACTION_ENTER_VOUCHER_DATA}<br>
 * {@value TextEntry#ACTION_ENTER_REFERENCE_NUMBER}<br>
 * {@value TextEntry#ACTION_ENTER_MERCHANT_REFERENCE_NUMBER}<br>
 * {@value TextEntry#ACTION_ENTER_OCT_REFERENCE_NUMBER}<br>
 */
public abstract class ANumTextFragment extends BaseEntryFragment {

    protected TextField textField;
    protected boolean allowPassword;
    protected boolean allowText;
    protected String valuePatten;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_base_num_text;
    }

    @Override
    protected void loadArgument(@NonNull Bundle bundle) {
        allowText = Arrays.asList(InputType.ALLTEXT, InputType.PASSWORD).contains(bundle.getString(EntryExtraData.PARAM_EINPUT_TYPE));
        allowPassword = Arrays.asList(InputType.PASSWORD, InputType.PASSCODE).contains(bundle.getString(EntryExtraData.PARAM_EINPUT_TYPE));
    }

    @Override
    protected void loadView(View rootView) {
        String message = formatMessage();

        TextView textView = rootView.findViewById(R.id.message);
        textView.setText(message);

        textField = rootView.findViewById(R.id.edit_number_text);
        int maxLength = getMaxLength();
        if (maxLength > 0) {
            textField.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
        }
        if (allowText) {
            textField.setInputType(android.text.InputType.TYPE_CLASS_TEXT);
        } else {
            textField.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
        }
        if (allowPassword) {
            textField.setTransformationMethod(PasswordTransformationMethod.getInstance());
        } else {
            textField.setTransformationMethod(null);
        }
        Button confirmBtn = rootView.findViewById(R.id.confirm_button);
        confirmBtn.setOnClickListener(v -> onConfirmButtonClicked());

    }

    @Override
    protected void onConfirmButtonClicked() {
        String value = textField.getText().toString();
        List<Integer> lengthList = ValuePatternUtils.getLengthList(valuePatten);
        //For patterns like "0,2" which means 0 or 2, when the input length is 1,
        // we cannot immediately validate its effectiveness during the input process.
        // Therefore, we need to check before submission.
        if (lengthList.contains(value.length())) {
            submit(value);
        } else {
            new Toast(getActivity()).show(getString(R.string.prompt_input_length, valuePatten), Toast.TYPE.FAILURE);
        }
    }

    protected abstract int getMaxLength();

    protected abstract String formatMessage();

    protected abstract String getRequestedParamName();

    @Override
    protected TextField[] focusableTextFields() {
        return new TextField[]{textField};
    }

    protected void submit(String value) {
        Bundle bundle = new Bundle();
        bundle.putString(getRequestedParamName(), value);
        sendNext(bundle);
    }
}
