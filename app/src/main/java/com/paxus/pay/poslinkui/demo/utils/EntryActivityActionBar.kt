package com.paxus.pay.poslinkui.demo.utils

import android.app.Activity
import android.graphics.Typeface
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.core.content.ContextCompat
import com.paxus.pay.poslinkui.demo.R

/**
 * Custom ActionBar for [com.paxus.pay.poslinkui.demo.entry.EntryActivity]: transaction type title
 * ([com.pax.us.pay.ui.constant.entry.EntryExtraData.PARAM_TRANS_TYPE]) on a bar that matches the
 * Entry Compose background ([R.color.pastel_background]).
 *
 * Layout follows `golive/v1.03.00` [R.layout.action_bar]: title (start) + CLSS lights (end, [R.layout.layout_action_bar_clss_lights]).
 */
class EntryActivityActionBar(
    private val activityContext: Activity,
    private var actionBar: ActionBar
) {
    private var titleTextView: TextView? = null
    private var clssLightsContainer: View? = null

    enum class Status {
        IDLE, PROCESSING, SUCCESS, FAIL
    }

    init {
        this.initialize()
    }

    private fun initialize() {
        actionBar.setDisplayShowCustomEnabled(true)
        actionBar.setElevation(0f)
        actionBar.setDisplayHomeAsUpEnabled(false)
        actionBar.setDisplayShowTitleEnabled(false)

        val res = activityContext.resources
        val padH = res.getDimensionPixelSize(R.dimen.padding_horizontal)

        val root = LinearLayout(activityContext).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            setBackgroundColor(ContextCompat.getColor(activityContext, R.color.pastel_background))
            setPadding(padH, 0, padH, 0)
        }

        val titleView = TextView(activityContext).apply {
            layoutParams = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1f)
            gravity = Gravity.CENTER_VERTICAL or Gravity.START
            setTextColor(ContextCompat.getColor(activityContext, R.color.pastel_text_color))
            setTextSize(TypedValue.COMPLEX_UNIT_PX, res.getDimension(R.dimen.text_size_normal))
            setTypeface(typeface, Typeface.BOLD)
            text = ""
            isSingleLine = true
            includeFontPadding = false
        }
        titleTextView = titleView
        root.addView(titleView)

        val lights = LayoutInflater.from(activityContext)
            .inflate(R.layout.layout_action_bar_clss_lights, root, false)
        clssLightsContainer = lights
        lights.visibility = View.INVISIBLE
        listOf(
            R.id.action_bar_light1,
            R.id.action_bar_light2,
            R.id.action_bar_light3,
            R.id.action_bar_light4
        ).forEach { id ->
            lights.findViewById<ImageView>(id)?.isEnabled = false
        }
        val lightsLp = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        root.addView(lights, lightsLp)

        actionBar.setCustomView(
            root,
            ActionBar.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        )

        (actionBar.customView.parent as? ViewGroup)?.setPadding(0, 0, 0, 0)
    }

    fun setTitle(title: String?): EntryActivityActionBar {
        titleTextView?.text = title.orEmpty()
        return this
    }

    /**
     * @param visible When false, uses [View.INVISIBLE] like `golive` [R.id.action_bar_clss_light] default.
     */
    fun setClssLightsVisible(visible: Boolean): EntryActivityActionBar {
        clssLightsContainer?.visibility = if (visible) View.VISIBLE else View.INVISIBLE
        return this
    }

    fun setStatus(
        @Suppress("UNUSED_PARAMETER") status: Status,
        @Suppress("UNUSED_PARAMETER") loop: Boolean
    ): EntryActivityActionBar {
        return this
    }

    fun swapActionBar(actionBar: ActionBar): EntryActivityActionBar {
        this.actionBar = actionBar
        this.initialize()
        return this
    }
}
