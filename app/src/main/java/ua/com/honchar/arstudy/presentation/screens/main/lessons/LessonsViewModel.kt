package ua.com.honchar.arstudy.presentation.screens.main.lessons

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ua.com.honchar.arstudy.domain.repository.ArStudyRepository
import ua.com.honchar.arstudy.domain.repository.model.Lesson
import ua.com.honchar.arstudy.domain.repository.model.Model
import ua.com.honchar.arstudy.extensions.checkFileIsDownloaded
import ua.com.honchar.arstudy.extensions.downloadFile
import ua.com.honchar.arstudy.util.Resource
import java.io.FileNotFoundException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class LessonsViewModel @Inject constructor(
    private val repository: ArStudyRepository
) : ViewModel() {

    var state by mutableStateOf(LessonsState())
        private set

    var openArActivity = MutableSharedFlow<Lesson>()
        private set

    fun getLessons(moduleId: Int?) {
        viewModelScope.launch {
            state = state.copy(
                isLoading = true,
                error = null
            )
            val langId = repository.getSavedLangId()
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

    fun checkFileExist(
        lesson: Lesson,
        context: Context
    ) {
        viewModelScope.launch {
            launch(Dispatchers.IO) {
                try {
                    val firstModel = lesson.firstLessonPartModel
                    if (firstModel != null) {
                        context.checkFileIsDownloaded(firstModel.fileName)
                        withContext(Dispatchers.Main) {
                            state = state.copy(isLoading = false)
                            openArActivity.emit(lesson)
                        }
                    }
                } catch (e: FileNotFoundException) {
                    downloadFile(lesson, context)
                } catch (e: Exception) {
                    state = state.copy(
                        isLoading = false,
                        error = e.localizedMessage
                    )
                }
            }
        }
    }

    private suspend fun downloadFile(
        lesson: Lesson,
        context: Context,
    ) {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            launch(Dispatchers.IO) {
                try {
                    val model = lesson.firstLessonPartModel
                    if (model != null) {
                        context.downloadFile(model.fileName, model.modelPath)
                        state = state.copy(isLoading = false)
                        openArActivity.emit(lesson)
                    } else {
                        state = state.copy(isLoading = false)
                    }
                } catch (e: IOException) {
                    state = state.copy(
                        isLoading = false,
                        error = e.localizedMessage
                    )
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