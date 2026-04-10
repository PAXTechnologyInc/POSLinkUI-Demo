package com.paxus.pay.poslinkui.demo.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.paxus.pay.poslinkui.demo.R
import com.paxus.pay.poslinkui.demo.utils.interfacefilter.EntryActionFilterManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class EntryActionAndCategoryFilterFragment : Fragment(R.layout.fragment_interface_action_filter) {
    @Inject
    lateinit var entryActionFilterManager: EntryActionFilterManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState) ?: return null

        val categories = view.findViewById<ViewPager2>(R.id.interface_filter_view_pager)
        categories.adapter = InterfaceActionFilterPagerAdapter(
            childFragmentManager,
            lifecycle
        )

        return view
    }

    private inner class InterfaceActionFilterPagerAdapter(
        fragmentManager: FragmentManager,
        lifecycle: Lifecycle
    ) : FragmentStateAdapter(fragmentManager, lifecycle) {
        override fun createFragment(position: Int): Fragment {
            return entryActionFilterManager.getCategories().getOrNull(position)?.let {
                EntryActionFilterByCategoryFragment(it)
            } ?: throw IndexOutOfBoundsException("Invalid category position: $position")
        }

        override fun getItemCount(): Int {
            return entryActionFilterManager.getCategories().size
        }
    }
}
