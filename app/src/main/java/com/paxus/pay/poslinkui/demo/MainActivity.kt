package com.paxus.pay.poslinkui.demo

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.CheckedTextView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.paxus.pay.poslinkui.demo.settings.EntryActionAndCategoryFilterFragment
import com.paxus.pay.poslinkui.demo.utils.AnimationSupport
import com.paxus.pay.poslinkui.demo.utils.Logger
import com.paxus.pay.poslinkui.demo.utils.currentAnimationPolicy
import dagger.hilt.android.AndroidEntryPoint

/**
 * Launcher activity for the POSLink UI demo: shows build version, opens the entry filter UI from the
 * customize row, and registers fragment callbacks so that row state stays consistent.
 *
 * Exposed as the HOME intent target to simplify install/uninstall during development.
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        try {
            val appVer = packageManager.getPackageInfo(packageName, 0).versionName
            (findViewById<View?>(R.id.version) as TextView).text = "Version: $appVer"
        } catch (e: PackageManager.NameNotFoundException) {
            Logger.e(e, "Could not resolve package info for version label")
        }

        findViewById<View?>(R.id.customize_filter).setOnClickListener { view: View? ->
            supportFragmentManager.executePendingTransactions()
            val fragmentManager = supportFragmentManager
            val backStackName = EntryActionAndCategoryFilterFragment::class.java.simpleName
            val baseTransaction = fragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .addToBackStack(backStackName)
            val withAnimations = AnimationSupport.applyFragmentTransition(
                transaction = baseTransaction,
                pointId = "A1",
                policy = currentAnimationPolicy,
                standardEnter = R.anim.anim_enter_from_bottom,
                standardExit = R.anim.anim_exit_to_bottom,
            )
            withAnimations.replace(
                R.id.activity_main_fragment_container,
                EntryActionAndCategoryFilterFragment(),
            ).commit()
            (view as CheckedTextView).isChecked = true
        }

        supportFragmentManager.registerFragmentLifecycleCallbacks(object :
            FragmentManager.FragmentLifecycleCallbacks() {
            override fun onFragmentDetached(fm: FragmentManager, f: Fragment) {
                super.onFragmentDetached(fm, f)
                (findViewById<View>(R.id.customize_filter) as? CheckedTextView)?.isChecked = false
            }
        }, false)
    }

    override fun onStop() {
        super.onStop()
        finish()
    }
}
