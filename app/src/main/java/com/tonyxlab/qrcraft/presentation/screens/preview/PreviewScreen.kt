package com.tonyxlab.qrcraft.presentation.screens.preview

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.tonyxlab.qrcraft.R
import com.tonyxlab.qrcraft.navigation.NavOperations
import com.tonyxlab.qrcraft.presentation.core.base.BaseContentLayout
import com.tonyxlab.qrcraft.presentation.core.components.AppTopBar
import com.tonyxlab.qrcraft.presentation.screens.preview.handling.PreviewActionEvent
import com.tonyxlab.qrcraft.presentation.screens.preview.handling.PreviewUiEvent
import com.tonyxlab.qrcraft.presentation.screens.preview.handling.PreviewUiState
import com.tonyxlab.qrcraft.presentation.theme.ui.OnOverlay
import org.koin.androidx.compose.koinViewModel

@Composable
fun PreviewScreen(
    navOperations: NavOperations,
    modifier: Modifier = Modifier,
    viewModel: PreviewViewModel = koinViewModel()
) {
    BaseContentLayout(
            modifier = modifier,
            viewModel = viewModel,
            topBar = {

                AppTopBar(
                        screenTitle = stringResource(id = R.string.topbar_text_preview),
                        topBarTextColor = MaterialTheme.colorScheme.onTertiary,
                        iconTintColor =MaterialTheme.colorScheme.onTertiary,
                        onChevronIconClick = {viewModel.onEvent(event = PreviewUiEvent.ExitPreviewScreen)}
                )
            },
            actionEventHandler = { _, action ->
                when (action) {
                    PreviewActionEvent.NavigateToEntryScreen -> {
                        navOperations.popBackStack()
                    }
                }
            },
            containerColor = MaterialTheme.colorScheme.onSurface,
            onBackPressed = { viewModel.onEvent(PreviewUiEvent.ExitPreviewScreen) }
    ) {

        PreviewContentScreen(
                modifier = modifier,
                uiState = it,
                onEvent = viewModel::onEvent
        )
    }

}

@Composable
private fun PreviewContentScreen(
    uiState: PreviewUiState,
    onEvent: (PreviewUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {

    Column(modifier = Modifier.fillMaxSize()) {

        uiState.valuesMap.ifEmpty { Text(text = "Empty Map", color = OnOverlay) }

        uiState.valuesMap.values.forEach { value ->

            Text(text = value, color = OnOverlay)
        }
    }
    // PreviewContainer()
}