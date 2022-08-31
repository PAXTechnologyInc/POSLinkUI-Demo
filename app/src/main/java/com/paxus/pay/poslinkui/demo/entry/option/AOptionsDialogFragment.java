package com.paxus.pay.poslinkui.demo.entry.option;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.pax.us.pay.ui.constant.entry.OptionEntry;
import com.paxus.pay.poslinkui.demo.R;
import com.paxus.pay.poslinkui.demo.entry.BaseEntryDialogFragment;
import com.paxus.pay.poslinkui.demo.utils.EntryRequestUtils;

/**
 * Abstract class for all option entry actions defined in {@link OptionEntry}
 * <p>
 * UI Tips:
 * 1.If confirm button clicked, sendNext
 * </p>
 */
public abstract class AOptionsDialogFragment extends BaseEntryDialogFragment {

    protected ListView listView;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_option_dialog;
    }

    @Override
    protected void loadView(View rootView) {
        TextView titleView = rootView.findViewById(R.id.title_view);
        titleView.setText(formatTitle());

        listView = rootView.findViewById(R.id.list_view);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(requireContext(),
                android.R.layout.simple_list_item_single_choice, getOptions());
        listView.setAdapter(adapter);

        Button cancelButton = rootView.findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(v-> onCancelButtonClicked());

        Button confirmButton = rootView.findViewById(R.id.confirm_button);
        confirmButton.setOnClickListener(v->onConfirmButtonClicked());
    }

    private void onCancelButtonClicked() {
        sendAbort();
    }

    private void onConfirmButtonClicked() {
        if (listView.getCheckedItemPosition() >= 0) {
            sendNext(listView.getCheckedItemPosition());
        }
    }

    protected void sendNext(int index) {
        EntryRequestUtils.sendNext(requireContext(), getSenderPackageName(), getEntryAction(), getRequestedParamName(), index);
    }

    protected abstract String getRequestedParamName();

    protected abstract @NonNull
    String[] getOptions();

    protected abstract String formatTitle();

    @Override
    protected void implementEnterKeyEvent(){
        onConfirmButtonClicked();
    }
}
