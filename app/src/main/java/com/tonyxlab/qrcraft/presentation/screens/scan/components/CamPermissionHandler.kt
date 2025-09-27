@file:OptIn(ExperimentalPermissionsApi::class)

package com.tonyxlab.qrcraft.presentation.screens.scan.components

import android.Manifest
import android.app.Activity
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.tonyxlab.qrcraft.R
import com.tonyxlab.qrcraft.presentation.core.components.AppDialog

@Composable
fun CamPermissionHandler(
    snackbarHostState: SnackbarHostState
) {

    val camPermissionState =
        rememberPermissionState(Manifest.permission.CAMERA)

    val permissionStatus = camPermissionState.status
    val context = LocalContext.current

    when {
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

   /*     permissionStatus.isGranted -> {
            // Automatically Check Permission State
            LaunchedEffect(permissionStatus.isGranted) {

                if (permissionStatus.isGranted) {
                    //  onPermissionGranted()
                    val snackbarMessage = context
                            .getText(R.string.snack_text_camera_perm_granted)
                            .toString()
                    snackbarHostState.showSnackbar(snackbarMessage)
                }
            }
        }*/

        else -> {
            // First launch — just ask
            LaunchedEffect(Unit) {
                camPermissionState.launchPermissionRequest()
            }
        }
    }

}