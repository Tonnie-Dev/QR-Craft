package com.tonyxlab.qrcraft.presentation.screens.scan.components

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.tonyxlab.qrcraft.R
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

    LaunchedEffect(isCamPermissionGranted) {

        if (isCamPermissionGranted) {
            delay(300)
            showBar = true
        } else {
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
                modifier = modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = MaterialTheme.spacing.spaceMedium)
        )
    }
}

@Composable
fun BottomNavButton(
    uiState: ScanUiState,
    onEvent: (ScanUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {

    val rowHeight = MaterialTheme.spacing.spaceDoubleDp * 26

    Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
    ) {
        Row(
                modifier = Modifier
                        .clip(RoundedCornerShape(MaterialTheme.spacing.spaceOneHundred))
                        .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                        .height(rowHeight)
                        .padding(horizontal = MaterialTheme.spacing.spaceSmall)
                        .padding(vertical = MaterialTheme.spacing.spaceExtraSmall),
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.spaceTwelve * 6),
                verticalAlignment = Alignment.CenterVertically
        ) {

            // Left NavButtonItem
            NavButtonItem(
                    icon = R.drawable.icon_history,
                    semanticLabel = stringResource(id = R.string.cds_text_view_history),
                    selected = uiState.fabNavOption == BottomNavOptions.History.navOption,
                    isPrimary = false
            ) {
                onEvent(ScanUiEvent.FabOptionSelected(fabNavOption = FabNavOption.HISTORY))
            }

            // Right NavButtonItem
            NavButtonItem(
                    icon = R.drawable.icon_create,
                    semanticLabel = stringResource(id = R.string.cds_text_create_qr),
                    selected = uiState.fabNavOption == BottomNavOptions.Create.navOption,
                    isPrimary = false
            ) {
                onEvent(ScanUiEvent.FabOptionSelected(fabNavOption = FabNavOption.CREATE))
            }
        }

        // Center NavButtonItem
        NavButtonItem(
                modifier = Modifier.align(Alignment.Center),
                icon = R.drawable.icon_scan,
                semanticLabel = stringResource(id = R.string.cds_text_scan_qr_code),
                selected = true,
                isPrimary = true
        ) {
            onEvent(ScanUiEvent.FabOptionSelected(fabNavOption = FabNavOption.SCAN))
        }
    }
}

@Composable
private fun NavButtonItem(
    @DrawableRes
    icon: Int,
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

    val containerSize = if (isPrimary)
        MaterialTheme.spacing.spaceExtraLarge
    else
        MaterialTheme.spacing.spaceDoubleDp * 22

    val iconTint = if (isPrimary)
        MaterialTheme.colorScheme.onPrimary
    else
        MaterialTheme.colorScheme.onSurface

    Box(
            modifier = modifier
                    .ifThen(isPrimary) {
                        shadow(
                                elevation = MaterialTheme.spacing.spaceExtraSmall,
                                shape = CircleShape,
                                clip = false
                        )
                    }
                    .clip(CircleShape)
                    .background(color = containerColor, shape = CircleShape)
                    .size(containerSize)
                    .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                    ) { onClickIcon() }
            , contentAlignment = Alignment.Center
    ) {
        Icon(
                modifier = Modifier,
                painter = painterResource(icon),
                contentDescription = semanticLabel,
                tint = iconTint
        )

    }
}

enum class BottomNavOptions(
    val semanticLabel: String,
    @DrawableRes
    val icon: Int,
    val navOption: FabNavOption
) {

    History(
            semanticLabel = "History",
            icon = R.drawable.icon_history,
            navOption = FabNavOption.HISTORY
    ),

    Scan(
            semanticLabel = "Scan",
            icon = R.drawable.icon_scan,
            navOption = FabNavOption.SCAN
    ),

    Create(
            semanticLabel = "Create",
            icon = R.drawable.icon_create,
            navOption = FabNavOption.CREATE
    )
}

@PreviewLightDark
@Composable
private fun BottomNavButton_Preview() {
    QRCraftTheme {
        Column(
                modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surface)
        ) {
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.spaceExtraLarge))
            Column(
                    modifier = Modifier
                            .fillMaxSize()
                            .padding(MaterialTheme.spacing.spaceMedium),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.spaceMedium)
            ) {
                BottomNavButton(
                        uiState = ScanUiState(fabNavOption = FabNavOption.CREATE),
                        onEvent = {},
                        modifier = Modifier
                )
            }
        }
    }
}