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
import com.pax.us.pay.ui.constant.entry.enumeration.DateFormat;
import com.paxus.pay.poslinkui.demo.R;
import com.paxus.pay.poslinkui.demo.entry.BaseEntryFragment;
import com.paxus.pay.poslinkui.demo.utils.EntryRequestUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Implement text entry action {@value TextEntry#ACTION_ENTER_ORIG_DATE}<br>
 */

public class EnterOrigTransDateFragment extends BaseEntryFragment {
    private long timeOut;
    private String message = "";
    private String dateFormat;
    private EditText editText;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_orig_trans_date;
    }

    private Map<String, String> hintMap = new HashMap<String, String>() {{
        put(DateFormat.MMYY, "MM/YY");
        put(DateFormat.MMDD, "MM/DD");
        put(DateFormat.YYYYMMDD, "YYYY/MM/DD");
    }};

    @Override
    protected void loadArgument(@NonNull Bundle bundle) {
        timeOut = bundle.getLong(EntryExtraData.PARAM_TIMEOUT, 30000);

        message = getString(R.string.pls_input_orig_trans_date);
        dateFormat = bundle.getString(EntryExtraData.PARAM_DATE_FORMAT, DateFormat.YYYYMMDD);
    }

    @Override
    protected void loadView(View rootView) {

        TextView textView = rootView.findViewById(R.id.message);
        textView.setText(message);

        editText = rootView.findViewById(R.id.edit_expiry);
        focusableEditTexts = new EditText[]{editText};
        editText.setHint(hintMap.get(dateFormat));
        editText.setSelection(editText.getEditableText().length());
        editText.addTextChangedListener(new DateTextWatcher(dateFormat));

        Button confirmBtn = rootView.findViewById(R.id.confirm_button);
        confirmBtn.setOnClickListener(v -> onConfirmButtonClicked());
    }

    @Override
    protected void onConfirmButtonClicked() {
        String value = editText.getText().toString();
        value = value.replaceAll("[^0-9]", "");
        submit(value);
    }

    private void submit(String value) {
        Bundle bundle = new Bundle();
        bundle.putString(EntryRequest.PARAM_ORIG_DATE, value);
        sendNext(bundle);
    }
}


class DateTextWatcher implements TextWatcher {
    protected boolean mEditing;
    private String mPreStr = "";
    private String format;

    public DateTextWatcher(String format) {
        this.format = format;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        if (!mEditing) {
            mPreStr = s.toString();
        }
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        // Do nothing here
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (!mEditing) {
            mEditing = true;
            String value = s.toString().replaceAll("[^0-9]", ""); // Remove non-digits
            StringBuilder sb = new StringBuilder(value);

            int[] formatSections = getFormatSections(format);
            int maxLength = getMaxLength(formatSections);

            if (value.length() > maxLength) {
                s.clear();
                s.append(mPreStr);
            } else {
                for (int i = 0; i < formatSections.length; i++) {
                    int pos = sum(formatSections, i);
                    if (value.length() > pos) {
                        sb.insert(pos+i, "/");
                    }
                }
                s.replace(0, s.length(), sb.toString());
            }
            mEditing = false;
        }
    }

    private int[] getFormatSections(String format) {
        switch (format) {
            case DateFormat.YYYYMMDD:
                return new int[]{4, 2, 2};
            case DateFormat.MMYY:
            case DateFormat.MMDD:
                return new int[]{2, 2};
            default:
                throw new IllegalArgumentException("Unknown format");
        }
    }

    private int getMaxLength(int[] formatSections) {
        int length = 0;
        for (int section : formatSections) {
            length += section;
        }
        return length;
    }

    private int sum(int[] array, int index) {
        int sum = 0;
        for (int i = 0; i <= index; i++) {
            sum += array[i];
        }
        return sum;
    }
}

