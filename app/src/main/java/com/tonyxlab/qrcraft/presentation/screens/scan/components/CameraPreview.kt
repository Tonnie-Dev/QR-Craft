package com.tonyxlab.qrcraft.presentation.screens.scan.components

import android.content.Context
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
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.tonyxlab.qrcraft.domain.model.QrData
import com.tonyxlab.qrcraft.presentation.screens.scan.handling.ScanUiState
import com.tonyxlab.qrcraft.presentation.screens.scan.utils.toQrData
import com.tonyxlab.qrcraft.util.Constants.SCREEN_REGION_OF_INTEREST_FRACTION
import timber.log.Timber
import kotlin.math.min
import kotlin.text.contains
import kotlin.text.firstOrNull
import kotlin.text.isEmpty
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

    // 1️⃣ Remember camera controller so it's not recreated on every recomposition
    val cameraController = remember {
        LifecycleCameraController(context).apply {
            cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
        }
    }

    // 2️⃣ Barcode scanner setup
    val options = remember {
        BarcodeScannerOptions.Builder()
                .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
                .build()
    }

    val scanner = remember { BarcodeScanning.getClient(options) }

    // 3️⃣ Bind analyzer only once (this is heavy work)
   LaunchedEffect(Unit) {
        cameraController.setImageAnalysisAnalyzer(
                executor,
                MlKitAnalyzer(
                        listOf(scanner),
                        ImageAnalysis.COORDINATE_SYSTEM_VIEW_REFERENCED,
                        executor
                ) { result ->

                    val barcodes = result?.getValue(scanner).orEmpty()

                    if (barcodes.isEmpty()) {
                        onAnalyzing(false)
                        return@MlKitAnalyzer
                    }

                  //  if(uiState.isLoading) return@MlKitAnalyzer
                    onAnalyzing(true)
                    val previewView = PreviewView(context).apply {

                        layoutParams = ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT
                        )
                        scaleType = PreviewView.ScaleType.FILL_CENTER
                    }

                    val w = previewView.width
                    val h = previewView.height

                    if (w == 0 || h == 0) return@MlKitAnalyzer

                    val sideDimen = (min(w, h) * SCREEN_REGION_OF_INTEREST_FRACTION).toInt()
                    val left = (w - sideDimen) / 2
                    val top = (h - sideDimen) / 2

                    val roiRectangle = Rect(left, top, (left + sideDimen), top + sideDimen)

                    val hit = barcodes.firstOrNull { barcode ->
                        barcode.boundingBox?.let { boundingBox ->
                            roiRectangle.contains(
                                    boundingBox.centerX(),
                                    boundingBox.centerY()
                            )
                        } == true
                    }

                    hit?.let {
                        onScanSuccess(it.toQrData())
                   onAnalyzing(false)
                    }
                }
        )

        cameraController.bindToLifecycle(lifecycleOwner)
    }

    // 4️⃣ Reactively toggle torch when state changes
    LaunchedEffect(isFlashLightOn) {
        Timber.tag("CameraPreview").i("Torch toggled: $isFlashLightOn")
        cameraController.enableTorch(isFlashLightOn)
    }

    // 5️⃣ AndroidView to display the PreviewView
    AndroidView(
            modifier = modifier,
            factory = { ctx ->
                PreviewView(ctx).apply {
                    layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    scaleType = PreviewView.ScaleType.FILL_CENTER
                    controller = cameraController
                }
            },
            update = { previewView ->
                // If you ever want to update settings dynamically, do it here
            }
    )
}






@Composable
fun CameraPreviewView(
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

    Timber.tag("CameraPreview").i("Composable - Torch toggled: $isFlashLightOn")


   LaunchedEffect(isFlashLightOn) {

        cameraController.enableTorch(isFlashLightOn)

    }


    // 4️⃣ Reactively toggle torch when state changes
    LaunchedEffect(isFlashLightOn) {
        Timber.tag("CameraPreview").i("Launched Effect Torch toggled: $isFlashLightOn")
        cameraController.enableTorch(isFlashLightOn)
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
                Timber.tag("CameraPreview")
                        .i("State is: ${uiState.isFlashLightOn}")

                cameraController.bindToLifecycle(lifecycleOwner)
                previewView
            }
    )



}





