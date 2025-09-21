package com.tonyxlab.qrcraft.presentation.screens.result.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.tonyxlab.qrcraft.R
import com.tonyxlab.qrcraft.domain.QrData
import com.tonyxlab.qrcraft.domain.QrDataType
import com.tonyxlab.qrcraft.presentation.core.components.AppButton
import com.tonyxlab.qrcraft.presentation.core.utils.spacing
import com.tonyxlab.qrcraft.presentation.theme.ui.QRCraftTheme
import com.tonyxlab.qrcraft.presentation.theme.ui.SurfaceHigher
import com.tonyxlab.qrcraft.util.generateLoremIpsum

@Composable
fun ResultContainer(
    qrData: QrData,
    onClickButton: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val shape = MaterialTheme.shapes.large

    val qrBoxSize = MaterialTheme.spacing.spaceMedium * 10
    val overlapSize = qrBoxSize / 2

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
                    horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                        text = qrData.displayName,
                        style = MaterialTheme.typography.titleMedium.copy(
                                color = MaterialTheme.colorScheme.onSurface
                        )
                )
                Text(
                        text = qrData.data,
                        style = MaterialTheme.typography.bodyLarge.copy(
                                color = MaterialTheme.colorScheme.onSurface
                        )
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
                        onClick = onClickButton,
                        leadingIcon = painterResource(R.drawable.share_icon)
                )

                AppButton(
                        modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                        buttonText = stringResource(id = R.string.btn_text_copy),
                        onClick = onClickButton,
                        leadingIcon = painterResource(R.drawable.copy_icon)
                )
            }
        }

        Box(
                modifier = Modifier
                        .align(Alignment.TopCenter)
                        .size(qrBoxSize)
                        .offset(y = -overlapSize)
                        .shadow(MaterialTheme.spacing.spaceSmall, shape, clip = false)
                        .clip(shape)
                        .background(SurfaceHigher, shape)
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
            val data = QrData(
                    displayName = "Text",
                    data = generateLoremIpsum(26),
                    qrDataType = QrDataType.TEXT
            )
            ResultContainer(qrData = data, onClickButton = {})
        }
    }
}
