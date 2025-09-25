@file:kotlin.OptIn(ExperimentalAtomicApi::class)
@file:OptIn(ExperimentalGetImage::class)

package com.tonyxlab.qrcraft.data

import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import com.tonyxlab.qrcraft.domain.QrData
import com.tonyxlab.qrcraft.domain.QrDataType
import kotlin.concurrent.atomics.AtomicBoolean
import kotlin.concurrent.atomics.ExperimentalAtomicApi

class QRCodeAnalyzer(
    private val onCodeScanned: (QrData) -> Unit,
    private val onAnalyzing: (Boolean) -> Unit = {},
    private val consumeOnce: Boolean = true
) : ImageAnalysis.Analyzer {

    private val onShotGuard = AtomicBoolean(false)

    private val options = BarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
            .build()

    private val scanner = BarcodeScanning.getClient(options)

    override fun analyze(imageProxy: ImageProxy) {

        val mediaImage = imageProxy.image ?: run {
            imageProxy.close()
            return
        }

        val image = InputImage.fromMediaImage(
                mediaImage, imageProxy.imageInfo.rotationDegrees
        )

        scanner.process(image)
                .addOnSuccessListener { barcodes ->

                    barcodes.ifEmpty { return@addOnSuccessListener }

                    onAnalyzing(true)

                    val barcode =
                        barcodes.maxByOrNull { it.boundingBox?.width() ?: 0 } ?: barcodes.first()

                    val data = barcode.toQrData()
                    if (!consumeOnce || onShotGuard.compareAndSet(
                                expectedValue = false,
                                newValue = true
                        )
                    ) {
                        onCodeScanned(data)
                    }
                }
                .addOnCompleteListener {
                    imageProxy.close()
                }
    }

    private fun Barcode.toQrData(): QrData {

        this.url?.let { url ->
            return QrData(
                    displayName = "Link",
                    prettifiedData = url.url ?: displayValue.orEmpty(),
                    qrDataType = QrDataType.LINK,
                    rawDataValue = rawValue.orEmpty()
            )
        }

        this.contactInfo?.let { contactInfo ->
            val name = listOfNotNull(
                    contactInfo.name?.formattedName,
                    contactInfo.name?.first,
                    // contactInfo.name?.last
            ).joinToString(" ")
                    .ifBlank { "Contact" }

            val pieces = buildList {
                add(name)
                contactInfo.emails.firstOrNull()?.address?.let { add(it) }
                contactInfo.phones.firstOrNull()?.number?.let { add(it) }
                contactInfo.organization?.firstOrNull()
                        ?.let { add(it) }
                contactInfo.addresses.firstOrNull()?.addressLines?.joinToString()
                        ?.let { add(it) }
            }

            return QrData(
                    displayName = "Contact",
                    prettifiedData = pieces.joinToString("\n")
                            .ifBlank { displayValue.orEmpty() },
                    qrDataType = QrDataType.CONTACT,

                    rawDataValue = rawValue.orEmpty()
            )
        }

        this.phone?.let { phone ->
            return QrData(
                    displayName = "Phone Number",
                    prettifiedData = phone.number ?: displayValue.orEmpty(),
                    qrDataType = QrDataType.PHONE_NUMBER,
                    rawDataValue = rawValue.orEmpty()
            )
        }

        this.geoPoint?.let { geoPoint ->
            val latLng = "${geoPoint.lat}, ${geoPoint.lng}"
            return QrData(
                    displayName = "Geolocation",
                    prettifiedData = latLng,
                    qrDataType = QrDataType.GEOLOCATION,
                    rawDataValue = rawValue.orEmpty()
            )
        }

        this.wifi?.let { wiFi ->
            val security = when (wiFi.encryptionType) {
                Barcode.WiFi.TYPE_OPEN -> "Open"
                Barcode.WiFi.TYPE_WEP -> "WEP"
                Barcode.WiFi.TYPE_WPA -> "WPA/WPA2"
                else -> "Unknown"
            }

            val block = buildString {
                appendLine("SSID: ${wiFi.ssid ?: "--"}")
                appendLine("Password: ${wiFi.password ?: "--"}")
                append("Encryption: $security")
            }

            return QrData(
                    displayName = "Wi-Fi",
                    prettifiedData = block,
                    qrDataType = QrDataType.WIFI,
                    rawDataValue = rawValue.orEmpty()
            )
        }

        return QrData(
                displayName = "Text",
                prettifiedData = displayValue ?: rawValue.orEmpty(),
                qrDataType = QrDataType.TEXT,
                rawDataValue = rawValue.orEmpty()
        )

    }

}



