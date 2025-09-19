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

@Composable
fun ScanScreen(
    navOperations: NavOperations,
    modifier: Modifier = Modifier
) {
    Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
    ) {
        var permissionGranted by remember { mutableStateOf(false) }
        CameraPreview(modifier = Modifier.fillMaxSize())

        CamPermissionHandler ()

        ScanOverlay(modifier = Modifier.matchParentSize(), isLoading = true)
    }
}