package ua.com.honchar.arstudy.presentation.screens.start.login

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ua.com.honchar.arstudy.R
import ua.com.honchar.arstudy.presentation.screens.BaseScreen
import ua.com.honchar.arstudy.preview.TwoThemePreview
import ua.com.honchar.arstudy.ui.theme.ARStudyTheme
import ua.com.honchar.arstudy.ui.theme.Typography

@Composable
fun LoginScreen(
    navigateToMain: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Yellow)
    ) {
        LaunchedEffect(Unit) {
            viewModel.goToMain.collect {
                navigateToMain.invoke()
            }
        }
        BaseScreen(
            error = viewModel.state.error,
            isLoading = viewModel.state.isLoading
        ) {
            LoginScreenDetails(
                btnClick = { login, pass, name, isRegister ->
                    if (isRegister) {
                        viewModel.signUp(login, pass, name)
                    } else {
                        viewModel.login(login, pass)
                    }
                },
                validateEmail = {
                    viewModel.validateEmail(it)
                },
                emailError = viewModel.state.emailError
            )
        }
    }
}

@Composable
fun LoginScreenDetails(
    btnClick: (login: String, pass: String, name: String?, isRegister: Boolean) -> Unit,
    validateEmail: (String) -> Unit,
    emailError: Boolean
) {
    Surface {
        Box(modifier = Modifier.fillMaxSize()) {
            val density = LocalDensity.current

            var login by remember {
                mutableStateOf("")
            }
            var password by remember {
                mutableStateOf("")
            }
            var name by remember {
                mutableStateOf("")
            }

            var isRegister by remember {
                mutableStateOf(false)
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center)
            ) {
                Icon(
                    imageVector = Icons.Filled.AccountCircle,
                    tint = MaterialTheme.colorScheme.onSurface,
                    contentDescription = null,
                    modifier = Modifier
                        .size(100.dp)
                        .align(Alignment.CenterHorizontally)
                )
                val colors = TextFieldDefaults.outlinedTextFieldColors(
                    textColor = MaterialTheme.colorScheme.onSurface,
                    focusedBorderColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onSurface,
                    focusedLabelColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedLabelColor = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = login,
                    onValueChange = {
                        login = it
                        validateEmail(it)
                    },
                    label = { Text(stringResource(id = R.string.email)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp),
                    colors = colors,
                    isError = emailError
                )
                if (emailError) {
                    Text(
                        text = stringResource(id = R.string.incorrect_email),
                        modifier = Modifier.padding(start = 32.dp),
                        color = Color.Red,
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text(stringResource(id = R.string.password)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp),
                    colors = colors,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                )
                AnimatedVisibility(
                    visible = isRegister,
                    enter = slideInVertically {
                        with(density) { -40.dp.roundToPx() }
                    } + expandVertically(
                        expandFrom = Alignment.Top
                    ) + fadeIn(
                        initialAlpha = 0.3f
                    ),
                    exit = slideOutVertically() + shrinkVertically() + fadeOut()
                ) {
                    Column {
                        Spacer(modifier = Modifier.height(16.dp))
                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            label = { Text(stringResource(id = R.string.name)) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 32.dp),
                            colors = colors
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(id = if (isRegister) R.string.log_in else R.string.sign_up),
                    style = Typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .clickable {
                            isRegister = !isRegister
                        }
                )

            }
            Button(
                onClick = {
                    btnClick(login, password, name, isRegister)
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onSurface
                ),
                enabled = !emailError
            ) {
                Text(text = stringResource(id = if (isRegister) R.string.sign_up else R.string.log_in))
            }
        }
    }
}

@TwoThemePreview
@Composable
private fun LoginPreview() {
    ARStudyTheme {
        LoginScreenDetails({ _, _, _, _ -> }, {}, false)
    }
}

@TwoThemePreview
@Composable
private fun LoginPreviewError() {
    ARStudyTheme {
        LoginScreenDetails({ _, _, _, _ -> }, {}, true)
    }
}