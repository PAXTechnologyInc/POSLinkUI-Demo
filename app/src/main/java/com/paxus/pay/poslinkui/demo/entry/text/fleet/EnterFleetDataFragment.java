package com.paxus.pay.poslinkui.demo.entry.text.fleet;

import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.pax.us.pay.ui.constant.entry.EntryExtraData;
import com.pax.us.pay.ui.constant.entry.EntryRequest;
import com.pax.us.pay.ui.constant.entry.TextEntry;
import com.paxus.pay.poslinkui.demo.R;
import com.paxus.pay.poslinkui.demo.entry.BaseEntryFragment;
import com.paxus.pay.poslinkui.demo.utils.ValuePatternUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Implement {@link TextEntry#ACTION_ENTER_FLEET_DATA}
 */
public class EnterFleetDataFragment extends BaseEntryFragment {
    private long timeOut;


    private List<IndividualFleetData> fleetDataList = new ArrayList<>();
    private IndividualFleetData currentFleetData;
    private Bundle output;

    private TextView title;
    private EditText editText;
    private Button confirm;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_fleet;
    }

    @Override
    protected void loadArgument(@NonNull Bundle bundle) {
        timeOut = bundle.getLong(EntryExtraData.PARAM_TIMEOUT,30000);

        if(bundle.containsKey(EntryExtraData.PARAM_FLEET_DRIVER_ID_PATTERN)) {
            fleetDataList.add(new IndividualFleetData(
                    getTitle(EntryRequest.PARAM_FLEET_DRIVER_ID),
                    InputType.TYPE_CLASS_NUMBER,
                    bundle.getString(EntryExtraData.PARAM_FLEET_DRIVER_ID_PATTERN),
                    EntryRequest.PARAM_FLEET_DRIVER_ID)
            );
        }

        if(bundle.containsKey(EntryExtraData.PARAM_FLEET_ODOMETER_PATTERN)) {
            fleetDataList.add(new IndividualFleetData(
                    getTitle(EntryRequest.PARAM_FLEET_ODOMETER),
                    InputType.TYPE_CLASS_NUMBER,
                    bundle.getString(EntryExtraData.PARAM_FLEET_ODOMETER_PATTERN),
                    EntryRequest.PARAM_FLEET_ODOMETER)
            );
        }

        if(bundle.containsKey(EntryExtraData.PARAM_FLEET_VEHICLE_NUMBER_PATTERN)) {
            fleetDataList.add(new IndividualFleetData(
                    getTitle(EntryRequest.PARAM_FLEET_VEHICLE_NUMBER),
                    InputType.TYPE_CLASS_NUMBER,
                    bundle.getString(EntryExtraData.PARAM_FLEET_VEHICLE_NUMBER_PATTERN),
                    EntryRequest.PARAM_FLEET_VEHICLE_NUMBER)
            );
        }

        if(bundle.containsKey(EntryExtraData.PARAM_FLEET_LICENSE_NUMBER_PATTERN)) {
            fleetDataList.add(new IndividualFleetData(
                    getTitle(EntryRequest.PARAM_FLEET_LICENSE_NUMBER),
                    InputType.TYPE_CLASS_NUMBER,
                    bundle.getString(EntryExtraData.PARAM_FLEET_LICENSE_NUMBER_PATTERN),
                    EntryRequest.PARAM_FLEET_LICENSE_NUMBER)
            );
        }

        if(bundle.containsKey(EntryExtraData.PARAM_FLEET_JOB_NUMBER_PATTERN)) {
            fleetDataList.add(new IndividualFleetData(
                    getTitle(EntryRequest.PARAM_FLEET_JOB_NUMBER),
                    InputType.TYPE_CLASS_NUMBER,
                    bundle.getString(EntryExtraData.PARAM_FLEET_JOB_NUMBER_PATTERN),
                    EntryRequest.PARAM_FLEET_JOB_NUMBER)
            );
        }

        if(bundle.containsKey(EntryExtraData.PARAM_FLEET_DEPARTMENT_NUMBER_PATTERN)) {
            fleetDataList.add(new IndividualFleetData(
                    getTitle(EntryRequest.PARAM_FLEET_DEPARTMENT_NUMBER),
                    InputType.TYPE_CLASS_TEXT,
                    bundle.getString(EntryExtraData.PARAM_FLEET_DEPARTMENT_NUMBER_PATTERN),
                    EntryRequest.PARAM_FLEET_DEPARTMENT_NUMBER)
            );
        }

        if(bundle.containsKey(EntryExtraData.PARAM_FLEET_CUSTOMER_DATA_PATTERN)) {
            fleetDataList.add(new IndividualFleetData(
                    getTitle(EntryRequest.PARAM_FLEET_CUSTOMER_DATA),
                    InputType.TYPE_CLASS_TEXT,
                    bundle.getString(EntryExtraData.PARAM_FLEET_CUSTOMER_DATA_PATTERN),
                    EntryRequest.PARAM_FLEET_CUSTOMER_DATA)
            );
        }

        if(bundle.containsKey(EntryExtraData.PARAM_FLEET_USER_ID_PATTERN)) {
            fleetDataList.add(new IndividualFleetData(
                    getTitle(EntryRequest.PARAM_FLEET_USER_ID),
                    InputType.TYPE_CLASS_TEXT,
                    bundle.getString(EntryExtraData.PARAM_FLEET_USER_ID_PATTERN),
                    EntryRequest.PARAM_FLEET_USER_ID)
            );
        }

        if(bundle.containsKey(EntryExtraData.PARAM_FLEET_VEHICLE_ID_PATTERN)) {
            fleetDataList.add(new IndividualFleetData(
                    getTitle(EntryRequest.PARAM_FLEET_VEHICLE_ID),
                    InputType.TYPE_CLASS_NUMBER,
                    bundle.getString(EntryExtraData.PARAM_FLEET_VEHICLE_ID_PATTERN),
                    EntryRequest.PARAM_FLEET_VEHICLE_ID)
            );
        }

        output = new Bundle();

    }

    @Override
    protected void loadView(View rootView) {
        title = rootView.findViewById(R.id.message);

        editText = rootView.findViewById(R.id.edit_number_text);

        confirm = rootView.findViewById(R.id.confirm_button);
        confirm.setOnClickListener(v -> onConfirmButtonClicked());

        resetFleetData();
        setCurrentFleetData();
    }

    private void setCurrentFleetData() {
        title.setText(currentFleetData.title);
        editText.setInputType(currentFleetData.inputType);
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(ValuePatternUtils.getMaxLength(currentFleetData.valuePattern))});
        editText.setText("");
        focusableEditTexts = new EditText[]{editText};
    }

    private void resetFleetData() {
        currentFleetData = fleetDataList.get(0);
        for(IndividualFleetData fleetData: fleetDataList){
            fleetData.outputValue = null;
        }
    }

    @Override
    protected void onConfirmButtonClicked() {
        if(editText.getText().toString().length() < ValuePatternUtils.getMinLength(currentFleetData.valuePattern)){
            Toast.makeText(requireContext(), getString(R.string.min_length_verification, ValuePatternUtils.getMinLength(currentFleetData.valuePattern)), Toast.LENGTH_SHORT).show();
            return;
        }

        currentFleetData.outputValue = editText.getText().toString();
        output.putString(currentFleetData.outputKey, currentFleetData.outputValue);
        focusableEditTexts = null;

        boolean completed = true;
        for(IndividualFleetData fleetData: fleetDataList){
            if(fleetData.outputValue == null) {
                currentFleetData = fleetData;
                completed = false;
                break;
            }
        }
        if(completed) {
            sendNext(output);
        } else {

        }
        setCurrentFleetData();
    }

    @Override
    protected void onEntryDeclined(int errCode, String errMessage) {
        super.onEntryDeclined(errCode, errMessage);
        resetFleetData();
        setCurrentFleetData();
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

    class IndividualFleetData {
        String title;
        int inputType;
        String valuePattern;
        String outputKey;
        String outputValue;

        IndividualFleetData(String title, int inputType, String valuePattern, String outputKey) {
            this.title = title;
            this.inputType = inputType;
            this.valuePattern = valuePattern;
            this.outputKey = outputKey;
        }
    }

}
