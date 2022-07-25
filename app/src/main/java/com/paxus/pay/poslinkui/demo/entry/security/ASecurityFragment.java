package com.paxus.pay.poslinkui.demo.entry.security;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.os.Build;
import android.os.Handler;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.pax.us.pay.ui.constant.entry.SecurityEntry;
import com.pax.us.pay.ui.constant.status.SecurityStatus;
import com.paxus.pay.poslinkui.demo.R;
import com.paxus.pay.poslinkui.demo.entry.BaseEntryFragment;
import com.paxus.pay.poslinkui.demo.utils.EntryRequestUtils;
import com.paxus.pay.poslinkui.demo.utils.Logger;

/**
 * Implement security entry actions:<br>
 * {@value SecurityEntry#ACTION_ENTER_VCODE}<br>
 * {@value SecurityEntry#ACTION_ENTER_CARD_LAST_4_DIGITS}<br>
 * {@value SecurityEntry#ACTION_ENTER_CARD_ALL_DIGITS}<br>
 * <p>
 *     UI Tips:
 *     1.When input box layout ready, send secure area location (Done on ViewTreeObserver.OnGlobalLayoutListener)
 *     2.When confirm button clicked, sendNext
 *     3.Update confirm button status when received SecurityStatus
 * </p>
 */
public abstract class ASecurityFragment extends BaseEntryFragment {

    protected EditText editText;
    protected Button confirmButton;
    protected int secureLength;
    protected BroadcastReceiver receiver;
    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_security;
    }

    @Override
    protected void loadView(View rootView) {
        TextView textView = rootView.findViewById(R.id.message);
        textView.setText(formatMessage());

        editText = rootView.findViewById(R.id.edit_security);
        ViewTreeObserver observer = editText.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                editText.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                onInputBoxLayoutReady();
            }
        });

        //Send Next when clicking confirm button
        confirmButton = rootView.findViewById(R.id.confirm_button);
        confirmButton.setOnClickListener(v->onConfirmButtonClicked());
        receiver = new ASecurityFragment.Receiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addCategory(SecurityStatus.CATEGORY);
        intentFilter.addAction(SecurityStatus.SECURITY_ENTER_CLEARED);
        intentFilter.addAction(SecurityStatus.SECURITY_ENTERING);
        intentFilter.addAction(SecurityStatus.SECURITY_ENTER_DELETE);

        requireContext().registerReceiver(receiver, intentFilter);
    }

    protected abstract String formatMessage();

    //1.When input box layout ready, send secure area location
    protected void onInputBoxLayoutReady() {
        if (Build.MODEL.equals("A35")) {
            new Handler().postDelayed(() -> {
                sendSecureArea(editText);
            }, 100);
        } else {
            sendSecureArea(editText);
        }
    }

    protected void sendSecureArea(EditText editText) {
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
        EntryRequestUtils.sendSecureArea(requireContext(), getSenderPackageName(), getEntryAction(), x, y - barHeight, editText.getWidth(), editText.getHeight(), fontSize,
                "",
                "FF9C27B0");
    }

    //2.When confirm button clicked, sendNext
    protected void onConfirmButtonClicked() {
        EntryRequestUtils.sendNext(requireContext(), getSenderPackageName(), getEntryAction());
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

            //3.Update confirm button status
            if(confirmButton!=null) {
                confirmButton.setEnabled(secureLength > 0);
            }

        }
    }
}


