package com.paxus.pay.poslinkui.demo.entry.text.amount;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.LayoutRes;

import com.pax.us.pay.ui.constant.entry.TextEntry;
import com.paxus.pay.poslinkui.demo.R;
import com.paxus.pay.poslinkui.demo.entry.BaseEntryFragment;
import com.paxus.pay.poslinkui.demo.utils.CurrencyUtils;
import com.paxus.pay.poslinkui.demo.view.AmountTextWatcher;

/**
 * Implement text entry actions:<br>
 * {@value TextEntry#'ACTION_ENTER_AMOUNT}<br>
 * {@value TextEntry#ACTION_ENTER_FUEL_AMOUNT}<br>
 * {@value TextEntry#ACTION_ENTER_TAX_AMOUNT}<br>
 */

public abstract class AAmountFragment extends BaseEntryFragment {

    private EditText editText;

    @Override
    protected @LayoutRes
    int getLayoutResourceId() {
        return R.layout.fragment_amount;
    }

    @Override
    protected void loadView(View rootView) {
        TextView textView = rootView.findViewById(R.id.message);
        textView.setText(formatMessage());

        editText = rootView.findViewById(R.id.edit_amount);
        focusableEditTexts = new EditText[]{editText};
        editText.setSelected(true);
        editText.setText(CurrencyUtils.convert(0, getCurrency()));
        editText.setSelection(editText.getEditableText().length());
        editText.addTextChangedListener(new AmountTextWatcher(getMaxLength(), getCurrency()));

        Button confirmBtn = rootView.findViewById(R.id.confirm_button);
        confirmBtn.setOnClickListener(v -> onConfirmButtonClicked());
    }

    @Override
    protected void onConfirmButtonClicked() {
        submit(CurrencyUtils.parse(editText.getText().toString()));
    }

    protected void submit(long value) {
        Bundle bundle = new Bundle();
        bundle.putLong(getRequestedParamName(), value);
        sendNext(bundle);
    }

    protected abstract String getRequestedParamName();
    protected abstract String formatMessage();
    protected abstract int getMaxLength();
    protected abstract String getCurrency();
}
