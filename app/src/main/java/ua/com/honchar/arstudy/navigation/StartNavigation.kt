package ua.com.honchar.arstudy.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ua.com.honchar.arstudy.presentation.screens.main.Main
import ua.com.honchar.arstudy.presentation.screens.start.login.LoginScreen
import ua.com.honchar.arstudy.presentation.screens.start.splash.SplashScreen

sealed class LogInScreen(val route: String) {
    object Main : LogInScreen(route = "main")
    object Login : LogInScreen(route = "login")
    object Splash : LogInScreen(route = "splash")
}

@Composable
fun StartNavigationGraph(
    navController: NavHostController,
    modifier: Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = LogInScreen.Splash.route,
        modifier = modifier
    ) {
        composable(route = LogInScreen.Main.route) {
            Main()
        }
        composable(route = LogInScreen.Login.route) {
            LoginScreen(navigateToMain = {
                navController.navigate(LogInScreen.Main.route) {
                    popUpTo(LogInScreen.Login.route) { inclusive = true }
                }
            })
        }
        composable(route = LogInScreen.Splash.route) {
            SplashScreen(
                navigateToMain = {
                    navController.navigate(LogInScreen.Main.route) {
                        popUpTo(LogInScreen.Splash.route) { inclusive = true }
                    }
                },
                navigateToLogIn = {
                    navController.navigate(LogInScreen.Login.route) {
                        popUpTo(LogInScreen.Splash.route) { inclusive = true }
                    }
                }
            )
        }
    }
}