package ua.com.honchar.arstudy.presentation.screens.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ua.com.honchar.arstudy.domain.repository.ArStudyRepository
import ua.com.honchar.arstudy.domain.repository.model.Model
import ua.com.honchar.arstudy.util.Resource
import javax.inject.Inject

@HiltViewModel
class ModelsViewModels @Inject constructor(
    private val repository: ArStudyRepository
) : ViewModel() {

    var state by mutableStateOf(ModelsState())
        private set

    fun getModels(categoryId: Int) {
        viewModelScope.launch {
            state = state.copy(
                isLoading = true,
                error = null
            )
            when (val resource = repository.getModelsByCategory(categoryId)) {
                is Resource.Success -> {
                    state = state.copy(
                        data = resource.data,
                        isLoading = false,
                        error = null
                    )
                }

                is Resource.Error -> {
                    state = state.copy(
                        data = null,
                        isLoading = false,
                        error = resource.message
                    )
                }
            }
        }
    }
}

data class ModelsState(
    val data: List<Model>? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)