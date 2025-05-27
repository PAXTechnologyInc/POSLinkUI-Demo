package com.paxus.pay.poslinkui.demo.entry.poslink;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
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

import java.util.List;

/**
 * Used by {@value PoslinkEntry#ACTION_SHOW_DIALOG_FORM}
 * <p>
 *     UI Tips:
 *     1. Index start from 1
 *
 * </p>
 */
public class ShowDialogFormCheckBoxFragment extends Fragment{
    public static final String ARG_TITLE = "title";
    public static final String ARG_OPTIONS = "options";
    public static final String ARG_OPTIONS_CHECKED = "options_checked";

    public static final String RESULT = "result";
    public static final String CHECKED_INDEX = "checked_index";

    private String title;
    RecyclerView recyclerView;
    private LabelAdapter adapter;

    private Bundle reqBundle;

    public static Fragment newInstance(String title, String[] options, String[] optionsChecked){
        ShowDialogFormCheckBoxFragment fragment = new ShowDialogFormCheckBoxFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_TITLE, title);
        bundle.putStringArray(ARG_OPTIONS, options);
        bundle.putStringArray(ARG_OPTIONS_CHECKED, optionsChecked);
        bundle.putString(EntryExtraData.PARAM_BUTTON_TYPE, ManageUIConst.ButtonType.CHECK_BOX);

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
        reqBundle = bundle;
        title = bundle.getString(ARG_TITLE);
    }

    protected void loadView(View rootView) {
        LinearLayout titleLayout = rootView.findViewById(R.id.title_layout_show_dialog_form_checkout);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
        for (TextView textView: TextShowingUtils.getTitleViewList(requireContext(), title, lp, Color.WHITE, requireContext().getResources().getDimension(R.dimen.text_size_title))) {
            titleLayout.addView(textView);
        }
        recyclerView = rootView.findViewById(R.id.my_recycler_view);
        setupRecyclerView(recyclerView);

        Button confirmButton = rootView.findViewById(R.id.confirm_button);
        confirmButton.setOnClickListener(v -> onConfirmButtonClicked());

        Button resetButton = rootView.findViewById(R.id.reset_button);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.clearCheckedItems();
            }
        });
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setItemViewCacheSize(4);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_AUTO);
        adapter = new LabelAdapter(requireContext(),reqBundle,null);
        recyclerView.setAdapter(adapter);
    }

    private void onConfirmButtonClicked(){
        StringBuilder sb = new StringBuilder();
        List<LabelAdapter.MyItem> itemList = adapter.getAllSelectedItems();
        for(LabelAdapter.MyItem item:itemList){
            sb.append(item.getLabelId()).append(",");
        }
        String selectItem = sb.toString();
        if (selectItem.endsWith(","))
            selectItem = selectItem.substring(0, selectItem.length() - 1);
        if (!TextUtils.isEmpty(selectItem)){
            Bundle bundle = new Bundle();
            bundle.putString(CHECKED_INDEX, selectItem.toString());
            getParentFragmentManager().setFragmentResult(RESULT, bundle);
        }
    }

}
