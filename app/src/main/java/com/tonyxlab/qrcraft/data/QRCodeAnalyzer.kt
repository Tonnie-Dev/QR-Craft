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
    private val consumeOnce: Boolean = true
) : ImageAnalysis.Analyzer {

    @kotlin.OptIn(ExperimentalAtomicApi::class)
    private val onShotGuard = AtomicBoolean(false)

    private val options = BarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
            .build()

    private val scanner = BarcodeScanning.getClient(options)

    @OptIn(ExperimentalGetImage::class)
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

                    val barcode =
                        barcodes.maxByOrNull { it.boundingBox?.width() ?: 0 } ?: barcodes.first()

                    val data = barcode.toQrData()
                    barcodes.forEach { barcode ->
                        barcode.rawValue?.let {
                            onCodeScanned(it)
                        }

                    }
                }
                .addOnCompleteListener {
                    imageProxy.close()
                }
    }

    private fun Barcode.toQrData(): QrData {

        this.url?.let { url ->
            val urlTitle = url.title ?: "Link"
            return QrData(
                    displayName = urlTitle,
                    data = url.url ?: displayValue.orEmpty(),
                    qrDataType = QrDataType.LINK
            )
        }

        this.contactInfo?.let { contactInfo ->
            val name = listOfNotNull(
                    contactInfo.name?.formattedName,
                    contactInfo.name?.first,
                    contactInfo.name?.last
            ).joinToString(" ")
                    .ifBlank { "Contact" }

            val pieces = buildList {

                contactInfo.emails.firstOrNull()?.address?.let { add("Email: $it") }
                contactInfo.phones.firstOrNull()?.number?.let { add("Phone:$it") }
                contactInfo.organization?.firstOrNull()
                        ?.let { add("Org: $it") }
                contactInfo.addresses.firstOrNull()?.addressLines?.joinToString()
                        ?.let { add("Addr: $it") }
            }

            return QrData(
                    displayName = name,
                    data = pieces.joinToString("\n")
                            .ifBlank { displayValue.orEmpty() },
                    qrDataType = QrDataType.CONTACT

            )
        }

        this.phone?.let { phone ->
            return QrData(
                    displayName = "Phone Number",
                    data = phone.number ?: displayValue.orEmpty(),
                    qrDataType = QrDataType.PHONE_NUMBER
            )
        }

        this.geoPoint?.let { geoPoint ->
            val latLng = "${geoPoint.lat}, ${geoPoint.lng}"
            return QrData(
                    displayName = "Geolocation",
                    data = latLng,
                    qrDataType = QrDataType.GEOLOCATION
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
                    data = block,
                    qrDataType = QrDataType.WIFI
            )
        }

        return QrData(
                displayName = "Text",
                data = displayValue ?: rawValue.orEmpty(),
                qrDataType = QrDataType.TEXT
        )

    }

}



