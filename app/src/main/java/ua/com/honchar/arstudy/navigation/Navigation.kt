package ua.com.honchar.arstudy.navigation

import android.os.Bundle
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.gson.Gson
import ua.com.honchar.arstudy.R
import ua.com.honchar.arstudy.presentation.Animal
import ua.com.honchar.arstudy.extensions.parcelable
import ua.com.honchar.arstudy.presentation.screens.AnimalsScreen
import ua.com.honchar.arstudy.presentation.screens.Empty
import ua.com.honchar.arstudy.presentation.screens.categories.CategoriesScreen

sealed class Screen(
    val route: String,
    val resourceId: Int,
    val icon: ImageVector
) {
    object AnimalsScreen : Screen(route = "animals_screen", R.string.home_feed, Icons.Outlined.Home)
    object Empty : Screen(route = "empty", R.string.home_search, Icons.Outlined.Info)


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
        startDestination = Screen.AnimalsScreen.route,
        modifier = modifier
    ) {
        composable(route = Screen.AnimalsScreen.route) {
            CategoriesScreen()
        }
        composable(
            route = Screen.Empty.route,
//            arguments = listOf(
//                navArgument("animal") {
//                    type = AssetParamType()
//                }
//            )
        ) { entry ->
            Empty()
        }
    }
}