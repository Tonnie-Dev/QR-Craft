package com.tonyxlab.qrcraft.presentation.screens.scan

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.tonyxlab.qrcraft.navigation.NavOperations
import com.tonyxlab.qrcraft.presentation.core.components.CamPermissionHandler
import com.tonyxlab.qrcraft.presentation.core.components.CameraPreview
import com.tonyxlab.qrcraft.presentation.screens.scan.components.ScanOverlay
import org.koin.androidx.compose.koinViewModel

@Composable
fun ScanScreen(
    navOperations: NavOperations,
    modifier: Modifier = Modifier,
    scanViewModel: ScanViewModel = koinViewModel()
) {

    Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
    ) {
        var permissionGranted by remember { mutableStateOf(false) }
        CameraPreview(modifier = Modifier.fillMaxSize())

        CamPermissionHandler()

        ScanOverlay(modifier = Modifier.matchParentSize(), isLoading = true)
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