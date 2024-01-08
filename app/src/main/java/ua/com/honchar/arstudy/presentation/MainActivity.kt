package ua.com.honchar.arstudy.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import ua.com.honchar.arstudy.presentation.screens.start.Start
import ua.com.honchar.arstudy.ui.theme.ARStudyTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ARStudyTheme {
                Start()
            }
        }
    }
}