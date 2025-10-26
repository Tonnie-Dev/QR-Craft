@file:OptIn(ExperimentalMaterial3Api::class)

package com.tonyxlab.qrcraft.presentation.screens.history.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.tonyxlab.qrcraft.R
import com.tonyxlab.qrcraft.presentation.core.utils.spacing
import com.tonyxlab.qrcraft.presentation.screens.history.handling.HistoryUiEvent
import com.tonyxlab.qrcraft.presentation.screens.history.handling.HistoryUiState
import com.tonyxlab.qrcraft.presentation.theme.ui.HorizontalRoundedCornerShape16
import com.tonyxlab.qrcraft.presentation.theme.ui.QRCraftTheme

@Composable
fun HistoryBottomSheet(
    uiState: HistoryUiState,
    onEvent: (HistoryUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val clippingShape = MaterialTheme.shapes.HorizontalRoundedCornerShape16

    if (uiState.showBottomHistoryBottomSheet) {

        ModalBottomSheet(
                onDismissRequest = { onEvent(HistoryUiEvent.DismissHistoryBottomSheet) },
                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                tonalElevation = MaterialTheme.spacing.spaceDoubleDp,
                dragHandle = {}
        ) {

            Column(
                    modifier = modifier
                            .clip(clippingShape)
                            .background(
                                    color = MaterialTheme.colorScheme.surfaceContainerHigh,
                                    shape = clippingShape
                            )
                            .fillMaxWidth()
                            .padding(horizontal = MaterialTheme.spacing.spaceMedium)
                            .padding(top = MaterialTheme.spacing.spaceSmall)
                            .padding(bottom = MaterialTheme.spacing.spaceMedium)
            ) {
                Row(
                        modifier = Modifier
                                .fillMaxWidth()
                                .height(MaterialTheme.spacing.spaceDoubleDp * 22)
                                .clickable{ onEvent(HistoryUiEvent.ShareHistoryItem)}
                                .padding(horizontal = MaterialTheme.spacing.spaceTwelve),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                ) {

                    Icon(
                            painter = painterResource(R.drawable.share_icon),
                            contentDescription = stringResource(id = R.string.btn_text_share),
                            tint = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.width(MaterialTheme.spacing.spaceSmall))

                    Text(
                            text = stringResource(id = R.string.btn_text_share),
                            style = MaterialTheme.typography.labelLarge.copy(
                                    color = MaterialTheme.colorScheme.onSurface
                            )
                    )
                }

                Row(
                        modifier = Modifier
                                .fillMaxWidth()
                                .height(MaterialTheme.spacing.spaceDoubleDp * 22)
                                .clickable{ onEvent(HistoryUiEvent.DeleteHistoryItem)}
                                .padding(horizontal = MaterialTheme.spacing.spaceTwelve),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                ) {

                    Icon(
                            painter = painterResource(R.drawable.icon_delete),
                            contentDescription = stringResource(id = R.string.btn_text_delete),
                            tint = MaterialTheme.colorScheme.error
                    )

                    Spacer(modifier = Modifier.width(MaterialTheme.spacing.spaceSmall))

                    Text(
                            text = stringResource(id = R.string.btn_text_delete),
                            style = MaterialTheme.typography.labelLarge.copy(
                                    color = MaterialTheme.colorScheme.error
                            )
                    )
                }
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun HistoryBottomSheet_Preview() {
    QRCraftTheme {
        Box(
                modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surface),
                contentAlignment = Alignment.BottomCenter
        ) {

            HistoryBottomSheet(
                    uiState = HistoryUiState(showBottomHistoryBottomSheet = true),
                    onEvent = {}
            )
        }
    }
}