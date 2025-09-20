package com.tonyxlab.qrcraft.presentation.screens.scan

import androidx.camera.compose.CameraXViewfinder
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tonyxlab.qrcraft.navigation.NavOperations
import com.tonyxlab.qrcraft.presentation.core.components.CamPermissionHandler
import com.tonyxlab.qrcraft.presentation.screens.scan.components.ScanOverlay
import org.koin.androidx.compose.koinViewModel

@Composable
fun ScanScreen(
    navOperations: NavOperations,
    modifier: Modifier = Modifier,
    scanViewModel: ScanViewModel = koinViewModel()
) {

    val lifecycleOwner = LocalLifecycleOwner.current
    val surfaceRequest by scanViewModel.surfaceRequest.collectAsStateWithLifecycle()
    val context = LocalContext.current

    Box {
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
    }

    /*Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
    ) {
        var permissionGranted by remember { mutableStateOf(false) }
        CameraPreview(modifier = Modifier.fillMaxSize())

        CamPermissionHandler()

        ScanOverlay(modifier = Modifier.matchParentSize(), isLoading = true)
    }*/
}