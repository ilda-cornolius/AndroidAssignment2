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


 //This is a composable ui function for task navigation 
@Composable
fun TaskNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    //The NavHost function is used to navigate between different screens
    NavHost(
        navController = navController,
        startDestination = NavigationRoute.Home.route,
        modifier = modifier
    ) {
        //The function to navigate to the home screen
        composable(NavigationRoute.Home.route) {
            HomeScreen(
                onNavigateToCreate = {
                    navController.navigate(NavigationRoute.CreateTask.route)
                },
                onNavigateToEdit = { taskId ->
                    navController.navigate(NavigationRoute.createEditTaskRoute(taskId).route)
                },
                modifier = Modifier.fillMaxSize()
            )
        }

        //The function to navigate to the create task screen
        composable(NavigationRoute.CreateTask.route) {
            CreateTaskScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxSize()
            )
        }

        //The function to navigate to the edit task screen
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
                //The taskId of the task to be edited 
                taskId = taskId,
                onNavigateBack = {
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

