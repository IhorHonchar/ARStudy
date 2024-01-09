package ua.com.honchar.arstudy.presentation.screens.main.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ua.com.honchar.arstudy.domain.repository.ArStudyRepository
import ua.com.honchar.arstudy.domain.model.Category
import ua.com.honchar.arstudy.domain.repository.Resource
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: ArStudyRepository
) : ViewModel() {

    var state by mutableStateOf(SearchState())
        private set

    init {
        getCategories()
    }

    private fun getCategories() {
        viewModelScope.launch {
            state = state.copy(
                isLoading = true,
                error = null
            )
            when(val resource = repository.getCategories()) {
                is Resource.Success -> {
                    state = state.copy(
                        data = resource.data?.sortedBy { it.order },
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

data class SearchState(
    val data: List<Category>? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)