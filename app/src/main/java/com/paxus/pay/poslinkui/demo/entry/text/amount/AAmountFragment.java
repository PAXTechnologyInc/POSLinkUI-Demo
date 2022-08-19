package com.paxus.pay.poslinkui.demo.entry.text.amount;

import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.LayoutRes;

import com.pax.us.pay.ui.constant.entry.TextEntry;
import com.paxus.pay.poslinkui.demo.R;
import com.paxus.pay.poslinkui.demo.entry.BaseEntryFragment;
import com.paxus.pay.poslinkui.demo.utils.CurrencyUtils;
import com.paxus.pay.poslinkui.demo.utils.EntryRequestUtils;
import com.paxus.pay.poslinkui.demo.utils.Logger;
import com.paxus.pay.poslinkui.demo.view.AmountTextWatcher;

/**
 * Implement text entry actions:<br>
 * {@value TextEntry#'ACTION_ENTER_AMOUNT}<br>
 * {@value TextEntry#ACTION_ENTER_FUEL_AMOUNT}<br>
 * {@value TextEntry#ACTION_ENTER_TAX_AMOUNT}<br>
 *
 * <p>
 * UI Tips:
 * If confirm button clicked, sendNext
 * </p>
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
        editText.setSelected(true);
        editText.setText(CurrencyUtils.convert(0, getCurrency()));
        editText.setSelection(editText.getEditableText().length());

        editText.addTextChangedListener(new AmountTextWatcher(getMaxLength(), getCurrency()));

        //show keyboard automatically
        editText.requestFocusFromTouch();

        Button confirmBtn = rootView.findViewById(R.id.confirm_button);
        confirmBtn.setOnClickListener(v -> onConfirmButtonClicked());

    }

    //1.If confirm button clicked, sendNext
    private void onConfirmButtonClicked() {
        long value = CurrencyUtils.parse(editText.getText().toString());
        sendNext(value);
    }

    protected abstract String formatMessage();

    protected abstract int getMaxLength();

    protected abstract String getCurrency();

    protected void sendNext(long value) {
        EntryRequestUtils.sendNext(requireContext(), getSenderPackageName(), getEntryAction(), getRequestedParamName(), value);
    }

    protected abstract String getRequestedParamName();


}
