package com.tonyxlab.qrcraft.presentation.screens.scan.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.tonyxlab.qrcraft.data.QRCodeAnalyzer
import com.tonyxlab.qrcraft.presentation.screens.scan.BottomSheetContent

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





@kotlin.OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QRCodeScannerWithBottomSheet() {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var scannedCode by remember { mutableStateOf<String?>(null) }
    var showSheet by remember { mutableStateOf(false) }
    if (showSheet && scannedCode != null) {
        ModalBottomSheet(
                onDismissRequest = {
                    showSheet = false
                    scannedCode = null
                },
                sheetState = sheetState
        ) {
            BottomSheetContent(
                    scannedCode = scannedCode.orEmpty(),
                    onCopy = {
                        clipboardManager.setPrimaryClip(
                                ClipData.newPlainText("QR Code", scannedCode)
                        )
                        Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT)
                                .show()
                    },
                    onShare = {
                        val intent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, scannedCode)
                        }
                        context.startActivity(Intent.createChooser(intent, "Share QR Code"))
                    },
                    onClose = {
                        showSheet = false
                        scannedCode = null
                    }
            )
        }
    }
    AndroidView(factory = { ctx ->
        val previewView = PreviewView(ctx)
        val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder()
                    .build()
                    .apply {
                        setSurfaceProvider(previewView.surfaceProvider)
                    }
            val analyzer = ImageAnalysis.Builder()
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build()
                    .also {
                       /* it.setAnalyzer(
                                ContextCompat.getMainExecutor(ctx),
                                QRCodeAnalyzer { qrCode ->
                                    if (!showSheet) {
                                        scannedCode = qrCode
                                        showSheet = true
                                    }
                                }
                        )*/
                    }
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    CameraSelector.DEFAULT_BACK_CAMERA,
                    preview,
                    analyzer
            )
        }, ContextCompat.getMainExecutor(ctx))
        previewView
    }, modifier = Modifier.fillMaxSize())
}
