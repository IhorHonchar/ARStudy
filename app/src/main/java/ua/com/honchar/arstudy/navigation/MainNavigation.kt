package ua.com.honchar.arstudy.navigation

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import ua.com.honchar.arstudy.R
import ua.com.honchar.arstudy.navigation.Screen.Companion.CATEGORY_ID
import ua.com.honchar.arstudy.navigation.Screen.Companion.MODULE_ID
import ua.com.honchar.arstudy.presentation.screens.main.categories.CategoriesScreen
import ua.com.honchar.arstudy.presentation.screens.main.lessons.LessonsScreen
import ua.com.honchar.arstudy.presentation.screens.main.models.ModelsScreen
import ua.com.honchar.arstudy.presentation.screens.main.modules.ModulesScreen
import ua.com.honchar.arstudy.presentation.screens.main.profile.ProfileScreen
import ua.com.honchar.arstudy.presentation.screens.main.search.SearchScreen
import ua.com.honchar.arstudy.presentation.screens.start.Start
import ua.com.honchar.arstudy.presentation.screens.start.login.LoginScreen

sealed class Screen(
    val route: String,
    val resourceId: Int,
    val icon: ImageVector?
) {

    // Bottom nav bar
    object Home : Screen(route = "home", R.string.home_home, Icons.Outlined.Home)
    object Search : Screen(route = "search", R.string.home_search, Icons.Outlined.Search)
    object Profile : Screen(route = "profile", R.string.home_profile, Icons.Outlined.Person)


    object ModelsByCategory :
        Screen(route = "modelsByCategory?$CATEGORY_ID={$CATEGORY_ID}", 0, null)

    object Modules : Screen(route = "modules?$CATEGORY_ID={$CATEGORY_ID}", 0, null)
    object Lessons : Screen(route = "lessons?$MODULE_ID={$MODULE_ID}", 0, null)

    fun updateWithParam(value: Any?, paramName: String): String = route.replace(
        "{$paramName}",
        value?.toString().orEmpty()
    )

    companion object {
        const val CATEGORY_ID = "categoryId"
        const val MODULE_ID = "module_id"
    }
}

@Composable
fun MainNavigationGraph(
    navController: NavHostController,
    modifier: Modifier,
    updateTopBar: (TopBarContent) -> Unit,
) {
    val context = LocalContext.current

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {
        composable(route = Screen.Home.route) {
            updateTopBar(
                TopBarContent(
                    title = "ArStudy",
                    actionClick = true
                )
            )
            CategoriesScreen(navController = navController)
        }
        composable(route = Screen.Profile.route) {
            updateTopBar(TopBarContent(title = stringResource(id = R.string.home_home)))
            ProfileScreen(goToStart = {
                restartApp(context)
            })
        }
        composable(route = Screen.Search.route) {
            updateTopBar(TopBarContent(title = stringResource(id = R.string.home_search)))
            SearchScreen(navHostController = navController)
        }
        composable(
            route = Screen.ModelsByCategory.route,
            arguments = listOf(navArgument(CATEGORY_ID) {
                type = NavType.StringType
                nullable = true
            })
        ) { entry ->
            updateTopBar(
                TopBarContent(
                    title = stringResource(id = R.string.by_category),
                    mainFlow = false,
                    navigationClick = {
                        navController.popBackStack()
                    }
                )
            )
            ModelsScreen(categoryId = entry.arguments?.getString(CATEGORY_ID)?.toIntOrNull())
        }
        composable(route = Screen.Modules.route) { entry ->
            updateTopBar(
                TopBarContent(
                    title = stringResource(id = R.string.unit_screen),
                    mainFlow = false,
                    navigationClick = {
                        navController.popBackStack()
                    }
                )
            )
            ModulesScreen(
                navController = navController,
                categoryId = entry.arguments?.getString(CATEGORY_ID)?.toIntOrNull() ?: 0
            )
        }
        composable(route = Screen.Lessons.route) { entry ->
            updateTopBar(
                TopBarContent(
                    title = stringResource(id = R.string.lessons_screen),
                    mainFlow = false,
                    navigationClick = {
                        navController.popBackStack()
                    }
                )
            )
            LessonsScreen(moduleId = entry.arguments?.getString(MODULE_ID)?.toIntOrNull())
        }
    }
}

private fun restartApp(context: Context) {
    val packageManager: PackageManager = context.packageManager
    val intent: Intent = packageManager.getLaunchIntentForPackage(context.packageName)!!
    val componentName: ComponentName = intent.component!!
    val restartIntent: Intent = Intent.makeRestartActivityTask(componentName)
    context.startActivity(restartIntent)
    Runtime.getRuntime().exit(0)
}

data class TopBarContent(
    val title: String = "ArStudy",
    val mainFlow: Boolean = true,
    val navigationClick: (() -> Unit)? = null,
    val actionClick: Boolean = false
)