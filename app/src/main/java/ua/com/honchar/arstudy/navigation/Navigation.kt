package ua.com.honchar.arstudy.navigation

import android.graphics.drawable.Icon
import android.os.Bundle
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.google.gson.Gson
import ua.com.honchar.arstudy.R
import ua.com.honchar.arstudy.presentation.Animal
import ua.com.honchar.arstudy.extensions.parcelable
import ua.com.honchar.arstudy.presentation.screens.Empty
import ua.com.honchar.arstudy.presentation.screens.categories.CategoriesScreen
import ua.com.honchar.arstudy.presentation.screens.search.ModelsScreen
import ua.com.honchar.arstudy.presentation.screens.search.SearchScreen

sealed class Screen(
    val route: String,
    val resourceId: Int,
    val icon: ImageVector?
) {
    object Home : Screen(route = "home", R.string.home_home, Icons.Outlined.Home)
    object Search : Screen(route = "search", R.string.home_search, Icons.Outlined.Search)
    object Profile : Screen(route = "profile", R.string.home_profile, Icons.Outlined.Person)
    object Models : Screen(route = "models", 0, null)


    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
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
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {
        composable(route = Screen.Home.route) {
            CategoriesScreen()
        }
        composable(
            route = Screen.Profile.route,
//            arguments = listOf(
//                navArgument("animal") {
//                    type = AssetParamType()
//                }
//            )
        ) { entry ->
            Empty()
        }
        composable(route = Screen.Search.route) {
            SearchScreen(navHostController = navController
//                openModels = {
//                navController.navigate(Screen.Models.route)
//                {
//                    popUpTo(navController.graph.findStartDestination().id)
//                    launchSingleTop = true
//                }
//            }
            )
        }
        composable(
            route = Screen.Models.route,
//            arguments = listOf(navArgument("category_id") {
//                type = NavType.IntType
//            })
        ) { entry ->
            ModelsScreen(categoryId = 3)//  entry.arguments?.getInt("category_id") ?: 0)
        }
    }
}