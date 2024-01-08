package ua.com.honchar.arstudy.presentation.screens.main.categories

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import ua.com.honchar.arstudy.R
import ua.com.honchar.arstudy.domain.repository.model.Category
import ua.com.honchar.arstudy.navigation.Screen
import ua.com.honchar.arstudy.navigation.lerp
import ua.com.honchar.arstudy.presentation.screens.BaseScreen
import ua.com.honchar.arstudy.presentation.screens.main.SettingsDialog
import ua.com.honchar.arstudy.preview.TwoThemePreview
import ua.com.honchar.arstudy.ui.theme.ARStudyTheme
import ua.com.honchar.arstudy.ui.theme.Typography
import kotlin.math.absoluteValue

@Composable
fun CategoriesScreen(
    viewModel: CategoriesViewModel = hiltViewModel(),
    navController: NavHostController,
) {

    var showSettingsDialog by rememberSaveable { mutableStateOf(false) }

    if (showSettingsDialog) {
        SettingsDialog(
            onDismiss = { showSettingsDialog = false },
        )
    }

    BaseScreen(
        error = viewModel.state.error,
        isLoading = viewModel.state.isLoading
    ) {
        CategoriesContent(
            categories = viewModel.state.data,
            categoryClick = {
                val routeWithData = Screen.Modules.updateWithParam(it, Screen.CATEGORY_ID)
                navController.navigate(routeWithData)
            }
        )
    }
}

@Composable
fun CategoriesContent(
    categories: List<Category>?,
    categoryClick: (Int?) -> Unit
) {
    CardsPager(
        list = categories.orEmpty(),
        content = { _, item: Category, cardModifier: Modifier, imageModifier: Modifier ->
            ItemCard(
                text = item.name,
                imagePath = item.imagePath,
                modifier = cardModifier,
                imageModifier = imageModifier,
                categoryClick = {
                    categoryClick(item.id)
                }
            )
        }
    )
}

@OptIn(ExperimentalFoundationApi::class)
fun PagerState.calculateCurrentOffsetForPage(page: Int): Float {
    return ((currentPage - page) + currentPageOffsetFraction).absoluteValue
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun <T> CardsPager(
    list: List<T>,
    content: @Composable (pageOffset: Float, item: T, cardModifier: Modifier, imageModifier: Modifier) -> Unit
) {
    Surface {
        Box(modifier = Modifier.fillMaxSize()) {
            val pagerState = rememberPagerState { list.size }
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(end = 50.dp, start = 30.dp)
            ) { page ->
                val pageOffset = pagerState.calculateCurrentOffsetForPage(page)
                val item = list[page]
                val cardModifier = Modifier
                    .height(
                        lerp(
                            start = 280.dp.value,
                            stop = 330.dp.value,
                            fraction = 1f - pageOffset.coerceIn(0f, 1f)
                        ).dp
                    )
                val imageModifier = Modifier.padding(
                    top = lerp(
                        start = 50.dp.value,
                        stop = 0f,
                        fraction = 1f - pageOffset.coerceIn(0f, 1f)
                    ).dp
                )
                content(pageOffset, item, cardModifier, imageModifier)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemCard(
    text: String,
    modifier: Modifier = Modifier,
    imageModifier: Modifier = Modifier,
    imagePath: String? = null,
    image: Int? = null,
    optionalCardContent: @Composable BoxScope.() -> Unit = {},
    categoryClick: () -> Unit
) {
    Box {
        Card(
            onClick = categoryClick,
            modifier = modifier
                .padding(top = 50.dp)
                .width(250.dp)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                optionalCardContent.invoke(this)
                Text(
                    text = text,
                    style = Typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(start = 16.dp, bottom = 16.dp),
                )
            }
        }
        imagePath?.let {
            AsyncImage(
                model = it,
                contentDescription = null,
                placeholder = painterResource(id = R.drawable.ic_category),
                error = painterResource(id = R.drawable.ic_category),
                modifier = imageModifier
                    .size(150.dp)
                    .align(Alignment.TopCenter)
            )
        } ?: image?.let {
            Image(
                painter = painterResource(id = it),
                contentDescription = null,
                colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onSurface),
                modifier = imageModifier
                    .size(150.dp)
                    .align(Alignment.TopCenter)
            )
        }
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

@TwoThemePreview
@Composable
private fun CategoriesPreview() {
    ARStudyTheme {
        CategoriesContent(
            categories = previewCategories,
            categoryClick = {}
        )
    }
}