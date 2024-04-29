package com.paxus.pay.poslinkui.demo.entry.option;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.pax.us.pay.ui.constant.entry.EntryExtraData;
import com.pax.us.pay.ui.constant.entry.OptionEntry;
import com.paxus.pay.poslinkui.demo.R;
import com.paxus.pay.poslinkui.demo.entry.BaseEntryFragment;
import com.paxus.pay.poslinkui.demo.view.SelectOptionsView;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract class for all option entry actions defined in {@link OptionEntry}
 */
public abstract class AOptionEntryFragment extends BaseEntryFragment {

    protected SelectOptionsView selectOptionsView;
    private List<SelectOptionsView.Option> options;
    private int selectedIndex = -1;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_option_dialog;
    }

    @Override
    protected void loadArgument(@NonNull Bundle bundle) {
        options = generateOptions(bundle);
    }


    protected List<SelectOptionsView.Option> generateOptions(Bundle bundle) {
        List<SelectOptionsView.Option> options = new ArrayList<>();
        String[] tempOptions = bundle.containsKey(EntryExtraData.PARAM_OPTIONS) ? bundle.getStringArray(EntryExtraData.PARAM_OPTIONS) : new String[]{};
        for(int i=0; i<tempOptions.length; i++){
            options.add(new SelectOptionsView.Option(i, tempOptions[i], null, i));
        }
        return options;
    }

    @Override
    protected void loadView(View rootView) {
        TextView titleView = rootView.findViewById(R.id.title_view);
        titleView.setText(formatTitle());

        selectOptionsView = rootView.findViewById(R.id.list_view);
        selectOptionsView.initialize(getActivity(), 1, options, optionSelectCallback);
    }

    private SelectOptionsView.OptionSelectListener optionSelectCallback = option -> {
        this.selectedIndex = (int) option.getValue();
        onConfirmButtonClicked();
    };

    @Override
    protected void onConfirmButtonClicked() {
        if (selectedIndex>=0 && selectedIndex<options.size()) {
            submit(selectedIndex);
        } else {
            Toast.makeText(requireContext(), "No option selected.", Toast.LENGTH_SHORT).show();
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
