package ua.com.honchar.arstudy.presentation

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ua.com.honchar.arstudy.domain.repository.model.LessonPart
import ua.com.honchar.arstudy.domain.repository.model.Model
import ua.com.honchar.arstudy.extensions.checkFileIsDownloaded
import ua.com.honchar.arstudy.extensions.downloadFile
import java.io.FileNotFoundException
import java.io.IOException
import javax.inject.Inject


@HiltViewModel
class ArViewModel @Inject constructor() : ViewModel() {

    var fileUri by mutableStateOf<String?>(null)
        private set

    var loader by mutableStateOf(false)
        private set

    fun getDownloadedModelUri(lessonPart: LessonPart, context: Context) {
        viewModelScope.launch {
            lessonPart.model?.let { model ->
                launch(Dispatchers.IO) {
                    try {
                        val uri = context.checkFileIsDownloaded(model.fileName)
                        emitUri(uri)
                    } catch (e: FileNotFoundException) {
                        downloadFile(model, context)
                    } catch (e: Exception) {
                        emitUri(null)
                    }
                }
            }
        }
    }

    private suspend fun downloadFile(
        model: Model,
        context: Context,
    ) {
        viewModelScope.launch {
            loader = true
            launch(Dispatchers.IO) {
                try {
                    val uri = context.downloadFile(model.fileName, model.modelPath)
                    loader = false
                    emitUri(uri)
                } catch (e: IOException) {
                    loader = false
                    emitUri(null)
                }
            }
        }
    }

    private suspend fun emitUri(uri: String?) {
        withContext(Dispatchers.Main) {
            fileUri = uri
        }
    }
}