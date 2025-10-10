package com.tonyxlab.qrcraft.navigation

import android.Manifest
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun AppNavHost(
    navOperations: NavOperations,
    modifier: Modifier = Modifier
) {
    val navController = navOperations.navHostController

    val backStackEntry by navController.currentBackStackEntryAsState()
    val destination = backStackEntry?.destination

    val camPermission = rememberPermissionState(Manifest.permission.CAMERA)

    val currentNavOption = remember(backStackEntry) {

        when {

            destination?.hasRoute<Destinations.HistoryScreenDestination>() == true ->
                BottomNavOption.History

            destination?.hasRoute<Destinations.CreateScreenDestination>() == true ->
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
                modifier = modifier,
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