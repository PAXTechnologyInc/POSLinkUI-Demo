package com.paxus.pay.poslinkui.demo.entry.security;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextPaint;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.pax.us.pay.ui.constant.entry.SecurityEntry;
import com.pax.us.pay.ui.constant.status.SecurityStatus;
import com.pax.us.pay.ui.constant.status.StatusData;
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
 * UI Tips:
 * 1.When input box layout ready, send secure area location (Done on ViewTreeObserver.OnGlobalLayoutListener)
 * 2.When confirm button clicked, sendNext
 * 3.Update confirm button status when received SecurityStatus
 * 4.Update view according keyboard location if you need
 * </p>
 */
public abstract class ASecurityFragment extends BaseEntryFragment {

    protected TextView editText;
    protected Button confirmButton;
    protected int secureLength;
    protected BroadcastReceiver receiver;
    private View mContentView;
    private int mOrigWidth;
    private int mOrigHeight;

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
                mContentView = requireActivity().getWindow().findViewById(Window.ID_ANDROID_CONTENT);
                if ("Q10A".equals(Build.MODEL)) {
                    editText.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                        @Override
                        public void onLayoutChange(View view, int left, int top, int right, int bottom,
                                                   int oldLeft, int oldTop, int oldRight, int oldBottom) {
                            Logger.d("Security EditText onLayoutChange:" + left + "," + top + "," + right + "," + bottom + "," + oldLeft + "," + oldTop + "," + oldRight + "," + oldBottom);
                            if (right != oldRight) {
                                onInputBoxLayoutReady();
                            }
                        }
                    });
                }
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
        intentFilter.addAction(SecurityStatus.SECURITY_KEYBOARD_LOCATION);

        requireContext().registerReceiver(receiver, intentFilter);
    }

    protected abstract String formatMessage();

    //1.When input box layout ready, send secure area location
    protected void onInputBoxLayoutReady() {
        if (Build.MODEL.equals("A35")) {
            new Handler().postDelayed(() -> {
                sendSecurityArea(editText);
            }, 100);
        } else {
            sendSecurityArea(editText);
        }
    }

    @Override
    protected void onConfirmButtonClicked() {
        sendNext(null);
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
            Logger.intent(intent, "STATUS BROADCAST:\t" + intent.getAction());
            switch (intent.getAction()) {
                case SecurityStatus.SECURITY_ENTER_CLEARED:{
                    secureLength = 0;
                    break;
                }
                case SecurityStatus.SECURITY_ENTERING: {
                    secureLength++;
                    break;
                }
                case SecurityStatus.SECURITY_ENTER_DELETE: {
                    secureLength--;
                    break;
                }
                case SecurityStatus.SECURITY_KEYBOARD_LOCATION: {
                    if ("Q10A".equals(Build.MODEL)) {
                        //4.Update view according keyboard location if you need
                        Bundle extra = intent.getExtras();
                        if (extra != null) {
                            int x = extra.getInt(StatusData.PARAM_X);
                            int y = extra.getInt(StatusData.PARAM_Y);
                            int width = extra.getInt(StatusData.PARAM_WIDTH);
                            int height = extra.getInt(StatusData.PARAM_HEIGHT);

                            Logger.d("SECURITY_KEYBOARD_LOCATION:" + x + "," + y + "," + width + "," + height);
                            if (x > 0) {
                                ViewGroup.LayoutParams params = mContentView.getLayoutParams();
                                mOrigWidth = mContentView.getMeasuredWidth();
                                mOrigHeight = mContentView.getMeasuredHeight();

                                params.height = mOrigHeight;
                                params.width = mOrigWidth - width;
                                mContentView.setLayoutParams(params);
                                mContentView.requestLayout();
                            } else {
                                ViewGroup.LayoutParams params = mContentView.getLayoutParams();
                                params.height = mOrigHeight;
                                params.width = mOrigWidth;
                                mContentView.setLayoutParams(params);
                                mContentView.requestLayout();
                            }
                        }
                        break;
                    }
                }
                default:
                    break;
            }

        }
    }
}


