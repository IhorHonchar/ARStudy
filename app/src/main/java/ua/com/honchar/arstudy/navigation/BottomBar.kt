package ua.com.honchar.arstudy.navigation

import android.content.res.Configuration
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationDefaults
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ua.com.honchar.arstudy.ui.theme.ARStudyTheme

@Composable
fun AppBottomBar(navController: NavHostController) {
    val screens = listOf(
        Screen.Home,
        Screen.Search,
        Screen.Profile
    )
    BottomNavigation(backgroundColor = MaterialTheme.colorScheme.surface,) {
        screens.forEach { screen ->
            AddItem(
                screen = screen,
                navController = navController
            )
        }
    }
}

@Composable
fun RowScope.AddItem(
    screen: Screen,
    navController: NavHostController
) {
    val backStackEntry = navController.currentBackStackEntryAsState()
    BottomNavigationItem(
        label = {
            Text(text = stringResource(id = screen.resourceId))
        },
        icon = {
            screen.icon?.let {
                Icon(
                    imageVector = it,
                    contentDescription = screen.route + " icon"
                )
            }
        },
        selectedContentColor = Color.Blue,
        unselectedContentColor = MaterialTheme.colorScheme.onSurface,
        selected = screen.route == backStackEntry.value?.destination?.route,
        onClick = {
            navController.navigate(screen.route) {
                popUpTo(navController.graph.findStartDestination().id)
                launchSingleTop = true
            }
        }
    )
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun BottomBarPreview() {
    ARStudyTheme {
        AppBottomBar(rememberNavController())
    }
}