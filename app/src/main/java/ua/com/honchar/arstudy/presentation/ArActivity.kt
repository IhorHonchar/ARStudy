package ua.com.honchar.arstudy.presentation

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.ar.core.Config
import com.google.ar.core.Plane
import dagger.hilt.android.AndroidEntryPoint
import io.github.sceneview.ar.ARScene
import io.github.sceneview.ar.arcore.getUpdatedPlanes
import io.github.sceneview.ar.node.AnchorNode
import io.github.sceneview.math.Position
import io.github.sceneview.node.ModelNode
import io.github.sceneview.rememberEngine
import io.github.sceneview.rememberModelLoader
import io.github.sceneview.rememberNodes
import kotlinx.coroutines.launch
import ua.com.honchar.arstudy.R
import ua.com.honchar.arstudy.domain.repository.model.Lesson
import ua.com.honchar.arstudy.extensions.parcelable
import ua.com.honchar.arstudy.ui.theme.ARStudyTheme

@AndroidEntryPoint
class ArActivity : ComponentActivity() {

    private var tts: TextToSpeech? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val lesson = intent.extras.parcelable<Lesson>(MODEL)
        val path = intent.extras?.getString(JUST_PATH).orEmpty()
//        tts = TextToSpeech(this) { status ->
//            if (status == TextToSpeech.SUCCESS) {
//                // Налаштуйте мову, виберіть українську
//                val result = tts.setLanguage(Locale("uk_UA"))
//                if (result == TextToSpeech.LANG_MISSING_DATA ||
//                    result == TextToSpeech.LANG_NOT_SUPPORTED
//                ) {
//                    // Мову не підтримується, робіть відповідні дії
//                } else {
//                    tts.speak(
//                        animal?.info,
//                        TextToSpeech.QUEUE_FLUSH,
//                        null,
//                        "lang"
//                    )
//                    // TextToSpeech готовий до використання української мови
//                }
//            } else {
//                // Помилка ініціалізації TextToSpeech
//            }
//        }
        setContent {
            ARStudyTheme {
                ARAnimalScreen(lesson, path)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        tts?.stop()
        tts?.shutdown()
    }

    companion object {
        const val MODEL = "model"
        const val JUST_PATH = "just_path"
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ARAnimalScreen(
    lesson: Lesson?,
    downloadedFileUri: String,
    viewModel: ArViewModel = hiltViewModel()
) {
    Surface {
        Box(modifier = Modifier.fillMaxSize()) {
            val context = LocalContext.current

            val sheetState = rememberModalBottomSheetState()
            var showBottomSheet by remember { mutableStateOf(false) }
            var isLoading by remember { mutableStateOf(true) }

            if (lesson != null) {

                var currentLessonPart by remember {
                    mutableStateOf(lesson.lessonParts.first())
                }

                val lessonPartIndex = lesson.lessonParts.indexOf(currentLessonPart)

                viewModel.getDownloadedModelUri(currentLessonPart, context)

                val fileUri = viewModel.fileUri
                if (fileUri != null) {
                    ArScreen(
                        downloadedFileUri = fileUri,
                        updateLoading = { loadingState ->
                            isLoading = loadingState
                        }
                    )
                }

                if (lessonPartIndex < lesson.lessonParts.lastIndex) {
                    Icon(
                        imageVector = Icons.Filled.KeyboardArrowRight,
                        contentDescription = null,
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .size(68.dp)
                            .clickable {
                                lesson
                                    .getNextLessonPart()
                                    ?.let { currentLessonPart = it }
                            }
                    )
                }

                if (lessonPartIndex > 0) {
                    Icon(
                        imageVector = Icons.Filled.KeyboardArrowLeft,
                        contentDescription = null,
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .size(68.dp)
                            .clickable {
                                lesson
                                    .getPrevLessonPart()
                                    ?.let { currentLessonPart = it }
                            }
                    )
                }

                Loading(isLoading = isLoading)

                Loader(viewModel.loader)

                ShowBottomSheetBtn {
                    showBottomSheet = true
                }

                Image(
                    painter = painterResource(id = R.drawable.ic_volume_up),
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(30.dp)
                )

                BottomSheet(
                    showBottomSheet = showBottomSheet,
                    sheetState = sheetState,
                    text = currentLessonPart.text,
                    hideBottomSheet = {
                        showBottomSheet = false
                    }
                )
            } else {
                ArScreen(
                    downloadedFileUri = downloadedFileUri,
                    updateLoading = { loadingState ->
                        isLoading = loadingState
                    }
                )

                Loading(isLoading = isLoading)
            }
        }
    }
}

@Composable
fun BoxScope.Loader(showLoader: Boolean) {
    if (showLoader) {
        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(
    showBottomSheet: Boolean,
    sheetState: SheetState,
    text: String,
    hideBottomSheet: () -> Unit
) {
    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = hideBottomSheet,
            sheetState = sheetState
        ) {
            LazyColumn {
                item {
                    Text(
                        text = text,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(30.dp))
                }
                item {
                    Spacer(modifier = Modifier.height(30.dp))
                }
            }
        }
    }

}

@Composable
fun BoxScope.ShowBottomSheetBtn(showBottomSheet: () -> Unit) {
    Button(
        onClick = showBottomSheet,
        modifier = Modifier.align(Alignment.BottomCenter)
    ) {
        Image(
            painter = painterResource(id = R.drawable.text),
            contentDescription = null,
            modifier = Modifier
                .size(30.dp)
                .align(Alignment.CenterVertically)
        )
    }
}

@Composable
fun BoxScope.Loading(isLoading: Boolean) {
    if (isLoading) {
        Text(
            text = "Loading...",
            modifier = Modifier.align(Alignment.Center),
            fontSize = 28.sp
        )
    }
}

@Composable
fun ArScreen(downloadedFileUri: String, updateLoading: (Boolean) -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        var planeRenderer by remember { mutableStateOf(true) }
        val engine = rememberEngine()
        val modelLoader = rememberModelLoader(engine)
        val childNodes = rememberNodes()
        val coroutineScope = rememberCoroutineScope()

        ARScene(
            modifier = Modifier.fillMaxSize(),
            childNodes = childNodes,
            engine = engine,
            modelLoader = modelLoader,
            planeRenderer = planeRenderer,
            sessionConfiguration = { session, config ->
                config.depthMode =
                    when (session.isDepthModeSupported(Config.DepthMode.AUTOMATIC)) {
                        true -> Config.DepthMode.AUTOMATIC
                        else -> Config.DepthMode.DISABLED
                    }
                config.instantPlacementMode = Config.InstantPlacementMode.DISABLED
                config.lightEstimationMode =
                    Config.LightEstimationMode.ENVIRONMENTAL_HDR
            },
            onSessionUpdated = { _, frame ->
                if (childNodes.isNotEmpty()) return@ARScene

                frame.getUpdatedPlanes()
                    .firstOrNull { it.type == Plane.Type.HORIZONTAL_UPWARD_FACING }
                    ?.let { plane ->
                        updateLoading.invoke(true)
                        childNodes += AnchorNode(
                            engine = engine,
                            anchor = plane.createAnchor(plane.centerPose)
                        ).apply {
                            isEditable = false
                            coroutineScope.launch {
                                modelLoader.loadModelInstance(downloadedFileUri)
                                    ?.let {
                                        addChildNode(
                                            ModelNode(
                                                modelInstance = it,
                                                // Scale to fit in a 0.5 meters cube
                                                scaleToUnits = 2.5f,
                                                // Bottom origin instead of center so the
                                                // model base is on floor
                                                centerOrigin = Position(y = -0.5f)
                                            ).apply {
                                                isEditable = false
                                            }
                                        )
                                    }
                                planeRenderer = false
                                updateLoading.invoke(false)
                            }
                        }
                    }
            }
        )

        LaunchedEffect(downloadedFileUri) {
            childNodes.clear()
        }
    }
}
