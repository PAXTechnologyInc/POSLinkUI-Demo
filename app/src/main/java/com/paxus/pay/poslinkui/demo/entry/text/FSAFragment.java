package com.paxus.pay.poslinkui.demo.entry.text;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.fragment.app.FragmentTransaction;

import com.pax.us.pay.ui.constant.entry.EntryExtraData;
import com.pax.us.pay.ui.constant.entry.EntryRequest;
import com.pax.us.pay.ui.constant.entry.enumeration.CurrencyType;
import com.pax.us.pay.ui.constant.entry.enumeration.FSAType;
import com.pax.us.pay.ui.constant.entry.enumeration.TransMode;
import com.paxus.pay.poslinkui.demo.R;
import com.paxus.pay.poslinkui.demo.entry.BaseEntryFragment;
import com.paxus.pay.poslinkui.demo.utils.Logger;
import com.paxus.pay.poslinkui.demo.utils.ViewUtils;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Yanina.Yang on 5/19/2022.
 */
public class FSAFragment extends BaseEntryFragment {
    private String transType;
    private String transMode;
    private long timeOut;
    private String currency;
    private long totalAmount;
    private List<String> fsaAmountOptions;

    private String fsaOption = "";
    private long healthAmt = 0;
    private long clinicAmt = 0;
    private long prescriptionAmt = 0;
    private long dentalAmt = 0;
    private long visionAmt = 0;
    private long copayAmt = 0;
    private long otcAmt = 0;
    private long transitAmt = 0;

    private int amtIndex = 0;

    public static FSAFragment newInstance(Intent intent){
        FSAFragment numFragment = new FSAFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EntryRequest.PARAM_ACTION, intent.getAction());
        bundle.putAll(intent.getExtras());
        numFragment.setArguments(bundle);
        return numFragment;
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_fsa;
    }

    @Override
    protected void loadArgument(@NonNull Bundle bundle) {
        action = bundle.getString(EntryRequest.PARAM_ACTION);
        packageName = bundle.getString(EntryExtraData.PARAM_PACKAGE);
        transType = bundle.getString(EntryExtraData.PARAM_TRANS_TYPE);
        transMode = bundle.getString(EntryExtraData.PARAM_TRANS_MODE);
        timeOut = bundle.getLong(EntryExtraData.PARAM_TIMEOUT,30000);
        currency =  bundle.getString(EntryExtraData.PARAM_CURRENCY, CurrencyType.USD);
        totalAmount = bundle.getLong(EntryExtraData.PARAM_TOTAL_AMOUNT);
        String[] options = bundle.getStringArray(EntryExtraData.PARAM_FSA_AMOUNT_OPTIONS);
        if(options != null) {
            fsaAmountOptions = Arrays.asList(options);
        }
    }

    @Override
    protected void loadView(View rootView) {
        if(!TextUtils.isEmpty(transType) && getActivity() instanceof AppCompatActivity){
            ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
            if(actionBar != null) {
                actionBar.setTitle(transType);
            }
        }

        String mode = null;
        if(!TextUtils.isEmpty(transMode)){
            if(TransMode.DEMO.equals(transMode)){
                mode = getString(R.string.demo_only);
            }else if(TransMode.TEST.equals(transMode)){
                mode = getString(R.string.test_only);
            }else if(TransMode.TEST_AND_DEMO.equals(transMode)){
                mode = getString(R.string.test_and_demo);
            }else {
                mode = "";
            }
        }
        if(!TextUtils.isEmpty(mode)){
            ViewUtils.addWaterMarkView(requireActivity(),mode);
        }else{
            ViewUtils.removeWaterMarkView(requireActivity());
        }

        if(fsaAmountOptions != null && fsaAmountOptions.size()>0){
            boolean healthCareEnabled = fsaAmountOptions.contains(EntryRequest.PARAM_HEALTH_CARE_AMOUNT);
            boolean transitEnabled = fsaAmountOptions.contains(EntryRequest.PARAM_TRANSIT_AMOUNT);
            if( healthCareEnabled && transitEnabled){
                selectFsaType();
            }else if(transitEnabled){
                enterTransitAmount();
            }else if(healthCareEnabled){
                selectHealthCareSubType();
            }
        }else {
            Logger.e("No fsa amount options.");
            sendAbort();
        }
    }

    private void selectFsaType(){

        String[] fsaTypes = new String[]{FSAType.TRANSIT, FSAType.HEALTH_CARE};
        DialogFragment dialogFragment = GeneralOptionsDialogFragment.newInstance(
                getString(R.string.select_fsa_type),
                fsaTypes
        );
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_placeholder, dialogFragment).commit();
        getChildFragmentManager().beginTransaction();

        getChildFragmentManager()
                .setFragmentResultListener(GeneralOptionsDialogFragment.RESULT, this, new FragmentResultListener() {
                    @Override
                    public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                        int index = bundle.getInt(GeneralOptionsDialogFragment.INDEX);
                        fsaOption = fsaTypes[index];
                        if(FSAType.TRANSIT.equals(fsaOption)){
                            enterTransitAmount();
                        }else {
                            selectHealthCareSubType();
                        }
                    }
                });
    }

    private void enterTransitAmount(){
        Fragment fragment = GeneralAmountFragment.newInstance(
                getString(R.string.prompt_input_transit_amount),0,9,currency
        );
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_placeholder, fragment).commit();
        getChildFragmentManager()
                .setFragmentResultListener(GeneralAmountFragment.RESULT, this, new FragmentResultListener() {
                    @Override
                    public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                        transitAmt = bundle.getLong(GeneralAmountFragment.VALUE);
                        sendNext();
                    }
                });
    }

    private void selectHealthCareSubType(){
        String[] healthCareTypes = new String[]{getString(R.string.healthcare_total),
                getString(R.string.healthcare_sub_type)};
        DialogFragment dialogFragment = GeneralOptionsDialogFragment.newInstance(
                getString(R.string.select_health_sub_types),
                healthCareTypes
        );
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_placeholder, dialogFragment).commit();
        getChildFragmentManager().beginTransaction();

        getChildFragmentManager()
                .setFragmentResultListener(GeneralOptionsDialogFragment.RESULT, this, new FragmentResultListener() {
                    @Override
                    public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                        int index = bundle.getInt(GeneralOptionsDialogFragment.INDEX);
                        fsaOption = FSAType.HEALTH_CARE;
                        if(index == 0){
                            enterTotalHealthCareAmount();
                        }else {
                            enterSubHealthCareAmount();
                        }
                    }
                });
    }

    private void enterTotalHealthCareAmount(){
        Fragment fragment = GeneralAmountFragment.newInstance(
                getString(R.string.prompt_input_healthcare_amount),0,9,currency
        );
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_placeholder, fragment).commit();
        getChildFragmentManager()
                .setFragmentResultListener(GeneralAmountFragment.RESULT, this, new FragmentResultListener() {
                    @Override
                    public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                        healthAmt = bundle.getLong(GeneralAmountFragment.VALUE);
                        sendNext();
                    }
                });
    }

    private void enterSubHealthCareAmount(){
        if(amtIndex>=0 && amtIndex< fsaAmountOptions.size()){
            String amtOption = fsaAmountOptions.get(amtIndex);
            if(!EntryRequest.PARAM_TRANSIT_AMOUNT.equals(amtOption) && !EntryRequest.PARAM_HEALTH_CARE_AMOUNT.equals(amtOption)){
                Fragment fragment = GeneralAmountFragment.newInstance(
                        getSubHealthCareAmountTitle(amtOption),0,9,currency
                );
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                transaction.add(R.id.fragment_placeholder, fragment).commit();
                getChildFragmentManager()
                        .setFragmentResultListener(GeneralAmountFragment.RESULT, this, new FragmentResultListener() {
                            @Override
                            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                               onGetSubHealthCareAmount(amtOption, bundle.getLong(GeneralAmountFragment.VALUE));
                            }
                        });
                return;
            }
        }
        healthAmt = clinicAmt + prescriptionAmt + dentalAmt + visionAmt + copayAmt + otcAmt;
        sendNext();
    }

    private String getSubHealthCareAmountTitle(String amtOption){
        if(EntryRequest.PARAM_CLINIC_AMOUNT.equals(amtOption)){
            return getString(R.string.prompt_input_healthcare_amount);
        }else if(EntryRequest.PARAM_PRESCRIPTION_AMOUNT.equals(amtOption)){
            return getString(R.string.prompt_input_rx_amount);
        }else if(EntryRequest.PARAM_DENTAL_AMOUNT.equals(amtOption)){
            return getString(R.string.prompt_input_dental_amount);
        }else if(EntryRequest.PARAM_VISION_AMOUNT.equals(amtOption)){
            return getString(R.string.prompt_input_vision_amount);
        }else if(EntryRequest.PARAM_COPAY_AMOUNT.equals(amtOption)){
            return getString(R.string.prompt_input_co_pay_amount);
        }else if(EntryRequest.PARAM_OTC_AMOUNT.equals(amtOption)){
            return getString(R.string.prompt_input_otc_amount);
        }
        return "";
    }

    private void onGetSubHealthCareAmount(String amtOption, long value){
        if(EntryRequest.PARAM_CLINIC_AMOUNT.equals(amtOption)){
            clinicAmt = value;
        }else if(EntryRequest.PARAM_PRESCRIPTION_AMOUNT.equals(amtOption)){
            prescriptionAmt = value;
        }else if(EntryRequest.PARAM_DENTAL_AMOUNT.equals(amtOption)){
            dentalAmt = value;
        }else if(EntryRequest.PARAM_VISION_AMOUNT.equals(amtOption)){
            visionAmt = value;
        }else if(EntryRequest.PARAM_COPAY_AMOUNT.equals(amtOption)){
            copayAmt = value;
        }else if(EntryRequest.PARAM_OTC_AMOUNT.equals(amtOption)){
            otcAmt = value;
        }
        amtIndex++;
        enterSubHealthCareAmount();
    }

    private void sendNext(){
        Bundle bundle = new Bundle();
        bundle.putString(EntryRequest.PARAM_ACTION, action);
        bundle.putString(EntryRequest.PARAM_FSA_OPTION, fsaOption);
        if(fsaAmountOptions.contains(EntryRequest.PARAM_HEALTH_CARE_AMOUNT)){
            bundle.putLong(EntryRequest.PARAM_HEALTH_CARE_AMOUNT, healthAmt);
        }
        if(fsaAmountOptions.contains(EntryRequest.PARAM_CLINIC_AMOUNT)){
            bundle.putLong(EntryRequest.PARAM_CLINIC_AMOUNT, clinicAmt);
        }
        if(fsaAmountOptions.contains(EntryRequest.PARAM_PRESCRIPTION_AMOUNT)){
            bundle.putLong(EntryRequest.PARAM_PRESCRIPTION_AMOUNT, prescriptionAmt);
        }

        if(fsaAmountOptions.contains(EntryRequest.PARAM_DENTAL_AMOUNT)){
            bundle.putLong(EntryRequest.PARAM_DENTAL_AMOUNT, dentalAmt);
        }

        if(fsaAmountOptions.contains(EntryRequest.PARAM_VISION_AMOUNT)){
            bundle.putLong(EntryRequest.PARAM_VISION_AMOUNT, visionAmt);
        }

        if(fsaAmountOptions.contains(EntryRequest.PARAM_COPAY_AMOUNT)){
            bundle.putLong(EntryRequest.PARAM_COPAY_AMOUNT, copayAmt);
        }

        if(fsaAmountOptions.contains(EntryRequest.PARAM_OTC_AMOUNT)){
            bundle.putLong(EntryRequest.PARAM_OTC_AMOUNT, otcAmt);
        }
        if(fsaAmountOptions.contains(EntryRequest.PARAM_TRANSIT_AMOUNT)){
            bundle.putLong(EntryRequest.PARAM_TRANSIT_AMOUNT, transitAmt);
        }
        Intent intent = new Intent(EntryRequest.ACTION_NEXT);
        intent.putExtras(bundle);
        intent.setPackage(packageName);
        requireContext().sendBroadcast(intent);
    }
}
