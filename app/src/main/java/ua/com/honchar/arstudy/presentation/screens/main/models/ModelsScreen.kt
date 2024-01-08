package ua.com.honchar.arstudy.presentation.screens.main.models

import android.content.Intent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ua.com.honchar.arstudy.domain.repository.model.Model
import ua.com.honchar.arstudy.presentation.ArActivity
import ua.com.honchar.arstudy.presentation.screens.BaseScreen
import ua.com.honchar.arstudy.presentation.screens.main.categories.CardsPager
import ua.com.honchar.arstudy.presentation.screens.main.categories.ItemCard
import ua.com.honchar.arstudy.ui.theme.ARStudyTheme
import ua.com.honchar.arstudy.ui.theme.Typography

@Composable
fun ModelsScreen(
    viewModel: ModelsViewModels = hiltViewModel(),
    categoryId: Int?
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.getModels(categoryId)
        viewModel.modelUri.collect {
            val intent = Intent(context, ArActivity::class.java).apply {
                putExtra(ArActivity.JUST_PATH, it)
            }
            context.startActivity(intent)
        }
    }

    BaseScreen(
        error = viewModel.state.error,
        isLoading = viewModel.state.isLoading
    ) {
        ModelsScreenDetails(
            models = viewModel.state.data.orEmpty(),
            downloadModel = {
                viewModel.getDownloadedModelUri(it, context)
            }
        )
    }
}

@Composable
fun ModelsScreenDetails(models: List<Model>, downloadModel: (Model) -> Unit) {
    CardsPager(
        list = models,
        content = { _, item, cardModifier, _ ->
            ItemCard(
                text = item.name,
                modifier = cardModifier,
                optionalCardContent = {
                    item.categoryName?.let {
                        Text(
                            text = it,
                            style = Typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(end = 16.dp, top = 16.dp),
                        )
                    }
                },
                categoryClick = {
                    downloadModel.invoke(item)
                }
            )
        },
    )
}

@Preview
@Composable
private fun ModelsPreview() {
    ARStudyTheme {
        ModelsScreenDetails(List(10) { Model(it, "model $it", "", 0, "category", "") }, {})
    }
}