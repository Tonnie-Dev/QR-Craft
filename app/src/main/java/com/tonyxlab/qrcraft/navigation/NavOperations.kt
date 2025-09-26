package com.tonyxlab.qrcraft.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.tonyxlab.qrcraft.domain.QrData

class NavOperations(val navHostController: NavHostController) {

    fun navigateToScanScreenDestination() {

        navHostController.navigate(route = Destinations.ScanScreenDestination)
    }

    fun navigateToResultScreenDestination(qrData: QrData) {

        navHostController.navigate(
                route = Destinations.ResultScreenDestination(
                        displayName = qrData.displayName,
                        prettifiedData = qrData.prettifiedData,
                        qrDataType = qrData.qrDataType,
                        rawDataValue = qrData.rawDataValue
                )
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
