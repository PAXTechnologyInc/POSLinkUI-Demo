package com.paxus.pay.poslinkui.demo.entry.text;

import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.paxus.pay.poslinkui.demo.R;
import com.paxus.pay.poslinkui.demo.utils.Logger;

/**
 * Fragment which used to input fleet data
 * See {@link FleetFragment}
 */
public class FleetDataFragment extends Fragment {
    private static final String ARG_TITLE = "title";
    private static final String ARG_MIN_LENGTH = "min_length";
    private static final String ARG_MAX_LENGTH = "max_length";
    private static final String ARG_INPUT_TYPE = "input_type";

    public static final String RESULT = "result";
    public static final String VALUE = "value";

    private int minLength;
    private int maxLength;
    private String message = "";
    private int inputType;

    private EditText editText;


    public static Fragment newInstance(String title, int minLength, int maxLength, int inputType){
        FleetDataFragment fragment = new FleetDataFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_TITLE, title);
        bundle.putInt(ARG_MIN_LENGTH, minLength);
        bundle.putInt(ARG_MAX_LENGTH, maxLength);
        bundle.putInt(ARG_INPUT_TYPE, inputType);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Logger.d(this.getClass().getSimpleName()+" onCreateView");

        //1. Load layout in onCreateView (getLayoutResourceId, loadParameter, loadView)
        Bundle bundle = getArguments();
        if(bundle != null) {
            loadArgument(bundle);
        }else {
            Logger.e(this.getClass().getSimpleName()+" arguments missing!!!");
        }

        View view = inflater.inflate(R.layout.fragment_base_num_text, container, false);
        loadView(view);
        return view;
    }

    protected void loadArgument(@NonNull Bundle bundle) {
        message = bundle.getString(ARG_TITLE);
        minLength = bundle.getInt(ARG_MIN_LENGTH,0);
        maxLength = bundle.getInt(ARG_MAX_LENGTH,32);
        inputType = bundle.getInt(ARG_INPUT_TYPE);
    }

    protected void loadView(View rootView) {
        TextView textView = rootView.findViewById(R.id.message);
        textView.setText(message);

        editText = rootView.findViewById(R.id.edit_number_text);
        editText.setInputType(inputType);
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
        editText.requestFocus();

        Button confirmBtn = rootView.findViewById(R.id.confirm_button);
        confirmBtn.setOnClickListener(v -> onConfirmButtonClicked());

    }

    //1.If confirm button clicked, sendNext
    private void onConfirmButtonClicked(){
        String value = editText.getText().toString();
        if(value.length() < minLength){
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
        }else {
            Bundle bundle = new Bundle();
            bundle.putString(VALUE, value);
            getParentFragmentManager().setFragmentResult(RESULT, bundle);
        }
    }

}
