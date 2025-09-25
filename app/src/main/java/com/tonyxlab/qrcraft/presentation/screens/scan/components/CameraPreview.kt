package com.tonyxlab.qrcraft.presentation.screens.scan.components

import android.view.ViewGroup
import androidx.annotation.OptIn
import androidx.camera.camera2.interop.ExperimentalCamera2Interop
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.FocusMeteringAction
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.tonyxlab.qrcraft.data.QRCodeAnalyzer
import com.tonyxlab.qrcraft.domain.QrData
import com.tonyxlab.qrcraft.presentation.screens.scan.handling.ScanUiState
import kotlin.math.min

@OptIn(ExperimentalCamera2Interop::class)
@Composable
fun CameraPreview(
    uiState: ScanUiState,
    onScanSuccess: (QrData) -> Unit,
    onAnalyzing: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {

    val lifecycleOwner = LocalLifecycleOwner.current

    val context = LocalContext.current

    val executor = ContextCompat.getMainExecutor(context)

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
                    val analyzer = ImageAnalysis.Builder()
                            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                            .build()
                            .also {

                                it.setAnalyzer(
                                        executor,
                                        QRCodeAnalyzer(
                                                onCodeScanned = { data ->
                                                    onScanSuccess(data)
                                                },
                                                onAnalyzing = { active ->
                                                    onAnalyzing(active)
                                                },
                                                consumeOnce = true
                                        )
                                )
                            }
                    val cameraProvider = cameraProviderFuture.get()
                    val preview = Preview.Builder()
                            .build()
                            .apply {

                                surfaceProvider = previewView.surfaceProvider
                            }

                    val selector = CameraSelector.DEFAULT_BACK_CAMERA

                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                            lifecycleOwner, selector, preview, analyzer
                    )

                    val camera = cameraProvider.bindToLifecycle(
                            lifecycleOwner, selector, preview, analyzer
                    )

                    //lockCenterFocus(previewView, camera)
                },
                executor
        )
    }

}

fun lockCenterFocus(previewView: PreviewView, camera: Camera) {

    val factory = previewView.meteringPointFactory
    val center = factory.createPoint(previewView.width / 2f, previewView.height / 2f)
    val action = FocusMeteringAction.Builder(
            center,
            FocusMeteringAction.FLAG_AF or FocusMeteringAction.FLAG_AE
    )
            .setAutoCancelDuration(2, java.util.concurrent.TimeUnit.SECONDS)
            .build()

    camera.cameraControl.startFocusAndMetering(action)

}

