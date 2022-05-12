package com.paxus.pay.poslinkui.demo.entry.option;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.pax.us.pay.ui.constant.entry.EntryExtraData;
import com.pax.us.pay.ui.constant.entry.EntryRequest;
import com.pax.us.pay.ui.constant.entry.OptionEntry;
import com.paxus.pay.poslinkui.demo.R;
import com.paxus.pay.poslinkui.demo.entry.BaseEntryDialogFragment;
import com.paxus.pay.poslinkui.demo.utils.EntryRequestUtils;

public class OptionsDialogFragment extends BaseEntryDialogFragment {
    private String[] items;
    private ListView listView;

    public static DialogFragment newInstance(Intent intent){
        OptionsDialogFragment dialogFragment = new OptionsDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EntryRequest.PARAM_ACTION, intent.getAction());
        bundle.putAll(intent.getExtras());

        dialogFragment.setArguments(bundle);
        return dialogFragment;
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_option_dialog;
    }

    @Override
    protected void loadParameter(@NonNull Bundle bundle) {
        action = bundle.getString(EntryRequest.PARAM_ACTION);
        packageName = bundle.getString(EntryExtraData.PARAM_PACKAGE);
        items = bundle.getStringArray(EntryExtraData.PARAM_OPTIONS);
    }

    @Override
    protected void loadView(View rootView) {
        TextView titleView = rootView.findViewById(R.id.title_view);
        titleView.setText(formatTitle(action));

        listView = rootView.findViewById(R.id.list_view);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(requireContext(),
                android.R.layout.simple_list_item_single_choice, items);
        listView.setAdapter(adapter);

        Button cancelButton = rootView.findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendAbort();
            }
        });

        Button confirmButton = rootView.findViewById(R.id.confirm_button);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listView.getCheckedItemPosition()>= 0 ){
                    sendNext(listView.getCheckedItemPosition());
                }
            }
        });
    }


    private String formatTitle(String action){
        String title = "";

        if(OptionEntry.ACTION_SELECT_AID.equals(action)){
            title = requireContext().getString(R.string.select_emv_aid);
        }else if(OptionEntry.ACTION_SELECT_SUB_TRANS_TYPE.equals(action)){
            title = requireContext().getString(R.string.select_sub_trans_type);
        }else if(OptionEntry.ACTION_SELECT_EBT_TYPE.equals(action)){
            title = requireContext().getString(R.string.select_ebt_type);
        }else if(OptionEntry.ACTION_SELECT_BY_PASS.equals(action)){
            title = requireContext().getString(R.string.select_bypass_reason);
        }else if(OptionEntry.ACTION_SELECT_REFUND_REASON.equals(action)){
            title = requireContext().getString(R.string.select_refund_reason);
        }else if(OptionEntry.ACTION_SELECT_MOTO_TYPE.equals(action)){
            title = requireContext().getString(R.string.select_moto_type);
        }else if(OptionEntry.ACTION_SELECT_CARD_TYPE.equals(action)){
            title = requireContext().getString(R.string.select_card_type);
        }else if(OptionEntry.ACTION_SELECT_TAX_REASON.equals(action)){
            title = requireContext().getString(R.string.select_tax_reason);
        }else if(OptionEntry.ACTION_SELECT_DUPLICATE_OVERRIDE.equals(action)){
            title = requireContext().getString(R.string.select_duplicate_override);
        }else if(OptionEntry.ACTION_SELECT_TRANS_TYPE.equals(action)){
            title = requireContext().getString(R.string.select_trans_type);
        }else if(OptionEntry.ACTION_SELECT_ACCOUNT_TYPE.equals(action)){
            title = requireContext().getString(R.string.select_account_type);
        }else if(OptionEntry.ACTION_SELECT_BATCH_TYPE.equals(action)){
            title = requireContext().getString(R.string.select_batch_menu);
        }else if(OptionEntry.ACTION_SELECT_SEARCH_CRITERIA.equals(action)){
            title = requireContext().getString(R.string.select_search_type);
        }else if(OptionEntry.ACTION_SELECT_REPORT_TYPE.equals(action)){
            title = requireContext().getString(R.string.select_report_type);
        }else if(OptionEntry.ACTION_SELECT_EDC_TYPE.equals(action)){
            title = requireContext().getString(R.string.select_edc_type);
        }else if(OptionEntry.ACTION_SELECT_EDC_GROUP.equals(action)){
            title = requireContext().getString(R.string.select_edc_type);
        }else if(OptionEntry.ACTION_SELECT_LANGUAGE.equals(action)){
            title = requireContext().getString(R.string.select_language);
        }else if(OptionEntry.ACTION_SELECT_MERCHANT.equals(action)){
            title = requireContext().getString(R.string.select_merchant);
        }else if(OptionEntry.ACTION_SELECT_ORIG_CURRENCY.equals(action)){
            title = requireContext().getString(R.string.select_orig_currency);
        }
        return title;
    }

    private void sendNext(int index){
        EntryRequestUtils.sendNext(requireContext(), packageName, action,
                EntryRequest.PARAM_INDEX, index);
    }
}
