package ua.com.honchar.arstudy.presentation.screens.start

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import ua.com.honchar.arstudy.navigation.StartNavigationGraph

@Composable
fun Start() {
    val navController = rememberNavController()

    Scaffold {
        StartNavigationGraph(navController = navController, modifier = Modifier.padding(it))
    }
}