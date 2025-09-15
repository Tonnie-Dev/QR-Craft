package com.tonyxlab.qrcraft.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

class NavOperations(val navHostController: NavHostController) {

    fun navigateToScanScreenDestination() {

        navHostController.navigate(route = Destinations.ScanScreenDestination)
    }
}

@Composable
fun rememberNavOperations(
    navController: NavHostController = rememberNavController()
): NavOperations {
    return remember { NavOperations(navHostController = navController) }
}
