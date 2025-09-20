package com.tonyxlab.qrcraft.presentation.core.components

import androidx.annotation.OptIn
import androidx.camera.camera2.interop.ExperimentalCamera2Interop
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.LocalLifecycleOwner

enum class FocusState {
    PassiveFocused, FocusedLocked, Unfocused, Idle
}

fun onFocusChanged(state: FocusState) {
    // Handle focus state changes
}

@OptIn(ExperimentalCamera2Interop::class)
@Composable
fun CameraPreview(modifier: Modifier = Modifier) {

    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

/*
    AndroidView(
            modifier = modifier,
            factory = { ctx ->
                PreviewView(ctx).apply {
                    layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    scaleType = PreviewView.ScaleType.FILL_CENTER
                }
            }
    ) { previewView ->
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        val executor = ContextCompat.getMainExecutor(context)

        cameraProviderFuture.addListener(
                {
                    val cameraProvider = cameraProviderFuture.get()
                    val preview = Preview.Builder()
                            .build()
                            .apply {
                                surfaceProvider = previewView.surfaceProvider
                            }

                    val selector = CameraSelector.DEFAULT_BACK_CAMERA
                    cameraProvider.unbindAll()
                    val camera = cameraProvider.bindToLifecycle(
                            lifecycleOwner, selector, preview
                    )
                    val c2Info = androidx.camera.camera2.interop.Camera2CameraInfo
                            .from(camera.cameraInfo)

                    val cb = object : CameraCaptureSession.CaptureCallback() {
                        override fun onCaptureCompleted(
                            session: android.hardware.camera2.CameraCaptureSession,
                            request: android.hardware.camera2.CaptureRequest,
                            result: android.hardware.camera2.TotalCaptureResult
                        ) {
                            val af = result.get(
                                    android.hardware.camera2.CaptureResult.CONTROL_AF_STATE
                            )
                            when (af) {
                                android.hardware.camera2.CaptureResult.CONTROL_AF_STATE_PASSIVE_FOCUSED ->
                                    onFocusChanged(FocusState.PassiveFocused)

                                android.hardware.camera2.CaptureResult.CONTROL_AF_STATE_FOCUSED_LOCKED ->
                                    onFocusChanged(FocusState.FocusedLocked)

                                android.hardware.camera2.CaptureResult.CONTROL_AF_STATE_NOT_FOCUSED_LOCKED,
                                android.hardware.camera2.CaptureResult.CONTROL_AF_STATE_PASSIVE_UNFOCUSED ->
                                    onFocusChanged(FocusState.Unfocused)

                                else -> onFocusChanged(FocusState.Idle)
                            }
                        }
                    }
                    //c2Info.registerCaptureCallback(exec, cb)
                },
                executor
        )
    }
*/
}