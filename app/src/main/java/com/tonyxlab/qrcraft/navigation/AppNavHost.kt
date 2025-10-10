package com.tonyxlab.qrcraft.navigation

import android.Manifest
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.tonyxlab.qrcraft.presentation.core.utils.spacing

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun AppNavHost(
    navOperations: NavOperations,
    modifier: Modifier = Modifier
) {
    val navController = navOperations.navHostController

    val backStackEntry by navController.currentBackStackEntryAsState()
    val camPermission = rememberPermissionState(Manifest.permission.CAMERA)

    val currentNavOption = remember(backStackEntry) {

        when (backStackEntry?.destination?.route.orEmpty()) {

            in listOf("com.tonyxlab.qrcraft.navigation.Destinations.HistoryScreenDestination") ->
                BottomNavOption.History

            in listOf("com.tonyxlab.qrcraft.navigation.Destinations.CreateScreenDestination") ->
                BottomNavOption.Create

            else -> BottomNavOption.Scan // default / unknown → highlight Scan
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {

        NavHost(
                navController = navController,
                startDestination = Destinations.ScanScreenDestination
        ) {

            appDestinations(
                    navOperations = navOperations,

                    modifier = modifier
            )
        }

        AnimatedBottomNavButton(
                modifier = modifier
                        .align(alignment = Alignment.BottomCenter)
                        .padding(bottom = MaterialTheme.spacing.spaceSmall),
                currentNavOption = currentNavOption,
                isCamPermissionGranted = camPermission.status.isGranted,
                onClickIcon = {

                    when (it) {
                        BottomNavOption.History -> {
                            navOperations.navigateToHistoryScreenDestination()
                        }

                        BottomNavOption.Scan -> {
                            navOperations.navigateToScanScreenDestination()
                        }

                        BottomNavOption.Create -> {
                            navOperations.navigateToCreateScreenDestination()
                        }
                    }
                }
        )

    }

}