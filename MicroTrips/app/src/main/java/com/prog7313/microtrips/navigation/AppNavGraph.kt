package com.prog7313.microtrips.navigation

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.prog7313.microtrips.screens.AddDestScreen
import com.prog7313.microtrips.screens.DestDetailsScreen
import com.prog7313.microtrips.screens.HomeScreen
import com.prog7313.microtrips.screens.SavedDestinationsScreen
import com.prog7313.microtrips.screens.SettingsScreen
import com.prog7313.microtrips.screens.ViewDestScreen
import com.prog7313.microtrips.viewmodel.DestinationViewModel
import com.prog7313.microtrips.viewmodel.SettingsViewModel
import kotlinx.coroutines.launch

@Composable
fun AppNavGraph(onExit: () -> Unit) {
    val navController = rememberNavController()
    val viewModel: DestinationViewModel = viewModel()
    val settingsViewModel: SettingsViewModel = viewModel()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            NavDrawer(
                onViewDestinations = {
                    navController.navigate(Routes.DESTINATIONS)
                    scope.launch { drawerState.close() }
                },
                onSaved = {
                    navController.navigate(Routes.SAVED_DESTINATIONS)
                    scope.launch { drawerState.close() }
                },
                onSettings = {
                    navController.navigate(Routes.SETTINGS)
                    scope.launch { drawerState.close() }
                }
            )
        }
    ) {
        NavHost(navController = navController, startDestination = Routes.HOME) {
            composable(Routes.HOME) {
                HomeScreen(
                    onOpenAddDest = { navController.navigate(Routes.ADD_DESTINATIONS) },
                    onOpenView = { navController.navigate(Routes.DESTINATIONS) },
                    onExit = onExit,
                    onOpenDrawer = { scope.launch { drawerState.open() } }
                )
            }

            composable(Routes.DESTINATIONS) {
                ViewDestScreen(
                    destinationVm = viewModel,
                    onBack = { navController.popBackStack() },
                    onDestinationClick = { id -> navController.navigate("${Routes.DESTINATION_DETAILS}/$id") },
                    settingsVm = settingsViewModel
                )
            }

            composable(
                route = "${Routes.DESTINATION_DETAILS}/{id}",
                arguments = listOf(navArgument("id") { type = NavType.LongType })
            ) { backStackEntry ->
                val destId = backStackEntry.arguments?.getLong("id") ?: 0L
                val destination = viewModel.getDestination(destId)

                if (destination != null) {
                    DestDetailsScreen(destination = destination, onBack = { navController.popBackStack() })
                }
            }

            composable(Routes.ADD_DESTINATIONS) {
                AddDestScreen(
                    onBack = { navController.popBackStack() },
                    onSave = {
                        viewModel.addDestination(it)
                        navController.popBackStack()
                    }
                )
            }

            composable(Routes.SAVED_DESTINATIONS) {
                SavedDestinationsScreen(
                    onBack = { navController.popBackStack() },
                    onDestinationClick = { id -> navController.navigate("${Routes.DESTINATION_DETAILS}/$id") },
                    destinationVm = viewModel,
                    settingsVm = settingsViewModel
                )
            }

            composable(Routes.SETTINGS) {
                SettingsScreen(onBack = { navController.popBackStack() }, settingsVm = settingsViewModel)
            }
        }
    }
}