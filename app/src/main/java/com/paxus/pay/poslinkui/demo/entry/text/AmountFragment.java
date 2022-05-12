package com.paxus.pay.poslinkui.demo.entry.text;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.pax.us.pay.ui.constant.entry.EntryExtraData;
import com.pax.us.pay.ui.constant.entry.EntryRequest;
import com.pax.us.pay.ui.constant.entry.TextEntry;
import com.pax.us.pay.ui.constant.entry.enumeration.CurrencyType;
import com.pax.us.pay.ui.constant.entry.enumeration.TransMode;
import com.paxus.pay.poslinkui.demo.R;
import com.paxus.pay.poslinkui.demo.entry.BaseEntryFragment;
import com.paxus.pay.poslinkui.demo.utils.CurrencyUtils;
import com.paxus.pay.poslinkui.demo.utils.EntryRequestUtils;
import com.paxus.pay.poslinkui.demo.utils.ViewUtils;
import com.paxus.pay.poslinkui.demo.view.AmountTextWatcher;

/**
 * Implement text entry actions:<br>
 * {@value TextEntry#ACTION_ENTER_AMOUNT}<br>
 * {@value TextEntry#ACTION_ENTER_FUEL_AMOUNT}<br>
 * {@value TextEntry#ACTION_ENTER_TAX_AMOUNT}<br>
 */

public class AmountFragment extends BaseEntryFragment {
    private String transType;
    private String transMode;

    private long timeOut;
    private int minLength;
    private int maxLength;
    private String message = "";
    private String currency = "";

    public static AmountFragment newInstance(Intent intent){
        AmountFragment numFragment = new AmountFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EntryRequest.PARAM_ACTION, intent.getAction());
        bundle.putAll(intent.getExtras());
        numFragment.setArguments(bundle);
        return numFragment;
    }

    @Override
    protected @LayoutRes
    int getLayoutResourceId() {
        return R.layout.fragment_amount;
    }

    @Override
    protected void loadArgument(@NonNull Bundle bundle) {
        action = bundle.getString(EntryRequest.PARAM_ACTION);
        packageName = bundle.getString(EntryExtraData.PARAM_PACKAGE);
        transType = bundle.getString(EntryExtraData.PARAM_TRANS_TYPE);
        transMode = bundle.getString(EntryExtraData.PARAM_TRANS_MODE);
        timeOut = bundle.getLong(EntryExtraData.PARAM_TIMEOUT,30000);
        currency =  bundle.getString(EntryExtraData.PARAM_CURRENCY, CurrencyType.USD);

        String valuePatten = "";
        if(TextEntry.ACTION_ENTER_AMOUNT.equals(action)){
            valuePatten = bundle.getString(EntryExtraData.PARAM_VALUE_PATTERN,"1-12");
            if(CurrencyType.POINT.equals(currency)) {
                message = getString(R.string.prompt_input_point);
            }else {
                message = getString(R.string.prompt_input_amount);
            }
        } else if(TextEntry.ACTION_ENTER_FUEL_AMOUNT.equals(action)){
            valuePatten = bundle.getString(EntryExtraData.PARAM_VALUE_PATTERN,"0-12");
            message = getString(R.string.prompt_input_fuel_amount);
        } else if(TextEntry.ACTION_ENTER_TAX_AMOUNT.equals(action)){
            valuePatten = bundle.getString(EntryExtraData.PARAM_VALUE_PATTERN,"0-12");
            message = getString(R.string.prompt_input_tax_amount);
        }

        if(!TextUtils.isEmpty(valuePatten) && valuePatten.contains("-")){
            String[] tmp = valuePatten.split("-");
            if(tmp.length == 2) {
                minLength = Integer.parseInt(tmp[0]);
                maxLength = Integer.parseInt(tmp[1]);
            }
        }

    }

    @Override
    protected void loadView(View rootView) {
        if(!TextUtils.isEmpty(transType) && getActivity() instanceof AppCompatActivity){
            ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
            if(actionBar != null) {
                actionBar.setTitle(transType);
            }
        }

        String mode = null;
        if(!TextUtils.isEmpty(transMode)){
            if(TransMode.DEMO.equals(transMode)){
                mode = getString(R.string.demo_only);
            }else if(TransMode.TEST.equals(transMode)){
                mode = getString(R.string.test_only);
            }else if(TransMode.TEST_AND_DEMO.equals(transMode)){
                mode = getString(R.string.test_and_demo);
            }else {
                mode = "";
            }
        }
        if(!TextUtils.isEmpty(mode)){
            ViewUtils.addWaterMarkView(requireActivity(),mode);
        }else{
            ViewUtils.removeWaterMarkView(requireActivity());
        }

        TextView textView = rootView.findViewById(R.id.message);
        textView.setText(message);

        EditText editText = rootView.findViewById(R.id.edit_amount);
        editText.setSelected(false);
        editText.setText(CurrencyUtils.convert(0,currency));
        editText.setSelection(editText.getEditableText().length());

        editText.addTextChangedListener(new AmountTextWatcher(maxLength, currency));

        Button confirmBtn = rootView.findViewById(R.id.confirm_button);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long value = CurrencyUtils.parse(editText.getText().toString());
                if(String.valueOf(value).length() < minLength){
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
                }else {
                    sendNext(value);
                }
            }
        });

    }


    private void sendNext(long value){

        String param = "";
        if(TextEntry.ACTION_ENTER_AMOUNT.equals(action)){
            param = EntryRequest.PARAM_AMOUNT;
        }else if(TextEntry.ACTION_ENTER_FUEL_AMOUNT.equals(action)){
            param = EntryRequest.PARAM_FUEL_AMOUNT;
        } else if(TextEntry.ACTION_ENTER_TAX_AMOUNT.equals(action)){
            param = EntryRequest.PARAM_TAX_AMOUNT;
        }
        if(!TextUtils.isEmpty(param)){
            EntryRequestUtils.sendNext(requireContext(), packageName, action, param,value);
        }
    }
}
