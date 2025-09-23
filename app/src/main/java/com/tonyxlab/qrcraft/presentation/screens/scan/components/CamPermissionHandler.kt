@file:OptIn(ExperimentalPermissionsApi::class)

package com.tonyxlab.qrcraft.presentation.screens.scan.components

import android.Manifest
import android.app.Activity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
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
import com.tonyxlab.qrcraft.presentation.core.components.AppDialog
import com.tonyxlab.qrcraft.presentation.core.utils.spacing
import com.tonyxlab.qrcraft.presentation.theme.ui.Success

@Composable
fun BoxScope.CamPermissionHandler() {

    val camPermissionState =
        rememberPermissionState(Manifest.permission.CAMERA)

    val permissionStatus = camPermissionState.status
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

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
    {
        Snackbar(
                modifier = Modifier.fillMaxWidth(.8f),
                shape = MaterialTheme.shapes.small,
                contentColor = MaterialTheme.colorScheme.onSurface,
                containerColor = Success
        ) {
            Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
            ) {

                Icon(modifier = Modifier.padding(end = MaterialTheme.spacing.spaceSmall),
                        imageVector = Icons.Default.Check,
                        contentDescription = stringResource(id = R.string.cds_text_check_mark))
                Text(
                        text = stringResource(id = R.string.snack_text_camera_perm_granted),
                        style = MaterialTheme.typography.labelLarge.copy(
                                color = MaterialTheme.colorScheme.onSurface
                        )
                )
            }

        }
    }
}