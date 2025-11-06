package com.tonyxlab.qrcraft.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.tonyxlab.qrcraft.domain.model.QrData
import com.tonyxlab.qrcraft.domain.model.QrDataType

class NavOperations(val navHostController: NavHostController) {

    fun navigateToHistoryScreenDestination() {
        navHostController.navigate(route = Destinations.HistoryScreenDestination) {
            launchSingleTop = true
            restoreState = true
            popUpTo(navHostController.graph.startDestinationId) { saveState = true }
        }
    }

    fun navigateToScanScreenDestination(fromResultScreen: Boolean = false) {
        if (fromResultScreen) {
            navHostController.navigate(route = Destinations.ScanScreenDestination)
        } else {

            navHostController.navigate(route = Destinations.ScanScreenDestination) {
                launchSingleTop = true
                restoreState = true
                popUpTo(navHostController.graph.startDestinationId) { saveState = true }
            }
        }
    }

    fun navigateToCreateScreenDestination(fromEntryScreen: Boolean = false) {

        if (fromEntryScreen) {
            navHostController.navigate(route = Destinations.CreateScreenDestination)
        } else {
            navHostController.navigate(route = Destinations.CreateScreenDestination) {
                launchSingleTop = true
                restoreState = true
                popUpTo(navHostController.graph.startDestinationId) { saveState = true }
            }
        }
    }

    fun navigateToResultScreenDestination(id: Long) {
        navHostController.navigate(
                route = Destinations.ResultScreenDestination(id = id)
        )
    }

    fun navigateToEntryScreenDestination(qrDataType: QrDataType) {
        navHostController.navigate(
                route = Destinations.EntryScreenDestination(
                        qrDataType = qrDataType

                )
        )
    }

    fun navigateToPreviewScreenDestination(id: Long) {
        navHostController.navigate(
                route = Destinations.PreviewScreenDestination(id = id)
        )
    }

    fun popBackStack() {
        navHostController.popBackStack()
    }
}

@Composable
fun rememberNavOperations(
    navController: NavHostController = rememberNavController()
): NavOperations {
    return remember { NavOperations(navHostController = navController) }
}
