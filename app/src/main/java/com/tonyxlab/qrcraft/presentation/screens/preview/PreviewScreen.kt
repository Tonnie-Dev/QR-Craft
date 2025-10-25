package com.tonyxlab.qrcraft.presentation.screens.preview

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
import androidx.core.content.getSystemService
import com.tonyxlab.qrcraft.R
import com.tonyxlab.qrcraft.navigation.NavOperations
import com.tonyxlab.qrcraft.presentation.core.base.BaseContentLayout
import com.tonyxlab.qrcraft.presentation.core.components.AppTopBar
import com.tonyxlab.qrcraft.presentation.core.components.PreviewContainer
import com.tonyxlab.qrcraft.presentation.core.utils.spacing
import com.tonyxlab.qrcraft.presentation.screens.preview.handling.PreviewActionEvent
import com.tonyxlab.qrcraft.presentation.screens.preview.handling.PreviewUiEvent
import com.tonyxlab.qrcraft.presentation.screens.preview.handling.PreviewUiState
import com.tonyxlab.qrcraft.presentation.screens.result.components.EditableText
import com.tonyxlab.qrcraft.util.DeviceType
import com.tonyxlab.qrcraft.util.SetStatusBarIconsColor
import org.koin.androidx.compose.koinViewModel
import timber.log.Timber

@Composable
fun PreviewScreen(
    navOperations: NavOperations,
    modifier: Modifier = Modifier,
    viewModel: PreviewViewModel = koinViewModel()
) {
    SetStatusBarIconsColor(darkIcons = false)

    val context = LocalContext.current

    BaseContentLayout(
            modifier = modifier,
            viewModel = viewModel,
            topBar = {
                AppTopBar(
                        screenTitle = stringResource(id = R.string.topbar_text_preview),
                        contentColor = MaterialTheme.colorScheme.onTertiary,
                        onChevronIconClick = {
                            viewModel.onEvent(event = PreviewUiEvent.ExitPreviewScreen)
                        }
                )
            },
            actionEventHandler = { _, action ->
                when (action) {
                    PreviewActionEvent.NavigateToEntryScreen -> {
                        navOperations.popBackStack()
                    }

                    is PreviewActionEvent.ShareText -> {

                        val sendIntent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, action.text)
                        }

                        val chooser = Intent.createChooser(
                                sendIntent,
                                context.getString(R.string.cap_text_share_with)
                        )
                        context.startActivity(chooser)
                    }

                    is PreviewActionEvent.CopyText -> {
                        val clipboardManager =
                            context.getSystemService<ClipboardManager>()

                        val clip = ClipData.newPlainText(
                                "QR Content", action.text
                        )
                        clipboardManager?.setPrimaryClip(clip)
                    }

                    is PreviewActionEvent.ShowToast -> {

                        Toast.makeText(context, action.messageRes, Toast.LENGTH_SHORT)
                                .show()
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
                qrData = uiState.qrDataState.qrData,
                onShare = { onEvent(PreviewUiEvent.ShareContent) },
                onCopy = { onEvent(PreviewUiEvent.CopyContent) },
                editableText = {

                    Timber.tag("PreviewScreen")
                            .i("Placeholder: ${uiState.qrDataState.qrData.displayName}")
                    EditableText(
                            modifier = modifier,
                            textFieldState = uiState.previewEditableTextState.textFieldState,
                            placeHolderText = uiState.qrDataState.qrData.displayName,
                            isEditing = uiState.previewEditableTextState.isEditing,
                            onClickText = { onEvent(PreviewUiEvent.EditDetectedContent) }
                    )
                }
        )
    }
}