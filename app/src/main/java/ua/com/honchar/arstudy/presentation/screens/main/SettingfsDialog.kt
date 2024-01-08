package ua.com.honchar.arstudy.presentation.screens.main

import android.app.LocaleManager
import android.content.Context
import android.os.Build
import android.os.LocaleList
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.core.os.LocaleListCompat
import ua.com.honchar.arstudy.R
import java.util.Locale

@Composable
fun SettingsDialog(onDismiss: () -> Unit) {
    val configuration = LocalConfiguration.current
    val context = LocalContext.current

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

            Column(modifier = Modifier.fillMaxSize()) {
                Button(onClick = {
                    val currentLocale = configuration.locales[0]
                    localeSelection(context,  if (currentLocale == Locale("en")) "uk" else "en")
                })
                {
                    Text(text = "Change Language")}
            }
        },
        confirmButton = {

        }
    )
}

fun localeSelection(context: Context, localeTag: String) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        context.getSystemService(LocaleManager::class.java).applicationLocales =
            LocaleList.forLanguageTags(localeTag)
    } else {
        AppCompatDelegate.setApplicationLocales(
            LocaleListCompat.forLanguageTags(localeTag)
        )
    }
}