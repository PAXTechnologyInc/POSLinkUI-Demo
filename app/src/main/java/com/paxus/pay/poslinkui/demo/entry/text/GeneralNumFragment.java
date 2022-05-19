package com.paxus.pay.poslinkui.demo.entry.text;

import android.os.Bundle;
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


public class GeneralNumFragment extends Fragment {
    private int minLength;
    private int maxLength;
    private String message = "";

    private EditText editText;

    public static final String TITLE = "title";
    public static final String MIN_LENGTH = "min_length";
    public static final String MAX_LENGTH = "max_length";

    public static final String RESULT = "result";
    public static final String VALUE = "value";
    public static Fragment newInstance(String title, int minLength, int maxLength){
        GeneralNumFragment numFragment = new GeneralNumFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TITLE, title);
        bundle.putInt(MIN_LENGTH, minLength);
        bundle.putInt(MAX_LENGTH, maxLength);
        numFragment.setArguments(bundle);
        return numFragment;
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

        View view = inflater.inflate(R.layout.fragment_base_num, container, false);
        loadView(view);
        return view;
    }

    protected void loadArgument(@NonNull Bundle bundle) {
        message = bundle.getString(TITLE);
        minLength = bundle.getInt(MIN_LENGTH,0);
        maxLength = bundle.getInt(MAX_LENGTH,32);
    }

    protected void loadView(View rootView) {
        TextView textView = rootView.findViewById(R.id.message);
        textView.setText(message);

        editText = rootView.findViewById(R.id.edit_amount);
        editText.setSelected(false);
        editText.setSelection(editText.getEditableText().length());

        Button confirmBtn = rootView.findViewById(R.id.confirm_button);
        confirmBtn.setOnClickListener(v -> onConfirmButtonClicked());

    }

    //1.If confirm button clicked, sendNext
    private void onConfirmButtonClicked(){
        String value = editText.getText().toString();
        if(String.valueOf(value).length() < minLength){
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
        }else {
            sendNext(value);
        }
    }


    private void sendNext(String value){
        Bundle bundle = new Bundle();
        bundle.putString(VALUE, value);
        getParentFragmentManager().setFragmentResult(RESULT, bundle);
    }
}
