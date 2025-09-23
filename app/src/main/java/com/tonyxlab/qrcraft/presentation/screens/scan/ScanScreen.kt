package com.tonyxlab.qrcraft.presentation.screens.scan

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
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

    /*  val lifecycleOwner = LocalLifecycleOwner.current
      val surfaceRequest by scanViewModel.surfaceRequest.collectAsStateWithLifecycle()
      val context = LocalContext.current

      Box(
              modifier = modifier.fillMaxSize(),
              contentAlignment = Alignment.Center
      ) {

          LaunchedEffect(lifecycleOwner) {
              scanViewModel.bindToCamera(
                      appContext = context.applicationContext,
                      lifecycleOwner = lifecycleOwner
              )
          }

          surfaceRequest?.let { request ->

              CameraXViewfinder(
                      surfaceRequest = request,
                      modifier = modifier
              )

          }


          CamPermissionHandler()

          ScanOverlay(modifier = Modifier.matchParentSize(), isLoading = true)
      }*/

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
        //QRCodeScannerWithBottomSheet()

        ScanOverlay(modifier = Modifier.matchParentSize(), isLoading = true)
    }
}

@Composable
fun BottomSheetContent(
    scannedCode: String,
    onCopy: () -> Unit,
    onShare: () -> Unit,
    onClose: () -> Unit
) {
    Column(
            modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Scanned QR Code", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(8.dp))
        Text(scannedCode, textAlign = TextAlign.Center, style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(24.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(onClick = onCopy) {
                Icon(Icons.Default.ContentCopy, contentDescription = "Copy")
                Spacer(modifier = Modifier.width(4.dp))
                Text("Copy")
            }
            Button(onClick = onShare) {
                Icon(Icons.Default.Share, contentDescription = "Share")
                Spacer(modifier = Modifier.width(4.dp))
                Text("Share")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedButton(onClick = onClose) {
            Text("Close")
        }
    }
}