package com.beka.jaqsyadet.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.beka.jaqsyadet.ui.screen.HabitDetailScreen
import com.beka.jaqsyadet.ui.screen.HabitListScreen

sealed class Screen(val route: String) {
    data object HabitList : Screen("habitList")
    data object HabitDetail : Screen("habitDetail/{habitId}") {
        fun createRoute(habitId: Long) = "habitDetail/$habitId"
    }
}

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String = Screen.HabitList.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.HabitList.route) {
            HabitListScreen(
                onHabitClick = { habitId ->
                    navController.navigate(Screen.HabitDetail.createRoute(habitId))
                },
                onAddHabitClick = {
                    // TODO: Implement add habit screen
                }
            )
        }

        composable(
            route = Screen.HabitDetail.route,
            arguments = listOf(
                navArgument("habitId") {
                    type = NavType.LongType
                }
            )
        ) {
            HabitDetailScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
} 