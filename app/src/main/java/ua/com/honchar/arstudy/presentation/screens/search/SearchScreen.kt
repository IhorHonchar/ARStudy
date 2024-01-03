package ua.com.honchar.arstudy.presentation.screens.search

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import ua.com.honchar.arstudy.R
import ua.com.honchar.arstudy.domain.repository.model.Category
import ua.com.honchar.arstudy.navigation.Screen
import ua.com.honchar.arstudy.presentation.screens.BaseScreen
import ua.com.honchar.arstudy.presentation.screens.categories.previewCategories
import ua.com.honchar.arstudy.ui.theme.ARStudyTheme
import ua.com.honchar.arstudy.ui.theme.Typography

@Composable
fun SearchScreen(
    viewModel: SearchViewModel = hiltViewModel(),
    navHostController: NavHostController,
) {
    LaunchedEffect(Unit) {
        viewModel.getCategories()
    }

    BaseScreen(
        error = viewModel.state.error,
        isLoading = viewModel.state.isLoading
    ) {
        SearchScreenDetails(
            categories = viewModel.state.data.orEmpty(),
            onCategoryClick = {
                navHostController.navigate(Screen.Models.route)
            }
        )
    }
}

@Composable
fun SearchScreenDetails(
    categories: List<Category>,
    onCategoryClick: (Int) -> Unit
) {
    Surface {
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(contentPadding = PaddingValues(all = 16.dp)) {
                items(categories) {
                    CategoryItem(
                        category = it,
                        onClick = { onCategoryClick(it.id) }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryItem(category: Category, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(8.dp),
        onClick = onClick
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = category.name,
                style = Typography.titleMedium,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp)
                    .align(Alignment.CenterVertically)
            )
            AsyncImage(
                model = category.imagePath,
                contentDescription = null,
                placeholder = painterResource(id = R.drawable.ic_category),
                modifier = Modifier
                    .padding(8.dp)
                    .size(32.dp)
            )
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SearchScreenPreview() {
    ARStudyTheme {
        SearchScreenDetails(categories = previewCategories) {}
    }
}
