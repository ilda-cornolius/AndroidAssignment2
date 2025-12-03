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

/**
 * Main Activity for the Task Manager app.
 * Implements responsive design using WindowSizeClass for large screens and foldable devices.
 */
class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Lab2_DemoTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
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
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun TaskManagerApp(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val activity = context as ComponentActivity
    val windowSizeClass = calculateWindowSizeClass(activity = activity)
    val navController = rememberNavController()

    // Responsive layout based on window size
    when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> {
            // Mobile phone layout
            TaskNavigation(
                navController = navController,
                modifier = modifier
            )
        }
        WindowWidthSizeClass.Medium -> {
            // Tablet or unfolded foldable layout
            TaskNavigation(
                navController = navController,
                modifier = modifier
            )
        }
        WindowWidthSizeClass.Expanded -> {
            // Large tablet or desktop layout
            TaskNavigation(
                navController = navController,
                modifier = modifier
            )
        }
        else -> {
            // Fallback
            TaskNavigation(
                navController = navController,
                modifier = modifier
            )
        }
    }
}

