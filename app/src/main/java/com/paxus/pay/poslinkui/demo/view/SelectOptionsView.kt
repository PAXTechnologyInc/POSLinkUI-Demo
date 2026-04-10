package com.paxus.pay.poslinkui.demo.view

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.AbsoluteSizeSpan
import android.text.style.StyleSpan
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckedTextView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.paxus.pay.poslinkui.demo.R
import com.paxus.pay.poslinkui.demo.utils.Logger
import com.paxus.pay.poslinkui.demo.view.SelectOptionsView.OptionAdapter.OptionHolder

class SelectOptionsView : RecyclerView {
    private var isInitialized = false
    private var isModified = false
    private var optionAdapter: OptionAdapter? = null
    private val siblings: MutableSet<SelectOptionsView> = HashSet()

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    fun initialize(
        activity: Activity?,
        columnCount: Int,
        options: MutableList<Option>,
        listener: OptionSelectListener
    ) {
        optionAdapter = OptionAdapter(activity, options, listener)
        layoutManager = GridLayoutManager(activity, columnCount)
        isInitialized = true
        super.setAdapter(optionAdapter)
    }

    fun clear() {
        if (!isInitialized) {
            Logger.e(
                "${javaClass.simpleName} not yet initialized; clear() cannot be executed."
            )
            return
        }

        if (isModified) {
            val ad = optionAdapter
            val root = this.rootView as? ViewGroup
            if (ad != null && root != null) ad.unselectAllOptions(root)
            this.isModified = false
        }

        if (siblings.isNotEmpty()) {
            for (sibling in siblings) {
                if (sibling.isModified) sibling.clear()
            }
        }
    }

    fun append(selectOptionsView: SelectOptionsView) {
        if (!selectOptionsView.isInitialized) {
            Logger.e(
                "${selectOptionsView.javaClass.simpleName} not yet initialized; append() cannot be executed."
            )
            return
        }

        for (sibling in this.siblings) {
            sibling.addSibling(selectOptionsView)
            selectOptionsView.addSibling(sibling)
        }
        this.siblings.add(selectOptionsView)
        selectOptionsView.addSibling(this)
    }

    private fun addSibling(selectOptionsView: SelectOptionsView?) {
        selectOptionsView?.let { this.siblings.add(it) }
    }

    class Option(var index: Int?, var title: String?, var subtitle: String?, var value: Any?)

    fun interface OptionSelectListener {
        fun onSelect(option: Option?)
    }

    private inner class OptionAdapter(
        inflationContext: Context?,
        private val options: MutableList<Option>,
        private val optionSelectListener: OptionSelectListener
    ) : Adapter<OptionHolder>() {
        private val layoutInflater: LayoutInflater
        private var isDoubleHeightRequired = false

        init {
            this.layoutInflater = LayoutInflater.from(inflationContext)
            for (option in this.options) {
                if (option.title != null && option.subtitle != null) {
                    isDoubleHeightRequired = true
                    break
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionHolder {
            val holder = layoutInflater.inflate(R.layout.layout_select_option_item, parent, false)
            if (isDoubleHeightRequired) {
                val layoutParams = holder.layoutParams
                layoutParams.height =
                    resources.getDimension(R.dimen.button_height_double_line).toInt()
                holder.layoutParams = layoutParams
            }
            return OptionHolder(holder)
        }

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: OptionHolder, position: Int) {
            val option = options[position]
            val text = SpannableStringBuilder()

            val titleText = option.title
            val subtitleText = option.subtitle
            if (titleText != null) {
                text.append(titleText)
                text.setSpan(
                    AbsoluteSizeSpan(
                        resources.getDimension(R.dimen.text_size_subtitle).toInt()
                    ), 0, titleText.length, Spannable.SPAN_INCLUSIVE_INCLUSIVE
                )
            }
            if (subtitleText != null) {
                text.append(if (titleText != null) "\n" else "").append(subtitleText)
                val subtitleSizePx = if (titleText != null) {
                    resources.getDimension(R.dimen.text_size_hint)
                } else {
                    resources.getDimension(R.dimen.text_size_subtitle)
                }
                text.setSpan(
                    AbsoluteSizeSpan(subtitleSizePx.toInt()),
                    text.length - subtitleText.length,
                    text.length,
                    Spannable.SPAN_INCLUSIVE_INCLUSIVE
                )
                if (titleText != null) {
                    text.setSpan(
                        StyleSpan(Typeface.BOLD),
                        0,
                        titleText.length,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }
            }

            holder.text.text = text
            holder.bindListener(option, optionSelectListener)
        }

        override fun getItemCount(): Int {
            return options.size
        }

        fun unselectAllOptions(parent: ViewGroup) {
            for (i in 0 until parent.childCount) {
                val child = parent.getChildAt(i)
                when (child) {
                    is CheckedTextView -> child.isChecked = false
                    is ViewGroup -> unselectAllOptions(child)
                    else -> Unit
                }
            }
        }

        private inner class OptionHolder(itemView: View) : ViewHolder(itemView) {
            var text: TextView

            init {
                text = itemView.findViewById<TextView>(R.id.layout_select_option_item_text)
                text.textAlignment = TEXT_ALIGNMENT_CENTER
            }

            fun bindListener(option: Option?, listener: OptionSelectListener) {
                itemView.setOnClickListener {
                    listener.onSelect(option)
                    clear()
                    (text as CheckedTextView).isChecked = true
                    isModified = true
                }
            }
        }
    }

    companion object {
        fun buildOptions(options: Array<String?>?): MutableList<Option?> {
            val list: MutableList<Option?> = ArrayList<Option?>()
            if (options == null) return list

            for (i in options.indices) {
                list.add(Option(null, options[i], null, i))
            }
            return list
        }
    }
}
