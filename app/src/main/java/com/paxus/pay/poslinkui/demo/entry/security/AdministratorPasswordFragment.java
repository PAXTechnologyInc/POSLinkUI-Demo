package com.paxus.pay.poslinkui.demo.entry.security;

import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.pax.us.pay.ui.constant.entry.EntryExtraData;
import com.pax.us.pay.ui.constant.entry.EntryRequest;
import com.pax.us.pay.ui.constant.entry.SecurityEntry;
import com.pax.us.pay.ui.constant.entry.enumeration.AdminPasswordType;
import com.paxus.pay.poslinkui.demo.R;
import com.paxus.pay.poslinkui.demo.entry.BaseEntryFragment;
import com.paxus.pay.poslinkui.demo.utils.EntryRequestUtils;

import java.util.Locale;

/**
 * Implement confirmation entry action {@value SecurityEntry#ACTION_ENTER_ADMINISTRATION_PASSWORD}<br>
 * <p>
 * UI Tips:
 * </p>
 */
public class AdministratorPasswordFragment extends BaseEntryFragment {
    private String action;
    private String packageName;
    private String merchantName;
    private String passwordType;
    private long timeout;
    private String transType;

    private TextView inputTextView;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_password;
    }

    @Override
    protected void loadArgument(@NonNull Bundle bundle) {
        action = bundle.getString(EntryRequest.PARAM_ACTION);
        packageName = bundle.getString(EntryExtraData.PARAM_PACKAGE);
        merchantName = bundle.getString(EntryExtraData.PARAM_MERCHANT_NAME);
        passwordType = bundle.getString(EntryExtraData.PARAM_ADMIN_PASSWORD_TYPE);
        timeout = bundle.getLong(EntryExtraData.PARAM_TIMEOUT);
        transType = bundle.getString(EntryExtraData.PARAM_TRANS_TYPE);
    }

    @Override
    protected void loadView(View rootView) {
        TextView titleView = rootView.findViewById(R.id.prompt_title);
        String prompt = getString(R.string.enter_pwd);

        if (AdminPasswordType.MANAGER.equals(passwordType)) {
            prompt = getString(R.string.prompt_manager_pwd);
        } else if (AdminPasswordType.OPERATOR.equals(passwordType)) {
            prompt = getString(R.string.prompt_operator_pwd);
            if (!TextUtils.isEmpty(merchantName)) {
                prompt = getString(R.string.prompt_merchant_pwd, merchantName);
            }
        } else {
            if (!TextUtils.isEmpty(merchantName)) {
                prompt = getString(R.string.prompt_merchant_pwd, merchantName);
            }
        }
        titleView.setText(prompt);

        inputTextView = rootView.findViewById(R.id.pwd_input_text);
        ViewTreeObserver observer = inputTextView.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                inputTextView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                onInputBoxLayoutReady();
            }
        });

    }

    @Override
    protected String getSenderPackageName() {
        return packageName;
    }

    @Override
    protected String getEntryAction() {
        return action;
    }


    //1.When input box layout ready, send secure area location
    protected void onInputBoxLayoutReady() {
        if (Build.MODEL.equals("A35")) {
            new Handler().postDelayed(() -> {
                sendSecureArea(inputTextView);
            }, 100);
        } else {
            sendSecureArea(inputTextView);
        }
    }

    protected void sendSecureArea(TextView textView) {
        int[] location = new int[2];
        textView.getLocationInWindow(location);
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
        TextPaint paint = textView.getPaint();
        int fontSize = (int) (paint.getTextSize() / paint.density);
        EntryRequestUtils.sendSecureArea(requireContext(), getSenderPackageName(), getEntryAction(), x, y - barHeight, textView.getWidth(), textView.getHeight(), fontSize,
                "",
                String.format(Locale.US, "%08X", textView.getCurrentTextColor()));
    }


}
