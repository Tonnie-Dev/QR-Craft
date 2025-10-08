package com.tonyxlab.qrcraft.presentation.screens.scan

import android.Manifest
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.tonyxlab.qrcraft.domain.QrData
import com.tonyxlab.qrcraft.navigation.NavOperations
import com.tonyxlab.qrcraft.presentation.core.base.BaseContentLayout
import com.tonyxlab.qrcraft.presentation.core.components.AppSnackbarHost
import com.tonyxlab.qrcraft.presentation.screens.scan.components.AnimatedBottomNavButton
import com.tonyxlab.qrcraft.presentation.screens.scan.components.CamPermissionHandler
import com.tonyxlab.qrcraft.presentation.screens.scan.components.CameraPreview
import com.tonyxlab.qrcraft.presentation.screens.scan.components.ScanOverlay
import com.tonyxlab.qrcraft.presentation.screens.scan.handling.ScanActionEvent
import com.tonyxlab.qrcraft.presentation.screens.scan.handling.ScanUiEvent
import com.tonyxlab.qrcraft.presentation.screens.scan.handling.ScanUiState
import org.koin.androidx.compose.koinViewModel

@Composable
fun ScanScreen(
    navOperations: NavOperations,
    modifier: Modifier = Modifier,
    viewModel: ScanViewModel = koinViewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }

    BaseContentLayout(
            viewModel = viewModel,
            snackbarHost = {
                AppSnackbarHost(snackbarHostState = snackbarHostState)
            },
            actionEventHandler = { _, action ->
                when (action) {

                    is ScanActionEvent.NavigateToScanResult -> navOperations.navigateToResultScreenDestination(
                            action.qrData
                    )

                    ScanActionEvent.NavigateToCreateScreen -> {
                        navOperations.navigateToCreateScreenDestination()
                    }

                    ScanActionEvent.NavigateToHistoryScreen -> {
                        navOperations.navigateToHistoryScreenDestination()
                    }

                    ScanActionEvent.NavigateToScanScreen -> {
                        navOperations.navigateToScanScreenDestination()
                    }
                }
            }
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

        val camPermission = rememberPermissionState(Manifest.permission.CAMERA)
        val isCamPermissionGranted = camPermission.status.isGranted
        AnimatedBottomNavButton(
                modifier = modifier,
                uiState = uiState,
                onEvent = onEvent,
                isCamPermissionGranted = isCamPermissionGranted
        )
    }
}

