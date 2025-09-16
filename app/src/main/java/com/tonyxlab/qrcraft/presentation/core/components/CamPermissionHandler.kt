@file:OptIn(ExperimentalPermissionsApi::class)

package com.tonyxlab.qrcraft.presentation.core.components

import android.Manifest
import android.app.Activity
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.tonyxlab.qrcraft.R
import timber.log.Timber

@Composable
fun BoxScope.CamPermissionHandler(onPermissionGranted: () -> Unit) {
Timber.tag("CamPermissionHandler").i("Handler Called")
    val camPermissionState =
        rememberPermissionState(Manifest.permission.CAMERA)

    val permissionStatus = camPermissionState.status
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current


    // Automatically Check Permission State
    LaunchedEffect(true) {

        Timber.tag("CamPermissionHandler").i("Handler Launch Block called")
        if (permissionStatus.isGranted) {
           onPermissionGranted()
            val snackbarMessage = context
                    .getText(R.string.snack_text_camera_perm_granted)
                    .toString()
            snackbarHostState.showSnackbar(snackbarMessage)
        }
    }

    when {
        // Already Granted
        permissionStatus.isGranted -> {
            Timber.tag("CamPermissionHandler").i("When already Granted called")
           // onPermissionGranted()
        }

        permissionStatus.shouldShowRationale -> {
            AppDialog(
                    dialogTitle = stringResource(id = R.string.dialog_text_camera_required),
                    dialogText = stringResource(id = R.string.dialog_text_rationale),
                    positiveButtonText = stringResource(id = R.string.dialog_text_grant_access),
                    negativeButtonText = stringResource(id = R.string.dialog_text_close_app),
                    onConfirm = { camPermissionState.launchPermissionRequest() },
                    onDismissRequest = { (context as? Activity)?.finish() },
            )
        }

        else -> {

            // First launch — just ask
            LaunchedEffect(Unit) {
                camPermissionState.launchPermissionRequest()
            }
        }
    }
    SnackbarHost(
            modifier = Modifier.align(Alignment.BottomCenter),
            hostState = snackbarHostState
    )
}