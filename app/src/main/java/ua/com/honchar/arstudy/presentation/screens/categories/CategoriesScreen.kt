package ua.com.honchar.arstudy.presentation.screens.categories

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Text
import androidx.compose.material3.Card
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import ua.com.honchar.arstudy.R
import ua.com.honchar.arstudy.domain.repository.model.Category
import ua.com.honchar.arstudy.navigation.lerp
import ua.com.honchar.arstudy.presentation.screens.BaseScreen
import ua.com.honchar.arstudy.ui.theme.ARStudyTheme
import ua.com.honchar.arstudy.ui.theme.Typography
import kotlin.math.absoluteValue

@Composable
fun CategoriesScreen(
    viewModel: CategoriesViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.getCategories()
    }

    BaseScreen(
        error = viewModel.state.error,
        isLoading = viewModel.state.isLoading
    ) {
        CategoriesContent(
            categories = viewModel.state.data,
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CategoriesContent(
    categories: List<Category>?,
    modifier: Modifier = Modifier
) {
    Surface {
        Box(modifier = modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "ArStudy",
                    style = Typography.headlineLarge,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = Icons.Filled.Settings,
                    contentDescription = "settings",
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .size(32.dp)
                )
            }
            val pagerState = rememberPagerState {
                categories?.size ?: 3
            }
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(end = 50.dp, start = 30.dp)
            ) { page ->
                val pageOffset = pagerState.calculateCurrentOffsetForPage(page)
                categories?.get(page)?.let {
                    CategoryCard(
                        category = it,
                        modifier = Modifier
                            .height(
                                lerp(
                                    start = 280.dp.value,
                                    stop = 330.dp.value,
                                    fraction = 1f - pageOffset.coerceIn(0f, 1f)
                                ).dp
                            ),
                        imageModifier = Modifier.padding(
                            top = lerp(
                                start = 50.dp.value,
                                stop = 0f,
                                fraction = 1f - pageOffset.coerceIn(0f, 1f)
                            ).dp
                        ),
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
fun PagerState.calculateCurrentOffsetForPage(page: Int): Float {
    return ((currentPage - page) + currentPageOffsetFraction).absoluteValue
}

@Composable
fun CategoryCard(
    category: Category,
    modifier: Modifier = Modifier,
    imageModifier: Modifier = Modifier,
) {
    Box {
        Card(
            modifier = modifier
                .padding(top = 50.dp)
                .width(250.dp)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Text(
                    text = category.name,
                    style = Typography.headlineMedium,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(start = 16.dp, bottom = 16.dp),
                )
            }
        }
        AsyncImage(
            model = category.imagePath,
            contentDescription = null,
            placeholder = painterResource(id = R.drawable.ic_category),
            modifier = imageModifier
                .size(150.dp)
                .align(Alignment.TopCenter)
        )
    }
}

val previewCategories = listOf(
    Category(
        id = 1,
        name = "Astronomy",
        imagePath = "https://www.asc-csa.gc.ca/images/astronomie/fiches-information/planetes.jpg",
        order = 1
    ),
    Category(
        id = 1,
        name = "Astronomy",
        imagePath = "https://www.asc-csa.gc.ca/images/astronomie/fiches-information/planetes.jpg",
        order = 1
    ),
    Category(
        id = 1,
        name = "Astronomy",
        imagePath = "https://www.asc-csa.gc.ca/images/astronomie/fiches-information/planetes.jpg",
        order = 1
    )
)

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun CategoriesPreview() {
    ARStudyTheme {
        CategoriesContent(
            categories = previewCategories,
        )
    }
}