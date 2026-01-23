package com.paxus.pay.poslinkui.demo.entry.text;

import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.pax.us.pay.ui.constant.entry.EntryExtraData;
import com.pax.us.pay.ui.constant.entry.EntryRequest;
import com.pax.us.pay.ui.constant.entry.TextEntry;
import com.paxus.pay.poslinkui.demo.R;
import com.paxus.pay.poslinkui.demo.entry.BaseEntryFragment;
import com.paxus.pay.poslinkui.demo.view.FormatTextWatcher;
import com.paxus.pay.poslinkui.demo.view.TextField;

/**
 * Implement text entry action {@value TextEntry#ACTION_ENTER_EXPIRY_DATE}<br>
 */
public class ExpiryFragment extends BaseEntryFragment {
    private long timeOut;
    private String message = "";

    private TextField textField;
    private static final String FORMAT_EXPIRY = "MM/YY";

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_expiry;
    }

    @Override
    protected void loadArgument(@NonNull Bundle bundle) {
        timeOut = bundle.getLong(EntryExtraData.PARAM_TIMEOUT, 30000);

        message = getString(R.string.pls_input_expiry_date);

    }

    @Override
    protected void loadView(View rootView) {
        TextView textView = rootView.findViewById(R.id.message);
        textView.setText(message);

        textField = rootView.findViewById(R.id.edit_expiry);

        textField.setSelection(textField.getEditableText().length());
        textField.setHint(FORMAT_EXPIRY);
        textField.setFilters(new InputFilter[]{new InputFilter.LengthFilter(textField.getHint().length())});
        textField.addTextChangedListener(new FormatTextWatcher(FORMAT_EXPIRY));

        Button confirmBtn = rootView.findViewById(R.id.confirm_button);
        confirmBtn.setOnClickListener(v -> onConfirmButtonClicked());
    }

    @Override
    protected void onConfirmButtonClicked() {
        String value = textField.getText().toString();
        value = value.replaceAll("[^0-9]", "");
        // for this page, the validation of this action will be handle by host.
        // so we don't need to validate the length of number
        submit(value);
    }

    @Override
    protected TextField[] focusableTextFields() {
        return new TextField[]{textField};
    }

    private void submit(String value) {
        Bundle bundle = new Bundle();
        bundle.putString(EntryRequest.PARAM_EXPIRY_DATE, value);
        sendNext(bundle);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        textField.clearFocus();
    }
}
