package com.paxus.pay.poslinkui.demo.entry.text.fleet;

import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.pax.us.pay.ui.constant.entry.EntryExtraData;
import com.pax.us.pay.ui.constant.entry.EntryRequest;
import com.pax.us.pay.ui.constant.entry.TextEntry;
import com.pax.us.pay.ui.constant.entry.enumeration.InputType;
import com.paxus.pay.poslinkui.demo.R;
import com.paxus.pay.poslinkui.demo.entry.text.numbertext.ANumTextFragment;
import com.paxus.pay.poslinkui.demo.utils.ValuePatternUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Implement text entry actions:<br>
 * {@value TextEntry#ACTION_ENTER_CUSTOMER_DATA}<br>
 * {@value TextEntry#ACTION_ENTER_DEPARTMENT_NUMBER}<br>
 * {@value TextEntry#ACTION_ENTER_DRIVER_ID}<br>
 * {@value TextEntry#ACTION_ENTER_EMPLOYEE_NUMBER}<br>
 * {@value TextEntry#ACTION_ENTER_FLEET_PROMPT_CODE}<br>
 * {@value TextEntry#ACTION_ENTER_HUBOMETER}<br>
 * {@value TextEntry#ACTION_ENTER_JOB_ID}<br>
 * {@value TextEntry#ACTION_ENTER_LICENSE_NUMBER}<br>
 * {@value TextEntry#ACTION_ENTER_MAINTENANCE_ID}<br>
 * {@value TextEntry#ACTION_ENTER_ODOMETER}<br>
 * {@value TextEntry#ACTION_ENTER_FLEET_PO_NUMBER}<br>
 * {@value TextEntry#ACTION_ENTER_REEFER_HOURS}<br>
 * {@value TextEntry#ACTION_ENTER_TRAILER_ID}<br>
 * {@value TextEntry#ACTION_ENTER_TRIP_NUMBER}<br>
 * {@value TextEntry#ACTION_ENTER_UNIT_ID}<br>
 * {@value TextEntry#ACTION_ENTER_USER_ID}<br>
 * {@value TextEntry#ACTION_ENTER_VEHICLE_ID}<br>
 * {@value TextEntry#ACTION_ENTER_VEHICLE_NUMBER}<br>
 * {@value TextEntry#ACTION_ENTER_ADDITIONAL_FLEET_DATA_1}<br>
 * {@value TextEntry#ACTION_ENTER_ADDITIONAL_FLEET_DATA_2}<br>
 */

public class NewEnterFleetDataFragment extends ANumTextFragment {
    protected long timeOut;
    protected int minLength;
    protected int maxLength;
    String action;
    FleetData fleetData;

    private static final Map<String, FleetData> fleetDataMap = new HashMap<String, FleetData>(){
        {
            put(TextEntry.ACTION_ENTER_CUSTOMER_DATA, new FleetData(R.string.enter_customer_data,"0-24", EntryRequest.PARAM_CUSTOMER_DATA, InputType.ALLTEXT));
            put(TextEntry.ACTION_ENTER_DEPARTMENT_NUMBER, new FleetData(R.string.enter_department_number,"0-24", EntryRequest.PARAM_DEPARTMENT_NUMBER, InputType.ALLTEXT));
            put(TextEntry.ACTION_ENTER_DRIVER_ID, new FleetData(R.string.enter_driver_id,"0-20", EntryRequest.PARAM_DRIVER_ID, InputType.NUM));
            put(TextEntry.ACTION_ENTER_EMPLOYEE_NUMBER, new FleetData(R.string.enter_employee_number,"0-20", EntryRequest.PARAM_EMPLOYEE_NUMBER, InputType.ALLTEXT));
            put(TextEntry.ACTION_ENTER_FLEET_PROMPT_CODE, new FleetData(R.string.enter_fleet_prompt_code,"0,2", EntryRequest.PARAM_FLEET_PROMPT_CODE, InputType.NUM));
            put(TextEntry.ACTION_ENTER_HUBOMETER, new FleetData(R.string.enter_hubometer,"0-9", EntryRequest.PARAM_HUBOMETER, InputType.NUM));
            put(TextEntry.ACTION_ENTER_JOB_ID, new FleetData(R.string.enter_job_id,"0-24", EntryRequest.PARAM_JOB_ID, InputType.NUM));
            put(TextEntry.ACTION_ENTER_LICENSE_NUMBER, new FleetData(R.string.enter_license_number,"0-20", EntryRequest.PARAM_LICENSE_NUMBER, InputType.ALLTEXT));
            put(TextEntry.ACTION_ENTER_MAINTENANCE_ID, new FleetData(R.string.enter_maintenance_id,"0-15", EntryRequest.PARAM_MAINTENANCE_ID, InputType.ALLTEXT));
            put(TextEntry.ACTION_ENTER_ODOMETER, new FleetData(R.string.enter_odometer,"0-16", EntryRequest.PARAM_ODOMETER, InputType.NUM));
            put(TextEntry.ACTION_ENTER_FLEET_PO_NUMBER, new FleetData(R.string.enter_fleet_po_number,"0-10", EntryRequest.PARAM_FLEET_PO_NUMBER, InputType.ALLTEXT));
            put(TextEntry.ACTION_ENTER_REEFER_HOURS, new FleetData(R.string.enter_reefer_hours,"0-7", EntryRequest.PARAM_REEFER_HOURS, InputType.NUM));
            put(TextEntry.ACTION_ENTER_TRAILER_ID, new FleetData(R.string.enter_trailer_id,"0-10", EntryRequest.PARAM_TRAILER_ID, InputType.ALLTEXT));
            put(TextEntry.ACTION_ENTER_TRIP_NUMBER, new FleetData(R.string.enter_trip_number,"0-15", EntryRequest.PARAM_TRIP_NUMBER, InputType.ALLTEXT));
            put(TextEntry.ACTION_ENTER_UNIT_ID, new FleetData(R.string.enter_unit_id,"0-10", EntryRequest.PARAM_UNIT_ID, InputType.ALLTEXT));
            put(TextEntry.ACTION_ENTER_USER_ID, new FleetData(R.string.enter_user_id,"0-24", EntryRequest.PARAM_USER_ID, InputType.ALLTEXT));
            put(TextEntry.ACTION_ENTER_VEHICLE_ID, new FleetData(R.string.enter_vehicle_id,"0-16", EntryRequest.PARAM_FLEET_VEHICLE_ID, InputType.NUM));
            put(TextEntry.ACTION_ENTER_VEHICLE_NUMBER, new FleetData(R.string.enter_vehicle_number,"0-20", EntryRequest.PARAM_VEHICLE_NUMBER, InputType.ALLTEXT));
            put(TextEntry.ACTION_ENTER_ADDITIONAL_FLEET_DATA_1, new FleetData(R.string.enter_additional_fleet_data_1,"0-20", EntryRequest.PARAM_ADDITIONAL_FLEET_DATA_1, InputType.ALLTEXT));
            put(TextEntry.ACTION_ENTER_ADDITIONAL_FLEET_DATA_2, new FleetData(R.string.enter_additional_fleet_data_2,"0-20", EntryRequest.PARAM_ADDITIONAL_FLEET_DATA_2, InputType.ALLTEXT));
        }
    };

    @Override
    protected void loadArgument(@NonNull Bundle bundle) {
        timeOut = bundle.getLong(EntryExtraData.PARAM_TIMEOUT, 30000);
        action = bundle.getString(EntryExtraData.PARAM_ACTION);
        fleetData = fleetDataMap.get(action);
        String defaultValuePattern;
        String eInputType;
        if (fleetData != null) {
            defaultValuePattern = fleetData.getDefaultValuePattern();
            eInputType = bundle.getString(EntryExtraData.PARAM_EINPUT_TYPE, fleetData.getDefaultInputType());
        } else {
            defaultValuePattern = "";
            eInputType = "";
        };
        valuePatten = bundle.getString(EntryExtraData.PARAM_VALUE_PATTERN, defaultValuePattern);
        allowText = Arrays.asList(InputType.ALLTEXT, InputType.PASSWORD).contains(eInputType);
        allowPassword = Arrays.asList(InputType.PASSWORD, InputType.PASSCODE).contains(eInputType);
        if (!TextUtils.isEmpty(valuePatten)) {
            minLength = ValuePatternUtils.getMinLength(valuePatten);
            maxLength = ValuePatternUtils.getMaxLength(valuePatten);
        }
    }

    @Override
    protected int getMaxLength() {
        return maxLength;
    }

    @Override
    protected String formatMessage() {
        if (fleetData != null) {
            return getString(fleetData.getTitleId());
        } else return "";
    }

    @Override
    protected String getRequestedParamName() {
        if (fleetData != null) {
            return fleetData.getRequestedParamName();
        } else return "";
    }
}
