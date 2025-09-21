package com.tonyxlab.qrcraft.presentation.core.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.window.DialogProperties
import com.tonyxlab.qrcraft.presentation.core.utils.spacing
import com.tonyxlab.qrcraft.presentation.theme.ui.QRCraftTheme

@Composable
fun AppDialog(
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    positiveButtonText: String,
    negativeButtonText: String? = null
) {
    AlertDialog(
            modifier = Modifier.padding(
                    vertical = MaterialTheme.spacing.spaceExtraSmall * 7,
                    horizontal = MaterialTheme.spacing.spaceTwelve * 2
            ),
            containerColor = MaterialTheme.colorScheme.surface,
            onDismissRequest = { onDismissRequest() },
            title = {
                Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = dialogTitle,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleMedium
                                .copy(color = MaterialTheme.colorScheme.onSurface),
                )
            },
            text = {
                Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = dialogText,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyLarge
                                .copy(color = MaterialTheme.colorScheme.onSurface),
                )
            },
            confirmButton = {
                Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.spaceTwelve)
                ) {
                    if (!negativeButtonText.isNullOrBlank()) {
                        AppButton(
                                modifier = Modifier
                                        .padding(vertical = MaterialTheme.spacing.spaceSmall)
                                        .padding(horizontal = MaterialTheme.spacing.spaceMedium),
                                buttonText = negativeButtonText,
                                onClick = onDismissRequest,
                                contentColor = MaterialTheme.colorScheme.error
                        )
                    }

                    AppButton(
                            buttonText = positiveButtonText,
                            onClick = onConfirm,
                    )
                }
            },
            properties = DialogProperties(usePlatformDefaultWidth = false)
    )
}

@PreviewLightDark
@Composable
private fun AppDialog_Preview() {
    QRCraftTheme {
        Box(
                modifier = Modifier
                        .fillMaxSize()
                        .background(color = MaterialTheme.colorScheme.background),

                contentAlignment = Alignment.Center
        ) {
            AppDialog(
                    onDismissRequest = {},
                    onConfirm = {},
                    dialogTitle = "Delete",
                    dialogText = "Delete Note?",
                    positiveButtonText = "OK",
                    negativeButtonText = "Cancel"
            )
        }
    }
}
