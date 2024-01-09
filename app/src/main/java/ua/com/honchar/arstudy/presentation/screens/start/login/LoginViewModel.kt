package ua.com.honchar.arstudy.presentation.screens.start.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import ua.com.honchar.arstudy.domain.repository.ArStudyRepository
import ua.com.honchar.arstudy.domain.repository.Resource
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: ArStudyRepository
) : ViewModel() {

    var state by mutableStateOf(LoginState())
        private set

    var goToMain = MutableSharedFlow<Unit>()
        private set

    fun login(login: String, pass: String) {
        viewModelScope.launch {
            state = LoginState(isLoading = true)
            when (val res = repository.login(login, pass)) {
                is Resource.Success -> {
                    state = LoginState()
                    goToMain.emit(Unit)
                }

                is Resource.Error -> {
                    state = LoginState(error = res.message)
                }
            }
        }
    }

    fun signUp(login: String, pass: String, name: String?) {
        viewModelScope.launch {
            state = LoginState(isLoading = true)
            when (val res = repository.register(login, pass, name)) {
                is Resource.Success -> {
                    state = LoginState()
                    goToMain.emit(Unit)
                }

                is Resource.Error -> {
                    state = LoginState(error = res.message)
                }
            }
        }
    }

    fun validateEmail(email: String) {
        viewModelScope.launch {
            val regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$".toRegex()
            val valid = regex.matches(email)
            state = LoginState(emailError = !valid)
        }
    }
}

data class LoginState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val emailError: Boolean = false
)