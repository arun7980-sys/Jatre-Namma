package com.example.jatrenamma

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.jatrenamma.ui.JatreViewModel
import com.example.jatrenamma.ui.screens.HomeScreen
import com.example.jatrenamma.ui.screens.LostAndFoundScreen
import com.example.jatrenamma.ui.screens.MapScreen
import com.example.jatrenamma.ui.screens.StoriesScreen
import com.example.jatrenamma.ui.theme.JatreNammaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JatreNammaTheme {
                MainApp()
            }
        }
    }
}

@Composable
fun MainApp() {
    val navController = rememberNavController()
    val viewModel: JatreViewModel = viewModel()
    var selectedItem by remember { mutableIntStateOf(0) }
    val items = listOf("Schedule", "Lost&Found", "Map", "Stories")
    val icons = listOf(Icons.Default.DateRange, Icons.Default.Search, Icons.Default.LocationOn, Icons.Default.Info)

    Scaffold(
        bottomBar = {
            NavigationBar {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = { Icon(icons[index], contentDescription = item) },
                        label = { Text(item) },
                        selected = selectedItem == index,
                        onClick = {
                            selectedItem = index
                            navController.navigate(item) {
                                popUpTo(navController.graph.startDestinationId)
                                launchSingleTop = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "Schedule",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("Schedule") { HomeScreen(viewModel) }
            composable("Lost&Found") { LostAndFoundScreen(viewModel) }
            composable("Map") { MapScreen() }
            composable("Stories") { StoriesScreen() }
        }
    }
}
