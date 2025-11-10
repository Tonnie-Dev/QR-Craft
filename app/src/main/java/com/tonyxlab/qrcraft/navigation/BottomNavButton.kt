package com.tonyxlab.qrcraft.navigation

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.tonyxlab.qrcraft.R
import com.tonyxlab.qrcraft.presentation.core.utils.spacing
import com.tonyxlab.qrcraft.presentation.theme.LinkBg
import com.tonyxlab.qrcraft.presentation.theme.QRCraftTheme
import com.tonyxlab.qrcraft.utils.ifThen
import kotlinx.coroutines.delay

@Composable
fun BoxScope.AnimatedBottomNavButton(
    currentNavOption: BottomNavOption,
    onClickIcon: (BottomNavOption) -> Unit,
    isCamPermissionGranted: Boolean,
    modifier: Modifier = Modifier
) {

    var showBar by rememberSaveable { mutableStateOf(false) }

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
                    .navigationBarsPadding()
                    .align(alignment = Alignment.BottomCenter), visible = showBar,
            enter = slideInVertically(
                    initialOffsetY = { full -> (full * 0.35f).toInt() },
                    animationSpec = spring(
                            dampingRatio = .5f,
                            stiffness = Spring.StiffnessLow
                    )
            ) + fadeIn(),
            exit = slideOutVertically(
                    targetOffsetY = { full -> (full * .35f).toInt() }
            ) + fadeOut()

    ) {

        BottomNavButton(
                currentNavOption = currentNavOption,
                onClickIcon = onClickIcon,
                modifier = modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = MaterialTheme.spacing.spaceMedium)
        )
    }
}

@Composable
fun BottomNavButton(
    currentNavOption: BottomNavOption,
    onClickIcon: (BottomNavOption) -> Unit,
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
                    isSelected = currentNavOption == BottomNavOption.History,
                    isPrimary = false
            ) {
                onClickIcon(BottomNavOption.History)
            }

            // Right NavButtonItem
            NavButtonItem(
                    icon = R.drawable.icon_create,
                    semanticLabel = stringResource(id = R.string.cds_text_create_qr),
                    isSelected = currentNavOption == BottomNavOption.Create,
                    isPrimary = false
            ) {
                onClickIcon(BottomNavOption.Create)
            }
        }

        // Center NavButtonItem
        NavButtonItem(
                modifier = Modifier.align(Alignment.Center),
                icon = R.drawable.icon_scan,
                semanticLabel = stringResource(id = R.string.cds_text_scan_qr_code),
                isSelected = true,
                isPrimary = true
        ) {
            onClickIcon(BottomNavOption.Scan)
        }
    }
}

@Composable
private fun NavButtonItem(
    @DrawableRes
    icon: Int,
    semanticLabel: String,
    isPrimary: Boolean,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClickIcon: () -> Unit,
) {

    val containerColor = when {
        isPrimary -> MaterialTheme.colorScheme.primary
        isSelected && !isPrimary -> LinkBg
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
                    .semantics {
                        role = Role.Button
                        selected = isSelected || isPrimary
                        contentDescription = semanticLabel
                    }
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
                    .clickable { onClickIcon() }, contentAlignment = Alignment.Center
    ) {
        Icon(
                modifier = Modifier,
                painter = painterResource(icon),
                contentDescription = semanticLabel,
                tint = iconTint
        )
    }
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
                        currentNavOption = BottomNavOption.Create,
                        onClickIcon = {},
                        modifier = Modifier
                )
            }
        }
    }
}