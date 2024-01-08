package ua.com.honchar.arstudy.presentation.screens.main.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import ua.com.honchar.arstudy.domain.repository.ArStudyRepository
import ua.com.honchar.arstudy.domain.repository.model.User
import ua.com.honchar.arstudy.util.Resource
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: ArStudyRepository,
) : ViewModel() {

    var state by mutableStateOf(ProfileState())
        private set

    var goToStart = MutableSharedFlow<Unit>()
        private set

    init {
        getUserData()
    }

    fun exitApp() {
        viewModelScope.launch {
            repository.exitApp()
            goToStart.emit(Unit)
        }
    }

    private fun getUserData() {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            state = when(val res = repository.getUserDb()) {
                is Resource.Success -> {
                    ProfileState(res.data)
                }
                is Resource.Error -> {
                    ProfileState(error = res.message)
                }
            }
        }
    }
}

data class ProfileState(
    val data: User? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)