package ua.com.honchar.arstudy.navigation

import android.os.Bundle
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.google.gson.Gson
import ua.com.honchar.arstudy.R
import ua.com.honchar.arstudy.extensions.parcelable
import ua.com.honchar.arstudy.navigation.Screen.Companion.CATEGORY_ID
import ua.com.honchar.arstudy.presentation.Animal
import ua.com.honchar.arstudy.presentation.screens.Empty
import ua.com.honchar.arstudy.presentation.screens.categories.CategoriesScreen
import ua.com.honchar.arstudy.presentation.screens.models.ModelsScreen
import ua.com.honchar.arstudy.presentation.screens.modules.ModulesScreen
import ua.com.honchar.arstudy.presentation.screens.search.SearchScreen

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
        Screen(route = "modelsByCategory?$CATEGORY_ID={$CATEGORY_ID}", 0, null) {
        fun updateRouteWithParam(id: Int?): String = ModelsByCategory.route.replace(
            "{${CATEGORY_ID}}",
            id?.toString().orEmpty()
        )
    }

    object Modules : Screen(route = "modules?$CATEGORY_ID={$CATEGORY_ID}", 0, null) {
        fun updateRouteWithParam(id: Int?): String = Modules.route.replace(
            "{${CATEGORY_ID}}",
            id?.toString().orEmpty()
        )
    }

    companion object {
        const val CATEGORY_ID = "categoryId"
    }
}

class AssetParamType : NavType<Animal>(isNullableAllowed = false) {
    override fun get(bundle: Bundle, key: String): Animal? {
        return bundle.parcelable(key)
    }

    override fun parseValue(value: String): Animal {
        return Gson().fromJson(value, Animal::class.java)
    }

    override fun put(bundle: Bundle, key: String, value: Animal) {
        bundle.putParcelable(key, value)
    }
}

@Composable
fun NavigationGraph(
    navController: NavHostController,
    modifier: Modifier,
    updateTopBar: (TopBarContent) -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {
        composable(route = Screen.Home.route) {
            updateTopBar(
                TopBarContent(
                    title = "ArStudy",
                    actionClick = {
                        // todo implement
                    }
                )
            )
            CategoriesScreen(navController = navController)
        }
        composable(route = Screen.Profile.route) {
            updateTopBar(TopBarContent(title = stringResource(id = R.string.home_home)))
            Empty()
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
                    title = "By category",
                    mainFlow = false,
                    navigationClick = {
                        navController.popBackStack()
                    }
                )
            )
            ModelsScreen(categoryId = entry.arguments?.getString(CATEGORY_ID)?.toIntOrNull())
        }
        composable(
            route = Screen.Modules.route
        ) { entry ->
            updateTopBar(
                TopBarContent(
                    title = "Modules",
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
    }
}

data class TopBarContent(
    val title: String = "ArStudy",
    val mainFlow: Boolean = true,
    val navigationClick: (() -> Unit)? = null,
    val actionClick: (() -> Unit)? = null
)