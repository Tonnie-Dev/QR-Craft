package com.tonyxlab.qrcraft.presentation.screens.result.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import com.tonyxlab.qrcraft.R
import com.tonyxlab.qrcraft.data.generateQrBitmap
import com.tonyxlab.qrcraft.presentation.core.utils.spacing

@Composable
fun BoxScope.QrImageTile(
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