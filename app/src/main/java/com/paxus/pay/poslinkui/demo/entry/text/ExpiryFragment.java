package com.paxus.pay.poslinkui.demo.entry.text;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.pax.us.pay.ui.constant.entry.EntryExtraData;
import com.pax.us.pay.ui.constant.entry.EntryRequest;
import com.pax.us.pay.ui.constant.entry.TextEntry;
import com.paxus.pay.poslinkui.demo.R;
import com.paxus.pay.poslinkui.demo.entry.BaseEntryFragment;
import com.paxus.pay.poslinkui.demo.utils.EntryRequestUtils;

/**
 * Implement text entry action {@value TextEntry#ACTION_ENTER_EXPIRY_DATE}<br>
 * <p>
 *     UI Tips:
 *     If confirm button clicked, sendNext
 * </p>
 */
public class ExpiryFragment extends BaseEntryFragment {

    private String transType;
    private String transMode;

    private long timeOut;
    private String message = "";

    private EditText editText;
    public static Fragment newInstance(Intent intent){
        ExpiryFragment fragment = new ExpiryFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EntryRequest.PARAM_ACTION, intent.getAction());
        bundle.putAll(intent.getExtras());
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_expiry;
    }

    @Override
    protected void loadArgument(@NonNull Bundle bundle) {
        action = bundle.getString(EntryRequest.PARAM_ACTION);
        packageName = bundle.getString(EntryExtraData.PARAM_PACKAGE);
        transType = bundle.getString(EntryExtraData.PARAM_TRANS_TYPE);
        transMode = bundle.getString(EntryExtraData.PARAM_TRANS_MODE);
        timeOut = bundle.getLong(EntryExtraData.PARAM_TIMEOUT,30000);

        message = getString(R.string.pls_input_expiry_date);

    }

    @Override
    protected void loadView(View rootView) {
        

        

        TextView textView = rootView.findViewById(R.id.message);
        textView.setText(message);

        editText = rootView.findViewById(R.id.edit_expiry);
        editText.setSelection(editText.getEditableText().length());

        editText.addTextChangedListener(new TextWatcher() {
            protected boolean mEditing;
            protected String mPreStr;

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
                    if(sb.length() > 4){
                        s.replace(0, s.length(), mPreStr);
                    }else {
                        if(sb.length() >= 2){
                            if(sb.length() == 2 && mPreStr.equals(sb+"/")){
                                sb.delete(sb.length()-1,sb.length());
                            }else {
                                sb.insert(2, "/");
                            }
                        }
                        s.replace(0, s.length(), sb.toString());
                    }
                    mEditing = false;
                }
            }
        });

        Button confirmBtn = rootView.findViewById(R.id.confirm_button);
        confirmBtn.setOnClickListener( v -> onConfirmButtonClicked());

    }

    //If confirm button clicked, sendNext
    private void onConfirmButtonClicked(){
        String value = editText.getText().toString();
        value = value.replaceAll("[^0-9]", "");
        if(value.length() == 4){
            sendNext(value);
        }
    }


    private void sendNext(String value){

        String param = EntryRequest.PARAM_EXPIRY_DATE;
        if(!TextUtils.isEmpty(param)){
            EntryRequestUtils.sendNext(requireContext(), packageName, action, param,value);
        }
    }

}
