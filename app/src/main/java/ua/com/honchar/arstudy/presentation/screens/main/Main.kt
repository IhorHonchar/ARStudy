package ua.com.honchar.arstudy.presentation.screens.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import ua.com.honchar.arstudy.navigation.AppBottomBar
import ua.com.honchar.arstudy.navigation.MainNavigationGraph
import ua.com.honchar.arstudy.navigation.TopBarContent
import ua.com.honchar.arstudy.ui.theme.ARStudyTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Main() {
    val navController = rememberNavController()
    var topBarState by remember {
        mutableStateOf(TopBarContent())
    }
    var showSettingsDialog by rememberSaveable { mutableStateOf(false) }

    if (showSettingsDialog) {
        SettingsDialog(
            onDismiss = { showSettingsDialog = false },
        )
    }
    Scaffold(
        topBar = {
            val topBarColors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface,
                titleContentColor = MaterialTheme.colorScheme.onSurface,
            )
            if (topBarState.mainFlow) {
                TopAppBar(
                    colors = topBarColors,
                    title = {
                        Text(topBarState.title)
                    },
                    actions = {
                        if (topBarState.actionClick) {
                            IconButton(onClick = {
                                showSettingsDialog = true

                            }) {
                                Icon(
                                    imageVector = Icons.Outlined.Settings,
                                    contentDescription = null
                                )
                            }
                        }
                    },
                )
            } else {
                CenterAlignedTopAppBar(
                    colors = topBarColors,
                    title = {
                        Text(
                            text = topBarState.title,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    navigationIcon = {
                        val navigationClick = topBarState.navigationClick
                        if (navigationClick != null) {
                            IconButton(onClick = navigationClick) {
                                Icon(
                                    imageVector = Icons.Outlined.ArrowBack,
                                    contentDescription = null
                                )
                            }
                        }
                    }
                )
            }
        },
        bottomBar = { AppBottomBar(navController = navController) }
    ) { innerPadding ->
        MainNavigationGraph(
            navController = navController,
            modifier = Modifier.padding(innerPadding),
            updateTopBar = {
                topBarState = it
            }
        )
    }
}

@Preview
@Composable
private fun Preview() {
    ARStudyTheme {
        Main()
    }
}