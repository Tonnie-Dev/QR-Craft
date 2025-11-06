package com.tonyxlab.qrcraft.presentation.core.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.tonyxlab.qrcraft.R
import com.tonyxlab.qrcraft.presentation.core.utils.spacing
import com.tonyxlab.qrcraft.presentation.theme.ui.OnOverlay
import com.tonyxlab.qrcraft.presentation.theme.ui.Overlay
import com.tonyxlab.qrcraft.presentation.theme.ui.QRCraftTheme
import com.tonyxlab.qrcraft.presentation.theme.ui.ShowLess

@Composable
fun AppTopBar(
    screenTitle: String,
    modifier: Modifier = Modifier,
    contentColor: Color = Overlay,
    height: Dp = Dp.Unspecified,
    isFavorite: Boolean = false,
    onMakeFavorite: (() -> Unit)? = null,
    onChevronIconClick: (() -> Unit)? = null,
) {

    val (painter, tint) = if (isFavorite) {

        painterResource(R.drawable.icon_star_filled) to OnOverlay

    } else {
        painterResource(R.drawable.icon_star_outline) to ShowLess
    }
    Row(
            modifier = modifier
                    .statusBarsPadding()
                    .height(height = height)
                    .fillMaxWidth()
                    .padding(horizontal = MaterialTheme.spacing.spaceMedium)
                    .padding(vertical = MaterialTheme.spacing.spaceTen * 2),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically

    ) {

        if (onChevronIconClick != null) {
            Icon(
                    modifier = Modifier
                            .size(MaterialTheme.spacing.spaceTwelve * 2)
                            .clickable { onChevronIconClick() }
                            .padding(end = MaterialTheme.spacing.spaceDoubleDp),
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(id = R.string.cds_text_back),
                    tint = contentColor
            )

        }

        Text(
                modifier = Modifier
                        .clickable { onChevronIconClick?.invoke() },
                text = screenTitle,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium.copy(
                        color = contentColor
                )
        )

        if (onMakeFavorite != null) {

            Icon(
                    painter = painter,
                    tint = tint,
                    contentDescription = if (isFavorite) stringResource(id = R.string.cds_text_favorite)
                    else
                        stringResource(id = R.string.cds_text_not_favorite)
            )
        } else {

            Spacer(modifier = Modifier.width(MaterialTheme.spacing.spaceMedium))
        }

    }
}

@PreviewLightDark
@Composable
private fun AppTopBar_Preview() {

    QRCraftTheme {
        Column(
                modifier = Modifier
                        .fillMaxSize()
                        .background(color = MaterialTheme.colorScheme.background),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.spaceMedium)
        ) {
            AppTopBar(
                    modifier = Modifier.fillMaxWidth(),
                    screenTitle = "Scan Result",
                    onChevronIconClick = {}
            )
            AppTopBar(
                    modifier = Modifier.fillMaxWidth(),
                    screenTitle = "Scan Result",
                    onChevronIconClick = {}
            )
            AppTopBar(
                    modifier = Modifier.fillMaxWidth(),
                    screenTitle = "Scan Result",
                    onMakeFavorite = {},
                    onChevronIconClick = {}
            )

            AppTopBar(
                    modifier = Modifier.fillMaxWidth(),
                    screenTitle = "Scan Result",
                    onChevronIconClick = {},
                    onMakeFavorite = {},
                    isFavorite = true
            )
        }
    }
}
