package com.paxus.pay.poslinkui.demo.entry.option;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.pax.us.pay.ui.constant.entry.EntryExtraData;
import com.pax.us.pay.ui.constant.entry.EntryRequest;
import com.pax.us.pay.ui.constant.entry.OptionEntry;
import com.paxus.pay.poslinkui.demo.R;
import com.paxus.pay.poslinkui.demo.utils.EntryRequestUtils;

public class OptionsDialogFragment extends DialogFragment {
    private String action;
    private String packageName;
    private int selectedItem = -1;

    public static DialogFragment newInstance(Intent intent){
        OptionsDialogFragment dialogFragment = new OptionsDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EntryRequest.PARAM_ACTION, intent.getAction());
        bundle.putAll(intent.getExtras());

        dialogFragment.setArguments(bundle);
        return dialogFragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());

        Bundle bundle = getArguments();

        if(bundle!= null) {
            String[] items = bundle.getStringArray(EntryExtraData.PARAM_OPTIONS);
            String title = "";
            action = bundle.getString(EntryRequest.PARAM_ACTION);
            packageName = bundle.getString(EntryExtraData.PARAM_PACKAGE);

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
            builder.setTitle(title);

            builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    selectedItem = i;
                }
            });

            builder.setNegativeButton(requireContext().getString(R.string.dialog_cancel), null);
            builder.setPositiveButton(requireContext().getString(R.string.confirm), null);
        }

        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                ((AlertDialog)dialogInterface).getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(selectedItem>= 0 ){
                            EntryRequestUtils.sendNext(requireContext(), packageName, action,
                                    EntryRequest.PARAM_INDEX, selectedItem);
                            dialogInterface.cancel();
                        }
                    }
                });
                ((AlertDialog)dialogInterface).getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        EntryRequestUtils.sendAbort(requireContext(), packageName, action);
                        dialogInterface.cancel();
                    }
                });
            }
        });

        return dialog;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

}
