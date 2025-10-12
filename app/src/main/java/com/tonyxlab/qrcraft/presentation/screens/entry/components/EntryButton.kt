package com.tonyxlab.qrcraft.presentation.screens.entry.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Dp
import com.tonyxlab.qrcraft.R
import com.tonyxlab.qrcraft.presentation.core.utils.spacing
import com.tonyxlab.qrcraft.presentation.theme.ui.QRCraftTheme
import com.tonyxlab.qrcraft.presentation.theme.ui.ShowLess

@Composable
fun EntryButton(
    enabled: Boolean,
    modifier: Modifier = Modifier,
    buttonHeight: Dp = MaterialTheme.spacing.spaceDoubleDp * 22,
    buttonText: String = stringResource(id = R.string.btn_text_generate_qr),
    onClick: () -> Unit,
) {

    val buttonContainerColor = if (enabled)
        MaterialTheme.colorScheme.primary
    else
        MaterialTheme.colorScheme.surface

    val buttonTextColor = if (enabled)
        MaterialTheme.colorScheme.onSurface
    else
        ShowLess

    Box(
            modifier = modifier
                    .clip(RoundedCornerShape(MaterialTheme.spacing.spaceOneHundred))
                    .background(buttonContainerColor)
                    .fillMaxWidth()
                    .height(buttonHeight)
                    .clickable {
                        onClick()
                    }
                    .padding(horizontal = MaterialTheme.spacing.spaceMedium)
                    .padding(vertical = MaterialTheme.spacing.spaceSmall),
            contentAlignment = Alignment.Center
    ) {

        Text(
                text = buttonText,
                style = MaterialTheme.typography.labelLarge.copy(color = buttonTextColor)
        )
    }
}

@PreviewLightDark
@Composable
private fun EntryButton_Preview() {
    QRCraftTheme {
        Column(
                modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                        .padding(MaterialTheme.spacing.spaceMedium),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.spaceSmall),
                horizontalAlignment = Alignment.CenterHorizontally
        ) {
            EntryButton(enabled = true, onClick = {})
            EntryButton(enabled = false, onClick = {})
        }
    }

}