package com.paxus.pay.poslinkui.demo.entry.text.amount;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.LayoutRes;

import com.pax.us.pay.ui.constant.entry.TextEntry;
import com.paxus.pay.poslinkui.demo.R;
import com.paxus.pay.poslinkui.demo.entry.BaseEntryFragment;
import com.paxus.pay.poslinkui.demo.utils.CurrencyUtils;
import com.paxus.pay.poslinkui.demo.utils.Toast;
import com.paxus.pay.poslinkui.demo.utils.ValuePatternUtils;
import com.paxus.pay.poslinkui.demo.view.AmountTextWatcher;
import com.paxus.pay.poslinkui.demo.view.TextField;

import java.util.List;

/**
 * Implement text entry actions:<br>
 * {@value TextEntry#'ACTION_ENTER_AMOUNT}<br>
 * {@value TextEntry#ACTION_ENTER_FUEL_AMOUNT}<br>
 * {@value TextEntry#ACTION_ENTER_TAX_AMOUNT}<br>
 */

public abstract class AAmountFragment extends BaseEntryFragment {

    private TextField textField;
    protected String valuePatten;
    @Override
    protected @LayoutRes
    int getLayoutResourceId() {
        return R.layout.fragment_amount;
    }

    @Override
    protected void loadView(View rootView) {
        TextView textView = rootView.findViewById(R.id.message);
        textView.setText(formatMessage());

        textField = rootView.findViewById(R.id.edit_amount);
        textField.setSelected(true);
        textField.setText(CurrencyUtils.convert(0, getCurrency()));
        textField.setSelection(textField.getEditableText().length());
        textField.addTextChangedListener(new AmountTextWatcher(getMaxLength(), getCurrency()));

        Button confirmBtn = rootView.findViewById(R.id.confirm_button);
        confirmBtn.setOnClickListener(v -> onConfirmButtonClicked());
    }

    @Override
    protected void onConfirmButtonClicked() {
        long value = CurrencyUtils.parse(textField.getText().toString());
        List<Integer> lengthList = ValuePatternUtils.getLengthList(valuePatten);
        // if value is 0 and pattern does not contains 0, that means it can not be skipped, show error message
        if (value == 0 && !lengthList.contains(0)) {
            new Toast(getActivity()).show(getString(R.string.prompt_input), Toast.TYPE.FAILURE);
        } else {
            submit(value);
        }
    }

    protected void submit(long value) {
        Bundle bundle = new Bundle();
        bundle.putLong(getRequestedParamName(), value);
        sendNext(bundle);
    }

    @Override
    protected TextField[] focusableTextFields() {
        return new TextField[]{textField};
    }

    protected abstract String getRequestedParamName();
    protected abstract String formatMessage();
    protected abstract int getMaxLength();
    protected abstract String getCurrency();
}
