package com.paxus.pay.poslinkui.demo.entry.poslink;

import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.DigitsKeyListener;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.pax.us.pay.ui.constant.entry.EntryExtraData;
import com.pax.us.pay.ui.constant.entry.EntryRequest;
import com.pax.us.pay.ui.constant.entry.PoslinkEntry;
import com.pax.us.pay.ui.constant.entry.enumeration.CurrencyType;
import com.pax.us.pay.ui.constant.entry.enumeration.ManageUIConst;
import com.pax.us.pay.ui.constant.status.POSLinkStatus;
import com.paxus.pay.poslinkui.demo.R;
import com.paxus.pay.poslinkui.demo.entry.BaseEntryFragment;
import com.paxus.pay.poslinkui.demo.utils.CurrencyUtils;
import com.paxus.pay.poslinkui.demo.utils.DateUtils;
import com.paxus.pay.poslinkui.demo.utils.TaskScheduler;
import com.paxus.pay.poslinkui.demo.utils.Toast;
import com.paxus.pay.poslinkui.demo.view.AmountTextWatcher;
import com.paxus.pay.poslinkui.demo.view.FormatTextWatcher;
import com.paxus.pay.poslinkui.demo.view.TextField;

/**
 * Implement text entry actions:<br>
 * {@value PoslinkEntry#ACTION_SHOW_INPUT_TEXT_BOX}
 */
public class ShowInputTextBoxFragment extends BaseEntryFragment {
    private static final String FORMAT_DATE = "MM/DD/YYYY";
    private static final String FORMAT_TIME = "HH:MM:SS";
    private static final String FORMAT_PHONE = "(XXX)XXX-XXXX";
    private static final String FORMAT_SSN = "XXX-XX-XXXX";

    private long timeOut;
    private int minLength;
    private int maxLength;
    private String title;
    private String inputTextTitle;
    private String text;
    private boolean continuousScreen;
    private String inputType;
    private String defaultValue;

    private TextField textField;
    private LinearLayout textLayout;

    //Interfaces of POSLink Category may need to listen to POSLinkStatus Broadcasts
    private POSLinkStatusManager posLinkStatusManager;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        posLinkStatusManager = new POSLinkStatusManager(getContext(), getViewLifecycleOwner());
        posLinkStatusManager.registerHandler(POSLinkStatus.CLEAR_MESSAGE, this::clearMessage);
    }

    private void clearMessage() {
        textLayout.removeAllViews();
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_input_text_box;
    }

    @Override
    protected void loadArgument(@NonNull Bundle bundle) {
        timeOut = bundle.getLong(EntryExtraData.PARAM_TIMEOUT,30000);
        text = bundle.getString(EntryExtraData.PARAM_TEXT);

        title = bundle.getString(EntryExtraData.PARAM_TITLE);
        inputTextTitle = bundle.getString(EntryExtraData.PARAM_INPUT_TEXT_TITLE);

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
        TextView titleView = rootView.findViewById(R.id.title_view);
        titleView.setText(title);

        textLayout = rootView.findViewById(R.id.text_view);
        if(text == null || text.isEmpty()){
            textLayout.setVisibility(View.GONE);
        }else {
            for(TextView tv: TextShowingUtils.getTextViewList(requireContext(),text)){
                textLayout.addView(tv);
            }
            textLayout.setVisibility(View.VISIBLE);
        }

        textField = rootView.findViewById(R.id.edit_text);
        textField.setTransformationMethod(null);
        if ("1".equals(inputType)) {
            textField.setInputType(InputType.TYPE_CLASS_NUMBER);
            if(maxLength > 0 ) {
                textField.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
            }
            textField.setText(defaultValue);
        }else if("2".equals(inputType)){//Date
            minLength = 8;
            maxLength = 8;
            textField.setInputType(InputType.TYPE_CLASS_NUMBER);
            textField.setHint(FORMAT_DATE);
            textField.setTextIsSelectable(false);
            textField.setFilters(new InputFilter[]{new InputFilter.LengthFilter(textField.getHint().length())});
            textField.setKeyListener(DigitsKeyListener.getInstance("0123456789/"));
            textField.addTextChangedListener(new FormatTextWatcher(FORMAT_DATE));
            textField.setText(defaultValue);

        }else if("3".equals(inputType)){//Time
            minLength = 6;
            maxLength = 6;
            textField.setTextIsSelectable(false);
            textField.setInputType(InputType.TYPE_CLASS_NUMBER);
            textField.setHint(FORMAT_TIME);
            textField.setFilters(new InputFilter[]{new InputFilter.LengthFilter(textField.getHint().length())});
            textField.setKeyListener(DigitsKeyListener.getInstance("0123456789:"));
            textField.addTextChangedListener(new FormatTextWatcher(FORMAT_TIME));
            textField.setText(defaultValue);

        }else if("4".equals(inputType)){//Amount
            textField.setTextIsSelectable(false);
            // for amount, only allow "0123456789,.$€"
            textField.setKeyListener(DigitsKeyListener.getInstance("0123456789,.$€"));
            if(defaultValue != null && !defaultValue.isEmpty()) {
                String def = defaultValue.replaceAll("[^0-9]","");
                if(!TextUtils.isEmpty(def)) {
                    textField.setText(CurrencyUtils.convert(Long.parseLong(def), CurrencyType.USD));
                }

            } else {
                textField.setText(CurrencyUtils.convert(0, CurrencyType.USD));
            }
            textField.setSelection(textField.getEditableText().length());
            textField.addTextChangedListener(new AmountTextWatcher(maxLength, CurrencyType.USD));
        }else if("5".equals(inputType)){//password
            textField.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
            textField.setTransformationMethod(PasswordTransformationMethod.getInstance());
            if(maxLength > 0 ) {
                textField.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
            }
            textField.setText(defaultValue);

        }else if("6".equals(inputType)){//Phone
            minLength = 10;
            maxLength = 10;

            textField.setTextIsSelectable(false);
            textField.setInputType(InputType.TYPE_CLASS_NUMBER);
            textField.setHint(FORMAT_PHONE);
            textField.setFilters(new InputFilter[]{new InputFilter.LengthFilter(textField.getHint().length())});
            textField.setKeyListener(DigitsKeyListener.getInstance("0123456789-()"));
            textField.addTextChangedListener(new FormatTextWatcher(FORMAT_PHONE));
            textField.setText(defaultValue);

        }else if("7".equals(inputType)){//SSN
            minLength = 9;
            maxLength = 9;
            textField.setTextIsSelectable(false);
            textField.setInputType(InputType.TYPE_CLASS_NUMBER);
            textField.setHint(FORMAT_SSN);
            textField.setFilters(new InputFilter[]{new InputFilter.LengthFilter(textField.getHint().length())});
            textField.setKeyListener(DigitsKeyListener.getInstance("0123456789-"));
            textField.addTextChangedListener(new FormatTextWatcher(FORMAT_SSN));
            textField.setText(defaultValue);

        }else {
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
    protected void onConfirmButtonClicked(){
        String value = textField.getText().toString();
        if(inputType.matches("[23467]")){
            value = value.replaceAll("[^0-9]","");
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
}
