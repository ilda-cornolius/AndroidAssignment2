package com.example.Lab2_Start.navigation

/**
 * Sealed class representing navigation routes in the app.
 */
sealed class NavigationRoute(val route: String) {
    object Home : NavigationRoute("home")
    object CreateTask : NavigationRoute("create_task")
    object EditTask : NavigationRoute("edit_task/{taskId}") {
        fun createRoute(taskId: String) = "edit_task/$taskId"
    }
}

