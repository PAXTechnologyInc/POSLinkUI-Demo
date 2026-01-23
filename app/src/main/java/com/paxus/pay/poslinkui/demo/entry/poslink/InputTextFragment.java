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
import com.paxus.pay.poslinkui.demo.utils.DateUtils;
import com.paxus.pay.poslinkui.demo.utils.TaskScheduler;
import com.paxus.pay.poslinkui.demo.utils.Toast;
import com.paxus.pay.poslinkui.demo.view.FormatTextWatcher;
import com.paxus.pay.poslinkui.demo.view.TextField;

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

    private TextField textField;

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

        textField = rootView.findViewById(R.id.edit_text);

        if ("1".equals(inputType)) {
            textField.setInputType(InputType.TYPE_CLASS_NUMBER);
            if(maxLength > 0 ) {
                textField.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
            }
            textField.setText(defaultValue);

        } else if("2".equals(inputType)) {//Date
            minLength = 8;
            maxLength = 8;
            textField.setInputType(InputType.TYPE_CLASS_NUMBER);
            textField.setHint(FORMAT_DATE);
            textField.setTextIsSelectable(false);
            textField.setFilters(new InputFilter[]{new InputFilter.LengthFilter(textField.getHint().length())});
            textField.setKeyListener(DigitsKeyListener.getInstance("0123456789/"));
            textField.addTextChangedListener(new FormatTextWatcher(FORMAT_DATE));
            textField.setText(defaultValue);

        } else if("3".equals(inputType)) {//Time
            minLength = 6;
            maxLength = 6;
            textField.setTextIsSelectable(false);
            textField.setInputType(InputType.TYPE_CLASS_NUMBER);
            textField.setHint(FORMAT_TIME);
            textField.setFilters(new InputFilter[]{new InputFilter.LengthFilter(textField.getHint().length())});
            textField.setKeyListener(DigitsKeyListener.getInstance("0123456789:"));
            textField.addTextChangedListener(new FormatTextWatcher(FORMAT_TIME));
            textField.setText(defaultValue);

        } else if("4".equals(inputType)) {//Amount
            textField.setTextIsSelectable(false);
            textField.addTextChangedListener(new CurrencyTextWatcher(textField, CurrencyType.USD, maxLength));
            textField.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
            textField.setKeyListener(DigitsKeyListener.getInstance("0123456789,.$€£"));
            textField.setHint(CurrencyUtils.CURRENCY_SYMBOL_MAP.get(CurrencyType.USD) + "0.00");

            if(defaultValue != null) {
                String def = defaultValue.replaceAll("[^0-9]","");
                if(!TextUtils.isEmpty(def)) {
                    textField.setText(CurrencyUtils.convert(Long.parseLong(def), CurrencyType.USD));
                }
            }

        } else if("5".equals(inputType)) {//password
            textField.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            if(maxLength > 0 ) {
                textField.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
            }
            textField.setText(defaultValue);

        } else if("6".equals(inputType)) {//Phone
            minLength = 10;
            maxLength = 10;

            textField.setTextIsSelectable(false);
            textField.setInputType(InputType.TYPE_CLASS_NUMBER);
            textField.setHint(FORMAT_PHONE);
            textField.setFilters(new InputFilter[]{new InputFilter.LengthFilter(textField.getHint().length())});
            textField.setKeyListener(DigitsKeyListener.getInstance("0123456789-()"));
            textField.addTextChangedListener(new FormatTextWatcher(FORMAT_PHONE));
            textField.setText(defaultValue);

        } else if("7".equals(inputType)) {//SSN
            minLength = 9;
            maxLength = 9;
            textField.setTextIsSelectable(false);
            textField.setInputType(InputType.TYPE_CLASS_NUMBER);
            textField.setHint(FORMAT_SSN);
            textField.setFilters(new InputFilter[]{new InputFilter.LengthFilter(textField.getHint().length())});
            textField.setKeyListener(DigitsKeyListener.getInstance("0123456789-"));
            textField.addTextChangedListener(new FormatTextWatcher(FORMAT_SSN));
            textField.setText(defaultValue);

        } else {
            textField.setInputType(InputType.TYPE_CLASS_TEXT);
            if(maxLength > 0 ) {
                textField.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
            }
            textField.setText(defaultValue);

        }

        Button confirmBtn = rootView.findViewById(R.id.confirm_button);
        confirmBtn.setOnClickListener(v -> onConfirmButtonClicked());

        if(timeOut > 0 ) {
            getParentFragmentManager().setFragmentResult(TaskScheduler.SCHEDULE, TaskScheduler.generateTaskRequestBundle(TaskScheduler.TASK.TIMEOUT, timeOut));
        }
    }

    @Override
    protected TextField[] focusableTextFields() {
        return new TextField[]{textField};
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
        String value = textField.getText().toString();
        if (inputType.matches("[23467]")) {
            value = value.replaceAll("[^0-9]", "");
        }
        //when input type is amount, only validate if non-null, not min length
        if ("4".equals(inputType)) {
            long amount = CurrencyUtils.parse(value);
            if (minLength > 0 && amount <=0) {
                new Toast(getActivity()).show(getString(R.string.prompt_input), Toast.TYPE.FAILURE);
                return;
            }
            submit(amount);
            return;
        }
        // add the validation of minValue when it is not amount type
        if (value.length() < minLength && !"4".equals(inputType)){
            String errMsg = minLength == maxLength ? getString(R.string.prompt_input_type) :
                    getString(R.string.prompt_input_length, minLength + "-" + maxLength);
            new Toast(getActivity()).show(errMsg, Toast.TYPE.FAILURE);
            return;
        }
        // validate date
        if ("2".equals(inputType)){
            if (DateUtils.isValidateDate(value))
                submit(value);
            else
                new Toast(getActivity()).show(getString(R.string.prompt_invalid_date), Toast.TYPE.FAILURE);
            return;
        }
        // validate time
        if ("3".equals(inputType)){
            if (DateUtils.isValidateTime(value))
                submit(value);
            else
                new Toast(getActivity()).show(getString(R.string.prompt_invalid_time), Toast.TYPE.FAILURE);
            return;
        }
        submit(value);
    }

    private void submit(String value){
        Bundle bundle = new Bundle();
        bundle.putString(EntryRequest.PARAM_INPUT_VALUE, value);
        sendNext(bundle);
    }

    private void submit(long value){
        Bundle bundle = new Bundle();
        bundle.putLong(EntryRequest.PARAM_INPUT_VALUE, value);
        sendNext(bundle);
    }

    private class CurrencyTextWatcher implements TextWatcher {
        private final WeakReference<TextField> editTextWeakReference;
        private String currencyType;
        private int maxLength;

        private String valueBeforeTextChange;
        public CurrencyTextWatcher(TextField textField, String currencyType, int maxLength) {
            this.editTextWeakReference = new WeakReference<>(textField);
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
