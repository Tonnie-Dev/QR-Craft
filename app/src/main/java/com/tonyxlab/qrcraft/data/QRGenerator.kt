package com.tonyxlab.qrcraft.data

import android.graphics.Bitmap
import androidx.core.graphics.createBitmap
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel

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