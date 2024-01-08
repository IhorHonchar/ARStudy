package ua.com.honchar.arstudy.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch

@Composable
fun BaseScreen(
    isLoading: Boolean = false,
    error: String?,
    content: @Composable () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val localCoroutineScope = rememberCoroutineScope()
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Box(Modifier.padding(paddingValues)) {
            content()
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
        error?.let {
            localCoroutineScope.launch {
                snackbarHostState.showSnackbar(it)
            }
        }
    }
}