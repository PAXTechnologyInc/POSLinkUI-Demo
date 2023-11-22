package com.paxus.pay.poslinkui.demo.entry.security;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.pax.us.pay.ui.constant.entry.EntryExtraData;
import com.pax.us.pay.ui.constant.entry.EntryRequest;
import com.pax.us.pay.ui.constant.entry.SecurityEntry;
import com.pax.us.pay.ui.constant.entry.enumeration.AdminPasswordType;
import com.pax.us.pay.ui.constant.status.SecurityStatus;
import com.paxus.pay.poslinkui.demo.R;
import com.paxus.pay.poslinkui.demo.entry.BaseEntryFragment;
import com.paxus.pay.poslinkui.demo.utils.DeviceUtils;
import com.paxus.pay.poslinkui.demo.utils.ViewUtils;

/**
 * Implement confirmation entry action {@value SecurityEntry#ACTION_ENTER_ADMINISTRATION_PASSWORD}<br>
 * <p>
 * UI Tips:
 * </p>
 */
public class AdministratorPasswordFragment extends BaseEntryFragment {
    private String merchantName;
    private String passwordType;
    private long timeout;

    private TextView inputTextView;

    private SecureKeyboardManager secureKeyboardManager;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_password;
    }

    @Override
    protected void loadArgument(@NonNull Bundle bundle) {
        merchantName = bundle.getString(EntryExtraData.PARAM_MERCHANT_NAME);
        passwordType = bundle.getString(EntryExtraData.PARAM_ADMIN_PASSWORD_TYPE);
        timeout = bundle.getLong(EntryExtraData.PARAM_TIMEOUT);
    }

    @Override
    protected void loadView(View rootView) {
        TextView titleView = rootView.findViewById(R.id.prompt_title);
        titleView.setText(generatePrompt(merchantName, passwordType));

        inputTextView = rootView.findViewById(R.id.pwd_input_text);

        ViewTreeObserver observer = inputTextView.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                inputTextView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                onInputBoxLayoutReady();
            }
        });

        rootView.findViewById(R.id.pwd_confirm_button).setOnClickListener(view -> onConfirmButtonClicked());
    }

    @NonNull
    private String generatePrompt(String merchantName, String passwordType) {
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
        return prompt;
    }

    @Override
    protected void onConfirmButtonClicked() {
        sendNext(null);
    }

    protected void onInputBoxLayoutReady() {
        Runnable securityAreaRequest = () -> {
            Bundle bundle = new Bundle();
            bundle.putAll(ViewUtils.generateInputTextAreaExtras(getActivity(), inputTextView, "Password"));
            bundle.putBoolean(EntryRequest.PARAM_DISABLE_DEFAULT_SECURE_KEYBOARD, true);
            sendRequestBroadcast(EntryRequest.ACTION_SECURITY_AREA, bundle);
        };

        new Handler(Looper.getMainLooper()).postDelayed(securityAreaRequest, DeviceUtils.brodcastProcessDelay());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        secureKeyboardManager = new SecureKeyboardManager(this, R.id.secure_keyboard_container);

        secureKeyboardManager.registerBroadcastHandler(SecurityStatus.READY_FOR_KEYBOARD_LOCATION, intent -> {
            secureKeyboardManager.showKeyboard(SecureKeyboardManager.INPUT_TYPE_ALPHANUMERIC,
                    ()-> sendRequestBroadcast(EntryRequest.ACTION_ACTIVATE_SECURE_KEYBOARD, secureKeyboardManager.getKeyboardLocation()));
        });

        secureKeyboardManager.registerBroadcastHandler(SecurityStatus.SECURE_KEYBOARD_DEACTIVATED, intent -> {
            secureKeyboardManager.processPayload(intent.getStringExtra(EntryRequest.PARAM_SECURE_KEYBOARD_PAYLOAD),
                    ()-> sendRequestBroadcast(EntryRequest.ACTION_ACTIVATE_SECURE_KEYBOARD, secureKeyboardManager.getKeyboardLocation()));
        });

        return view;
    }
}
