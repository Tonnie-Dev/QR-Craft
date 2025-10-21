package com.tonyxlab.qrcraft.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
        HistoryScreen()

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

@Composable
fun FakeHistoryDestination(modifier: Modifier = Modifier) {
    Box(
            modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface),
            contentAlignment = Alignment.Center
    ) {
        Text(text = "This is a Fake History Destination")
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
    data class PreviewScreenDestination(val jsonMapString: String) : Destinations()
}