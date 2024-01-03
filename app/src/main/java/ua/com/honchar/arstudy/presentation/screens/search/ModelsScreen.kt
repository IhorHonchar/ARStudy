package ua.com.honchar.arstudy.presentation.screens.search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ua.com.honchar.arstudy.domain.repository.model.Model
import ua.com.honchar.arstudy.presentation.screens.BaseScreen
import ua.com.honchar.arstudy.ui.theme.Typography

@Composable
fun ModelsScreen(
    viewModel: ModelsViewModels = hiltViewModel(),
    categoryId: Int
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

@Composable
fun ModelsScreenDetails(models: List<Model>) {
    Surface {
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(contentPadding = PaddingValues(all = 16.dp)) {
                items(models) {
                    ModelItem(
                        model = it,
                        onClick = {
                            // todo implement
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModelItem(model: Model, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(8.dp),
        onClick = onClick
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = model.name,
                style = Typography.titleMedium,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp)
                    .align(Alignment.CenterVertically)
            )
        }
    }
}