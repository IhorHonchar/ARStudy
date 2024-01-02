package ua.com.honchar.arstudy.presentation.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import ua.com.honchar.arstudy.navigation.AppBottomBar
import ua.com.honchar.arstudy.navigation.NavigationGraph
import ua.com.honchar.arstudy.ui.theme.ARStudyTheme

@Composable
fun Main() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { AppBottomBar(navController = navController) }
    ) { innerPadding ->
        NavigationGraph(navController, Modifier.padding(innerPadding))
    }
}

@Preview
@Composable
private fun Preview() {
    ARStudyTheme {
        Main()
    }
}