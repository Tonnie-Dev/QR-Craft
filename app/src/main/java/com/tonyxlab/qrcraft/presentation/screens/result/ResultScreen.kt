package com.tonyxlab.qrcraft.presentation.screens.result

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.tonyxlab.qrcraft.R
import com.tonyxlab.qrcraft.navigation.NavOperations
import com.tonyxlab.qrcraft.presentation.core.base.BaseContentLayout
import com.tonyxlab.qrcraft.presentation.core.components.AppTopBar
import com.tonyxlab.qrcraft.presentation.core.utils.spacing
import com.tonyxlab.qrcraft.presentation.screens.result.components.ResultContainer
import com.tonyxlab.qrcraft.presentation.screens.result.handling.ResultActionEvent
import com.tonyxlab.qrcraft.presentation.screens.result.handling.ResultUiEvent
import com.tonyxlab.qrcraft.presentation.screens.result.handling.ResultUiState
import com.tonyxlab.qrcraft.presentation.theme.ui.QRCraftTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun ResultScreen(
    navOperations: NavOperations,
    modifier: Modifier = Modifier,
    viewModel: ResultViewModel = koinViewModel()
) {
    BaseContentLayout(
            viewModel = viewModel,
            topBar = {
                AppTopBar(
                        modifier = modifier,
                        screenTitle = stringResource(id = R.string.topbar_text_scan_result),
                        onChevronIconClick = {
                            viewModel.onEvent(ResultUiEvent.ExitResultScreen)
                        },
                )
            },
            actionEventHandler = { _, action ->

                when (action) {

                    ResultActionEvent.NavigateToScanScreen -> {
                        navOperations.popBackStack()
                        navOperations.navigateToScanScreenDestination()
                    }
                }
            },
            onBackPressed = { viewModel.onEvent(ResultUiEvent.ExitResultScreen) }
    ) {

        ResultContentScreen(
                modifier = modifier,
                uiState = it,
                onEvent = viewModel::onEvent
        )
    }
}

@Composable
fun ResultContentScreen(
    uiState: ResultUiState,
    onEvent: (ResultUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {

    Box(
            modifier = Modifier
                    .fillMaxSize()
                    .padding(MaterialTheme.spacing.spaceMedium),
            contentAlignment = Alignment.Center
    ) {
        ResultContainer(
                uiState = uiState,
                onEvent = onEvent,
                modifier = modifier
        )
    }
}

@PreviewLightDark
@Composable
private fun ResultContentScreen_Preview() {
    QRCraftTheme {

        ResultContentScreen(
                uiState = ResultUiState(),
                onEvent = {}
        )
    }
}
