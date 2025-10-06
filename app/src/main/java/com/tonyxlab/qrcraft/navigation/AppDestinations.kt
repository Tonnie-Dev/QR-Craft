package com.tonyxlab.qrcraft.navigation

import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.tonyxlab.qrcraft.domain.QrDataType
import com.tonyxlab.qrcraft.presentation.screens.result.ResultScreen
import com.tonyxlab.qrcraft.presentation.screens.scan.ScanScreen
import kotlinx.serialization.Serializable

fun NavGraphBuilder.appDestinations(
    navOperations: NavOperations,
    navController: NavController,
    modifier: Modifier = Modifier
) {

    composable<Destinations.ScanScreenDestination> {

        ScanScreen(modifier = modifier, navController = navController, navOperations = navOperations)
    }

    composable<Destinations.ResultScreenDestination> {

        ResultScreen(modifier = modifier, navOperations = navOperations)
    }

    composable<Destinations.HistoryScreenDestination> {

        ScanScreen(modifier = modifier, navController = navController, navOperations = navOperations)
    }


    composable<Destinations.CreateScreenDestination> {

        ScanScreen(modifier = modifier, navController = navController, navOperations = navOperations)
    }
}

sealed class Destinations {

    @Serializable
    data object HistoryScreenDestination : Destinations()

    @Serializable
    data object ScanScreenDestination : Destinations()

    @Serializable
    data object CreateScreenDestination : Destinations()

    @Serializable
    data class ResultScreenDestination(
        val displayName: String,
        val prettifiedData: String,
        val rawDataValue: String,
        val qrDataType: QrDataType
    ) : Destinations()

}