package com.tonyxlab.qrcraft.navigation

import android.R.attr.data
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

        navHostController.navigate(route = Destinations.ResultScreenDestination(
                displayName = qrData.displayName,
                data = qrData.data,
                qrDataType = qrData.qrDataType
        ))
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
