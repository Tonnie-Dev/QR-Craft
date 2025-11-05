package com.tonyxlab.qrcraft.presentation.screens.scan.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.tonyxlab.qrcraft.R
import com.tonyxlab.qrcraft.presentation.core.utils.spacing
import com.tonyxlab.qrcraft.presentation.theme.ui.QRCraftTheme
import kotlinx.coroutines.delay

@Composable
fun ScanDialog(
    showDialog: Boolean,
    onDismissDialog: () -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
            visible = showDialog,
            enter = fadeIn(),
            exit = fadeOut()
    ) {
        Box(
                modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.scrim.copy(alpha = 0.4f)), // dim background
                contentAlignment = Alignment.Center
        ) {

            LaunchedEffect(Unit) {
                delay(2500)
                onDismissDialog()
            }

            Surface(
                    modifier = modifier,
                    shape = MaterialTheme.shapes.medium,
                    color = MaterialTheme.colorScheme.surfaceContainerHigh,
                    tonalElevation = 6.dp
            ) {
                Column(
                        modifier = Modifier
                                .padding(
                                        horizontal = MaterialTheme.spacing.spaceLarge,
                                        vertical = MaterialTheme.spacing.spaceTen * 2
                                ),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.spaceSmall)
                ) {
                    Icon(
                            painter = painterResource(R.drawable.icon_alert),
                            contentDescription = stringResource(R.string.cds_text_alert),
                            tint = MaterialTheme.colorScheme.error
                    )

                    Text(
                            text = stringResource(R.string.dialog_text_no_qr_found),
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
private fun ScanDialog_Preview() {

    QRCraftTheme {

        Column(
                modifier = Modifier
                        .background(MaterialTheme.colorScheme.surface)
                        .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
        ) {
            ScanDialog(showDialog = true, onDismissDialog = {})
        }
    }
}

