package com.example.Lab2_Start.navigation



 //This Class contains navigation routes that are used to navigate between screens
class NavigationRoute(val route: String) {
    companion object {
        //Object containing the home route
        val Home = NavigationRoute("home")
        //Object containing the create task route
        val CreateTask = NavigationRoute("create_task")
        //Object containing the edit task route template with a taskId parameter
        val EditTask = NavigationRoute("edit_task/{taskId}")
        
        //Function to create edit task route with a specific task ID
        fun createEditTaskRoute(taskId: String) = NavigationRoute("edit_task/$taskId")
    }
}

