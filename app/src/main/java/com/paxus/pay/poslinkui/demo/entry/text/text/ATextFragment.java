package com.paxus.pay.poslinkui.demo.entry.text.text;

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
 * {@value TextEntry#ACTION_ENTER_ADDRESS}<br>
 * {@value TextEntry#ACTION_ENTER_AUTH}<br>
 * {@value TextEntry#ACTION_ENTER_CUSTOMER_CODE}<br>
 * {@value TextEntry#ACTION_ENTER_ORDER_NUMBER}<br>
 * {@value TextEntry#ACTION_ENTER_PO_NUMBER}<br>
 * {@value TextEntry#ACTION_ENTER_PROD_DESC}<br>
 */
public abstract class ATextFragment extends BaseEntryFragment {

    protected EditText editText;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_base_text;
    }

    @Override
    protected void loadView(View rootView) {
        TextView textView = rootView.findViewById(R.id.message);
        textView.setText(formatMessage());

        editText = rootView.findViewById(R.id.edit_text);
        int maxLength = getMaxLength();
        if (maxLength > 0) {
            editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
        }

        Button confirmBtn = rootView.findViewById(R.id.confirm_button);
        confirmBtn.setOnClickListener(v -> onConfirmButtonClicked());
        focusableEditTexts = new EditText[]{editText};
    }

    protected abstract int getMaxLength();

    protected abstract String formatMessage();

    @Override
    protected void onConfirmButtonClicked() {
        String value = editText.getText().toString();
        submit(value);
    }

    protected void submit(String value) {
        Bundle bundle = new Bundle();
        bundle.putString(getRequestedParamName(), value);
        sendNext(bundle);
    }

    protected abstract String getRequestedParamName();
}
