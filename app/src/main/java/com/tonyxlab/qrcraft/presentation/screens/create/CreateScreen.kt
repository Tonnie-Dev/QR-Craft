package com.tonyxlab.qrcraft.presentation.screens.create

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.window.core.layout.WindowSizeClass
import com.tonyxlab.qrcraft.R
import com.tonyxlab.qrcraft.domain.QrDataType
import com.tonyxlab.qrcraft.navigation.NavOperations
import com.tonyxlab.qrcraft.presentation.core.base.BaseContentLayout
import com.tonyxlab.qrcraft.presentation.core.components.AppTopBar
import com.tonyxlab.qrcraft.presentation.core.utils.spacing
import com.tonyxlab.qrcraft.presentation.screens.create.components.QrOptionCard
import com.tonyxlab.qrcraft.presentation.screens.create.components.toUi
import com.tonyxlab.qrcraft.presentation.screens.create.handling.CreateActionEvent
import com.tonyxlab.qrcraft.presentation.screens.create.handling.CreateUiEvent
import com.tonyxlab.qrcraft.presentation.screens.create.handling.CreateUiEvent.SelectQrTab
import com.tonyxlab.qrcraft.presentation.theme.ui.QRCraftTheme
import com.tonyxlab.qrcraft.util.DeviceType
import com.tonyxlab.qrcraft.util.SetStatusBarIconsColor
import org.koin.androidx.compose.koinViewModel

@Composable
fun CreateScreen(
    navOperations: NavOperations,
    modifier: Modifier = Modifier,
    viewModel: CreateViewModel = koinViewModel()
) {
    SetStatusBarIconsColor(darkIcons = true)

    BaseContentLayout(
            modifier = modifier,
            viewModel = viewModel,
            actionEventHandler = { _, action ->

                when (action) {

                    is CreateActionEvent.NavigateToEntryScreen -> {
                        navOperations.navigateToEntryScreenDestination(action.qrDataType)
                    }

                    CreateActionEvent.NavigateToScanScreen -> {
                        navOperations.navigateToScanScreenDestination()
                    }
                }
            },
            topBar = {
                AppTopBar(
                        screenTitle = stringResource(id = R.string.topbar_text_create_qr),
                        topBarTextColor = MaterialTheme.colorScheme.onSurface,
                        onChevronIconClick = {
                            viewModel.onEvent(
                                    CreateUiEvent.ExitCreateScreen
                            )
                        }
                )
            },
            containerColor = MaterialTheme.colorScheme.surface,
            onBackPressed = { viewModel.onEvent(CreateUiEvent.ExitCreateScreen) }
    ) {

        CreateScreenContent(
                modifier = modifier,
                onEvent = viewModel::onEvent
        )
    }
}

@Composable
fun CreateScreenContent(
    onEvent: (CreateUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {

    val windowClass = currentWindowAdaptiveInfo().windowSizeClass
    val deviceType = DeviceType.fromWindowSizeClass(windowClass)

    val isWideDevice = when(deviceType){
        DeviceType.MOBILE_PORTRAIT -> false
        else -> true
    }


    val numberOfColumns = if (isWideDevice) 3 else 2
    LazyVerticalGrid(
            modifier = modifier.fillMaxWidth(),
            columns = GridCells.Fixed(count = numberOfColumns),
            contentPadding = PaddingValues(MaterialTheme.spacing.spaceMedium),
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.spaceSmall),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.spaceSmall)
    ) {

        items(
                items = QrDataType.entries,
                key = { it }
        ) { qrDataType ->

            QrOptionCard(
                    modifier = Modifier
                            .fillMaxWidth()
                            //.aspectRatio(1.3f),
                    ,qrUiType = qrDataType.toUi()
            ) {

                onEvent(SelectQrTab(qrDataType))
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun CreateScreenContent_Preview() {

    QRCraftTheme {

        Column(
                modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surface)

        ) {
            CreateScreenContent(onEvent = {})
        }
    }
}
