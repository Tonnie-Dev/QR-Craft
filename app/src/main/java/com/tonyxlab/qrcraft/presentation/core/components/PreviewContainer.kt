package com.tonyxlab.qrcraft.presentation.core.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Dp
import com.tonyxlab.qrcraft.R
import com.tonyxlab.qrcraft.utils.generateQrBitmap
import com.tonyxlab.qrcraft.domain.model.QrData
import com.tonyxlab.qrcraft.domain.model.QrDataType
import com.tonyxlab.qrcraft.presentation.core.utils.spacing
import com.tonyxlab.qrcraft.presentation.screens.result.components.ExpandableText
import com.tonyxlab.qrcraft.presentation.theme.ui.Link
import com.tonyxlab.qrcraft.presentation.theme.ui.LinkBg
import com.tonyxlab.qrcraft.presentation.theme.ui.QRCraftTheme
import com.tonyxlab.qrcraft.utils.generateLoremIpsum
import com.tonyxlab.qrcraft.utils.ifThen

@Composable
fun PreviewContainer(
    qrData: QrData,
    onShare: () -> Unit,
    onCopy: () -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier,
    editableText: @Composable (() -> Unit)? = null
) {

    val shape = MaterialTheme.shapes.large

    val qrBoxSize = MaterialTheme.spacing.spaceMedium * 10
    val overlapSize = qrBoxSize / 2

    val qrDataType = qrData.qrDataType

    Box(modifier = modifier.fillMaxWidth()) {

        Column(
                modifier = Modifier
                        .clip(shape)
                        .background(color = MaterialTheme.colorScheme.surface, shape = shape)
                        .fillMaxWidth()
                        .padding(MaterialTheme.spacing.spaceMedium)
                        .padding(top = overlapSize),
                horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                    modifier = Modifier
                            .padding(bottom = MaterialTheme.spacing.spaceTwelve * 2),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.spaceTen)
            ) {

                if (editableText == null) {

                    Text(
                            text = qrData.displayName,
                            style = MaterialTheme.typography.titleMedium.copy(
                                    color = MaterialTheme.colorScheme.onSurface,
                            )
                    )

                } else {
                    editableText()
                }

                if (qrDataType == QrDataType.TEXT) {

                    ExpandableText(text = qrData.prettifiedData)
                } else {
                    Text(
                            modifier = Modifier
                                    .ifThen(qrDataType == QrDataType.LINK)
                                    {
                                        background(color = LinkBg)
                                                .widthIn()
                                    },
                            text = qrData.prettifiedData,
                            style = if (qrDataType == QrDataType.LINK) {
                                MaterialTheme.typography.labelLarge.copy(
                                        color = Link
                                )
                            } else {
                                MaterialTheme.typography.bodyLarge.copy(
                                        color = MaterialTheme.colorScheme.onSurface
                                )
                            },
                            textAlign = TextAlign.Center
                    )
                }
            }
            ButtonsRow(
                    modifier = modifier,
                    onShare = onShare,
                    onCopy = onCopy,
                    onSave = onSave
            )
        }
        QrImageTile(
                data = qrData.rawData,
                qrBoxSizeInDp = qrBoxSize,
                overlapSize = overlapSize,
                shape = shape
        )
    }
}

@Composable
private fun ButtonsRow(
    onShare: () -> Unit,
    onCopy: () -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(
                    MaterialTheme.spacing.spaceSmall
            )
    ) {

        AppButton(
                buttonShape = CircleShape,
                isCircularButton = true,
                onClick = onShare,
                leadingIcon = painterResource(R.drawable.icon_share)
        )

        AppButton(
                buttonShape = CircleShape,
                isCircularButton = true,
                onClick = onCopy,
                leadingIcon = painterResource(R.drawable.icon_copy)
        )

        AppButton(
                modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                buttonText = stringResource(id = R.string.btn_text_save),
                onClick = onSave,
                leadingIcon = painterResource(R.drawable.icon_download)
        )
    }
}

@Composable
private fun BoxScope.QrImageTile(
    data: String,
    qrBoxSizeInDp: Dp,
    overlapSize: Dp,
    shape: Shape
) {
    val sizePx = with(LocalDensity.current) { qrBoxSizeInDp.roundToPx() }

    val bitmap by remember(data, qrBoxSizeInDp) {
        mutableStateOf(generateQrBitmap(data = data, sizePx = sizePx))
    }

    Card(
            modifier = Modifier
                    .align(Alignment.TopCenter)
                    .size(qrBoxSizeInDp)
                    .offset(y = -overlapSize)
                    .shadow(MaterialTheme.spacing.spaceSmall, shape, clip = false), shape = shape,
            elevation = CardDefaults.cardElevation(defaultElevation = MaterialTheme.spacing.spaceSmall)
    ) {

        Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = stringResource(id = R.string.cds_text_qr_code),
                modifier = Modifier
                        .size(qrBoxSizeInDp)
        )
    }
}

@PreviewLightDark
@Composable
fun PreviewContainer_Preview(modifier: Modifier = Modifier) {

    val qrData = QrData(
            displayName = "Text",
            prettifiedData = generateLoremIpsum(26),
            qrDataType = QrDataType.TEXT,
            rawData = "",
            favorite = true
    )

    QRCraftTheme {
        Column(
                modifier = Modifier
                        .background(MaterialTheme.colorScheme.onSurface)
                        .fillMaxSize()
                        .padding(MaterialTheme.spacing.spaceMedium),
                horizontalAlignment = Alignment.CenterHorizontally
        ) {

            PreviewContainer(
                    qrData = qrData,
                    onShare = {},
                    onCopy = {},
                    onSave = {}
            )
        }
    }
}


