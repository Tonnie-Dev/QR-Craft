package com.tonyxlab.qrcraft.domain

import android.content.Context
import android.net.Uri
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import com.tonyxlab.qrcraft.domain.model.QrData
import com.tonyxlab.qrcraft.domain.model.QrDataType

class QRCodeAnalyzer(
   private val context: Context,
   private val imageUri: Uri,
    private val onCodeScanned: (String) -> Unit) : ImageAnalysis.Analyzer {

    private val scanner = BarcodeScanning.getClient()

    @OptIn(ExperimentalGetImage::class)
    override fun analyze(imageProxy: ImageProxy) {
      //  val mediaImage = imageProxy.image ?: return imageProxy.close()
        val image = InputImage.fromFilePath(
                context, imageUri
        )

        scanner.process(image)
                .addOnSuccessListener { barcodes ->
                    barcodes.ifEmpty { return@addOnSuccessListener }
                    barcodes.forEach { barcode ->
                        barcode.rawValue?.let {
                            onCodeScanned(it)
                        }

                    }
                }
                .addOnCompleteListener { imageProxy.close() }
    }



}

