package com.paxus.pay.poslinkui.demo.entry.text;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.paxus.pay.poslinkui.demo.R;
import com.paxus.pay.poslinkui.demo.entry.option.OptionsDialogFragment;
import com.paxus.pay.poslinkui.demo.utils.Logger;

/**
 * Created by Yanina.Yang on 5/19/2022.
 */
public class GeneralOptionsDialogFragment extends DialogFragment {

    public static final String TITLE = "title";
    public static final String OPTIONS = "options";

    public static final String RESULT = "result";
    public static final String INDEX = "index";

    public static DialogFragment newInstance(String title, String[] options){
        if(options == null || options.length <= 0){
            throw  new IllegalArgumentException("options could not be none.");
        }
        OptionsDialogFragment dialogFragment = new OptionsDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TITLE, title);
        bundle.putStringArray(OPTIONS, options);

        dialogFragment.setArguments(bundle);
        return dialogFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Logger.d(this.getClass().getSimpleName()+" onCreateView");
        //------1. Load layout in onCreateView (getLayoutResourceId, loadParameter, loadView)---------
        View view = inflater.inflate(R.layout.fragment_option_dialog, container, false);

        Bundle bundle = getArguments();
        if(bundle!= null) {
            loadParameter(bundle);
        }

        loadView(view);

        Dialog dialog = getDialog();
        if (dialog != null) {
            //-------2. Dialog should not be canceled by touch outside-------
            dialog.setCanceledOnTouchOutside(false);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP){
                        onBackPressed();
                        return true;
                    }
                    return false;
                }
            });
        }
        return view;
    }

    private String title;
    private String[] items;
    private ListView listView;

    void loadParameter(@NonNull Bundle bundle){
        title = bundle.getString(TITLE);
        items = bundle.getStringArray(OPTIONS);
    }

    void loadView(View rootView){
        TextView titleView = rootView.findViewById(R.id.title_view);
        titleView.setText(title);

        listView = rootView.findViewById(R.id.list_view);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(requireContext(),
                android.R.layout.simple_list_item_single_choice, items);
        listView.setAdapter(adapter);

        Button cancelButton = rootView.findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(v-> onCancelButtonClicked());

        Button confirmButton = rootView.findViewById(R.id.confirm_button);
        confirmButton.setOnClickListener(v->onConfirmButtonClicked());
    }

    private void onCancelButtonClicked(){

    }

    private void onBackPressed(){

    }

    private void onConfirmButtonClicked(){
        if(listView.getCheckedItemPosition()>= 0 ){
            Bundle bundle = new Bundle();
            bundle.putInt(INDEX, listView.getCheckedItemPosition());
            getParentFragmentManager().setFragmentResult(RESULT, bundle);
        }
    }
}
