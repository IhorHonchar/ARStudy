package ua.com.honchar.arstudy.presentation.screens.start.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.hilt.navigation.compose.hiltViewModel
import ua.com.honchar.arstudy.R
import ua.com.honchar.arstudy.preview.TwoThemePreview
import ua.com.honchar.arstudy.ui.theme.ARStudyTheme

@Composable
fun SplashScreen(
    navigateToMain: () -> Unit,
    navigateToLogIn: () -> Unit,
    viewModel: SplashViewModel = hiltViewModel()
) {
    Box {
        viewModel.checkUserData()
        LaunchedEffect(key1 = Unit) {
            viewModel.openMain.collect {
                navigateToMain.invoke()
            }
        }
        LaunchedEffect(key1 = Unit) {
            viewModel.openLogin.collect {
                navigateToLogIn.invoke()
            }
        }
        SplashScreenDetails()
    }
}

@Composable
fun SplashScreenDetails() {
    Surface {
        Box(modifier = Modifier.fillMaxSize()) {
            Text(
                text = stringResource(id = R.string.app_name),
                fontSize = TextUnit(60f, TextUnitType.Sp),
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@TwoThemePreview
@Composable
private fun SplashPreview() {
    ARStudyTheme {
        SplashScreenDetails()
    }
}