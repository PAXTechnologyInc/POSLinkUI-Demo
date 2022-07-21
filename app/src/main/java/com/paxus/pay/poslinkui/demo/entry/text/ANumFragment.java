package com.paxus.pay.poslinkui.demo.entry.text;

import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.pax.us.pay.ui.constant.entry.TextEntry;
import com.paxus.pay.poslinkui.demo.R;
import com.paxus.pay.poslinkui.demo.entry.BaseEntryFragment;

/**
 * Implement text entry actions:<br>
 * {@value TextEntry#ACTION_ENTER_CLERK_ID}<br>
 * {@value TextEntry#ACTION_ENTER_SERVER_ID}<br>
 * {@value TextEntry#ACTION_ENTER_TABLE_NUMBER}<br>
 * {@value TextEntry#ACTION_ENTER_CS_PHONE_NUMBER}<br>
 * {@value TextEntry#ACTION_ENTER_PHONE_NUMBER}<br>
 * {@value TextEntry#ACTION_ENTER_GUEST_NUMBER}<br>
 * {@value TextEntry#ACTION_ENTER_MERCHANT_TAX_ID}<br>
 * {@value TextEntry#ACTION_ENTER_PROMPT_RESTRICTION_CODE}<br>
 * {@value TextEntry#ACTION_ENTER_TRANS_NUMBER}<br>
 *
 * <p>
 *     UI Tips:
 *     If confirm button clicked, sendNext
 * </p>
 */
public abstract class ANumFragment extends BaseEntryFragment {
    protected String transType;
    protected long timeOut;
    protected int minLength;
    protected int maxLength;
    protected String message = "";
    protected String transMode;

    protected EditText editText;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_base_num;
    }


    @Override
    protected void loadView(View rootView) {
        TextView textView = rootView.findViewById(R.id.message);
        textView.setText(message);

        editText = rootView.findViewById(R.id.edit_number);
        if(maxLength > 0 ) {
            editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
        }

        Button confirmBtn = rootView.findViewById(R.id.confirm_button);
        confirmBtn.setOnClickListener(v->onConfirmButtonClicked());
    }

    //If confirm button clicked, sendNext
    private void onConfirmButtonClicked(){
        String value = editText.getText().toString();
        if(minLength == maxLength && maxLength>0 && value.length() != minLength){
            Toast.makeText(requireContext(), "Must be "+minLength+" digits.", Toast.LENGTH_SHORT).show();
        }else if(value.length() < minLength){
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
        }else {
            sendNext(value);
        }
    }

    protected abstract void sendNext(String value);
}


//    public static Fragment newInstance(Intent intent){
//        ANumFragment fragment = new ANumFragment();
//        Bundle bundle = new Bundle();
//        bundle.putString(EntryRequest.PARAM_ACTION, intent.getAction());
//        bundle.putAll(intent.getExtras());
//        fragment.setArguments(bundle);
//        return fragment;
//    }

//    private void sendNext(String value){
//        if(TextEntry.ACTION_ENTER_TRANS_NUMBER.equals(action)){
//            EntryRequestUtils.sendNext(requireContext(), packageName, action, EntryRequest.PARAM_TRANS_NUMBER,Long.parseLong(value));
//            return;
//        }
//        String param = "";
//
//        if(TextEntry.ACTION_ENTER_CLERK_ID.equals(action)){
//            param = EntryRequest.PARAM_CLERK_ID;
//        }else if(TextEntry.ACTION_ENTER_SERVER_ID.equals(action)){
//            param = EntryRequest.PARAM_SERVER_ID;
//        } else if(TextEntry.ACTION_ENTER_TABLE_NUMBER.equals(action)){
//            param = EntryRequest.PARAM_TABLE_NUMBER;
//        } else if(TextEntry.ACTION_ENTER_CS_PHONE_NUMBER.equals(action)){
//            param = EntryRequest.PARAM_CS_PHONE_NUMBER;
//        } else if(TextEntry.ACTION_ENTER_PHONE_NUMBER.equals(action)){
//            param = EntryRequest.PARAM_PHONE_NUMBER;
//        } else if(TextEntry.ACTION_ENTER_GUEST_NUMBER.equals(action)){
//            param = EntryRequest.PARAM_GUEST_NUMBER;
//        } else if(TextEntry.ACTION_ENTER_MERCHANT_TAX_ID.equals(action)){
//            param = EntryRequest.PARAM_MERCHANT_TAX_ID;
//        } else if(TextEntry.ACTION_ENTER_PROMPT_RESTRICTION_CODE.equals(action)){
//            param = EntryRequest.PARAM_PROMPT_RESTRICTION_CODE;
//        }
//        if(!TextUtils.isEmpty(param)){
//            EntryRequestUtils.sendNext(requireContext(), packageName, action, param,value);
//        }
//    }

//    @Override
//    protected void loadArgument(@NonNull Bundle bundle) {
//
//        action = bundle.getString(EntryRequest.PARAM_ACTION);
//        packageName = bundle.getString(EntryExtraData.PARAM_PACKAGE);
//        transType = bundle.getString(EntryExtraData.PARAM_TRANS_TYPE);
//        transMode = bundle.getString(EntryExtraData.PARAM_TRANS_MODE);
//        timeOut = bundle.getLong(EntryExtraData.PARAM_TIMEOUT,30000);
//
//        String valuePatten = "";
//        if(TextEntry.ACTION_ENTER_CLERK_ID.equals(action)){
//            valuePatten = bundle.getString(EntryExtraData.PARAM_VALUE_PATTERN,"0-4");
//            message = getString(R.string.pls_input_clerk_id);
//        } else if(TextEntry.ACTION_ENTER_SERVER_ID.equals(action)){
//            valuePatten = bundle.getString(EntryExtraData.PARAM_VALUE_PATTERN,"0-4");
//            message = getString(R.string.pls_input_server_id);
//        } else if(TextEntry.ACTION_ENTER_TABLE_NUMBER.equals(action)){
//            valuePatten = bundle.getString(EntryExtraData.PARAM_VALUE_PATTERN,"0-4");
//            message = getString(R.string.pls_input_table_number);
//        } else if(TextEntry.ACTION_ENTER_CS_PHONE_NUMBER.equals(action)){
//            valuePatten = bundle.getString(EntryExtraData.PARAM_VALUE_PATTERN,"1-32");
//            message = getString(R.string.pls_input_cs_phone_number);
//        } else if(TextEntry.ACTION_ENTER_PHONE_NUMBER.equals(action)){
//            valuePatten = bundle.getString(EntryExtraData.PARAM_VALUE_PATTERN,"1-32");
//            message = getString(R.string.pls_input_phone_number);
//        } else if(TextEntry.ACTION_ENTER_GUEST_NUMBER.equals(action)){
//            valuePatten = bundle.getString(EntryExtraData.PARAM_VALUE_PATTERN,"0-4");
//            message = getString(R.string.pls_input_guest_number);
//        } else if(TextEntry.ACTION_ENTER_MERCHANT_TAX_ID.equals(action)){
//            valuePatten = bundle.getString(EntryExtraData.PARAM_VALUE_PATTERN,"0-15");
//            message = getString(R.string.prompt_merchant_tax_id);
//        } else if(TextEntry.ACTION_ENTER_PROMPT_RESTRICTION_CODE.equals(action)){
//            valuePatten = bundle.getString(EntryExtraData.PARAM_VALUE_PATTERN,"2-2");
//            message = getString(R.string.pls_input_prompt_restriction_code);
//        }else if(TextEntry.ACTION_ENTER_TRANS_NUMBER.equals(action)){
//            valuePatten = bundle.getString(EntryExtraData.PARAM_VALUE_PATTERN,"1-4");
//            message = getString(R.string.pls_input_transaction_number);
//        }
//
//        if(!TextUtils.isEmpty(valuePatten)){
//            minLength = ValuePatternUtils.getMinLength(valuePatten);
//            maxLength = ValuePatternUtils.getMaxLength(valuePatten);
//        }
//
//    }
