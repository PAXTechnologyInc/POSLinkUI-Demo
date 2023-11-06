package com.paxus.pay.poslinkui.demo.settings;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.paxus.pay.poslinkui.demo.R;
import com.paxus.pay.poslinkui.demo.utils.interfacefilter.EntryAction;
import com.paxus.pay.poslinkui.demo.utils.interfacefilter.EntryCategory;
import com.paxus.pay.poslinkui.demo.utils.interfacefilter.EntryActionFilterManager;

import java.util.List;

public class EntryActionFilterByCategoryFragment extends Fragment {
    EntryCategory category;

    public EntryActionFilterByCategoryFragment(EntryCategory category) {
        super(R.layout.fragment_interface_action_by_category);
        this.category = category;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        ((TextView) view.findViewById(R.id.interface_filter_title)).setText(category.name);

        RecyclerView interfaceActionRecyclerView = view.findViewById(R.id.interface_filter_recycler_view);
        interfaceActionRecyclerView.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(getContext()));
        interfaceActionRecyclerView.setAdapter(new InterfaceActionFilterAdapter(getContext(), EntryActionFilterManager.getStaticEntryActionListByCategory(category.category)));

        return view;
    }

    private class InterfaceActionFilterAdapter extends RecyclerView.Adapter<InterfaceActionFilterAdapter.InterfaceActionHolder> {
        LayoutInflater layoutInflater;
        List<String> entryActionList;

        public InterfaceActionFilterAdapter(Context context, List<String> entryActionList) {
            this.layoutInflater = LayoutInflater.from(context);
            this.entryActionList = entryActionList;
        }

        @NonNull
        @Override
        public InterfaceActionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View holder = layoutInflater.inflate(R.layout.layout_item_interface_action, parent, false);
            return new InterfaceActionHolder(holder);
        }

        @Override
        public void onBindViewHolder(@NonNull InterfaceActionHolder holder, int position) {
            EntryAction entryAction = EntryActionFilterManager.getSyncedEntryActionState(getContext(), entryActionList.get(position));

            holder.interfaceActionNameTextView.setText(entryAction.name);
            holder.interfaceActionNameTextView.setChecked(entryAction.isCurrentlyEnabled);
            holder.interfaceActionNameTextView.setTypeface(entryAction.isCurrentlyEnabled ? Typeface.DEFAULT_BOLD : Typeface.DEFAULT);

            holder.interfaceActionNameTextView.setOnClickListener(v -> EntryActionFilterManager.switchEntryActionState(getContext(), entryAction.action, !((CheckedTextView) v).isChecked(), enabled -> {
                CheckedTextView checkedTextView = (CheckedTextView) v;
                checkedTextView.setChecked(enabled);
                checkedTextView.setTypeface(enabled ? Typeface.DEFAULT_BOLD : Typeface.DEFAULT);
            }));

            holder.interfaceActionNameTextView.setOnLongClickListener(v -> {
                CheckedTextView checkedTextView = (CheckedTextView) v;
                checkedTextView.setText(checkedTextView.getText().toString().equals(entryAction.name) ? entryAction.action : entryAction.name);
                return true;
            });
        }

        @Override
        public int getItemCount() {
            return entryActionList.size();
        }

        private class InterfaceActionHolder extends RecyclerView.ViewHolder {
            public CheckedTextView interfaceActionNameTextView;

            public InterfaceActionHolder(@NonNull View itemView) {
                super(itemView);
                interfaceActionNameTextView = itemView.findViewById(R.id.interface_action_name);
            }
        }


    }
}
