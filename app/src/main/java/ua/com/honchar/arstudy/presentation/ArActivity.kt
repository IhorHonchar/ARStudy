package ua.com.honchar.arstudy.presentation

import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
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
import ua.com.honchar.arstudy.ui.theme.ARStudyTheme

@AndroidEntryPoint
class ArActivity : ComponentActivity() {

    private var tts: TextToSpeech? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        val animal = intent.extras.parcelable<Animal>("animal")
        val path = intent.extras?.getString("path").orEmpty()
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
                ARAnimalScreen(path)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        tts?.stop()
        tts?.shutdown()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ARAnimalScreen(path: String, viewModel: MainViewModel = hiltViewModel()) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }
//    if (animal != null) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        var isLoading by remember { mutableStateOf(true) }
        var planeRenderer by remember { mutableStateOf(true) }
        val engine = rememberEngine()
        val modelLoader = rememberModelLoader(engine)
        val childNodes = rememberNodes()
        val coroutineScope = rememberCoroutineScope()

        val context = LocalContext.current
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
                        isLoading = true
                        childNodes += AnchorNode(
                            engine = engine,
                            anchor = plane.createAnchor(plane.centerPose)
                        ).apply {
                            isEditable = true
                            coroutineScope.launch {
                                modelLoader.loadModelInstance(path)
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
                                                isEditable = true
                                            }
                                        )
                                    }
                                planeRenderer = false
                                isLoading = false
                            }
                        }
                    }
            }
        )
        if (isLoading) {
            Text(
                text = "Loading...",
                modifier = Modifier.align(Alignment.Center),
                fontSize = 28.sp
            )
        }
        Button(
            onClick = { showBottomSheet = true },
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            Row {
                Image(
                    painter = painterResource(id = R.drawable.text),
                    contentDescription = null,
                    modifier = Modifier
                        .size(30.dp)
                        .align(Alignment.CenterVertically)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Показати інфо",
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }
        }

        Image(
            painter = painterResource(id = R.drawable.ic_volume_up),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(30.dp)
        )

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheet = false
                },
                sheetState = sheetState
            ) {
                LazyColumn {
                    item {
//                            Text(
//                                text = animal.info,
//                                modifier = Modifier.padding(horizontal = 16.dp)
//                            )
                    }
                    item {
                        Spacer(modifier = Modifier.height(30.dp))
                    }
                    item {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Button(
                                onClick = {
                                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                                        if (!sheetState.isVisible) {
                                            showBottomSheet = false
                                        }
                                    }
                                },
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            ) {
                                Text("Сховати інфо")
                            }
                        }
                    }
                    item {
                        Spacer(modifier = Modifier.height(30.dp))
                    }
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun Preview() {
    val tiger = Animal(
        name = "Тигр",
        modelName = "Tiger",
        icon = R.drawable.tiger,
        info = "Тигр, пантера тигр — великий ссавець родини котових, один із сучасних п'ятьох видів роду пантер (Panthera). Слово «тигр» походить від дав.-гр. τίγρις, яке у свою чергу походить із індоіранських мов, пов'язано з ав. t̰igri — «стріла», натякаючи на стрімкість тигра. Еволюційним центром походження і сучасного ареалу є Східна та Південно-Східна Азія. Бувши найбільшим у світі представником родини котячих, тигр у всіх екосистемах є верхівкою харчової піраміди. Згідно з палеонтологічними даними, розмір сучасних тигрів приблизно дорівнює найбільшим із викопних представників родини.\n" +
                "\n" +
                "Найчисельнішим із підвидів тигра є бенгальський тигр (номінативний підвид), чисельність якого становить близько 80 % загальної кількості виду. Він розповсюджений на території Індії, Бангладеш, М'янми, Бутану та Непалу.\n" +
                "\n" +
                "Тигр — вид, що перебуває під загрозою вимирання. Нині кількість цих тварин у неволі більша, ніж у природі.\n" +
                "\n" +
                "За способом життя тигр є одинаком. Він є видом виразно територіальним, що взагалі характерне для котячих. Звичним для тигра ландшафтом є середньо- та щільнозарослі лісові ділянки, хоча інколи його можна зустріти і на відкритій місцевості.\n" +
                "\n" +
                "Як і в більшості інших котячих, основним способом полювання є підкрадання або вичікування в засідці зі швидким, але коротким фінальним кидком. Більша частина здобичі становить середні та великі копитні, але тигр може полювати і на зовсім дрібних тварин, аж до щурів.\n" +
                "\n" +
                "Самці помітно більші за самиць, і мають більшу територію. Також виразні розмірні розбіжності спостерігаються між дев'ятьма підвидами цього виду, що збереглися до історичних часів."
    )
    ARStudyTheme {
        ARAnimalScreen("")
    }
}
