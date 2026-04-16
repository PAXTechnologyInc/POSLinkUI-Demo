package com.paxus.pay.poslinkui.demo.entry.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.paxus.pay.poslinkui.demo.R
import com.paxus.pay.poslinkui.demo.entry.EntryActivity
import com.paxus.pay.poslinkui.demo.entry.compose.EntryScreenRouter
import com.paxus.pay.poslinkui.demo.entry.compose.LocalEntryInteractionLocked
import com.paxus.pay.poslinkui.demo.entry.compose.StatusEntryOverlay
import com.paxus.pay.poslinkui.demo.ui.PosLinkScreenRoot
import com.paxus.pay.poslinkui.demo.ui.components.PosLinkTopBar
import com.paxus.pay.poslinkui.demo.ui.components.PosLinkToastHost
import com.paxus.pay.poslinkui.demo.ui.theme.PosLinkDesignTokens
import com.paxus.pay.poslinkui.demo.viewmodel.EntryViewModel

/**
 * Activity-level [NavHost] for Entry + Compose status overlay (no status [androidx.fragment.app.Fragment]).
 */
@Composable
fun EntryNavigationHost(
    viewModel: EntryViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val statusOverlay by viewModel.statusOverlay.collectAsState()
    val interactionLocked by viewModel.interactionLocked.collectAsState()
    val activity = LocalContext.current as EntryActivity
    val wantsDemo = activity.intent.getBooleanExtra(EntryActivity.EXTRA_NAV_COMPOSE_DEMO, false)

    Box(modifier = modifier.fillMaxSize()) {
        key(uiState.revision, wantsDemo) {
            val navController = rememberNavController()
            val start =
                if (wantsDemo) TransactionRoute.ComposeDemo.route else TransactionRoute.EntryMain.route
            CompositionLocalProvider(
                LocalEntryInteractionLocked provides interactionLocked
            ) {
                NavHost(
                    navController = navController,
                    startDestination = start,
                    modifier = Modifier.fillMaxSize()
                ) {
                    composable(TransactionRoute.EntryMain.route) {
                        val overlay by viewModel.statusOverlay.collectAsState()
                        if (overlay != null) {
                            Box(
                                Modifier
                                    .fillMaxSize()
                                    .background(PosLinkDesignTokens.BackgroundColor)
                            )
                        } else {
                            EntryScreenRouter(state = uiState, viewModel = viewModel)
                        }
                    }
                    composable(TransactionRoute.ComposeDemo.route) {
                        val ctx = LocalContext.current
                        PosLinkScreenRoot {
                            Column(Modifier.fillMaxSize()) {
                                PosLinkTopBar(title = ctx.getString(R.string.app_name))
                                Text(
                                    text = "Nav demo: clear EXTRA_NAV_COMPOSE_DEMO for real Entry (state-driven screens).",
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        }
                    }
                }
            }
        }
        statusOverlay?.let { StatusEntryOverlay(overlay = it) }
        PosLinkToastHost()
    }
}
