package com.paxus.pay.poslinkui.demo.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.paxus.pay.poslinkui.demo.R;
import com.paxus.pay.poslinkui.demo.utils.interfacefilter.EntryActionFilterManager;

public class EntryActionAndCategoryFilterFragment extends Fragment {

    public EntryActionAndCategoryFilterFragment() {
        super(R.layout.fragment_interface_action_filter);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        ViewPager2 categories = view.findViewById(R.id.interface_filter_view_pager);
        categories.setAdapter(new InterfaceActionFilterPagerAdapter(getChildFragmentManager(), getLifecycle()));

        //getParentFragmentManager().popBackStack(this.getClass().getSimpleName(), FragmentManager.POP_BACK_STACK_INCLUSIVE)
        return view;
    }

    private class InterfaceActionFilterPagerAdapter extends FragmentStateAdapter {

        public InterfaceActionFilterPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
            super(fragmentManager, lifecycle);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return new EntryActionFilterByCategoryFragment(EntryActionFilterManager.getCategories().get(position));
        }

        @Override
        public int getItemCount() {
            return EntryActionFilterManager.getCategories().size();
        }
    }
}
