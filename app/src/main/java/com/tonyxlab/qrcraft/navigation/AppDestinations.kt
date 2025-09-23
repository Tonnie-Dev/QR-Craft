package com.tonyxlab.qrcraft.navigation

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.tonyxlab.qrcraft.domain.QrData
import com.tonyxlab.qrcraft.domain.QrDataType
import com.tonyxlab.qrcraft.presentation.screens.result.ResultScreen
import com.tonyxlab.qrcraft.presentation.screens.scan.ScanScreen
import kotlinx.serialization.Serializable

fun NavGraphBuilder.appDestinations(
    navOperations: NavOperations,
    modifier: Modifier = Modifier
) {

    composable<Destinations.ScanScreenDestination> {

        ScanScreen(modifier = modifier, navOperations = navOperations)
    }

    composable<Destinations.ResultScreenDestination> {

        ResultScreen(modifier = modifier, navOperations = navOperations)
    }
}

sealed class Destinations {

    @Serializable
    data object ScanScreenDestination : Destinations()

    @Serializable
    data class ResultScreenDestination(
        val displayName: String,
        val data: String,
        val qrDataType: QrDataType
    ) : Destinations()

}