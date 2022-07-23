package com.paxus.pay.poslinkui.demo.entry.text.text;

import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.pax.us.pay.ui.constant.entry.TextEntry;
import com.paxus.pay.poslinkui.demo.R;
import com.paxus.pay.poslinkui.demo.entry.BaseEntryFragment;
import com.paxus.pay.poslinkui.demo.utils.EntryRequestUtils;

/**
 * Implement text entry actions:<br>
 * {@value TextEntry#ACTION_ENTER_ADDRESS}<br>
 * {@value TextEntry#ACTION_ENTER_AUTH}<br>
 * {@value TextEntry#ACTION_ENTER_CUSTOMER_CODE}<br>
 * {@value TextEntry#ACTION_ENTER_ORDER_NUMBER}<br>
 * {@value TextEntry#ACTION_ENTER_PO_NUMBER}<br>
 * {@value TextEntry#ACTION_ENTER_PROD_DESC}<br>
 *
 * <p>
 * UI Tips:
 * If confirm button clicked, sendNext
 * </p>
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
    }

    protected abstract int getMaxLength();

    protected abstract String formatMessage();

    //If confirm button clicked, sendNext
    private void onConfirmButtonClicked() {
        String value = editText.getText().toString();
        sendNext(value);

    }

    protected void sendNext(String value) {
        EntryRequestUtils.sendNext(requireContext(), getSenderPackageName(), getEntryAction(), getRequestedParamName(), value);
    }

    protected abstract String getRequestedParamName();

}
