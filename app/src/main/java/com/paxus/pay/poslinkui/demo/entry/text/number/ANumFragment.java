package com.paxus.pay.poslinkui.demo.entry.text.number;

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
 * {@value TextEntry#ACTION_ENTER_CLERK_ID}<br>
 * {@value TextEntry#ACTION_ENTER_SERVER_ID}<br>
 * {@value TextEntry#ACTION_ENTER_TABLE_NUMBER}<br>
 * {@value TextEntry#ACTION_ENTER_CS_PHONE_NUMBER}<br>
 * {@value TextEntry#ACTION_ENTER_PHONE_NUMBER}<br>
 * {@value TextEntry#ACTION_ENTER_GUEST_NUMBER}<br>
 * {@value TextEntry#ACTION_ENTER_MERCHANT_TAX_ID}<br>
 * {@value TextEntry#ACTION_ENTER_PROMPT_RESTRICTION_CODE}<br>
 * {@value TextEntry#ACTION_ENTER_TRANS_NUMBER}<br>
 */
public abstract class ANumFragment extends BaseEntryFragment {

    protected EditText editText;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_base_num;
    }

    @Override
    protected void loadView(View rootView) {
        String message = formatMessage();
        TextView textView = rootView.findViewById(R.id.message);
        textView.setText(message);

        editText = rootView.findViewById(R.id.edit_number);
        focusableEditTexts = new EditText[]{editText};
        int maxLength = getMaxLength();
        if (maxLength > 0) {
            editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
        }

        Button confirmBtn = rootView.findViewById(R.id.confirm_button);
        confirmBtn.setOnClickListener(v -> onConfirmButtonClicked());
    }

    protected abstract String formatMessage();

    protected abstract int getMaxLength();

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

