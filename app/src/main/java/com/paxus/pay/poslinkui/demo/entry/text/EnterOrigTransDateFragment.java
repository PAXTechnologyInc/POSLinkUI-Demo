package com.paxus.pay.poslinkui.demo.entry.text;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.pax.us.pay.ui.constant.entry.EntryExtraData;
import com.pax.us.pay.ui.constant.entry.EntryRequest;
import com.pax.us.pay.ui.constant.entry.TextEntry;
import com.paxus.pay.poslinkui.demo.R;
import com.paxus.pay.poslinkui.demo.entry.BaseEntryFragment;
import com.paxus.pay.poslinkui.demo.utils.EntryRequestUtils;

/**
 * Implement text entry action {@value TextEntry#ACTION_ENTER_ORIG_DATE}<br>
 *
 * <p>
 * UI Tips:
 * If confirm button clicked, sendNext
 * </p>
 */

public class EnterOrigTransDateFragment extends BaseEntryFragment {

    private String transType;
    private String transMode;
    private long timeOut;
    private String message = "";
    private EditText editText;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_orig_trans_date;
    }

    @Override
    protected void loadArgument(@NonNull Bundle bundle) {
        transType = bundle.getString(EntryExtraData.PARAM_TRANS_TYPE);
        transMode = bundle.getString(EntryExtraData.PARAM_TRANS_MODE);
        timeOut = bundle.getLong(EntryExtraData.PARAM_TIMEOUT, 30000);

        message = getString(R.string.pls_input_orig_trans_date);

    }

    @Override
    protected void loadView(View rootView) {

        TextView textView = rootView.findViewById(R.id.message);
        textView.setText(message);

        editText = rootView.findViewById(R.id.edit_expiry);
        focusableEditTexts = new EditText[]{editText};
        editText.setSelection(editText.getEditableText().length());
        editText.addTextChangedListener(new TextWatcher() {
            protected boolean mEditing;
            private String mPreStr;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (!mEditing) {
                    mPreStr = s.toString();
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!mEditing) {
                    mEditing = true;
                    String value = s.toString().replaceAll("[^0-9]", "");
                    StringBuilder sb = new StringBuilder(value);
                    if (sb.length() > 8) {
                        s.replace(0, s.length(), mPreStr);
                    } else {
                        if (sb.length() >= 6) {
                            if (sb.length() > 6) {
                                sb = sb.insert(6, "/");
                            } else {
                                sb.append("/");
                            }
                            sb = sb.insert(4, "/");
                        } else if (sb.length() >= 4) {
                            sb = sb.insert(4, "/");
                        }

                        if (mPreStr.equals(sb.toString())) {
                            sb.delete(sb.length() - 2, sb.length());
                        }

                        s.replace(0, s.length(), sb.toString());
                    }
                    mEditing = false;
                }
            }
        });

        Button confirmBtn = rootView.findViewById(R.id.confirm_button);
        confirmBtn.setOnClickListener(v -> onConfirmButtonClicked());
    }

    @Override
    protected void onConfirmButtonClicked() {
        String value = editText.getText().toString();
        value = value.replaceAll("[^0-9]", "");
        if (value.length() == 8) {
            submit(value);
        }
    }

    private void submit(String value) {
        Bundle bundle = new Bundle();
        bundle.putString(EntryRequest.PARAM_ORIG_DATE, value);
        sendNext(bundle);
    }
}
