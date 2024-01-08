package ua.com.honchar.arstudy.presentation.screens.main.modules

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import ua.com.honchar.arstudy.R
import ua.com.honchar.arstudy.domain.repository.model.Module
import ua.com.honchar.arstudy.navigation.Screen
import ua.com.honchar.arstudy.presentation.screens.BaseScreen
import ua.com.honchar.arstudy.presentation.screens.main.categories.CardsPager
import ua.com.honchar.arstudy.presentation.screens.main.categories.ItemCard
import ua.com.honchar.arstudy.presentation.screens.main.modules.widget.CustomPopup
import ua.com.honchar.arstudy.presentation.screens.main.modules.widget.PopupState
import ua.com.honchar.arstudy.preview.TwoThemePreview
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
                val routeWithData = Screen.Lessons.updateWithParam(it, Screen.MODULE_ID)
                navController.navigate(routeWithData)
            }
        )
    }
}

@Composable
fun ModulesScreenDetails(
    modules: List<Module>,
    moduleClick: (Int) -> Unit
) {
    CardsPager(list = modules) { _, module, cardModifier, imageModifier ->
        Column {
            ItemCard(
                text = module.name,
                modifier = cardModifier,
                imageModifier = imageModifier,
                optionalCardContent = {
                    IconWithCustomPopup(
                        text = module.info, modifier = Modifier.align(Alignment.CenterEnd)
                    )
                },
                image = R.drawable.ic_unit,
                categoryClick = {
                    moduleClick(module.id)
                }
            )
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
            },
        ) {
            Box(
                modifier = Modifier
                    .padding(2.dp)
                    .shadow(elevation = 2.dp, shape = RoundedCornerShape(8.dp))
            ) {
                Surface {
                    Text(
                        text = text,
                        modifier = Modifier.padding(8.dp),
                        style = Typography.bodyLarge,
                    )
                }
            }
        }
        Icon(
            imageVector = Icons.Outlined.Info,
            contentDescription = null,
            modifier = Modifier
                .padding(end = 16.dp)
                .size(40.dp)
                .clickable {
                    popupState.isVisible = !popupState.isVisible
                }
        )
    }
}

@TwoThemePreview
@Composable
private fun ModulesPreview() {
    ARStudyTheme {
        ModulesScreenDetails(
            modules = List(4) { Module(it, "Module $it", "module module $it") },
            moduleClick = {}
        )
    }
}
