@file:RequiresApi(Build.VERSION_CODES.Q)

package com.tonyxlab.qrcraft.presentation.screens.preview

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.core.content.getSystemService
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tonyxlab.qrcraft.R
import com.tonyxlab.qrcraft.navigation.NavOperations
import com.tonyxlab.qrcraft.presentation.core.base.BaseContentLayout
import com.tonyxlab.qrcraft.presentation.core.components.AppSnackbarHost
import com.tonyxlab.qrcraft.presentation.core.components.AppTopBar
import com.tonyxlab.qrcraft.presentation.core.components.PreviewContainer
import com.tonyxlab.qrcraft.presentation.core.components.ShowAppSnackbar
import com.tonyxlab.qrcraft.presentation.core.components.rememberSnackbarController
import com.tonyxlab.qrcraft.presentation.core.utils.spacing
import com.tonyxlab.qrcraft.presentation.screens.preview.handling.PreviewActionEvent
import com.tonyxlab.qrcraft.presentation.screens.preview.handling.PreviewUiEvent
import com.tonyxlab.qrcraft.presentation.screens.preview.handling.PreviewUiState
import com.tonyxlab.qrcraft.presentation.screens.result.components.EditableText
import com.tonyxlab.qrcraft.utils.DeviceType
import com.tonyxlab.qrcraft.utils.SetStatusBarIconsColor
import com.tonyxlab.qrcraft.utils.saveQrImage
import org.koin.androidx.compose.koinViewModel

@Composable
fun PreviewScreen(
    navOperations: NavOperations,
    modifier: Modifier = Modifier,
    viewModel: PreviewViewModel = koinViewModel()
) {
    SetStatusBarIconsColor(darkIcons = false)

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val keyboardController = LocalSoftwareKeyboardController.current
    val snackbarController =
        rememberSnackbarController<PreviewUiEvent>()

    val snackbarHostState = remember { SnackbarHostState() }

    ShowAppSnackbar(
            triggerId = snackbarController.triggerId,
            snackbarHostState = snackbarHostState,
            message = snackbarController.message,
            actionLabel = snackbarController.actionLabel,
            onActionClick = {
                snackbarController.actionEvent?.let {
                    viewModel.onEvent(it)
                }
            }
    )

    var isError by remember { mutableStateOf(false) }

    BaseContentLayout(
            modifier = modifier,
            viewModel = viewModel,
            topBar = {
                AppTopBar(
                        screenTitle = stringResource(id = R.string.topbar_text_preview),
                        contentColor = MaterialTheme.colorScheme.onTertiary,
                        onChevronIconClick = {
                            viewModel.onEvent(event = PreviewUiEvent.ExitPreviewScreen)
                        },
                        isFavorite = uiState.qrData.favorite,
                        onMarkFavorite = { viewModel.onEvent(PreviewUiEvent.MarkFavorite) }
                )
            },
            snackbarHost = {
                AppSnackbarHost(
                        modifier = Modifier,
                        snackbarHostState = snackbarHostState,
                        isError = isError
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

                    is PreviewActionEvent.SaveImage -> {
                        keyboardController?.hide()
                        saveQrImage(
                                context = context,
                                coroutineScope = coroutineScope,
                                qrData = action.qrData,
                                showSuccessSnackbar = { uri ->
                                    isError = false
                                    snackbarController.showSnackbar(
                                            message = context.getString(R.string.snack_text_image_saved_to_downloads),
                                            actionLabel = context.getString(
                                                    R.string.snack_text_open
                                            ),
                                            actionEvent = PreviewUiEvent.OpenSavedImageLocation
                                    )
                                    viewModel.setSavedImageUri(uri)
                                },
                                showErrorSnackbar = {
                                    isError = true
                                    snackbarController.showSnackbar(
                                            message = context.getString(
                                                    R.string.snack_text_image_not_saved
                                            )
                                    )
                                }
                        )
                    }

                    is PreviewActionEvent.OpenImageLocation -> {
                        val openIntent = Intent(Intent.ACTION_VIEW).apply {

                            setDataAndType(action.uri, "image/png")
                            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        }

                        runCatching {

                            context.startActivity(openIntent)
                        }.onFailure {

                            Toast.makeText(
                                    context,
                                    R.string.toast_text_cannot_open_downloads,
                                    Toast.LENGTH_SHORT
                            )
                                    .show()
                        }
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
                qrData = uiState.qrData,
                onShare = { onEvent(PreviewUiEvent.ShareContent) },
                onCopy = { onEvent(PreviewUiEvent.CopyContent) },
                onSave = { onEvent(PreviewUiEvent.SaveQrPhoto) },
                editableText = {
                    EditableText(
                            modifier = modifier,
                            textFieldState = uiState.previewEditableTextState.textFieldState,
                            placeHolderText = uiState.qrData.displayName,
                            isEditing = uiState.previewEditableTextState.isEditing,
                            onClickText = { onEvent(PreviewUiEvent.EditDetectedContent) }
                    )
                }
        )
    }
}