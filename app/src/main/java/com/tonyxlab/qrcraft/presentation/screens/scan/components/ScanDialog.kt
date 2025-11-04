package com.tonyxlab.qrcraft.presentation.screens.scan.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import com.tonyxlab.qrcraft.presentation.theme.ui.QRCraftTheme

@Composable
fun ScanDialog(modifier: Modifier = Modifier) {

    Column(
            modifier = Modifier
                    .clip(MaterialTheme.shapes.medium)
                    .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                    .padding(horizontal = MaterialTheme.spacing.spaceLarge)
                    .padding(vertical = MaterialTheme.spacing.spaceTen * 2),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.spaceSmall)
    ) {

        Icon(
                painter = painterResource(R.drawable.icon_alert),
                contentDescription = stringResource(id = R.string.cds_text_alert),
                tint = MaterialTheme.colorScheme.error
        )
        Text(
                text = stringResource(id = R.string.dialog_text_no_qr_found),
                style = MaterialTheme.typography.labelLarge.copy(color = MaterialTheme.colorScheme.error)
        )
    }
}

@PreviewLightDark
@Composable
private fun ScanDialog_Preview() {

    QRCraftTheme {

        Column(
                modifier = Modifier
                        .background(MaterialTheme.colorScheme.surface)
                        .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
        ) {
            ScanDialog()
        }
    }
}