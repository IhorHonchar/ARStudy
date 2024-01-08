package ua.com.honchar.arstudy.presentation.screens.main.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ua.com.honchar.arstudy.domain.repository.model.User
import ua.com.honchar.arstudy.presentation.screens.BaseScreen
import ua.com.honchar.arstudy.preview.TwoThemePreview
import ua.com.honchar.arstudy.ui.theme.ARStudyTheme
import ua.com.honchar.arstudy.ui.theme.Typography

@Composable
fun ProfileScreen(
    goToStart: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {

    LaunchedEffect(Unit) {
        viewModel.goToStart.collect {
            goToStart.invoke()
        }
    }

    var openAlertDialog by remember { mutableStateOf(false) }

    if (openAlertDialog) {
        ExitAlertDialog(
            onDismissRequest = {
                openAlertDialog = false
            },
            onConfirmation = {
                viewModel.exitApp()
            }
        )
    }

    BaseScreen(
        error = viewModel.state.error,
        isLoading = viewModel.state.isLoading
    ) {
        ProfileScreenDetails(
            data = viewModel.state.data,
            exitApp = { openAlertDialog = true }
        )
    }
}

@Composable
fun ProfileScreenDetails(data: User?, exitApp: () -> Unit) {
    Surface {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center)
                    .padding(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.AccountCircle,
                    tint = MaterialTheme.colorScheme.onSurface,
                    contentDescription = null,
                    modifier = Modifier
                        .size(100.dp)
                        .align(Alignment.CenterHorizontally)
                )
                if (data != null) {
                    Spacer(modifier = Modifier.height(100.dp))
                    Text(
                        text = data.name,
                        color = MaterialTheme.colorScheme.onSurface,
                        style = Typography.headlineMedium
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = data.login,
                        color = MaterialTheme.colorScheme.onSurface,
                        style = Typography.headlineMedium

                    )
                }
            }

            FloatingActionButton(
                onClick = { exitApp.invoke() },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.ExitToApp,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(8.dp)
                        .size(48.dp)
                )
            }
        }
    }
}

@TwoThemePreview
@Composable
private fun ProfilePreview() {
    ARStudyTheme {
        ProfileScreenDetails(data = User("ihor693@gmail.com", "Ihor Honchar")) {}
    }
}