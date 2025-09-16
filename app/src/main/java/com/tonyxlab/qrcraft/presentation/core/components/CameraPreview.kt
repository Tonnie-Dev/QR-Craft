package com.tonyxlab.qrcraft.presentation.core.components

import android.view.ViewGroup
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner

@Composable
fun CameraPreview(modifier: Modifier = Modifier) {

    val lifecycleOwner = LocalLifecycleOwner.current

    val context = LocalContext.current

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
        val cameraProviderFuture =
            ProcessCameraProvider.getInstance(context)

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
                    cameraProvider.bindToLifecycle(
                            lifecycleOwner, selector, preview
                    )
                },
                ContextCompat.getMainExecutor(context)
        )
    }
}