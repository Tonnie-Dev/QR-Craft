package com.tonyxlab.qrcraft.presentation.screens.scan

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import timber.log.Timber

@Composable
fun ScanScreen(
    navOperations: NavOperations,
    modifier: Modifier = Modifier
) {

    Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
    ) {


        var permissionGranted by remember { mutableStateOf(false) }





        if (permissionGranted) {
            Timber.tag("ScanScreen").i("Perm Granted -> Preview")
            CameraPreview(modifier = Modifier.fillMaxSize())
        } else {

            Timber.tag("ScanScreen").i("Perm Not Granted -> Else")
            CamPermissionHandler { permissionGranted = true }
        }
    }
}