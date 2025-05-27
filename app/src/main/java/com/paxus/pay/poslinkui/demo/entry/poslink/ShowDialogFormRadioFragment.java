package com.paxus.pay.poslinkui.demo.entry.poslink;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pax.us.pay.ui.constant.entry.EntryExtraData;
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
public class ShowDialogFormRadioFragment extends Fragment implements LabelAdapter.SelectCallback {
    public static final String ARG_TITLE = "title";
    public static final String ARG_OPTIONS = "options";
    public static final String RESULT = "result";
    public static final String INDEX = "index";

    private String title;
    private int checkedIndex = -1;

    RecyclerView recyclerView;
    private LabelAdapter adapter;

    private Bundle reqBundle;

    public static Fragment newInstance(String title, String[] options){
        ShowDialogFormRadioFragment fragment = new ShowDialogFormRadioFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_TITLE, title);
        bundle.putStringArray(ARG_OPTIONS, options);
        bundle.putString(EntryExtraData.PARAM_BUTTON_TYPE, ManageUIConst.ButtonType.RADIO_BUTTON);

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
        reqBundle = bundle;
        title = bundle.getString(ARG_TITLE);
    }

    protected void loadView(View rootView) {

        LinearLayout titleLayout = rootView.findViewById(R.id.title_layout_show_dialog_from_radio);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
        for (TextView textView: TextShowingUtils.getTitleViewList(requireContext(), title, lp, Color.WHITE, requireContext().getResources().getDimension(R.dimen.text_size_title))) {
            titleLayout.addView(textView);
        }
        recyclerView = rootView.findViewById(R.id.my_recycler_view);
        setupRecyclerView(recyclerView);
        Button confirmButton = rootView.findViewById(R.id.confirm_button);
        confirmButton.setOnClickListener(v -> onConfirmButtonClicked());
    }


    private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setItemViewCacheSize(4);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_AUTO);
        adapter = new LabelAdapter(requireContext(),reqBundle,this);
        recyclerView.setAdapter(adapter);
    }

    private void onConfirmButtonClicked(){
        if(checkedIndex > 0){
            Bundle bundle = new Bundle();
            bundle.putInt(INDEX, checkedIndex);
            getParentFragmentManager().setFragmentResult(RESULT, bundle);
        }
    }

    @Override
    public void onItemSelect(int labelId) {
        checkedIndex = labelId;
    }
}
