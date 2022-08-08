package com.paxus.pay.poslinkui.demo.entry.poslink;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.pax.us.pay.ui.constant.entry.PoslinkEntry;
import com.pax.us.pay.ui.constant.entry.enumeration.ManageUIConst;
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
public class ShowDialogFormCheckBoxFragment extends Fragment {
    public static final String ARG_TITLE = "title";
    public static final String ARG_OPTIONS = "options";
    public static final String ARG_OPTIONS_CHECKED = "options_checked";

    public static final String RESULT = "result";
    public static final String CHECKED_INDEX = "checked_index";

    private String title;
    private String button1;
    private String button2;
    private String button3;
    private String button4;

    private boolean button1Check;
    private boolean button2Check;
    private boolean button3Check;
    private boolean button4Check;

    private CheckBox btn1;
    private CheckBox btn2;
    private CheckBox btn3;
    private CheckBox btn4;


    public static Fragment newInstance(String title, String[] options, String[] optionsChecked){
        ShowDialogFormCheckBoxFragment fragment = new ShowDialogFormCheckBoxFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_TITLE, title);
        bundle.putStringArray(ARG_OPTIONS, options);
        bundle.putStringArray(ARG_OPTIONS_CHECKED, optionsChecked);

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

        View view = inflater.inflate(R.layout.fragment_show_dialog_form_checkbox, container, false);
        loadView(view);
        return view;
    }

    protected void loadArgument(@NonNull Bundle bundle) {

        title = bundle.getString(ARG_TITLE);
        String[] options = bundle.getStringArray(ARG_OPTIONS);
        String[] optionsChecked = bundle.getStringArray(ARG_OPTIONS_CHECKED);

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

        if(optionsChecked!=null) {
            if (optionsChecked.length >= 1) {
                button1Check = ManageUIConst.LabelProperty.CHECKED.equals(optionsChecked[0]);
            }
            if (optionsChecked.length >= 2) {
                button2Check = ManageUIConst.LabelProperty.CHECKED.equals(optionsChecked[1]);
            }
            if (optionsChecked.length >= 3) {
                button3Check = ManageUIConst.LabelProperty.CHECKED.equals(optionsChecked[2]);
            }
            if (optionsChecked.length >= 4) {
                button4Check = ManageUIConst.LabelProperty.CHECKED.equals(optionsChecked[3]);
            }
        }

    }

    protected void loadView(View rootView) {
        TextView textView = rootView.findViewById(R.id.title);
        textView.setText(title);

        btn1 = rootView.findViewById(R.id.button1);
        formatButton(btn1, button1, button1Check);

        btn2 = rootView.findViewById(R.id.button2);
        formatButton(btn2, button2,  button2Check);

        btn3 = rootView.findViewById(R.id.button3);
        formatButton(btn3, button3,  button3Check);

        btn4 = rootView.findViewById(R.id.button4);
        formatButton(btn4, button4, button4Check);

        Button confirmButton = rootView.findViewById(R.id.confirm_button);
        confirmButton.setOnClickListener(v -> onConfirmButtonClicked());

        Button resetButton = rootView.findViewById(R.id.reset_button);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn1.setChecked(false);
                btn2.setChecked(false);
                btn3.setChecked(false);
                btn4.setChecked(false);
            }
        });
    }

    private void formatButton(CheckBox button, String message, boolean checked){
        if(!TextUtils.isEmpty(message)){
            button.setText(message);
            button.setChecked(checked);
        }else{
            button.setVisibility(View.GONE);
        }
    }

    private void onConfirmButtonClicked(){
        StringBuilder ret = new StringBuilder();
        if(btn1.isChecked()){
            ret.append("1,");
        }
        if(btn2.isChecked()){
            ret.append("2,");
        }
        if(btn3.isChecked()){
            ret.append("3,");
        }
        if(btn4.isChecked()){
            ret.append("4,");
        }

        if(!TextUtils.isEmpty(ret)){
            ret.deleteCharAt(ret.length()-1);

            Bundle bundle = new Bundle();
            bundle.putString(CHECKED_INDEX, ret.toString());
            getParentFragmentManager().setFragmentResult(RESULT, bundle);
        }
    }

}
