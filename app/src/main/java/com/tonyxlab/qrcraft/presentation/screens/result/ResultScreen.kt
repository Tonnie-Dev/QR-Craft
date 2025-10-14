package com.tonyxlab.qrcraft.presentation.screens.result

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.tonyxlab.qrcraft.R
import com.tonyxlab.qrcraft.navigation.NavOperations
import com.tonyxlab.qrcraft.presentation.core.base.BaseContentLayout
import com.tonyxlab.qrcraft.presentation.core.components.AppTopBar
import com.tonyxlab.qrcraft.presentation.core.components.PreviewContainer
import com.tonyxlab.qrcraft.presentation.core.utils.spacing
import com.tonyxlab.qrcraft.presentation.screens.result.handling.ResultActionEvent
import com.tonyxlab.qrcraft.presentation.screens.result.handling.ResultUiEvent
import com.tonyxlab.qrcraft.presentation.screens.result.handling.ResultUiState
import com.tonyxlab.qrcraft.presentation.theme.ui.QRCraftTheme
import com.tonyxlab.qrcraft.util.DeviceType
import com.tonyxlab.qrcraft.util.ifThen
import org.koin.androidx.compose.koinViewModel

@Composable
fun ResultScreen(
    navOperations: NavOperations,
    modifier: Modifier = Modifier,
    viewModel: ResultViewModel = koinViewModel()
) {
    val context = LocalContext.current
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
                        navOperations.navigateToScanScreenDestination(true)
                    }

                    is ResultActionEvent.ShareText -> {
                        val sendIntent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, action.text)
                        }
                        val chooser = Intent.createChooser(
                                sendIntent,
                                context.getText(R.string.cap_text_share_with)
                        )
                        context.startActivity(chooser)
                    }

                    is ResultActionEvent.CopyText -> {
                        val clipboard = context.getSystemService(
                                ClipboardManager::class.java
                        )

                        val clip = ClipData.newPlainText(
                                "QR Content", action.text
                        )
                        clipboard.setPrimaryClip(clip)
                    }

                    is ResultActionEvent.ShowToastMessage -> {

                        Toast.makeText(
                                context,
                                context.getText(action.messageRes),
                                Toast.LENGTH_SHORT
                        )
                                .show()
                    }
                }
            },
            containerColor = MaterialTheme.colorScheme.onSurface,
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

    val windowClass = currentWindowAdaptiveInfo().windowSizeClass
    val deviceType = DeviceType.fromWindowSizeClass(windowClass)
    val isWideDevice = when (deviceType) {

        DeviceType.MOBILE_PORTRAIT -> true
        else -> false
    }
    Box(
            modifier = Modifier
                    .fillMaxSize()
                    .ifThen(isWideDevice) {
                        padding(horizontal = MaterialTheme.spacing.spaceTen * 6)
                                .padding(vertical = MaterialTheme.spacing.spaceMedium)
                    }
                    .ifThen(isWideDevice.not()) {

                        padding(horizontal = MaterialTheme.spacing.spaceMedium)
                                .padding(vertical = MaterialTheme.spacing.spaceMedium)
                    },

            contentAlignment = Alignment.Center
    ) {
        PreviewContainer(
                modifier = modifier,
                qrData = uiState.dataState.qrData,
                onShare = { onEvent(ResultUiEvent.ShareContent) },
                onCopy = { onEvent(ResultUiEvent.CopyContent) },
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
