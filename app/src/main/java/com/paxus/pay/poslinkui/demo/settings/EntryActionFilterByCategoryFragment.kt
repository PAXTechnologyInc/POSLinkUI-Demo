package com.paxus.pay.poslinkui.demo.settings

import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckedTextView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.paxus.pay.poslinkui.demo.R
import com.paxus.pay.poslinkui.demo.utils.interfacefilter.EntryActionFilterManager
import com.paxus.pay.poslinkui.demo.utils.interfacefilter.EntryCategory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class EntryActionFilterByCategoryFragment(var category: EntryCategory) :
    Fragment(R.layout.fragment_interface_action_by_category) {
    @Inject
    lateinit var entryActionFilterManager: EntryActionFilterManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState) ?: return null

        (view.findViewById<View>(R.id.interface_filter_title) as TextView).text = category.name

        val interfaceActionRecyclerView =
            view.findViewById<RecyclerView>(R.id.interface_filter_recycler_view)
        interfaceActionRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        interfaceActionRecyclerView.adapter = InterfaceActionFilterAdapter(
            requireContext(),
            entryActionFilterManager.getStaticEntryActionListByCategory(category.category)
        )

        return view
    }

    private inner class InterfaceActionFilterAdapter(
        hostContext: Context?,
        var entryActionList: MutableList<String?>
    ) : RecyclerView.Adapter<InterfaceActionFilterAdapter.InterfaceActionHolder>() {
        var layoutInflater: LayoutInflater

        init {
            this.layoutInflater = LayoutInflater.from(hostContext)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InterfaceActionHolder {
            val holder =
                layoutInflater.inflate(R.layout.layout_item_interface_action, parent, false)
            return InterfaceActionHolder(holder)
        }

        override fun onBindViewHolder(holder: InterfaceActionHolder, position: Int) {
            val entryAction = entryActionFilterManager.getSyncedEntryActionState(
                requireContext(),
                entryActionList[position]
            ) ?: return

            holder.interfaceActionNameTextView.text = entryAction.name
            holder.interfaceActionNameTextView.isChecked = entryAction.isCurrentlyEnabled
            holder.interfaceActionNameTextView.typeface =
                if (entryAction.isCurrentlyEnabled) Typeface.DEFAULT_BOLD else Typeface.DEFAULT

            holder.interfaceActionNameTextView.setOnClickListener { v: View? ->
                entryActionFilterManager.switchEntryActionState(
                    requireContext(),
                    entryAction.action,
                    !(v as CheckedTextView).isChecked,
                    EntryActionFilterManager.Callback { enabled: Boolean ->
                        val checkedTextView = v
                        checkedTextView.isChecked = enabled
                        checkedTextView.typeface =
                            if (enabled) Typeface.DEFAULT_BOLD else Typeface.DEFAULT
                    }
                )
            }

            holder.interfaceActionNameTextView.setOnLongClickListener { v: View? ->
                val checkedTextView = v as CheckedTextView
                checkedTextView.text =
                    if (checkedTextView.text.toString() == entryAction.name) {
                        entryAction.action
                    } else {
                        entryAction.name
                    }
                true
            }
        }

        override fun getItemCount(): Int {
            return entryActionList.size
        }

        private inner class InterfaceActionHolder(itemView: View) :
            RecyclerView.ViewHolder(itemView) {
            var interfaceActionNameTextView: CheckedTextView

            init {
                interfaceActionNameTextView =
                    itemView.findViewById<CheckedTextView>(R.id.interface_action_name)
            }
        }
    }
}
