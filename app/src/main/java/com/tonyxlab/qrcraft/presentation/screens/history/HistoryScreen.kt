package com.tonyxlab.qrcraft.presentation.screens.history

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.tonyxlab.qrcraft.R
import com.tonyxlab.qrcraft.navigation.NavOperations
import com.tonyxlab.qrcraft.presentation.core.base.BaseContentLayout
import com.tonyxlab.qrcraft.presentation.core.components.AppTopBar
import com.tonyxlab.qrcraft.presentation.screens.history.components.HistoryBottomSheet
import com.tonyxlab.qrcraft.presentation.screens.history.components.HistoryTabRow
import com.tonyxlab.qrcraft.presentation.screens.history.handling.HistoryActionEvent
import com.tonyxlab.qrcraft.presentation.screens.history.handling.HistoryUiEvent
import com.tonyxlab.qrcraft.presentation.screens.history.handling.HistoryUiState
import com.tonyxlab.qrcraft.util.SetStatusBarIconsColor
import org.koin.androidx.compose.koinViewModel

@Composable
fun HistoryScreen(
    navOperations: NavOperations,
    modifier: Modifier = Modifier,
    viewModel: HistoryViewModel = koinViewModel()
) {

    SetStatusBarIconsColor(darkIcons = true)

    val context = LocalContext.current

    BaseContentLayout(
            modifier = modifier,
            viewModel = viewModel,
            topBar = {
                AppTopBar(
                        screenTitle = stringResource(id = R.string.topbar_text_history),
                        contentColor = MaterialTheme.colorScheme.onSurface
                )
            },
            containerColor = MaterialTheme.colorScheme.surface,
            actionEventHandler = { _, action ->
                when (action) {

                    HistoryActionEvent.ExitHistoryScreen -> navOperations.popBackStack()

                    is HistoryActionEvent.OpenShareMenu -> {

                        val intent = Intent().apply {

                            this.action = Intent.ACTION_SEND
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, action.text)
                        }

                        context.startActivity(intent)
                    }

                    is HistoryActionEvent.ShowToast -> {

                        Toast.makeText(context, action.messageRes, Toast.LENGTH_SHORT)
                                .show()
                    }

                    is HistoryActionEvent.NavigateToPreview -> {
                        navOperations.navigateToPreviewScreenDestination(id = action.id)
                    }
                }
            }, onBackPressed = { viewModel.onEvent(HistoryUiEvent.ExitHistoryScreen) }
    ) {

        uiState ->

        HistoryScreenContent(
                uiState = uiState,
                onEvent = viewModel::onEvent
        )
    }

}

@Composable
private fun HistoryScreenContent(
    uiState: HistoryUiState,
    onEvent: (HistoryUiEvent) -> Unit
) {

    val pagerState = rememberPagerState { 2 }

    Column {

        HistoryTabRow(
                uiState = uiState,
                pagerState = pagerState,
                onEvent = onEvent
        )
        HistoryBottomSheet(
                uiState = uiState,
                onEvent = onEvent
        )
    }
}