package com.paxus.pay.poslinkui.demo.entry.information;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;

import com.pax.us.pay.ui.constant.entry.EntryExtraData;
import com.pax.us.pay.ui.constant.entry.InformationEntry;
import com.pax.us.pay.ui.constant.entry.enumeration.TransactionStatus;
import com.pax.us.pay.ui.constant.status.StatusData;
import com.paxus.pay.poslinkui.demo.R;
import com.paxus.pay.poslinkui.demo.entry.BaseEntryFragment;
import com.paxus.pay.poslinkui.demo.view.TextField;
import com.paxus.pay.poslinkui.demo.viewmodel.SecondScreenInfoViewModel;

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
        // update trans status in second screen.
        viewModel.updateAllData("","", TransactionStatus.APPROVED.name(), null, bundle.getString(StatusData.PARAM_MSG_PRIMARY, ""), "");
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
