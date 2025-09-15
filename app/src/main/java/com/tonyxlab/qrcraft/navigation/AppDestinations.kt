package com.tonyxlab.qrcraft.navigation

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.tonyxlab.qrcraft.presentation.screens.scan.ScanScreen
import kotlinx.serialization.Serializable

fun NavGraphBuilder.appDestinations(
    navOperations: NavOperations,
    modifier: Modifier = Modifier
) {

    composable < Destinations.ScanScreenDestination> {

        ScanScreen(modifier = modifier, navOperations = navOperations)
    }
}

sealed class Destinations {

    @Serializable
    data object ScanScreenDestination : Destinations()
}