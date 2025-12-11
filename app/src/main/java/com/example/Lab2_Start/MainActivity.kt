package com.example.Lab2_Start

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import com.example.Lab2_Start.navigation.TaskNavigation
import com.example.Lab2_Start.ui.theme.Lab2_DemoTheme
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass



 //Initializing the MainActivity of the Task Manager App
 //It is used as the entry point to the application
class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    //OnCreate function to start the application
    override fun onCreate(savedInstanceState: Bundle?) {
        
        super.onCreate(savedInstanceState)

        //enables app content to extend behind system UI bars
        enableEdgeToEdge()
        setContent {
            Lab2_DemoTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    //runs the TaskManagerApp function 
                    TaskManagerApp()
                }
            }
        }
    }
}

/**
 * Main app composable that handles navigation and responsive layout.
 * Uses WindowSizeClass to adapt UI for different screen sizes including foldable devices.
 */
 //Using WindowsSizeClass to adapt the UI for different screen sizes 
 //jetpack compose function to create the UI
 //it is also used for handling navigation between screens
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun TaskManagerApp(
    modifier: Modifier = Modifier
) {
    //variable that stores the current context of the activity
    val context = LocalContext.current
    //casting the context to a ComponentActivity, stored in the activity variable
    val activity = context as ComponentActivity
    //variable that stores the current window size class based on the activity
    val windowSizeClass = calculateWindowSizeClass(activity = activity)
    val navController = rememberNavController()

    //Changing the layout based on the window size class
    when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> {
            // Mobile phone layout properties
            TaskNavigation(
                navController = navController,
                modifier = modifier
            )
        }
        WindowWidthSizeClass.Medium -> {
            // Tablet layout properties
            TaskNavigation(
                navController = navController,
                modifier = modifier
            )
        }
        WindowWidthSizeClass.Expanded -> {
            // Large tablet or desktop layout properties
            TaskNavigation(
                navController = navController,
                modifier = modifier
            )
        }
        else -> {
            // Fallback properties
            TaskNavigation(
                navController = navController,
                modifier = modifier
            )
        }
    }
}

