package ua.com.honchar.arstudy.presentation.screens.modules

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import ua.com.honchar.arstudy.domain.repository.model.Module
import ua.com.honchar.arstudy.navigation.lerp
import ua.com.honchar.arstudy.presentation.screens.BaseScreen
import ua.com.honchar.arstudy.presentation.screens.categories.calculateCurrentOffsetForPage
import ua.com.honchar.arstudy.presentation.screens.modules.widget.CustomPopup
import ua.com.honchar.arstudy.presentation.screens.modules.widget.PopupState
import ua.com.honchar.arstudy.ui.theme.ARStudyTheme
import ua.com.honchar.arstudy.ui.theme.Typography

@Composable
fun ModulesScreen(
    viewModel: ModulesViewModel = hiltViewModel(),
    navController: NavHostController,
    categoryId: Int
) {

    LaunchedEffect(Unit) {
        viewModel.getModules(categoryId)
    }

    BaseScreen(
        error = viewModel.state.error,
        isLoading = viewModel.state.isLoading
    ) {
        ModulesScreenDetails(
            modules = viewModel.state.data.orEmpty(),
            moduleClick = {
                // todo implement
            }
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ModulesScreenDetails(
    modules: List<Module>,
    moduleClick: (Int) -> Unit
) {
    Surface {
        Box(modifier = Modifier.fillMaxSize()) {
            val pagerState = rememberPagerState { modules.size }
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(end = 50.dp, start = 30.dp)
            ) { page ->
                val pageOffset = pagerState.calculateCurrentOffsetForPage(page)
                val module = modules[page]
                ModuleItem(
                    module = module,
                    modifier = Modifier
                        .height(
                            lerp(
                                start = 280.dp.value,
                                stop = 330.dp.value,
                                fraction = 1f - pageOffset.coerceIn(0f, 1f)
                            ).dp
                        ),
                    onCardClick = {
                        // todo implement
                    },
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModuleItem(
    module: Module,
    modifier: Modifier,
    onCardClick: () -> Unit,
) {
    Box {
        Card(
            onClick = onCardClick,
            modifier = modifier
                .padding(top = 50.dp)
                .width(250.dp)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                IconWithCustomPopup(
                    text = module.info, modifier = Modifier.align(Alignment.TopEnd)
                )
                Text(
                    text = module.name,
                    style = Typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(start = 16.dp, bottom = 16.dp),
                )
            }
        }
    }
}

@Composable
private fun IconWithCustomPopup(
    modifier: Modifier = Modifier,
    popupState: PopupState = remember { PopupState(false) },
    text: String
) {
    Box(modifier = modifier) {
        CustomPopup(
            popupState = popupState,
            onDismissRequest = {
                popupState.isVisible = false
            }
        ) {
            Text(
                text = text,
                modifier = Modifier.background(Color.Yellow)
            )
        }
        Icon(
            imageVector = Icons.Outlined.Info,
            contentDescription = null,
            modifier = Modifier
                .padding(top = 16.dp, end = 16.dp)
                .size(40.dp)
                .clickable {
                    popupState.isVisible = !popupState.isVisible
                }
        )
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ModulesPreview() {
    ARStudyTheme {
        ModulesScreenDetails(
            modules = List(4) { Module(it, "Module $it", "module module $it") },
            moduleClick = {}
        )
    }
}
