package com.tonyxlab.qrcraft.presentation.screens.scan.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.tonyxlab.qrcraft.navigation.Destinations
import com.tonyxlab.qrcraft.presentation.core.utils.spacing
import com.tonyxlab.qrcraft.presentation.theme.ui.QRCraftTheme

@Composable
fun ExtendedFabButton(
    navController: NavController,
    modifier: Modifier = Modifier
) {

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val currentHierarchy = navBackStackEntry?.destination?.hierarchy

    Row(
            modifier = modifier
                    .clip(RoundedCornerShape(MaterialTheme.spacing.spaceOneHundred))
                    .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                    .padding(horizontal = MaterialTheme.spacing.spaceSmall)
                    .padding(vertical = MaterialTheme.spacing.spaceExtraSmall)

    ) {

        ExtendedFabNavOptions.entries.forEachIndexed { i, options ->

            val selected by remember(currentRoute) {

                derivedStateOf {

                    currentHierarchy?.any { it.hasRoute(options.route::class) } != false
                }
            }


            IconFabButton(
                    imageVector = options.icon,
                    semanticLabel = options.semanticLabel,
                    isActive = selected,
                    onClickIcon = { navController.navigate(options.route) }
            )

        }

    }
}

@Composable
private fun IconFabButton(
    imageVector: ImageVector,
    semanticLabel: String,
    isActive: Boolean,
    onClickIcon: () -> Unit
) {

    val containerColor = if (isActive)
        MaterialTheme.colorScheme.primary
    else
        MaterialTheme.colorScheme.surfaceContainerHigh

    Box(
            modifier = Modifier
                    .clip(CircleShape)
                    .background(color = containerColor, shape = CircleShape)
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
                tint = MaterialTheme.colorScheme.onSurface
        )
    }
}

enum class ExtendedFabNavOptions(
    val semanticLabel: String,
    val icon: ImageVector,
    val route: Destinations
) {

    History(
            semanticLabel = "History",
            icon = Icons.Default.History,
            route = Destinations.HistoryScreenDestination
    ),

    Scan(
            semanticLabel = "Scan",
            icon = Icons.Default.QrCodeScanner,
            route = Destinations.ScanScreenDestination
    ),

    Create(
            semanticLabel = "Create",
            icon = Icons.Default.AddCircleOutline,
            route = Destinations.CreateScreenDestination
    )
}


@PreviewLightDark
@Composable
private fun ExtendedFabButton_Preview() {

    val navController = rememberNavController()
    QRCraftTheme {

        Box (modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter){

            ExtendedFabButton(
                    navController = navController,
                    modifier = Modifier
            )

        }
    }


}