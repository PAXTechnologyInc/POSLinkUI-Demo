package com.paxus.pay.poslinkui.demo.entry.text.fleet;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.fragment.app.FragmentTransaction;

import com.pax.us.pay.ui.constant.entry.EntryExtraData;
import com.pax.us.pay.ui.constant.entry.EntryRequest;
import com.pax.us.pay.ui.constant.entry.TextEntry;
import com.paxus.pay.poslinkui.demo.R;
import com.paxus.pay.poslinkui.demo.entry.BaseEntryFragment;
import com.paxus.pay.poslinkui.demo.utils.ValuePatternUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yanina.Yang on 5/19/2022.
 *
 * Implement {@link TextEntry#ACTION_ENTER_FLEET_DATA}
 *
 * <p>
 *     UI Tips:
 *     This action is a complex action. Please see implementation details on {@link #enterFleetData()} and {@link #loadArgument(Bundle)}
 * </p>
 */
public class FleetFragment extends BaseEntryFragment {
    private String transType;
    private String transMode;
    private long timeOut;

    private String driverIdPattern;
    private String odometerPattern;
    private String vehicleNumberPattern;
    private String licenseNumberPattern;
    private String jobNumberPattern;
    private String departmentNumberPattern;
    private String customerDataPattern;
    private String userIdPattern;
    private String vehicleIdPattern;
    private String packageName;
    private String action;

    @Override
    protected String getSenderPackageName() {
        return packageName;
    }

    @Override
    protected String getEntryAction() {
        return action;
    }

    private final List<String> requestList = new ArrayList<>();
    private int requestIndex = 0;

    private Bundle nextBundle;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_fleet;
    }

    @Override
    protected void loadArgument(@NonNull Bundle bundle) {
        action = bundle.getString(EntryRequest.PARAM_ACTION);
        packageName = bundle.getString(EntryExtraData.PARAM_PACKAGE);
        transType = bundle.getString(EntryExtraData.PARAM_TRANS_TYPE);
        transMode = bundle.getString(EntryExtraData.PARAM_TRANS_MODE);
        timeOut = bundle.getLong(EntryExtraData.PARAM_TIMEOUT,30000);
        driverIdPattern = bundle.getString(EntryExtraData.PARAM_FLEET_DRIVER_ID_PATTERN);
        odometerPattern = bundle.getString(EntryExtraData.PARAM_FLEET_ODOMETER_PATTERN);
        vehicleNumberPattern = bundle.getString(EntryExtraData.PARAM_FLEET_VEHICLE_NUMBER_PATTERN);
        licenseNumberPattern = bundle.getString(EntryExtraData.PARAM_FLEET_LICENSE_NUMBER_PATTERN);
        jobNumberPattern = bundle.getString(EntryExtraData.PARAM_FLEET_JOB_NUMBER_PATTERN);
        departmentNumberPattern = bundle.getString(EntryExtraData.PARAM_FLEET_DEPARTMENT_NUMBER_PATTERN);
        customerDataPattern = bundle.getString(EntryExtraData.PARAM_FLEET_CUSTOMER_DATA_PATTERN);
        userIdPattern = bundle.getString(EntryExtraData.PARAM_FLEET_USER_ID_PATTERN);
        vehicleIdPattern = bundle.getString(EntryExtraData.PARAM_FLEET_VEHICLE_ID_PATTERN);

        nextBundle = new Bundle();
        bundle.putString(EntryRequest.PARAM_ACTION, action);

        requestList.clear();
        if(!TextUtils.isEmpty(driverIdPattern)){
            requestList.add(EntryRequest.PARAM_FLEET_DRIVER_ID);
        }
        if(!TextUtils.isEmpty(odometerPattern)){
            requestList.add(EntryRequest.PARAM_FLEET_ODOMETER);
        }
        if(!TextUtils.isEmpty(vehicleNumberPattern)){
            requestList.add(EntryRequest.PARAM_FLEET_VEHICLE_NUMBER);
        }
        if(!TextUtils.isEmpty(licenseNumberPattern)){
            requestList.add(EntryRequest.PARAM_FLEET_LICENSE_NUMBER);
        }
        if(!TextUtils.isEmpty(jobNumberPattern)){
            requestList.add(EntryRequest.PARAM_FLEET_JOB_NUMBER);
        }
        if(!TextUtils.isEmpty(departmentNumberPattern)){
            requestList.add(EntryRequest.PARAM_FLEET_DEPARTMENT_NUMBER);
        }
        if(!TextUtils.isEmpty(customerDataPattern)){
            requestList.add(EntryRequest.PARAM_FLEET_CUSTOMER_DATA);
        }
        if(!TextUtils.isEmpty(userIdPattern)){
            requestList.add(EntryRequest.PARAM_FLEET_USER_ID);
        }
        if(!TextUtils.isEmpty(vehicleIdPattern)){
            requestList.add(EntryRequest.PARAM_FLEET_VEHICLE_ID);
        }
    }

    @Override
    protected void loadView(View rootView) {
        enterFleetData();
    }

    private void enterFleetData(){
        if(requestIndex>=0 && requestIndex< requestList.size()){
            String requestKey = requestList.get(requestIndex);
            String valuePattern = getValuePattern(requestKey);
            int inputType = InputType.TYPE_CLASS_NUMBER;
            if(EntryRequest.PARAM_FLEET_DEPARTMENT_NUMBER.equals(requestKey)
                    || EntryRequest.PARAM_FLEET_CUSTOMER_DATA.equals(requestKey)
                    || EntryRequest.PARAM_FLEET_USER_ID.equals(requestKey)){
                inputType = InputType.TYPE_CLASS_TEXT;

            }
            Fragment fragment = FleetDataFragment.newInstance(
                    getTitle(requestKey), ValuePatternUtils.getMinLength(valuePattern),ValuePatternUtils.getMaxLength(valuePattern), inputType
            );
            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_placeholder, fragment).commit();
            getChildFragmentManager().setFragmentResultListener(FleetDataFragment.RESULT, this, new FragmentResultListener() {
                        @Override
                        public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                            onGetEnterValue(requestKey, bundle.getString(FleetDataFragment.VALUE));
                        }
                    });
            return;
        }
        sendNext();
    }

    private void onGetEnterValue(String requestKey, String value){
        nextBundle.putString(requestKey, value);
        requestIndex++;
        enterFleetData();
    }

    @Override
    protected void onEntryDeclined(long errCode, String errMessage) {
        super.onEntryDeclined(errCode, errMessage);

        //re enter if next request declined
        requestIndex = 0;
        enterFleetData();
    }

    private String getTitle(String requestData){
        switch (requestData){
            case EntryRequest.PARAM_FLEET_DRIVER_ID: return getString(R.string.prompt_input_fleet_driver_id);
            case EntryRequest.PARAM_FLEET_ODOMETER: return getString(R.string.prompt_input_fleet_odometer);
            case EntryRequest.PARAM_FLEET_VEHICLE_NUMBER: return getString(R.string.prompt_input_fleet_vehicle_no);
            case EntryRequest.PARAM_FLEET_LICENSE_NUMBER: return getString(R.string.prompt_input_fleet_license_no);
            case EntryRequest.PARAM_FLEET_JOB_NUMBER: return getString(R.string.prompt_input_fleet_job_no);
            case EntryRequest.PARAM_FLEET_DEPARTMENT_NUMBER: return getString(R.string.prompt_input_fleet_department_no);
            case EntryRequest.PARAM_FLEET_CUSTOMER_DATA: return getString(R.string.prompt_input_fleet_customer_data);
            case EntryRequest.PARAM_FLEET_USER_ID: return getString(R.string.prompt_input_fleet_user_id);
            case EntryRequest.PARAM_FLEET_VEHICLE_ID: return getString(R.string.prompt_input_fleet_vehicle_id);
            default:return "";
        }
    }

    private String getValuePattern(String requestData){
        switch (requestData){
            case EntryRequest.PARAM_FLEET_DRIVER_ID: return driverIdPattern;
            case EntryRequest.PARAM_FLEET_ODOMETER: return odometerPattern;
            case EntryRequest.PARAM_FLEET_VEHICLE_NUMBER: return vehicleNumberPattern;
            case EntryRequest.PARAM_FLEET_LICENSE_NUMBER: return licenseNumberPattern;
            case EntryRequest.PARAM_FLEET_JOB_NUMBER: return jobNumberPattern;
            case EntryRequest.PARAM_FLEET_DEPARTMENT_NUMBER: return departmentNumberPattern;
            case EntryRequest.PARAM_FLEET_CUSTOMER_DATA: return customerDataPattern;
            case EntryRequest.PARAM_FLEET_USER_ID: return userIdPattern;
            case EntryRequest.PARAM_FLEET_VEHICLE_ID: return vehicleIdPattern;
            default:return "";
        }
    }

    private void sendNext(){
        Intent intent = new Intent(EntryRequest.ACTION_NEXT);
        intent.putExtras(nextBundle);
        intent.setPackage(packageName);
        requireContext().sendBroadcast(intent);
    }
}
