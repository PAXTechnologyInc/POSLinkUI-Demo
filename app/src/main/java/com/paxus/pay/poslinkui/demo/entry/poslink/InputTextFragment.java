package com.paxus.pay.poslinkui.demo.entry.poslink;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.pax.us.pay.ui.constant.entry.EntryExtraData;
import com.pax.us.pay.ui.constant.entry.EntryRequest;
import com.pax.us.pay.ui.constant.entry.PoslinkEntry;
import com.pax.us.pay.ui.constant.entry.enumeration.CurrencyType;
import com.pax.us.pay.ui.constant.entry.enumeration.ManageUIConst;
import com.paxus.pay.poslinkui.demo.R;
import com.paxus.pay.poslinkui.demo.entry.BaseEntryFragment;
import com.paxus.pay.poslinkui.demo.utils.CurrencyUtils;
import com.paxus.pay.poslinkui.demo.utils.TaskScheduler;

import java.lang.ref.WeakReference;

/**
 * Implement text entry actions:<br>
 * {@value PoslinkEntry#ACTION_INPUT_TEXT}
 */
public class InputTextFragment extends BaseEntryFragment {
    private static final String FORMAT_DATE = "MM/DD/YYYY";
    private static final String FORMAT_TIME = "HH:MM:SS";
    private static final String FORMAT_PHONE = "(XXX)XXX-XXXX";
    private static final String FORMAT_SSN = "XXX-XX-XXXX";
    private long timeOut;
    private int minLength;
    private int maxLength;
    private String title;
    private boolean continuousScreen;
    private String inputType;
    private String defaultValue;

    private EditText editText;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_input_text;
    }

    @Override
    protected void loadArgument(@NonNull Bundle bundle) {
        timeOut = bundle.getLong(EntryExtraData.PARAM_TIMEOUT,30000);

        title = bundle.getString(EntryExtraData.PARAM_TITLE);
        continuousScreen = ManageUIConst.ContinuousScreen.DO_NOT_GO_TO_IDLE.equals(
                bundle.getString(EntryExtraData.PARAM_CONTINUE_SCREEN, ""));

        inputType = bundle.getString(EntryExtraData.PARAM_INPUT_TYPE, "");
        defaultValue = bundle.getString(EntryExtraData.PARAM_DEFAULT_VALUE, "");
        String minLen = bundle.getString(EntryExtraData.PARAM_MIN_LENGTH, "0");
        String maxLen = bundle.getString(EntryExtraData.PARAM_MAX_LENGTH,"32");
        minLength = Integer.parseInt(minLen);
        maxLength = Integer.parseInt(maxLen);

    }

    @Override
    protected void loadView(View rootView) {
        TextView textView = rootView.findViewById(R.id.message);
        textView.setText(title);

        editText = rootView.findViewById(R.id.edit_text);
        focusableEditTexts = new EditText[]{editText};

        if ("1".equals(inputType)) {
            editText.setInputType(InputType.TYPE_CLASS_NUMBER);
            if(maxLength > 0 ) {
                editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
            }
            editText.setText(defaultValue);

        } else if("2".equals(inputType)) {//Date
            minLength = 8;
            maxLength = 8;
            editText.setInputType(InputType.TYPE_CLASS_NUMBER);
            editText.setHint(FORMAT_DATE);
            editText.setTextIsSelectable(false);
            editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(editText.getHint().length())});
            editText.setKeyListener(DigitsKeyListener.getInstance("0123456789/"));
            editText.addTextChangedListener(new FormatTextWatcher(FORMAT_DATE));
            editText.setText(defaultValue);

        } else if("3".equals(inputType)) {//Time
            minLength = 6;
            maxLength = 6;
            editText.setTextIsSelectable(false);
            editText.setInputType(InputType.TYPE_CLASS_NUMBER);
            editText.setHint(FORMAT_TIME);
            editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(editText.getHint().length())});
            editText.setKeyListener(DigitsKeyListener.getInstance("0123456789:"));
            editText.addTextChangedListener(new FormatTextWatcher(FORMAT_TIME));
            editText.setText(defaultValue);

        } else if("4".equals(inputType)) {//Amount
            editText.setTextIsSelectable(false);
            editText.addTextChangedListener(new CurrencyTextWatcher(editText, CurrencyType.USD, maxLength));
            editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
            editText.setKeyListener(DigitsKeyListener.getInstance("0123456789,.$€£"));
            editText.setHint(CurrencyUtils.CURRENCY_SYMBOL_MAP.get(CurrencyType.USD) + "0.00");

            if(defaultValue != null) {
                String def = defaultValue.replaceAll("[^0-9]","");
                if(!TextUtils.isEmpty(def)) {
                    editText.setText(CurrencyUtils.convert(Long.parseLong(def), CurrencyType.USD));
                }
            }

        } else if("5".equals(inputType)) {//password
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            if(maxLength > 0 ) {
                editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
            }
            editText.setText(defaultValue);

        } else if("6".equals(inputType)) {//Phone
            minLength = 10;
            maxLength = 10;

            editText.setTextIsSelectable(false);
            editText.setInputType(InputType.TYPE_CLASS_NUMBER);
            editText.setHint(FORMAT_PHONE);
            editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(editText.getHint().length())});
            editText.setKeyListener(DigitsKeyListener.getInstance("0123456789-()"));
            editText.addTextChangedListener(new FormatTextWatcher(FORMAT_PHONE));
            editText.setText(defaultValue);

        } else if("7".equals(inputType)) {//SSN
            minLength = 9;
            maxLength = 9;
            editText.setTextIsSelectable(false);
            editText.setInputType(InputType.TYPE_CLASS_NUMBER);
            editText.setHint(FORMAT_SSN);
            editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(editText.getHint().length())});
            editText.setKeyListener(DigitsKeyListener.getInstance("0123456789-"));
            editText.addTextChangedListener(new FormatTextWatcher(FORMAT_SSN));
            editText.setText(defaultValue);

        } else {
            editText.setInputType(InputType.TYPE_CLASS_TEXT);
            if(maxLength > 0 ) {
                editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
            }
            editText.setText(defaultValue);

        }

        Button confirmBtn = rootView.findViewById(R.id.confirm_button);
        confirmBtn.setOnClickListener(v -> onConfirmButtonClicked());

        if(timeOut > 0 ) {
            getParentFragmentManager().setFragmentResult(TaskScheduler.SCHEDULE, TaskScheduler.generateTaskRequestBundle(TaskScheduler.TASK.TIMEOUT, timeOut));
        }
    }

    @Override
    protected void onEntryAccepted() {
        super.onEntryAccepted();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onConfirmButtonClicked() {
        String value = editText.getText().toString();
        if (inputType.matches("[23467]")) {
            value = value.replaceAll("[^0-9]", "");
        }
        submit(value);
    }

    private void submit(String value){
        Bundle bundle = new Bundle();
        bundle.putString(EntryRequest.PARAM_INPUT_VALUE, value);
        sendNext(bundle);
    }

    private static class FormatTextWatcher implements TextWatcher{

        private String format;
        public FormatTextWatcher(String format){
            this.format = format;
        }

        boolean editing;
        int deleteIndex = -1;

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after){
            boolean needDelete = count == 1 && after == 0 && (s.charAt(start)<'0' ||s.charAt(start)>'9');
            if(needDelete){
                deleteIndex = start;
            }else {
                deleteIndex = -1;
            }
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if(!editing) {
                editing = true;
                if(deleteIndex>0){
                    editable.delete(deleteIndex-1, deleteIndex);
                }
                StringBuilder value = new StringBuilder(editable.toString().replaceAll("[^0-9]", ""));
                if(FORMAT_DATE.equals(format)) {
                    if (value.length() >= 2) {
                        value.insert(2, "/");
                    }
                    if (value.length() >= 5) {
                        value.insert(5, "/");
                    }
                }else if(FORMAT_TIME.equals(format)){
                    if (value.length() >= 2) {
                        value.insert(2, ":");
                    }
                    if (value.length() >= 5) {
                        value.insert(5, ":");
                    }
                }else if(FORMAT_PHONE.equals(format)){
                    if (value.length() > 0) {
                        value.insert(0, "(");
                    }
                    if (value.length() >= 4) {
                        value.insert(4, ")");
                    }
                    if (value.length() >= 8) {
                        value.insert(8, "-");
                    }
                }else if(FORMAT_SSN.equals(format)){
                    if (value.length() >= 3) {
                        value.insert(3, "-");
                    }
                    if (value.length() >= 6) {
                        value.insert(6, "-");
                    }
                }
                editable.replace(0, editable.length(), value);
                editing = false;
            }
        }
    }

    private class CurrencyTextWatcher implements TextWatcher {
        private final WeakReference<EditText> editTextWeakReference;
        private String currencyType;
        private int maxLength;

        private String valueBeforeTextChange;
        public CurrencyTextWatcher(EditText editText, String currencyType, int maxLength) {
            this.editTextWeakReference = new WeakReference<>(editText);
            this.currencyType = currencyType;
            this.maxLength = maxLength;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            valueBeforeTextChange = charSequence.toString();
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        @Override
        public void afterTextChanged(Editable editable) {
            String temp = editable.toString().replaceAll("[^0-9]", "");

            editTextWeakReference.get().removeTextChangedListener(this);

            if(temp.length() > maxLength) {
                editable.replace(0, editable.length(), valueBeforeTextChange);
            } else {
                String currencySymbol = CurrencyUtils.CURRENCY_SYMBOL_MAP.containsKey(currencyType) ?
                        CurrencyUtils.CURRENCY_SYMBOL_MAP.get(currencyType) : "";
                String character = temp.length() > 2 ? removeLeadingZero(temp.substring(0, temp.length() - 2)) : "0";
                String mantissa = temp.length() > 2 ? temp.substring(temp.length() - 2) : putLeadingZero(temp, 2);

                editable.replace(0, editable.length(), currencySymbol + character + "." + mantissa);
            }

            editTextWeakReference.get().addTextChangedListener(this);
        }

        private String putLeadingZero(String value, int totalLength) {
            while (value.length() < totalLength) {
                value = "0" + value;
            }
            return value;
        }
        private String removeLeadingZero(String value) {
            while (value.length() > 1 && value.indexOf("0")==0) {
                value = value.substring(1);
            }
            return value;
        }
    }
}
