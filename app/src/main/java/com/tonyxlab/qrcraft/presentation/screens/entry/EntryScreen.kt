package com.tonyxlab.qrcraft.presentation.screens.entry

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
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
import com.tonyxlab.qrcraft.presentation.theme.ui.QRCraftTheme
import com.tonyxlab.qrcraft.util.DeviceType
import com.tonyxlab.qrcraft.util.ifThen
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
                        contentColor = MaterialTheme.colorScheme.onSurface
                ) {
                    viewModel.onEvent(EntryUiEvent.ExitEntryScreen)
                }
            },

            actionEventHandler = { _, action ->
                when (action) {
                    EntryActionEvent.NavigateToCreateScreen -> {
                        navOperations.navigateToCreateScreenDestination(fromEntryScreen = true)
                    }

                    is EntryActionEvent.NavigateToPreviewScreen -> {

                        navOperations.navigateToPreviewScreenDestination(jsonMapString = action.jsonMapString)
                    }
                }
            },
            containerColor = MaterialTheme.colorScheme.surface,
            onBackPressed = {
                viewModel.onEvent(EntryUiEvent.ExitEntryScreen)
            }
    ) { uiState ->

        EntryScreenContent(
                modifier = modifier,
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

    val windowClass = currentWindowAdaptiveInfo().windowSizeClass
    val deviceType = DeviceType.fromWindowSizeClass(windowClass)

    val isWideDevice = when (deviceType) {

        DeviceType.MOBILE_PORTRAIT -> false
        else -> true
    }
    val graduatedHorizontalPadding = when (deviceType) {

        DeviceType.MOBILE_PORTRAIT -> MaterialTheme.spacing.spaceMedium
        DeviceType.TABLET_PORTRAIT -> MaterialTheme.spacing.spaceTen * 3
        DeviceType.MOBILE_LANDSCAPE -> MaterialTheme.spacing.spaceTen * 5
        else -> MaterialTheme.spacing.spaceTen * 10

    }

    val fields = uiState.formFields

    Column(
            modifier = modifier
                    .padding(horizontal = graduatedHorizontalPadding)
                    .padding(top = MaterialTheme.spacing.spaceMedium),
    ) {
        Column(
                modifier = Modifier
                        .fillMaxWidth()
                        .clip(MaterialTheme.shapes.large)
                        .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                        .ifThen(isWideDevice) {
                            padding(MaterialTheme.spacing.spaceTwelve * 2)
                        }
                        .ifThen(!isWideDevice) {

                            padding(MaterialTheme.spacing.spaceMedium)
                        },
                verticalArrangement = if (isWideDevice)
                    Arrangement.spacedBy(MaterialTheme.spacing.spaceMedium)
                else
                    Arrangement.spacedBy(MaterialTheme.spacing.spaceSmall)
        ) {

            fields.forEach { field ->

                EntryTextField(
                        textFieldState = field.textFieldState,
                        placeholderString = field.placeHolder,
                )
            }

            EntryButton(enabled = uiState.isValidForm) {
                onEvent(EntryUiEvent.GenerateQrCode)
            }

        }
    }
}

@PreviewLightDark
@Composable
private fun EntryScreenContent_Preview() {

    QRCraftTheme {
        Column(
                modifier = Modifier
                        .background(MaterialTheme.colorScheme.surface)
                        .fillMaxSize()
                        .padding(MaterialTheme.spacing.spaceMedium)
        ) {

            EntryScreenContent(
                    uiState = EntryUiState(),
                    onEvent = {}
            )
        }
    }
}