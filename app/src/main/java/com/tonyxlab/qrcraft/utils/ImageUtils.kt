@file:RequiresApi(Build.VERSION_CODES.Q)

package com.tonyxlab.qrcraft.utils

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import androidx.core.graphics.createBitmap
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import com.tonyxlab.qrcraft.domain.model.QrData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

fun generateQrBitmap(
    data: String,
    sizePx: Int = 512,
    marginModules: Int = 2,
    errorCorrection: ErrorCorrectionLevel = ErrorCorrectionLevel.M
): Bitmap {

    val hints = mapOf(
            EncodeHintType.CHARACTER_SET to "UTF-8",
            EncodeHintType.ERROR_CORRECTION to errorCorrection,
            EncodeHintType.MARGIN to marginModules
    )

    val matrix = QRCodeWriter().encode(
            data,
            BarcodeFormat.QR_CODE,
            sizePx,
            sizePx,
            hints
    )

    return createBitmap(sizePx, sizePx)
            .apply {

                for (y in 0 until sizePx) {
                    for (x in 0 until sizePx) {
                        setPixel(x, y, if (matrix[x, y]) 0xFF000000.toInt() else 0xFFFFFFFF.toInt())

                    }
                }
            }
}

fun saveQrImage(
    context: Context,
    coroutineScope: CoroutineScope,
    qrData: QrData,
    showSuccessSnackbar: (imageUri: Uri?) -> Unit,
    showErrorSnackbar: () -> Unit,
) {
    coroutineScope.launch(Dispatchers.IO) {

        val resolver = context.contentResolver
        val fileName = "${qrData.displayName.ifBlank { "qr_code" }}.png"

        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
        }

        var savedUri: Uri? = null

        val success = try {
            val imageUri = resolver.insert(
                    MediaStore.Downloads.EXTERNAL_CONTENT_URI,
                    contentValues
            ) ?: throw IllegalStateException("Failed to create MediaStore record")

            resolver.openOutputStream(imageUri)?.use { outputStream ->
                val qrBitmap = generateQrBitmap(qrData.prettifiedData)
                qrBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            } ?: throw IllegalStateException("Failed to open output stream")

            savedUri = imageUri
            true
        } catch (e: Exception) {
            false
        }

        withContext(Dispatchers.Main) {
            if (success) showSuccessSnackbar(savedUri) else showErrorSnackbar()
        }
    }
}


