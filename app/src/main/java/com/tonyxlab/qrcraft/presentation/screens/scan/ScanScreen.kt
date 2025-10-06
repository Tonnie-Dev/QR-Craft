package com.tonyxlab.qrcraft.presentation.screens.scan

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.navigation.NavController
import com.tonyxlab.qrcraft.domain.QrData
import com.tonyxlab.qrcraft.navigation.NavOperations
import com.tonyxlab.qrcraft.presentation.core.base.BaseContentLayout
import com.tonyxlab.qrcraft.presentation.core.components.AppSnackbarHost
import com.tonyxlab.qrcraft.presentation.screens.scan.components.CamPermissionHandler
import com.tonyxlab.qrcraft.presentation.screens.scan.components.CameraPreview
import com.tonyxlab.qrcraft.presentation.screens.scan.components.ExtendedFabButton
import com.tonyxlab.qrcraft.presentation.screens.scan.components.ScanOverlay
import com.tonyxlab.qrcraft.presentation.screens.scan.handling.ScanActionEvent
import com.tonyxlab.qrcraft.presentation.screens.scan.handling.ScanUiState
import org.koin.androidx.compose.koinViewModel

@Composable
fun ScanScreen(
    navOperations: NavOperations,
    navController: NavController,
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
                }
            }
    ) {
        HomeScreenContent(
                modifier = modifier,
                uiState = it,
                snackbarHostState = snackbarHostState,
                navController = navController,
                updateCamSnackbarShownStatus = viewModel::updateCamSnackbarShownStatus,
                onAnalyzing = viewModel::onAnalyzing,
                onScanSuccess = viewModel::onScanSuccess
        )
    }
}

@Composable
fun HomeScreenContent(
    uiState: ScanUiState,
    snackbarHostState: SnackbarHostState,
    onScanSuccess: (QrData) -> Unit,
    onAnalyzing: (Boolean) -> Unit,
    navController: NavController,
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

        ExtendedFabButton(navController = navController,modifier = Modifier.align(alignment = Alignment.BottomCenter))
    }
}

