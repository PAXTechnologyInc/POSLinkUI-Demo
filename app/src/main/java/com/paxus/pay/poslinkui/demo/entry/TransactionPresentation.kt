package com.paxus.pay.poslinkui.demo.entry

import android.app.Presentation
import android.content.Intent
import android.os.Bundle
import android.view.Display
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.lifecycle.lifecycleScope
import com.paxus.pay.poslinkui.demo.entry.compose.SecondScreenPresentationContent
import com.paxus.pay.poslinkui.demo.utils.Logger
import com.paxus.pay.poslinkui.demo.viewmodel.SecondScreenInfoViewModel
import com.paxus.pay.poslinkui.demo.viewmodel.SecondScreenInfoViewModel.ScreenInfo
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * Author: Elaine Xie
 * Date: 2025/11/25
 * Desc:
 */
class TransactionPresentation(
    var hostActivity: AppCompatActivity,
    display: Display?,
    var mintent: Intent?,
    private val viewModel: SecondScreenInfoViewModel
) : Presentation(
    hostActivity, display
) {
    private var rootComposeView: ComposeView? = null
    private var collectJob: Job? = null
    private var latestScreenInfo: ScreenInfo? by mutableStateOf(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val composeView = ComposeView(context)
        setViewTreeOwnerIfAvailable(
            className = "androidx.lifecycle.ViewTreeLifecycleOwner",
            composeView = composeView
        )
        setViewTreeOwnerIfAvailable(
            className = "androidx.savedstate.ViewTreeSavedStateRegistryOwner",
            composeView = composeView
        )
        setViewTreeOwnerIfAvailable(
            className = "androidx.lifecycle.ViewTreeViewModelStoreOwner",
            composeView = composeView
        )
        composeView.setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnDetachedFromWindow)
        rootComposeView = composeView
        setContentView(composeView)
        // ComposeView in Presentation must be attached before creating recomposer.
        composeView.post {
            if (rootComposeView === composeView) {
                composeView.setContent {
                    SecondScreenPresentationContent(screenInfo = latestScreenInfo)
                }
            }
        }

        collectJob = hostActivity.lifecycleScope.launch {
            viewModel.screenInfo.collectLatest { info ->
                latestScreenInfo = info
            }
        }
    }

    override fun dismiss() {
        collectJob?.cancel()
        collectJob = null
        rootComposeView?.disposeComposition()
        rootComposeView = null
        super.dismiss()
    }

    private fun setViewTreeOwnerIfAvailable(className: String, composeView: ComposeView) {
        try {
            val ownerClass = Class.forName(className)
            val setMethod = ownerClass.methods.firstOrNull {
                it.name == "set" && it.parameterTypes.size == 2
            } ?: return
            setMethod.invoke(null, composeView, hostActivity)
        } catch (t: Throwable) {
            Logger.w("Skip %s owner binding: %s", className, t.message)
        }
    }
}
