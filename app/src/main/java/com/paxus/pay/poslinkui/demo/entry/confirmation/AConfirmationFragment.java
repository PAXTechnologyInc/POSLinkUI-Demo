package com.paxus.pay.poslinkui.demo.entry.confirmation;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.pax.us.pay.ui.constant.entry.ConfirmationEntry;
import com.pax.us.pay.ui.constant.entry.EntryExtraData;
import com.pax.us.pay.ui.constant.entry.EntryRequest;
import com.paxus.pay.poslinkui.demo.R;
import com.paxus.pay.poslinkui.demo.entry.BaseEntryFragment;

/**
 * Abstract class for all entry actions defined in {@link ConfirmationEntry} <br>
 * except for {@value ConfirmationEntry#ACTION_CONFIRM_SURCHARGE_FEE} and {@value ConfirmationEntry#ACTION_CONFIRM_RECEIPT_VIEW}
 */
public abstract class AConfirmationFragment extends BaseEntryFragment {
    private String message;
    private String[] options;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_confirmation;
    }


    @Override
    protected void loadArgument(@NonNull Bundle bundle){
        message = bundle.getString(EntryExtraData.PARAM_MESSAGE);
        options = bundle.getStringArray(EntryExtraData.PARAM_OPTIONS);
    }

    @Override
    protected void loadView(View rootView) {
        TextView messageTv = rootView.findViewById(R.id.message);
        messageTv.setText(formatMessage(message));

        Button positiveButton = rootView.findViewById(R.id.confirm_button);
        positiveButton.setText(getPositiveText());
        positiveButton.setOnClickListener( v-> onConfirmButtonClicked());

        Button negativeButton = rootView.findViewById(R.id.cancel_button);
        String negativeText = getNegativeText();
        if(!TextUtils.isEmpty(negativeText)) {
            negativeButton.setVisibility(View.VISIBLE);
            negativeButton.setText(negativeText);
            negativeButton.setOnClickListener(v -> onNegativeButtonClicked());
        }
    }

    @Override
    protected void onConfirmButtonClicked() {
        submit(true);
    }

    private void onNegativeButtonClicked() {
        submit(false);
    }

    protected void submit(boolean confirm) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(EntryRequest.PARAM_CONFIRMED, confirm);
        sendNext(bundle);
    }

    protected abstract String formatMessage(String message);

    @NonNull protected String getPositiveText(){
        return (options != null && options.length>0) ? options[0] : getResources().getString(R.string.confirm_option_accept);
    }

    protected String getNegativeText(){
        return (options != null && options.length>1) ? options[1] : null;
    }

}
