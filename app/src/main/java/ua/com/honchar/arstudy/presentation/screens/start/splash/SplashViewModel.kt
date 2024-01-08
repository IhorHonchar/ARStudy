package ua.com.honchar.arstudy.presentation.screens.start.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import ua.com.honchar.arstudy.domain.repository.ArStudyRepository
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val repository: ArStudyRepository,
): ViewModel() {

    val openMain = MutableSharedFlow<Unit>()
    val openLogin = MutableSharedFlow<Unit>()

    fun checkUserData() {
        viewModelScope.launch {
            launch(Dispatchers.IO) {
                downloadLanguages()
                val startTime = System.currentTimeMillis()
                val userDataRes = repository.getUserDb()

                val timeSpent = System.currentTimeMillis() - startTime
                if (timeSpent < TWO_SEC) {
                    delay(TWO_SEC - timeSpent)
                }

                if (userDataRes.data != null) {
                    openMain.emit(Unit)
                } else {
                    openLogin.emit(Unit)
                }
            }
        }
    }

    private suspend fun downloadLanguages() {
        coroutineScope {
            launch(Dispatchers.IO + Job()) {
                repository.getLanguages()
            }
        }
    }

    companion object {
        private const val TWO_SEC = 2000
    }
}