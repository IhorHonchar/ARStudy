package ua.com.honchar.arstudy.presentation.screens.main

import android.app.LocaleManager
import android.content.Context
import android.os.Build
import android.os.LocaleList
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.Divider
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.core.os.LocaleListCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ua.com.honchar.arstudy.R
import ua.com.honchar.arstudy.domain.model.Language
import ua.com.honchar.arstudy.domain.repository.ArStudyRepository
import ua.com.honchar.arstudy.domain.repository.Resource
import ua.com.honchar.arstudy.ui.theme.Typography
import javax.inject.Inject

@Composable
fun SettingsDialog(
    onDismiss: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val configuration = LocalConfiguration.current
    val context = LocalContext.current

    var selectedLocale by remember {
        mutableStateOf(configuration.locales[0].toLanguageTag())
    }

    var playText by rememberSaveable {
        mutableStateOf(viewModel.state.speakerOn)
    }

    LaunchedEffect(key1 = viewModel.state.speakerOn) {
        playText = viewModel.state.speakerOn
    }

    LaunchedEffect(key1 = Unit) {
        viewModel.checkSettings()
    }

    AlertDialog(
        properties = DialogProperties(usePlatformDefaultWidth = false),
        modifier = Modifier.widthIn(max = configuration.screenWidthDp.dp - 80.dp),
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(id = R.string.settings),
                style = MaterialTheme.typography.titleLarge,
            )
        },
        text = {
            Column {

                if (viewModel.state.languages != null) {
                    Column {
                        Text(
                            text = stringResource(id = R.string.language),
                            style = Typography.titleLarge
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Divider()
                        Spacer(modifier = Modifier.height(8.dp))
                        viewModel.state.languages?.forEach { language ->

                            Row(modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedLocale = language.code
                                }
                            ) {
                                RadioButton(
                                    selected = selectedLocale == language.code,
                                    onClick = { selectedLocale = language.code }
                                )
                                val langString =
                                    language.getLangResId()?.let { stringResource(id = it) }
                                        ?: language.code
                                Text(
                                    text = langString,
                                    style = Typography.bodyLarge,
                                    modifier = Modifier.align(Alignment.CenterVertically)
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
                Column {
                    Text(
                        text = stringResource(id = R.string.play_text),
                        style = Typography.titleLarge
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Divider()
                    Spacer(modifier = Modifier.height(8.dp))
                    listOf(
                        (false to android.R.string.cancel),
                        (true to android.R.string.ok)
                    ).forEach { (play, testRes) ->
                        Row(modifier = Modifier.clickable {
                            playText = play
                        }) {
                            RadioButton(
                                selected = playText == play,
                                onClick = { playText = play }
                            )
                            Text(
                                text = stringResource(id = testRes),
                                style = Typography.bodyLarge,
                                modifier = Modifier.align(Alignment.CenterVertically)
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    viewModel.saveSpeakText(playText)
                    viewModel.saveLang(selectedLocale, context)
                    onDismiss.invoke()
                }
            ) {
                Text(stringResource(id = R.string.save))
            }
        }
    )
}

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repository: ArStudyRepository
) : ViewModel() {

    var state by mutableStateOf(SettingsState())
        private set

    fun saveLang(localeTag: String, context: Context) {
        viewModelScope.launch {
            state.languages?.firstOrNull { it.code == localeTag }?.let { lang ->
                repository.saveSelectedLangId(lang.id)
                localeSelection(context, lang.code)
            }
        }
    }

    fun saveSpeakText(speak: Boolean) {
        viewModelScope.launch {
            repository.saveSpeakInfo(speak)
        }
    }

    fun checkSettings() {
        viewModelScope.launch {
            val speakerOn = repository.getSpeakInfo()
            state = when (val res = repository.getLanguagesDb()) {
                is Resource.Success -> {
                    SettingsState(res.data, speakerOn)
                }

                is Resource.Error -> {
                    SettingsState(isError = true)
                }
            }
        }
    }

    private fun localeSelection(context: Context, localeTag: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.getSystemService(LocaleManager::class.java).applicationLocales =
                LocaleList.forLanguageTags(localeTag)
        } else {
            AppCompatDelegate.setApplicationLocales(
                LocaleListCompat.forLanguageTags(localeTag)
            )
        }
    }
}

data class SettingsState(
    val languages: List<Language>? = null,
    val speakerOn: Boolean = false,
    val isError: Boolean = false
)