package com.paxus.pay.poslinkui.demo.entry.poslink;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.pax.us.pay.ui.constant.entry.PoslinkEntry;
import com.paxus.pay.poslinkui.demo.R;
import com.paxus.pay.poslinkui.demo.utils.Logger;

/**
 * Used by {@value PoslinkEntry#ACTION_SHOW_DIALOG_FORM}
 * <p>
 *     UI Tips:
 *     1. Index start from 1
 *
 * </p>
 */
public class ShowDialogFormRadioFragment extends Fragment {
    public static final String ARG_TITLE = "title";
    public static final String ARG_OPTIONS = "options";
    public static final String RESULT = "result";
    public static final String INDEX = "index";

    private String title;
    private String button1;
    private String button2;
    private String button3;
    private String button4;
    private int checkedIndex = -1;

    public static Fragment newInstance(String title, String[] options){
        ShowDialogFormRadioFragment fragment = new ShowDialogFormRadioFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_TITLE, title);
        bundle.putStringArray(ARG_OPTIONS, options);

        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Logger.d(this.getClass().getSimpleName()+" onCreateView");

        Bundle bundle = getArguments();
        if(bundle != null) {
            loadArgument(bundle);
        }else {
            Logger.e(this.getClass().getSimpleName()+" arguments missing!!!");
        }

        View view = inflater.inflate(R.layout.fragment_show_dialog_form_radio, container, false);
        loadView(view);
        return view;
    }

    protected void loadArgument(@NonNull Bundle bundle) {

        title = bundle.getString(ARG_TITLE);
        String[] options = bundle.getStringArray(ARG_OPTIONS);
        if(options!=null) {
            if (options.length >= 1) {
                button1 = options[0];
            }
            if (options.length >= 2) {
                button2 = options[1];
            }
            if (options.length >= 3) {
                button3 = options[2];
            }
            if (options.length >= 4) {
                button4 = options[3];
            }
        }

    }

    protected void loadView(View rootView) {
        TextView textView = rootView.findViewById(R.id.title);
        textView.setText(title);

        RadioButton btn1 = rootView.findViewById(R.id.button1);
        formatButton(btn1, button1, 1);

        RadioButton btn2 = rootView.findViewById(R.id.button2);
        formatButton(btn2, button2, 2);

        RadioButton btn3 = rootView.findViewById(R.id.button3);
        formatButton(btn3, button3, 3);

        RadioButton btn4 = rootView.findViewById(R.id.button4);
        formatButton(btn4, button4, 4);

        Button confirmButton = rootView.findViewById(R.id.confirm_button);
        confirmButton.setOnClickListener(v -> onConfirmButtonClicked());
    }

    private void formatButton(RadioButton button, String message, int index){
        if(!TextUtils.isEmpty(message)){
            button.setText(message);
            button.setChecked(false);
            button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        checkedIndex = index;
                    }
                }
            });
        }else{
            button.setVisibility(View.GONE);
        }
    }

    private void onConfirmButtonClicked(){
        if(checkedIndex > 0){
            Bundle bundle = new Bundle();
            bundle.putInt(INDEX, checkedIndex);
            getParentFragmentManager().setFragmentResult(RESULT, bundle);
        }
    }

}
