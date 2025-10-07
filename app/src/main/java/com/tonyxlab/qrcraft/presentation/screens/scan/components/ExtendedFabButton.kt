package com.tonyxlab.qrcraft.presentation.screens.scan.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.tonyxlab.qrcraft.presentation.core.utils.spacing
import com.tonyxlab.qrcraft.presentation.screens.scan.handling.FabNavOption
import com.tonyxlab.qrcraft.presentation.screens.scan.handling.ScanUiEvent
import com.tonyxlab.qrcraft.presentation.screens.scan.handling.ScanUiState
import com.tonyxlab.qrcraft.presentation.theme.ui.QRCraftTheme
import com.tonyxlab.qrcraft.util.ifThen

@Composable
fun ExtendedFabButton(
    uiState: ScanUiState,
    onEvent: (ScanUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
            modifier = modifier
                    .clip(RoundedCornerShape(MaterialTheme.spacing.spaceOneHundred))
                    .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                    .padding(horizontal = MaterialTheme.spacing.spaceSmall)
                    .padding(vertical = MaterialTheme.spacing.spaceExtraSmall)
                    .navigationBarsPadding()

    ) {

        ExtendedFabNavOptions.entries.forEachIndexed { i, option ->

            FabButtonItem(
                    imageVector = option.icon,
                    semanticLabel = option.semanticLabel,
                    isActive = uiState.fabNavOption == option.fabNavOption
            ) {

                onEvent(ScanUiEvent.FabOptionSelected(option.fabNavOption))

            }

        }

    }
}

@Composable
private fun FabButtonItem(
    imageVector: ImageVector,
    semanticLabel: String,
    isActive: Boolean,
    onClickIcon: () -> Unit
) {

    val containerColor = if (isActive)
        MaterialTheme.colorScheme.primary
    else
        MaterialTheme.colorScheme.surfaceContainerHigh

    val iconTint = if (isActive)
        MaterialTheme.colorScheme.onPrimary
    else
        MaterialTheme.colorScheme.onSurface

    val scale by animateFloatAsState( if (isActive) 1.2f else 1f)
    Box(
            modifier = Modifier
                    .clip(CircleShape)
                    .ifThen(isActive) {
                        shadow(
                                elevation = MaterialTheme.spacing.spaceExtraSmall,
                                shape = CircleShape,
                                clip = false
                        )
                    }
                    .background(color = containerColor, shape = CircleShape)
                    .scale(scale = scale)
                    .padding(MaterialTheme.spacing.spaceExtraSmall)
                    .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                    ) {
                        onClickIcon()
                    }
    ) {

        Icon(
                modifier = Modifier
                        .size(MaterialTheme.spacing.spaceMedium)
                        .padding(),
                imageVector = imageVector,
                contentDescription = semanticLabel,
                tint = iconTint
        )
    }
}

enum class ExtendedFabNavOptions(
    val semanticLabel: String,
    val icon: ImageVector,
    val fabNavOption: FabNavOption
) {

    History(
            semanticLabel = "History",
            icon = Icons.Default.History,
            fabNavOption = FabNavOption.HISTORY
    ),

    Scan(
            semanticLabel = "Scan",
            icon = Icons.Default.QrCodeScanner,
            fabNavOption = FabNavOption.SCAN
    ),

    Create(
            semanticLabel = "Create",
            icon = Icons.Default.AddCircleOutline,
            fabNavOption = FabNavOption.CREATE
    )
}

@PreviewLightDark
@Composable
private fun ExtendedFabButton_Preview() {
    QRCraftTheme {

        Box(
                modifier = Modifier
                        .background(MaterialTheme.colorScheme.surface)
                        .fillMaxSize(), contentAlignment = Alignment.BottomCenter
        ) {

            ExtendedFabButton(
                    uiState = ScanUiState(),
                    onEvent = {},
                    modifier = Modifier
            )
        }
    }
}