package com.paxus.pay.poslinkui.demo.entry.text.numbertext;

import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.pax.us.pay.ui.constant.entry.TextEntry;
import com.paxus.pay.poslinkui.demo.R;
import com.paxus.pay.poslinkui.demo.entry.BaseEntryFragment;

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

    protected EditText editText;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_base_num_text;
    }

    @Override
    protected void loadView(View rootView) {
        String message = formatMessage();

        TextView textView = rootView.findViewById(R.id.message);
        textView.setText(message);

        editText = rootView.findViewById(R.id.edit_number_text);
        focusableEditTexts = new EditText[]{editText};
        int maxLength = getMaxLength();
        if (maxLength > 0) {
            editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
        }
        if (allowText()) {
            editText.setInputType(android.text.InputType.TYPE_CLASS_TEXT);
        } else {
            editText.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
        }
        Button confirmBtn = rootView.findViewById(R.id.confirm_button);
        confirmBtn.setOnClickListener(v -> onConfirmButtonClicked());

    }

    @Override
    protected void onConfirmButtonClicked() {
        String value = editText.getText().toString();
        submit(value);
    }

    protected abstract int getMaxLength();

    protected abstract boolean allowText();

    protected abstract String formatMessage();

    protected abstract String getRequestedParamName();

    protected void submit(String value) {
        Bundle bundle = new Bundle();
        bundle.putString(getRequestedParamName(), value);
        sendNext(bundle);
    }
}
