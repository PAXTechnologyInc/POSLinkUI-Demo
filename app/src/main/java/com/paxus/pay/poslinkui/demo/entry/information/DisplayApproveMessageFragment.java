package com.paxus.pay.poslinkui.demo.entry.information;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.pax.us.pay.ui.constant.entry.EntryExtraData;
import com.pax.us.pay.ui.constant.entry.InformationEntry;
import com.paxus.pay.poslinkui.demo.R;
import com.paxus.pay.poslinkui.demo.entry.BaseEntryFragment;
import com.paxus.pay.poslinkui.demo.utils.Toast;
import com.paxus.pay.poslinkui.demo.view.TextField;

/**
 * Implement information entry action {@value InformationEntry#ACTION_DISPLAY_APPROVE_MESSAGE}
 */
public class DisplayApproveMessageFragment extends BaseEntryFragment {

    private String cardType;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_display_approve_message;
    }

    @Override
    protected void loadArgument(@NonNull Bundle bundle) {
        cardType = bundle.getString(EntryExtraData.PARAM_CARD_TYPE);
    }

    @Override
    protected void loadView(View rootView) {
        new DisplayApprovalUtils().getApprovalStrategy(cardType).displayApproval(getContext(), (ConstraintLayout) rootView, cardType, () -> sendNext(null));
    }

    @Override
    protected TextField[] focusableTextFields() {
        return null;
    }

}
