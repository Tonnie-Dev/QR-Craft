package com.tonyxlab.qrcraft.presentation.screens.entry

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tonyxlab.qrcraft.navigation.NavOperations
import com.tonyxlab.qrcraft.presentation.core.base.BaseContentLayout
import com.tonyxlab.qrcraft.presentation.core.components.AppTopBar
import com.tonyxlab.qrcraft.presentation.core.utils.spacing
import com.tonyxlab.qrcraft.presentation.screens.entry.components.EntryButton
import com.tonyxlab.qrcraft.presentation.screens.entry.components.EntryTextField
import com.tonyxlab.qrcraft.presentation.screens.entry.handling.EntryActionEvent
import com.tonyxlab.qrcraft.presentation.screens.entry.handling.EntryUiEvent
import com.tonyxlab.qrcraft.presentation.screens.entry.handling.EntryUiState
import com.tonyxlab.qrcraft.util.toTopBarString
import org.koin.androidx.compose.koinViewModel

@Composable
fun EntryScreen(
    navOperations: NavOperations,
    modifier: Modifier = Modifier,
    viewModel: EntryViewModel = koinViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    BaseContentLayout(
            viewModel = viewModel,
            topBar = {
                AppTopBar(
                        screenTitle = stringResource(
                                id = state.selectedQrType.toTopBarString()
                        ),
                        topBarTextColor = MaterialTheme.colorScheme.onSurface,
                        iconTintColor = MaterialTheme.colorScheme.onSurface
                ){
                    viewModel.onEvent(EntryUiEvent.ExitEntryScreen)
                }
            },

            actionEventHandler = {_, action -> when(action){
                EntryActionEvent.NavigateToCreateScreen -> {
                    navOperations.navigateToCreateScreenDestination(fromEntryScreen = true)
                }
                EntryActionEvent.NavigateToPreviewScreen -> {}
            } },
            containerColor = MaterialTheme.colorScheme.surface
    ) { uiState ->

        EntryScreenContent(
                modifier = modifier.padding(MaterialTheme.spacing.spaceMedium),
                uiState = uiState, onEvent = viewModel::onEvent
        )
    }
}

@Composable
fun EntryScreenContent(
    uiState: EntryUiState,
    onEvent: (EntryUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {

    val fields = uiState.formFields

    Column(
            modifier = modifier
                    .clip(MaterialTheme.shapes.large)
                    .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                    .padding(MaterialTheme.spacing.spaceMedium),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.spaceMedium)
    ) {

        fields.forEach { field ->

            EntryTextField(
                    textFieldState = field.textFieldState,
                    placeholderString = field.placeHolder,
            )
        }

        EntryButton(enabled = false) {
            onEvent(EntryUiEvent.GenerateQrCode)
        }

    }

}