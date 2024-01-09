package ua.com.honchar.arstudy.presentation

import android.content.Context
import android.speech.tts.TextToSpeech
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ua.com.honchar.arstudy.domain.model.LessonPart
import ua.com.honchar.arstudy.domain.model.Model
import ua.com.honchar.arstudy.domain.repository.ArStudyRepository
import ua.com.honchar.arstudy.extensions.checkFileIsDownloaded
import ua.com.honchar.arstudy.extensions.downloadFile
import java.io.FileNotFoundException
import java.io.IOException
import javax.inject.Inject


@HiltViewModel
class ArViewModel @Inject constructor(private val repository: ArStudyRepository) : ViewModel() {

    var fileUri by mutableStateOf<String?>(null)
        private set

    var loader by mutableStateOf(false)
        private set

    var showSpeaker by mutableStateOf(true)
        private set

    private var tts: TextToSpeech? = null

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

    fun checkSettings(context: Context, lessonPartText: String) {
        viewModelScope.launch {
            val speak = repository.getSpeakInfo()
            if (speak) {
                initTts(context, lessonPartText)
            } else {
                showSpeaker = false
            }
        }
    }

    fun speakerClick(context: Context, lessonPartText: String): Boolean {
        return if (tts != null) {
            destroyTts()
            tts = null
            false
        } else {
            initTts(context, lessonPartText)
            true
        }
    }

    fun updateTtsText(text: String) {
        tts?.speak(
            text,
            TextToSpeech.QUEUE_FLUSH,
            null,
            "lang"
        )
    }

    fun initTts(context: Context, lessonPartText: String) {
        if (tts == null) {
            tts = TextToSpeech(context) { status ->
                if (status == TextToSpeech.SUCCESS) {
                    val configuration = context.resources.configuration
                    val currentLocale = configuration.locales[0]
                    val result = tts?.setLanguage(currentLocale)
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        showSpeaker = false
                    } else {
                        tts?.speak(
                            lessonPartText,
                            TextToSpeech.QUEUE_FLUSH,
                            null,
                            "lang"
                        )
                    }
                } else {
                    showSpeaker = false
                }
            }
        }
    }

    fun destroyTts() {
        tts?.stop()
        tts?.shutdown()
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