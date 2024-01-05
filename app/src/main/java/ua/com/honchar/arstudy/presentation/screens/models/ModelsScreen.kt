package ua.com.honchar.arstudy.presentation.screens.models

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ua.com.honchar.arstudy.domain.repository.model.Model
import ua.com.honchar.arstudy.navigation.lerp
import ua.com.honchar.arstudy.presentation.screens.BaseScreen
import ua.com.honchar.arstudy.presentation.screens.categories.CategoryCard
import ua.com.honchar.arstudy.presentation.screens.categories.calculateCurrentOffsetForPage
import ua.com.honchar.arstudy.ui.theme.ARStudyTheme
import ua.com.honchar.arstudy.ui.theme.Typography

@Composable
fun ModelsScreen(
    viewModel: ModelsViewModels = hiltViewModel(),
    categoryId: Int?
) {

    LaunchedEffect(Unit) {
        viewModel.getModels(categoryId)
    }

    BaseScreen(
        error = viewModel.state.error,
        isLoading = viewModel.state.isLoading
    ) {
        ModelsScreenDetails(viewModel.state.data.orEmpty())
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ModelsScreenDetails(models: List<Model>) {
    Surface {
        Box(modifier = Modifier.fillMaxSize()) {
            val pagerState = rememberPagerState { models.size }
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(end = 50.dp, start = 30.dp)
            ) { page ->
                val pageOffset = pagerState.calculateCurrentOffsetForPage(page)
                val model = models[page]
                ModelItem(
                    model = model,
                    modifier = Modifier
                        .height(
                            lerp(
                                start = 280.dp.value,
                                stop = 330.dp.value,
                                fraction = 1f - pageOffset.coerceIn(0f, 1f)
                            ).dp
                        ),
                    onClick = {
                        // todo implement
                    }
                )
            }
        }
    }
//    Surface {
//        Box(modifier = Modifier.fillMaxSize()) {
//            LazyColumn(contentPadding = PaddingValues(all = 16.dp)) {
//                items(models) {
//                    ModelItem(
//                        model = it,
//                        onClick = {
//                            // todo implement
//                        }
//                    )
//                }
//            }
//        }
//    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModelItem(model: Model, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = modifier
            .padding(top = 50.dp)
            .width(250.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            model.categoryName?.let {
                Text(
                    text = it,
                    style = Typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(end = 16.dp, top = 16.dp),
                )
            }
            Text(
                text = model.name,
                style = Typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 16.dp, bottom = 16.dp),
            )
        }
    }
}

@Preview
@Composable
private fun ModelsPreview() {
    ARStudyTheme {
        ModelsScreenDetails(List(10) { Model(it, "model $it", "", 0, "category") })
    }
}