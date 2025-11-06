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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.core.content.getSystemService
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tonyxlab.qrcraft.R
import com.tonyxlab.qrcraft.navigation.NavOperations
import com.tonyxlab.qrcraft.presentation.core.base.BaseContentLayout
import com.tonyxlab.qrcraft.presentation.core.components.AppTopBar
import com.tonyxlab.qrcraft.presentation.core.components.PreviewContainer
import com.tonyxlab.qrcraft.presentation.core.utils.spacing
import com.tonyxlab.qrcraft.presentation.screens.result.components.EditableText
import com.tonyxlab.qrcraft.presentation.screens.result.handling.ResultActionEvent
import com.tonyxlab.qrcraft.presentation.screens.result.handling.ResultUiEvent
import com.tonyxlab.qrcraft.presentation.screens.result.handling.ResultUiState
import com.tonyxlab.qrcraft.presentation.theme.ui.QRCraftTheme
import com.tonyxlab.qrcraft.util.DeviceType
import com.tonyxlab.qrcraft.util.SetStatusBarIconsColor
import org.koin.androidx.compose.koinViewModel

@Composable
fun ResultScreen(
    navOperations: NavOperations,
    modifier: Modifier = Modifier,
    viewModel: ResultViewModel = koinViewModel()
) {
    SetStatusBarIconsColor(darkIcons = false)

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    BaseContentLayout(
            viewModel = viewModel,
            topBar = {
                AppTopBar(
                        modifier = modifier,
                        screenTitle = stringResource(id = R.string.topbar_text_scan_result),
                        contentColor = MaterialTheme.colorScheme.onTertiary,
                        onChevronIconClick = {
                            viewModel.onEvent(ResultUiEvent.ExitResultScreen)
                        },
                        isFavorite = uiState.qrData.favorite,
                        onMarkFavorite = { viewModel.onEvent(ResultUiEvent.ToggleFavorite) }
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
                        val clipboardManager =
                            context.getSystemService<ClipboardManager>()

                        val clip = ClipData.newPlainText(
                                "QR Content", action.text
                        )

                        clipboardManager?.setPrimaryClip(clip)
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

    val graduatedHorizontalPadding = when (deviceType) {
        DeviceType.MOBILE_PORTRAIT -> MaterialTheme.spacing.spaceMedium
        DeviceType.MOBILE_LANDSCAPE -> MaterialTheme.spacing.spaceTen * 10
        else -> MaterialTheme.spacing.spaceTen * 15
    }

    Box(
            modifier = modifier
                    .fillMaxSize()
                    .padding(horizontal = graduatedHorizontalPadding)
                    .padding(top = MaterialTheme.spacing.spaceOneHundredFifty),
            contentAlignment = Alignment.TopCenter
    ) {

        PreviewContainer(
                qrData = uiState.qrData,
                onShare = { onEvent(ResultUiEvent.ShareContent) },
                onCopy = { onEvent(ResultUiEvent.CopyContent) },
                editableText = {
                    EditableText(
                            modifier = modifier,
                            textFieldState = uiState.resultEditableTextState.textFieldState,
                            placeHolderText = uiState.qrData.displayName,
                            isEditing = uiState.resultEditableTextState.isEditing,
                            onClickText = { onEvent(ResultUiEvent.EditDetectedContent) }
                    )
                }
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
