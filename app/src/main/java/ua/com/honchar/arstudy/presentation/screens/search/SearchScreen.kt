package ua.com.honchar.arstudy.presentation.screens.search

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import ua.com.honchar.arstudy.R
import ua.com.honchar.arstudy.domain.repository.model.Category
import ua.com.honchar.arstudy.navigation.Screen
import ua.com.honchar.arstudy.navigation.Screen.Companion.CATEGORY_ID
import ua.com.honchar.arstudy.navigation.lerp
import ua.com.honchar.arstudy.presentation.screens.BaseScreen
import ua.com.honchar.arstudy.presentation.screens.categories.CategoriesContent
import ua.com.honchar.arstudy.presentation.screens.categories.CategoryCard
import ua.com.honchar.arstudy.presentation.screens.categories.calculateCurrentOffsetForPage
import ua.com.honchar.arstudy.presentation.screens.categories.previewCategories
import ua.com.honchar.arstudy.ui.theme.ARStudyTheme
import ua.com.honchar.arstudy.ui.theme.Typography

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
                val routeWithData = Screen.ModelsByCategory.updateRouteWithParam(it)
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
