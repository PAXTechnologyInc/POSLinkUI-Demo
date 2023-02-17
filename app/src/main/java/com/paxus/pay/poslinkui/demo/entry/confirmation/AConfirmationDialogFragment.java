package com.paxus.pay.poslinkui.demo.entry.confirmation;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.pax.us.pay.ui.constant.entry.ConfirmationEntry;
import com.paxus.pay.poslinkui.demo.R;
import com.paxus.pay.poslinkui.demo.entry.BaseEntryDialogFragment;
import com.paxus.pay.poslinkui.demo.utils.EntryRequestUtils;

/**
 * Abstract class for all entry actions defined in {@link ConfirmationEntry} <br>
 * except for {@value ConfirmationEntry#ACTION_CONFIRM_SURCHARGE_FEE} and {@value ConfirmationEntry#ACTION_CONFIRM_RECEIPT_VIEW}
 */
public abstract class AConfirmationDialogFragment extends BaseEntryDialogFragment {
    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_confirmation_dialog;
    }

    @Override
    protected abstract void loadParameter(@NonNull Bundle bundle);

    @Override
    protected void loadView(View rootView) {
        TextView messageTv = rootView.findViewById(R.id.message);
        messageTv.setText(formatMessage());

        Button positiveButton = rootView.findViewById(R.id.confirm_button);
        String positiveText = getPositiveText();
        if(!TextUtils.isEmpty(positiveText)) {
            positiveButton.setText(positiveText);
            positiveButton.setOnClickListener( v-> onConfirmButtonClicked());
        }else{
            positiveButton.setVisibility(View.GONE);
        }

        Button negativeButton = rootView.findViewById(R.id.cancel_button);
        String negativeText = getNegativeText();
        if(!TextUtils.isEmpty(negativeText)) {
            negativeButton.setText(negativeText);
            negativeButton.setOnClickListener(v -> onNegativeButtonClicked());
        }else{
            negativeButton.setVisibility(View.GONE);
        }

    }

    private void onNegativeButtonClicked() {
        sendNext(false);
    }

    @Override
    protected void onConfirmButtonClicked() {
        sendNext(true);
    }

    protected void sendNext(boolean confirm) {
        EntryRequestUtils.sendNext(requireContext(), getSenderPackageName(), getEntryAction(), getRequestedParamName(), confirm);
    }

    protected abstract @NonNull
    String getRequestedParamName();

    protected abstract String formatMessage();

    protected abstract String getPositiveText();

    protected abstract String getNegativeText();

}
