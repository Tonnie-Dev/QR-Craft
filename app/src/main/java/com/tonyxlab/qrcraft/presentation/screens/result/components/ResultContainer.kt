package com.tonyxlab.qrcraft.presentation.screens.result.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.tonyxlab.qrcraft.R
import com.tonyxlab.qrcraft.domain.QrDataType
import com.tonyxlab.qrcraft.presentation.core.components.AppButton
import com.tonyxlab.qrcraft.presentation.core.utils.spacing
import com.tonyxlab.qrcraft.presentation.screens.result.handling.ResultUiEvent
import com.tonyxlab.qrcraft.presentation.screens.result.handling.ResultUiState
import com.tonyxlab.qrcraft.presentation.theme.ui.LinkBg
import com.tonyxlab.qrcraft.presentation.theme.ui.QRCraftTheme
import com.tonyxlab.qrcraft.util.ifThen

@Composable
fun ResultContainer(
    uiState: ResultUiState,
    onEvent: (ResultUiEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val qrData = uiState.dataState.qrData
    val shape = MaterialTheme.shapes.large

    val qrBoxSize = MaterialTheme.spacing.spaceMedium * 10
    val overlapSize = qrBoxSize / 2

    val qrDataType = uiState.dataState.qrData.qrDataType

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

                Text(
                        text = qrData.displayName,
                        style = MaterialTheme.typography.titleMedium.copy(
                                color = MaterialTheme.colorScheme.onSurface,
                        )
                )
                Text(
                        modifier = Modifier
                                .ifThen(qrDataType == QrDataType.LINK)
                                {
                                    background(color = LinkBg)
                                            .widthIn()
                                },
                        text = qrData.prettifiedData,
                        style = MaterialTheme.typography.bodyLarge.copy(
                                color = MaterialTheme.colorScheme.onSurface
                        ),
                        textAlign = if (qrDataType == QrDataType.TEXT)
                            TextAlign.Start
                        else
                            TextAlign.Center
                )
            }
            Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(
                            MaterialTheme.spacing.spaceSmall
                    )
            ) {

                AppButton(
                        modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                        buttonText = stringResource(id = R.string.btn_text_share),
                        onClick = { onEvent(ResultUiEvent.ShareContent) },
                        leadingIcon = painterResource(R.drawable.share_icon)
                )

                AppButton(
                        modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                        buttonText = stringResource(id = R.string.btn_text_copy),
                        onClick = { onEvent(ResultUiEvent.CopyContent) },
                        leadingIcon = painterResource(R.drawable.copy_icon)
                )
            }
        }
        QrImageTile(
                data = uiState.dataState.qrData.rawDataValue,
                qrBoxSizeInDp = qrBoxSize,
                overlapSize = overlapSize,
                shape = shape
        )
    }
}

@PreviewLightDark
@Composable
private fun ResultContainer_Preview() {
    QRCraftTheme {
        Column(
                modifier = Modifier
                        .fillMaxSize()
                        .background(color = MaterialTheme.colorScheme.background)
                        .padding(top = MaterialTheme.spacing.spaceMedium * 10)
                        .padding(horizontal = MaterialTheme.spacing.spaceMedium)

        ) {
            ResultContainer(
                    uiState = ResultUiState(),
                    onEvent = {}
            )
        }
    }
}
