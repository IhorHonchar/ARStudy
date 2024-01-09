package ua.com.honchar.arstudy.presentation.screens.main.models

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
import ua.com.honchar.arstudy.domain.model.Model
import ua.com.honchar.arstudy.extensions.checkFileIsDownloaded
import ua.com.honchar.arstudy.extensions.downloadFile
import ua.com.honchar.arstudy.domain.repository.Resource
import java.io.FileNotFoundException
import javax.inject.Inject

@HiltViewModel
class ModelsViewModels @Inject constructor(
    private val repository: ArStudyRepository
) : ViewModel() {

    var state by mutableStateOf(ModelsState())
        private set

    var modelUri = MutableSharedFlow<String>()
        private set

    fun getModels(categoryId: Int?) {
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

    fun getDownloadedModelUri(model: Model, context: Context) {
        viewModelScope.launch {
            launch(Dispatchers.IO) {
                try {
                    val uri = context.checkFileIsDownloaded(model.fileName)
                    withContext(Dispatchers.Main) {
                        modelUri.emit(uri.orEmpty())
                    }
                } catch (e: FileNotFoundException) {
                    downloadFile(model, context)
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
        model: Model,
        context: Context,
    ) {
        state = state.copy(isLoading = true)
        try {
            val uri = context.downloadFile(model.fileName, model.modelPath)
            state = state.copy(isLoading = false)
            withContext(Dispatchers.Main) {
                modelUri.emit(uri)
            }
        } catch (e: Exception) {
            state = state.copy(
                isLoading = false,
                error = e.localizedMessage
            )
        }
    }
}

data class ModelsState(
    val data: List<Model>? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)