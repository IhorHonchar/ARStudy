package ua.com.honchar.arstudy.presentation.screens.lessons

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ua.com.honchar.arstudy.domain.repository.ArStudyRepository
import ua.com.honchar.arstudy.domain.repository.model.Lesson
import ua.com.honchar.arstudy.util.Resource
import javax.inject.Inject

@HiltViewModel
class LessonsViewModel @Inject constructor(
    private val repository: ArStudyRepository
) : ViewModel() {

    var state by mutableStateOf(LessonsState())
        private set

    fun getLessons(moduleId: Int?) {
        viewModelScope.launch {
            state = state.copy(
                isLoading = true,
                error = null
            )
            val langId = 1 // todo temporary solution
            moduleId?.let {
                when (val resource = repository.getModuleLessons(moduleId, langId)) {
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
}

data class LessonsState(
    val data: List<Lesson>? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)