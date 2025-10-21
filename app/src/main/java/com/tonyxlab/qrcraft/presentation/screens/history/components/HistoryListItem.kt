package com.tonyxlab.qrcraft.presentation.screens.history.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.tonyxlab.qrcraft.domain.model.QrData
import com.tonyxlab.qrcraft.domain.model.QrDataType
import com.tonyxlab.qrcraft.presentation.core.utils.spacing
import com.tonyxlab.qrcraft.presentation.theme.ui.QRCraftTheme
import com.tonyxlab.qrcraft.presentation.theme.ui.ShowLess
import com.tonyxlab.qrcraft.util.generateLoremIpsum
import com.tonyxlab.qrcraft.util.getRandomQrDataItems
import com.tonyxlab.qrcraft.util.getTintedIconModifier
import com.tonyxlab.qrcraft.util.toFormattedDate
import com.tonyxlab.qrcraft.util.toUi

@Composable
fun HistoryListItem(
    qrData: QrData,
    modifier: Modifier = Modifier
) {

    val qrUiType = qrData.qrDataType.toUi()

    val displayText = when(qrData.qrDataType){
       QrDataType.CONTACT, QrDataType.WIFI -> middleEllipsis(qrData.prettifiedData, 40)
       else -> qrData.prettifiedData
    }

    Card(
            shape = MaterialTheme.shapes.large,
            colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
            )
    ) {

        Row(
                modifier = modifier
                        .fillMaxWidth()
                        .padding(all = MaterialTheme.spacing.spaceTwelve),
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.spaceTwelve),
                verticalAlignment = Alignment.Top
        ) {

            with(qrUiType) {
                Box(
                        modifier = Modifier.getTintedIconModifier(tintBg = tintBg),
                        contentAlignment = Alignment.Center
                ) {
                    Icon(
                            painter = painterResource(id = iconRes),
                            contentDescription = stringResource(id = label),
                            tint = tint
                    )
                }
                Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(
                                MaterialTheme.spacing.spaceSmall
                        )
                ) {
                    Column(
                            verticalArrangement = Arrangement.spacedBy(
                                    MaterialTheme.spacing.spaceExtraSmall
                            )
                    ) {
                        Text(
                                text = qrData.displayName,
                                style = MaterialTheme.typography.titleSmall.copy(
                                        color = MaterialTheme.colorScheme.onSurface
                                )
                        )
                        Text(
                                text = displayText,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                ),
                                overflow = TextOverflow.MiddleEllipsis,
                                maxLines = 2
                        )
                    }

                    Text(
                            text = qrData.timestamp.toFormattedDate(),
                            style = MaterialTheme.typography.bodySmall.copy(
                                    color = ShowLess
                            )
                    )
                }
            }
        }
    }
}

private fun middleEllipsis(text:String, maxLength: Int) : String{
    if (text.length <=maxLength) return  text
val keep = (maxLength -3)/2
    return text.take(keep)  + "...\n" + text.takeLast(keep)
}

@PreviewLightDark
@Composable
private fun HistoryListItem_Preview() {

    QRCraftTheme {
        Column(
                modifier = Modifier
                        .background(MaterialTheme.colorScheme.surface)
                        .fillMaxSize()
                        .padding(MaterialTheme.spacing.spaceMedium),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.spaceMedium)
        ) {

            getRandomQrDataItems(10).forEach {

                HistoryListItem(qrData = it)

            }

        }
    }
}