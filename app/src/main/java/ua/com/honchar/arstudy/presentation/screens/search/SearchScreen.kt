package ua.com.honchar.arstudy.presentation.screens.search

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import ua.com.honchar.arstudy.R
import ua.com.honchar.arstudy.domain.repository.model.Category
import ua.com.honchar.arstudy.navigation.Screen
import ua.com.honchar.arstudy.presentation.screens.BaseScreen
import ua.com.honchar.arstudy.presentation.screens.categories.CategoriesContent
import ua.com.honchar.arstudy.presentation.screens.categories.previewCategories
import ua.com.honchar.arstudy.ui.theme.ARStudyTheme

@Composable
fun SearchScreen(
    viewModel: SearchViewModel = hiltViewModel(),
    navHostController: NavHostController,
) {
    val allCategories = Category(null, stringResource(id = R.string.all_models), "", 999)
    val updatedList = listOf(allCategories) + viewModel.state.data.orEmpty()
    BaseScreen(
        error = viewModel.state.error,
        isLoading = viewModel.state.isLoading
    ) {
        CategoriesContent(
            categories = updatedList,
            categoryClick = {
                val routeWithData = Screen.ModelsByCategory.updateWithParam(it, Screen.CATEGORY_ID)
                navHostController.navigate(routeWithData)
            }
        )
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SearchScreenPreview() {
    ARStudyTheme {
        CategoriesContent(categories = previewCategories) {}
    }
}
