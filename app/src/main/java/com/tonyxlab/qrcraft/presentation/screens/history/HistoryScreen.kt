package com.tonyxlab.qrcraft.presentation.screens.history

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.tonyxlab.qrcraft.R
import com.tonyxlab.qrcraft.navigation.NavOperations
import com.tonyxlab.qrcraft.presentation.core.base.BaseContentLayout
import com.tonyxlab.qrcraft.presentation.core.components.AppTopBar
import com.tonyxlab.qrcraft.presentation.screens.history.components.HistoryTabRow
import com.tonyxlab.qrcraft.presentation.screens.history.handling.HistoryActionEvent
import com.tonyxlab.qrcraft.presentation.screens.history.handling.HistoryUiEvent
import com.tonyxlab.qrcraft.presentation.screens.history.handling.HistoryUiState
import com.tonyxlab.qrcraft.util.getRandomQrDataItems
import org.koin.androidx.compose.koinViewModel

@Composable
fun HistoryScreen(
    navOperations: NavOperations,
    modifier: Modifier = Modifier,
    viewModel: HistoryViewModel = koinViewModel()
) {

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
                }
            }, onBackPressed = { viewModel.onEvent(HistoryUiEvent.PressBack) }
    ) {

        uiState ->

        HistoryScreenContent(uiState = uiState)
    }

}

@Composable
private fun HistoryScreenContent(uiState: HistoryUiState) {

    val pagerState = rememberPagerState { 2 }


    Column {

        /*Text(
                text = "Scan History",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineSmall
        )*/

        HistoryTabRow(
                uiState = HistoryUiState(historyList = getRandomQrDataItems(13)),
                pagerState = pagerState
        )

    }

}