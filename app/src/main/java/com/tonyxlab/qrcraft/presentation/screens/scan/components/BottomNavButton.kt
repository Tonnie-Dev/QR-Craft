package com.tonyxlab.qrcraft.presentation.screens.scan.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.tonyxlab.qrcraft.presentation.core.utils.spacing
import com.tonyxlab.qrcraft.presentation.screens.scan.handling.FabNavOption
import com.tonyxlab.qrcraft.presentation.screens.scan.handling.ScanUiEvent
import com.tonyxlab.qrcraft.presentation.screens.scan.handling.ScanUiState
import com.tonyxlab.qrcraft.presentation.theme.ui.LinkBg
import com.tonyxlab.qrcraft.presentation.theme.ui.QRCraftTheme
import com.tonyxlab.qrcraft.util.ifThen
import kotlinx.coroutines.delay

@Composable
fun BoxScope.AnimatedBottomNavButton(
    uiState: ScanUiState,
    onEvent: (ScanUiEvent) -> Unit,
   isCamPermissionGranted: Boolean,
    modifier: Modifier = Modifier
) {

    var showBar by rememberSaveable { mutableStateOf(false) }

    val offSetY by animateDpAsState(
            targetValue = if (showBar)
                MaterialTheme.spacing.spaceDefault
            else
                MaterialTheme.spacing.spaceTen * 8,
            animationSpec = spring(
                    dampingRatio = 0.55f,
                    stiffness = Spring.StiffnessMediumLow
            ),
            label = "bottomBarOffset"
    )

    LaunchedEffect (isCamPermissionGranted){

        if (isCamPermissionGranted){
            delay(220)
            showBar = true
        }else{
            showBar = false
        }
    }

    AnimatedVisibility(
            modifier = modifier
                    .align(Alignment.BottomCenter)
                    .offset(y = offSetY),
            visible = showBar,
            enter = slideInVertically(
                    initialOffsetY = { full -> (full * 0.35f).toInt() },
                    animationSpec = spring(
                            dampingRatio = .7f,
                            stiffness = Spring.StiffnessLow
                    )
            ) + fadeIn(),
            exit = slideOutVertically(
                    targetOffsetY = { full -> (full * .35f).toInt() }
            ) + fadeOut()

    ) {

        BottomNavButton(
                uiState = uiState,
                onEvent = onEvent,
                modifier = modifier.align(Alignment.BottomCenter).padding(bottom = MaterialTheme.spacing.spaceMedium)
        )
    }

}

@Composable
fun BottomNavButton(
    uiState: ScanUiState,
    onEvent: (ScanUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
            modifier = modifier
                    .clip(RoundedCornerShape(MaterialTheme.spacing.spaceOneHundred))
                    .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                    .padding(horizontal = MaterialTheme.spacing.spaceSmall)
                    .padding(vertical = MaterialTheme.spacing.spaceExtraSmall),

            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.spaceExtraSmall),
            verticalAlignment = Alignment.CenterVertically,

            ) {

        BottomNavOptions.entries.forEachIndexed { i, option ->

            val selected = uiState.fabNavOption == option.navOption
            val isPrimary = option.navOption == FabNavOption.SCAN
            NavButtonItem(
                    imageVector = option.icon,
                    semanticLabel = option.semanticLabel,
                    selected = selected,
                    isPrimary = isPrimary
            ) {

                onEvent(ScanUiEvent.FabOptionSelected(option.navOption))

            }

        }

    }
}

@Composable
private fun NavButtonItem(
    imageVector: ImageVector,
    semanticLabel: String,
    isPrimary: Boolean,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClickIcon: () -> Unit,
) {

    val containerColor = when {
        isPrimary -> MaterialTheme.colorScheme.primary
        selected && !isPrimary -> LinkBg
        else -> MaterialTheme.colorScheme.surfaceContainerHigh
    }
    val iconSize = if (isPrimary)

        MaterialTheme.spacing.spaceExtraSmall * 7
    else

        MaterialTheme.spacing.spaceExtraSmall * 7
    //  MaterialTheme.spacing.spaceMedium

    val containerSize = if (isPrimary)

        MaterialTheme.spacing.spaceTwelve * 6
    else
        MaterialTheme.spacing.spaceTwelve * 6
    //        MaterialTheme.spacing.spaceDoubleDp * 22

    val iconTint = if (isPrimary)
        MaterialTheme.colorScheme.onPrimary
    else
        MaterialTheme.colorScheme.onSurface

    val elevate = isPrimary || selected

    val scale by animateFloatAsState(if (elevate) 1.1f else 1f)

    Box(
            modifier = modifier

                    .ifThen(elevate) {
                        shadow(
                                elevation = MaterialTheme.spacing.spaceExtraSmall,
                                shape = CircleShape,
                                clip = false
                        )
                    }
                    .clip(CircleShape)
                    .background(color = containerColor, shape = CircleShape)
                    //  .scale(scale = scale)
                    .size(containerSize)
                    .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                    ) {
                        onClickIcon()
                    }
                    .padding(horizontal = MaterialTheme.spacing.spaceMedium)
                    .padding(vertical = MaterialTheme.spacing.spaceSmall),
            contentAlignment = Alignment.Center
    ) {

        Icon(
                modifier = Modifier.size(iconSize),
                imageVector = imageVector,
                contentDescription = semanticLabel,
                tint = iconTint
        )
    }
}

enum class BottomNavOptions(
    val semanticLabel: String,
    val icon: ImageVector,
    val navOption: FabNavOption
) {

    History(
            semanticLabel = "History",
            icon = Icons.Default.History,
            navOption = FabNavOption.HISTORY
    ),

    Scan(
            semanticLabel = "Scan",
            icon = Icons.Default.QrCodeScanner,
            navOption = FabNavOption.SCAN
    ),

    Create(
            semanticLabel = "Create",
            icon = Icons.Default.AddCircleOutline,
            navOption = FabNavOption.CREATE
    )
}

@PreviewLightDark
@Composable
private fun BottomNavButton_Preview() {
    QRCraftTheme {

        Box(
                modifier = Modifier
                        .background(MaterialTheme.colorScheme.surface)
                        .fillMaxSize(),
                contentAlignment = Alignment.Center
        ) {

            BottomNavButton(
                    uiState = ScanUiState(),
                    onEvent = {},
                    modifier = Modifier
            )
        }
    }
}