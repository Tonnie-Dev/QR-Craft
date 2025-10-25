package com.tonyxlab.qrcraft.navigation

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.tonyxlab.qrcraft.domain.model.QrDataType
import com.tonyxlab.qrcraft.presentation.screens.create.CreateScreen
import com.tonyxlab.qrcraft.presentation.screens.entry.EntryScreen
import com.tonyxlab.qrcraft.presentation.screens.history.HistoryScreen
import com.tonyxlab.qrcraft.presentation.screens.preview.PreviewScreen
import com.tonyxlab.qrcraft.presentation.screens.result.ResultScreen
import com.tonyxlab.qrcraft.presentation.screens.scan.ScanScreen
import kotlinx.serialization.Serializable

fun NavGraphBuilder.appDestinations(
    navOperations: NavOperations,
    modifier: Modifier = Modifier
) {
    composable<Destinations.ScanScreenDestination> {

        ScanScreen(
                modifier = modifier,
                navOperations = navOperations
        )
    }

    composable<Destinations.ResultScreenDestination> {

        ResultScreen(modifier = modifier, navOperations = navOperations)
    }

    composable<Destinations.HistoryScreenDestination> {
        HistoryScreen(navOperations = navOperations)

    }

    composable<Destinations.CreateScreenDestination> {
        CreateScreen(
                navOperations = navOperations,
                modifier = modifier
        )
    }


    composable<Destinations.EntryScreenDestination> {
        EntryScreen(
                navOperations = navOperations,
                modifier = modifier
        )
    }


    composable<Destinations.PreviewScreenDestination> {
        PreviewScreen(
                modifier = modifier,
                navOperations = navOperations
        )
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

    @Serializable
    data class EntryScreenDestination(val qrDataType: QrDataType) : Destinations()

    @Serializable
    data class PreviewScreenDestination(val id: Long) : Destinations()
}