package com.tonyxlab.qrcraft.presentation.screens.scan.components

import android.graphics.Rect
import android.view.ViewGroup
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.mlkit.vision.MlKitAnalyzer
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.tonyxlab.qrcraft.domain.model.QrData
import com.tonyxlab.qrcraft.presentation.screens.scan.handling.ScanUiState
import com.tonyxlab.qrcraft.presentation.screens.scan.utils.toQrData
import com.tonyxlab.qrcraft.util.Constants.SCREEN_REGION_OF_INTEREST_FRACTION
import kotlin.math.min

@Composable
fun CameraPreview(
    uiState: ScanUiState,
    isFlashLightOn: Boolean,
    onScanSuccess: (QrData) -> Unit,
    onAnalyzing: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val executor = ContextCompat.getMainExecutor(context)

    val cameraController = remember {
        LifecycleCameraController(context).apply {
            cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
        }
    }

    LaunchedEffect(isFlashLightOn) {
        cameraController.enableTorch(isFlashLightOn)
    }

    LifecycleResumeEffect(uiState.isFlashLightOn) {
        cameraController.enableTorch(uiState.isFlashLightOn)
        onPauseOrDispose { }
    }

    AndroidView(
            modifier = modifier,
            factory = { cxt ->

                val previewView = PreviewView(cxt).apply {

                    layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    scaleType = PreviewView.ScaleType.FILL_CENTER
                }

                val options = BarcodeScannerOptions.Builder()
                        .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
                        .build()

                val scanner = BarcodeScanning.getClient(options)

                cameraController.setImageAnalysisAnalyzer(
                        executor,
                        MlKitAnalyzer(
                                listOf(scanner),
                                ImageAnalysis.COORDINATE_SYSTEM_VIEW_REFERENCED,
                                executor

                        ) { result ->

                            val barcodes = result?.getValue(scanner)
                                    .orEmpty()

                            barcodes.ifEmpty {
                                onAnalyzing(false)
                                return@MlKitAnalyzer
                            }

                            onAnalyzing(true)

                            val w = previewView.width
                            val h = previewView.height

                            if (w == 0 || h == 0) return@MlKitAnalyzer

                            val sideDimen =
                                (min(w, h) * SCREEN_REGION_OF_INTEREST_FRACTION)
                                        .toInt()

                            val left = (w - sideDimen) / 2
                            val top = (h - sideDimen) / 2

                            val roiRectangle =
                                Rect(left, top, (left + sideDimen), top + sideDimen)

                            val hit = barcodes.firstOrNull { barcode ->
                                barcode.boundingBox?.let { boundingBox ->
                                    roiRectangle.contains(
                                            boundingBox.centerX(),
                                            boundingBox.centerY()
                                    )
                                } == true
                            }
                            hit?.let { onScanSuccess(it.toQrData()) }
                        }
                )

                previewView.controller = cameraController
                cameraController.bindToLifecycle(lifecycleOwner)
                previewView
            }
    )
}





