package com.tonyxlab.qrcraft.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost

@Composable
fun AppNavHost(
    navOperations: NavOperations,
    modifier: Modifier = Modifier
) {
    val navController = navOperations.navHostController

    NavHost(
            navController = navController,
            startDestination = Destinations.ScanScreenDestination
    ) {

        appDestinations(
                navOperations = navOperations,

                modifier = modifier
        )
    }

}