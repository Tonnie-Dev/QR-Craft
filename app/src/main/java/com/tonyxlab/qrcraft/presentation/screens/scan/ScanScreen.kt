package com.tonyxlab.qrcraft.presentation.screens.scan

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.tonyxlab.qrcraft.domain.QrData
import com.tonyxlab.qrcraft.navigation.NavOperations
import com.tonyxlab.qrcraft.presentation.core.base.BaseContentLayout
import com.tonyxlab.qrcraft.presentation.screens.scan.components.CamPermissionHandler
import com.tonyxlab.qrcraft.presentation.screens.scan.components.CameraPreview
import com.tonyxlab.qrcraft.presentation.screens.scan.components.ScanOverlay
import com.tonyxlab.qrcraft.presentation.screens.scan.handling.ScanActionEvent
import com.tonyxlab.qrcraft.presentation.screens.scan.handling.ScanUiState
import org.koin.androidx.compose.koinViewModel

@Composable
fun ScanScreen(
    navOperations: NavOperations,
    modifier: Modifier = Modifier,
    viewModel: ScanViewModel = koinViewModel()
) {
    BaseContentLayout(
            viewModel = viewModel,
            actionEventHandler = { _, action ->
                when (action) {

                    is ScanActionEvent.NavigateToScanResult -> navOperations.navigateToResultScreenDestination(
                            action.qrData
                    )
                }

            }) {

        HomeScreenContent(
                modifier = modifier,
                uiState = it,
                onAnalyzing = viewModel::onAnalyzing,
                onScanSuccess = viewModel::onScanSuccess
        )
    }

}

@Composable
fun HomeScreenContent(
    uiState: ScanUiState,
    onScanSuccess: (QrData) -> Unit,
    onAnalyzing: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
    ) {

        CamPermissionHandler()
        CameraPreview(
                modifier = Modifier.fillMaxSize(),
                uiState = uiState,
                onScanSuccess = onScanSuccess,
                onAnalyzing = onAnalyzing,
        )
        ScanOverlay(modifier = Modifier.matchParentSize(), isLoading = uiState.isLoading)
    }
}

