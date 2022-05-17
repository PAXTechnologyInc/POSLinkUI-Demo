package com.paxus.pay.poslinkui.demo.entry.security;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.pax.us.pay.ui.constant.entry.EntryExtraData;
import com.pax.us.pay.ui.constant.entry.EntryRequest;
import com.pax.us.pay.ui.constant.entry.SecurityEntry;
import com.pax.us.pay.ui.constant.entry.enumeration.TransMode;
import com.pax.us.pay.ui.constant.entry.enumeration.VCodeName;
import com.pax.us.pay.ui.constant.status.SecurityStatus;
import com.paxus.pay.poslinkui.demo.R;
import com.paxus.pay.poslinkui.demo.entry.BaseEntryFragment;
import com.paxus.pay.poslinkui.demo.utils.EntryRequestUtils;
import com.paxus.pay.poslinkui.demo.utils.Logger;
import com.paxus.pay.poslinkui.demo.utils.ViewUtils;

/**
 * Implement security entry actions:<br>
 * {@value SecurityEntry#ACTION_ENTER_VCODE}<br>
 * {@value SecurityEntry#ACTION_ENTER_CARD_LAST_4_DIGITS}<br>
 * {@value SecurityEntry#ACTION_ENTER_CARD_ALL_DIGITS}<br>
 */
public class SecurityFragment extends BaseEntryFragment {
    private String transType;
    private long timeOut;
    private int minLength;
    private int maxLength;
    private String message = "";
    private String transMode;

    private Button confirmButton;
    private int secureLength;
    private BroadcastReceiver receiver;

    public static SecurityFragment newInstance(Intent intent){
        SecurityFragment numFragment = new SecurityFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EntryRequest.PARAM_ACTION, intent.getAction());
        bundle.putAll(intent.getExtras());
        numFragment.setArguments(bundle);
        return numFragment;
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_security;
    }

    @Override
    protected void loadArgument(@NonNull Bundle bundle) {
        action = bundle.getString(EntryRequest.PARAM_ACTION);
        packageName = bundle.getString(EntryExtraData.PARAM_PACKAGE);
        transType = bundle.getString(EntryExtraData.PARAM_TRANS_TYPE);
        transMode = bundle.getString(EntryExtraData.PARAM_TRANS_MODE);
        timeOut = bundle.getLong(EntryExtraData.PARAM_TIMEOUT,30000);

        String valuePatten = "";
        if(SecurityEntry.ACTION_ENTER_VCODE.equals(action)){
            valuePatten = bundle.getString(EntryExtraData.PARAM_VALUE_PATTERN,"3-4");
            message = getString(R.string.pls_input_vcode);
            String vcodeName = bundle.getString(EntryExtraData.PARAM_VCODE_NAME);
            if(!TextUtils.isEmpty(vcodeName)) {
                if (VCodeName.CVV2.equals(vcodeName)) {
                    message = getString(R.string.pls_input_cvv2);
                } else if (VCodeName.CAV2.equals(vcodeName)) {
                    message = getString(R.string.pls_input_cav2);
                } else if (VCodeName.CID.equals(vcodeName)) {
                    message = getString(R.string.pls_input_cid);
                } else {
                    message = vcodeName;
                    Logger.e("unknown vcode name:"+vcodeName);
                }
            }
        } else if(SecurityEntry.ACTION_ENTER_CARD_LAST_4_DIGITS.equals(action)){
            valuePatten = bundle.getString(EntryExtraData.PARAM_VALUE_PATTERN,"4-4");
            message = getString(R.string.prompt_input_4digit);
        } else if(SecurityEntry.ACTION_ENTER_CARD_ALL_DIGITS.equals(action)){
            valuePatten = bundle.getString(EntryExtraData.PARAM_VALUE_PATTERN,"0-19");
            message = getString(R.string.prompt_input_all_digit);
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

        EditText editText = rootView.findViewById(R.id.edit_security);
        ViewTreeObserver observer = editText.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                editText.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                if(Build.MODEL.equals("A35")){
                    new Handler().postDelayed(()-> {
                        sendSecureArea(editText);
                    },100);
                }else{
                    sendSecureArea(editText);
                }
            }
        });

        confirmButton = rootView.findViewById(R.id.confirm_button);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EntryRequestUtils.sendNext(requireContext(),packageName,action);
            }
        });
        receiver = new SecurityFragment.Receiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addCategory(SecurityStatus.CATEGORY);
        intentFilter.addAction(SecurityStatus.SECURITY_ENTER_CLEARED);
        intentFilter.addAction(SecurityStatus.SECURITY_ENTERING);
        intentFilter.addAction(SecurityStatus.SECURITY_ENTER_DELETE);

        requireContext().registerReceiver(receiver,intentFilter);
    }
    private void sendSecureArea(EditText editText){
        int[] location = new int[2];
        editText.getLocationInWindow(location);
        int x = location[0];
        int y = location[1];
        int barHeight = 0;
        boolean immersiveSticky = (requireActivity().getWindow().getDecorView().getSystemUiVisibility() &
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY) > 0;
        if (!immersiveSticky) {
            //area of application
            Rect outRect1 = new Rect();
            requireActivity().getWindow().getDecorView().getWindowVisibleDisplayFrame(outRect1);
            barHeight = outRect1.top;  //statusBar's height
        }
        int fontSize = (int)editText.getTextSize();
        EntryRequestUtils.sendSecureArea(requireContext(), packageName, action, x, y - barHeight, editText.getWidth(), editText.getHeight(), fontSize,
                "",
                "FF9C27B0");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if(receiver != null){
            requireContext().unregisterReceiver(receiver);
        }
    }

    private class Receiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Logger.i("receive Status Action \""+intent.getAction()+"\"");
            switch (intent.getAction()) {
                case SecurityStatus.SECURITY_ENTER_CLEARED:{
                    secureLength = 0;
                    break;
                }
                case SecurityStatus.SECURITY_ENTERING:{
                    secureLength++;
                    break;
                }
                case SecurityStatus.SECURITY_ENTER_DELETE: {
                    secureLength--;
                    break;
                }
                default:
                    break;
            }

            if(confirmButton!=null) {
                confirmButton.setEnabled(secureLength > 0);
            }

        }
    }
}
