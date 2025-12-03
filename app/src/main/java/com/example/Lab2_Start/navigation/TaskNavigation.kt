package com.example.Lab2_Start.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.Lab2_Start.ui.screen.CreateTaskScreen
import com.example.Lab2_Start.ui.screen.EditTaskScreen
import com.example.Lab2_Start.ui.screen.HomeScreen

/**
 * Navigation graph for the Task Manager app.
 * Handles navigation between Home, Create Task, and Edit Task screens.
 */
@Composable
fun TaskNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = NavigationRoute.Home.route,
        modifier = modifier
    ) {
        composable(NavigationRoute.Home.route) {
            HomeScreen(
                onNavigateToCreate = {
                    navController.navigate(NavigationRoute.CreateTask.route)
                },
                onNavigateToEdit = { taskId ->
                    navController.navigate(NavigationRoute.EditTask.createRoute(taskId))
                },
                modifier = Modifier.fillMaxSize()
            )
        }

        composable(NavigationRoute.CreateTask.route) {
            CreateTaskScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxSize()
            )
        }

        composable(
            route = NavigationRoute.EditTask.route,
            arguments = listOf(
                navArgument("taskId") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val taskId = backStackEntry.arguments?.getString("taskId") ?: ""
            EditTaskScreen(
                taskId = taskId,
                onNavigateBack = {
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

