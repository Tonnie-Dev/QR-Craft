package com.tonyxlab.qrcraft.presentation.screens.scan

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.tonyxlab.qrcraft.domain.model.QrData
import com.tonyxlab.qrcraft.navigation.NavOperations
import com.tonyxlab.qrcraft.presentation.core.base.BaseContentLayout
import com.tonyxlab.qrcraft.presentation.core.components.AppSnackbarHost
import com.tonyxlab.qrcraft.presentation.screens.scan.components.CamPermissionHandler
import com.tonyxlab.qrcraft.presentation.screens.scan.components.CameraPreview
import com.tonyxlab.qrcraft.presentation.screens.scan.components.ScanOverlay
import com.tonyxlab.qrcraft.presentation.screens.scan.handling.ScanActionEvent
import com.tonyxlab.qrcraft.presentation.screens.scan.handling.ScanUiEvent
import com.tonyxlab.qrcraft.presentation.screens.scan.handling.ScanUiState
import com.tonyxlab.qrcraft.presentation.theme.ui.Overlay
import com.tonyxlab.qrcraft.util.SetStatusBarIconsColor
import org.koin.androidx.compose.koinViewModel

@Composable
fun ScanScreen(
    navOperations: NavOperations,
    modifier: Modifier = Modifier,
    viewModel: ScanViewModel = koinViewModel()
) {
    SetStatusBarIconsColor(darkIcons = false)
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    BaseContentLayout(
            viewModel = viewModel,
            snackbarHost = {
                AppSnackbarHost(snackbarHostState = snackbarHostState)
            },
            actionEventHandler = { _, action ->
                when (action) {

                    is ScanActionEvent.NavigateToScanResult -> {
                        navOperations.navigateToResultScreenDestination(qrData = action.qrData)
                    }

                    is ScanActionEvent.ShowToast -> {
                        Toast.makeText(context, action.messageRes, Toast.LENGTH_SHORT)
                                .show()
                    }
                }
            },
            containerColor = Overlay
    ) {
        HomeScreenContent(
                modifier = modifier,
                uiState = it,
                snackbarHostState = snackbarHostState,
                updateCamSnackbarShownStatus = viewModel::updateCamSnackbarShownStatus,
                onEvent = viewModel::onEvent,
                onAnalyzing = viewModel::onAnalyzing,
                onScanSuccess = viewModel::onScanSuccess
        )
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomeScreenContent(
    uiState: ScanUiState,
    snackbarHostState: SnackbarHostState,
    onScanSuccess: (QrData) -> Unit,
    onAnalyzing: (Boolean) -> Unit,
    onEvent: (ScanUiEvent) -> Unit,
    modifier: Modifier = Modifier,
    updateCamSnackbarShownStatus: (Boolean) -> Unit
) {

    Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
    ) {

        CamPermissionHandler(
                snackbarHostState = snackbarHostState,
                uiState = uiState,
                updateCamSnackbarShownStatus = updateCamSnackbarShownStatus
        )
        CameraPreview(
                modifier = Modifier.fillMaxSize(),
                onScanSuccess = onScanSuccess,
                onAnalyzing = onAnalyzing,
        )
        ScanOverlay(modifier = Modifier.matchParentSize(), isLoading = uiState.isLoading)
    }
}

