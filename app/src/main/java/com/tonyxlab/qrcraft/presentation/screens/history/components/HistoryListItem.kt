package com.tonyxlab.qrcraft.presentation.screens.history.components

import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.tonyxlab.qrcraft.R
import com.tonyxlab.qrcraft.domain.model.QrData
import com.tonyxlab.qrcraft.domain.model.QrDataType
import com.tonyxlab.qrcraft.presentation.core.utils.spacing
import com.tonyxlab.qrcraft.presentation.screens.history.handling.HistoryUiEvent
import com.tonyxlab.qrcraft.presentation.theme.ui.QRCraftTheme
import com.tonyxlab.qrcraft.presentation.theme.ui.ShowLess
import com.tonyxlab.qrcraft.utils.getRandomQrDataItems
import com.tonyxlab.qrcraft.utils.getTintedIconModifier
import com.tonyxlab.qrcraft.utils.ifThen
import com.tonyxlab.qrcraft.utils.toFormattedDate
import com.tonyxlab.qrcraft.utils.toUi

@Composable
fun HistoryListItem(
    qrData: QrData,
    isSelected: Boolean,
    isDisplayDeviceWide: Boolean,
    modifier: Modifier = Modifier,
    onEvent: (HistoryUiEvent) -> Unit
) {

    val selectionColor =
        if (isSelected) MaterialTheme.colorScheme.error.copy(alpha = .1f)
        else
            MaterialTheme.colorScheme.surfaceContainerHigh

    val qrUiType = qrData.qrDataType.toUi()

    val middleEllipsis = when (qrData.qrDataType) {
        QrDataType.CONTACT, QrDataType.WIFI -> true
        else -> false
    }

    val favorite = qrData.favorite

    val (iconPainter, iconContentDescription) = if (favorite)
        painterResource(R.drawable.icon_star_filled) to
                stringResource(id = R.string.cds_text_starred)
    else
        painterResource(R.drawable.icon_star_outline) to
                stringResource(id = R.string.cds_text_not_starred)

    Card(
            modifier = modifier.combinedClickable(
                    onClick = {
                        onEvent(HistoryUiEvent.SelectHistoryItem(id = qrData.id))
                    },
                    onLongClick = {
                        onEvent(HistoryUiEvent.LongPressHistoryItem(id = qrData.id))
                    }
            ),
            shape = MaterialTheme.shapes.large,
            colors = CardDefaults.cardColors(
                    containerColor = selectionColor
   //                 containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
            )
    ) {

        Row(
                modifier = Modifier
                        .ifThen(isDisplayDeviceWide) {
                            width(630.dp)

                        }
                        .ifThen(isDisplayDeviceWide.not()) {
                            fillMaxWidth()
                        }
                        .align(Alignment.CenterHorizontally)
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
                                ),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                    text = qrData.prettifiedData,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                    ),
                                    overflow = TextOverflow.Ellipsis,
                                    maxLines = 2
                            )

                            if (middleEllipsis) {
                                Text(
                                        text = stringResource(id = R.string.cap_text_ellipsis),
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                )
                            }
                        }
                    }

                    Text(
                            text = qrData.timestamp.toFormattedDate(),
                            style = MaterialTheme.typography.bodySmall.copy(
                                    color = ShowLess
                            )
                    )
                }
                IconButton(
                        modifier = Modifier.size(MaterialTheme.spacing.spaceMedium),
                        onClick = { onEvent(HistoryUiEvent.MarkFavorite(qrData.id)) }
                ) {
                    Icon(
                            painter = iconPainter,
                            contentDescription = iconContentDescription,
                            tint = if (favorite) MaterialTheme.colorScheme.onSurface else ShowLess
                    )
                }
            }
        }
    }
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
                HistoryListItem(
                        qrData = it,
                        isDisplayDeviceWide = false,
                        isSelected = false,
                        onEvent = {},

                )
            }
        }
    }
}