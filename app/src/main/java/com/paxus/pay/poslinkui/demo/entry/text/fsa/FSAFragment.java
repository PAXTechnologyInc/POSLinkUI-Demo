package com.paxus.pay.poslinkui.demo.entry.text.fsa;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.fragment.app.FragmentTransaction;

import com.pax.us.pay.ui.constant.entry.EntryExtraData;
import com.pax.us.pay.ui.constant.entry.EntryRequest;
import com.pax.us.pay.ui.constant.entry.TextEntry;
import com.pax.us.pay.ui.constant.entry.enumeration.CurrencyType;
import com.pax.us.pay.ui.constant.entry.enumeration.FSAType;
import com.paxus.pay.poslinkui.demo.R;
import com.paxus.pay.poslinkui.demo.entry.BaseEntryFragment;
import com.paxus.pay.poslinkui.demo.utils.Logger;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Yanina.Yang on 5/19/2022.
 *
 * Implement {@link TextEntry#ACTION_ENTER_FSA_DATA}
 *
 * <p>
 *     UI Tips:
 *     This action is a complex action. Please see implementation details on {@link #loadFsaAmountOptions()} and {@link #sendNext()}
 * </p>
 */
public class FSAFragment extends BaseEntryFragment {
    private String transType;
    private String transMode;
    private long timeOut;
    private String currency;
    private long totalAmount;
    private List<String> fsaAmountOptions;
    protected String packageName;
    protected String action;

    @Override
    protected String getSenderPackageName() {
        return packageName;
    }

    @Override
    protected String getEntryAction() {
        return action;
    }

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
        loadFsaAmountOptions();
    }

    private void loadFsaAmountOptions(){
        if(fsaAmountOptions != null && fsaAmountOptions.size()>0){
            boolean healthCareEnabled = fsaAmountOptions.contains(EntryRequest.PARAM_HEALTH_CARE_AMOUNT);
            boolean transitEnabled = fsaAmountOptions.contains(EntryRequest.PARAM_TRANSIT_AMOUNT);
            if( healthCareEnabled && transitEnabled){
                selectFsaType();
            }else if(transitEnabled){
                fsaOption = FSAType.TRANSIT;
                enterTransitAmount();
            }else if(healthCareEnabled){
                fsaOption = FSAType.HEALTH_CARE;
                selectHealthCareSubType();
            }else {
                Logger.e("NOT Typical FSA Options:"+Arrays.toString(fsaAmountOptions.toArray(new String[0])));
                selectHealthCareSubType();
            }
        }else {
            Logger.e("No fsa amount options.");
            sendAbort();
        }
    }

    private void selectFsaType(){

        String[] fsaTypes = new String[]{FSAType.TRANSIT, FSAType.HEALTH_CARE};
        Fragment fragment = FsaOptionsFragment.newInstance(
                getString(R.string.select_fsa_type),
                fsaTypes
        );
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_placeholder, fragment).commit();
        getChildFragmentManager().beginTransaction();

        getChildFragmentManager().setFragmentResultListener(FsaOptionsFragment.RESULT, this, new FragmentResultListener() {
                    @Override
                    public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                        int index = bundle.getInt(FsaOptionsFragment.INDEX);
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
        Fragment fragment = FSAAmountFragment.newInstance(
                getString(R.string.prompt_input_transit_amount),0,9,currency,totalAmount
        );
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_placeholder, fragment).commit();
        getChildFragmentManager().setFragmentResultListener(FSAAmountFragment.RESULT, this, new FragmentResultListener() {
                    @Override
                    public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                        transitAmt = bundle.getLong(FSAAmountFragment.VALUE);
                        sendNext();
                    }
                });
    }

    private void selectHealthCareSubType(){
        String[] healthCareTypes = new String[]{getString(R.string.healthcare_total),
                getString(R.string.healthcare_sub_type)};
        Fragment fragment = FsaOptionsFragment.newInstance(
                getString(R.string.select_health_sub_types),
                healthCareTypes
        );
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_placeholder, fragment).commit();
        getChildFragmentManager().beginTransaction();

        getChildFragmentManager().setFragmentResultListener(FsaOptionsFragment.RESULT, this, new FragmentResultListener() {
                    @Override
                    public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                        int index = bundle.getInt(FsaOptionsFragment.INDEX);
                        if(index == 0){
                            enterTotalHealthCareAmount();
                        }else {
                            enterSubHealthCareAmount();
                        }
                    }
                });
    }

    private void enterTotalHealthCareAmount(){
        Fragment fragment = FSAAmountFragment.newInstance(
                getString(R.string.prompt_input_healthcare_amount),0,9,currency,totalAmount
        );
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_placeholder, fragment).commit();
        getChildFragmentManager().setFragmentResultListener(FSAAmountFragment.RESULT, this, new FragmentResultListener() {
                    @Override
                    public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                        healthAmt = bundle.getLong(FSAAmountFragment.VALUE);
                        sendNext();
                    }
                });
    }

    private void enterSubHealthCareAmount(){
        if(amtIndex>=0 && amtIndex< fsaAmountOptions.size()){
            String amtOption = fsaAmountOptions.get(amtIndex);
            if(!EntryRequest.PARAM_TRANSIT_AMOUNT.equals(amtOption) && !EntryRequest.PARAM_HEALTH_CARE_AMOUNT.equals(amtOption)){
                Fragment fragment = FSAAmountFragment.newInstance(
                        getSubHealthCareAmountTitle(amtOption),0,9,currency, totalAmount
                );
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_placeholder, fragment).commit();
                getChildFragmentManager().setFragmentResultListener(FSAAmountFragment.RESULT, this, new FragmentResultListener() {
                            @Override
                            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                               onGetSubHealthCareAmount(amtOption, bundle.getLong(FSAAmountFragment.VALUE));
                            }
                        });
                return;
            }
            amtIndex++;
            enterSubHealthCareAmount();
            return;
        }
        sendNext();
    }

    private String getSubHealthCareAmountTitle(String amtOption){
        if(EntryRequest.PARAM_CLINIC_AMOUNT.equals(amtOption)){
            return getString(R.string.prompt_input_clinical_amount);
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

    @Override
    protected void onEntryDeclined(long errCode, String errMessage) {
        super.onEntryDeclined(errCode, errMessage);

        //Re enter sub health care amount
        if(FSAType.HEALTH_CARE.equals(fsaOption)){
            if(healthAmt <= 0) {
                amtIndex = 0;
                clinicAmt = 0;
                prescriptionAmt = 0;
                dentalAmt = 0;
                visionAmt = 0;
                copayAmt = 0;
                otcAmt = 0;
                enterSubHealthCareAmount();
            }
        }
    }

    private long getHealthCareAmount(){
        return healthAmt>0 ? healthAmt : clinicAmt + prescriptionAmt + dentalAmt + visionAmt + copayAmt + otcAmt;
    }

    private void sendNext(){
        Bundle bundle = new Bundle();
        bundle.putString(EntryRequest.PARAM_ACTION, action);
        bundle.putString(EntryRequest.PARAM_FSA_OPTION, fsaOption);
        if(fsaAmountOptions.contains(EntryRequest.PARAM_HEALTH_CARE_AMOUNT)){
            bundle.putLong(EntryRequest.PARAM_HEALTH_CARE_AMOUNT, getHealthCareAmount());
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
