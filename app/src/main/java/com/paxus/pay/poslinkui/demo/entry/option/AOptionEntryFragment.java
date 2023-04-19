package com.paxus.pay.poslinkui.demo.entry.option;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.pax.us.pay.ui.constant.entry.EntryExtraData;
import com.pax.us.pay.ui.constant.entry.OptionEntry;
import com.paxus.pay.poslinkui.demo.R;
import com.paxus.pay.poslinkui.demo.entry.BaseEntryFragment;

/**
 * Abstract class for all option entry actions defined in {@link OptionEntry}
 */
public abstract class AOptionEntryFragment extends BaseEntryFragment {

    protected ListView listView;
    private String[] options;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_option_dialog;
    }

    @Override
    protected void loadArgument(@NonNull Bundle bundle) {
        options = bundle.containsKey(EntryExtraData.PARAM_OPTIONS) ? bundle.getStringArray(EntryExtraData.PARAM_OPTIONS) : new String[]{};
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

    protected @NonNull String[] getOptions() {
        return options;
    }

    private void onCancelButtonClicked() {
        sendAbort();
    }

    @Override
    protected void onConfirmButtonClicked() {
        if (listView.getCheckedItemPosition() >= 0) {
            submit(listView.getCheckedItemPosition());
        }
    }

    protected void submit(int index) {
        Bundle bundle = new Bundle();
        bundle.putInt(getRequestedParamName(), index);
        sendNext(bundle);
    }

    protected abstract String getRequestedParamName();

    protected abstract String formatTitle();

}
