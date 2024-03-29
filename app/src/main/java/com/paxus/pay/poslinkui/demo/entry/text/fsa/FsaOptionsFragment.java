package com.paxus.pay.poslinkui.demo.entry.text.fsa;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.paxus.pay.poslinkui.demo.R;
import com.paxus.pay.poslinkui.demo.utils.Logger;
import com.paxus.pay.poslinkui.demo.view.SelectOptionsView;

import java.util.List;

/**
 * Created by Yanina.Yang on 5/19/2022.
 */
public class FsaOptionsFragment extends Fragment {

    public static final String TITLE = "title";
    public static final String OPTIONS = "options";

    public static final String RESULT = "result";
    public static final String INDEX = "index";

    public static Fragment newInstance(String title, String[] options){
        if(options == null || options.length <= 0){
            throw  new IllegalArgumentException("options could not be none.");
        }
        Fragment fragment = new FsaOptionsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TITLE, title);
        bundle.putStringArray(OPTIONS, options);

        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Logger.d(this.getClass().getSimpleName()+" onCreateView");
        //------1. Load layout in onCreateView (getLayoutResourceId, loadParameter, loadView)---------
        View view = inflater.inflate(R.layout.fragment_fsa_option, container, false);

        Bundle bundle = getArguments();
        if(bundle!= null) {
            loadParameter(bundle);
        }

        loadView(view);

        return view;
    }

    private String title;
    private List<SelectOptionsView.Option> items;
    private SelectOptionsView selectOptionsView;

    private int resultIndex = -1;

    void loadParameter(@NonNull Bundle bundle){
        title = bundle.getString(TITLE);
        items = SelectOptionsView.buildOptions(bundle.getStringArray(OPTIONS));
    }

    void loadView(View rootView){
        TextView titleView = rootView.findViewById(R.id.title_view);
        titleView.setText(title);

        selectOptionsView = rootView.findViewById(R.id.select_options_view);
        selectOptionsView.initialize(getActivity(), 1, items, option -> {
            resultIndex = (int) option.getValue();
        });

        rootView.findViewById(R.id.confirm_button).setOnClickListener(v -> submit());
        getParentFragmentManager().setFragmentResultListener(FSAFragment.REQUEST_KEY_CONFIRM, this, (requestKey, result) -> submit());
    }

    private void submit(){
        if(resultIndex < 0) return;

        Bundle result = new Bundle();
        result.putInt(INDEX, resultIndex);
        getParentFragmentManager().setFragmentResult(RESULT, result);
    }
}
