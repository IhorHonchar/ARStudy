package ua.com.honchar.arstudy.presentation.screens.lessons

import android.content.Intent
import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import ua.com.honchar.arstudy.R
import ua.com.honchar.arstudy.domain.repository.model.Lesson
import ua.com.honchar.arstudy.presentation.ArActivity
import ua.com.honchar.arstudy.presentation.screens.BaseScreen
import ua.com.honchar.arstudy.presentation.screens.categories.CardsPager
import ua.com.honchar.arstudy.presentation.screens.categories.ItemCard
import ua.com.honchar.arstudy.ui.theme.ARStudyTheme

@Composable
fun LessonsScreen(
    viewModel: LessonsViewModel = hiltViewModel(),
    moduleId: Int?
) {
    LaunchedEffect(Unit) {
        viewModel.getLessons(moduleId)
    }

    val context = LocalContext.current

    BaseScreen(
        error = viewModel.state.error,
        isLoading = viewModel.state.isLoading
    ) {
        LessonsScreenDetails(
            lessons = viewModel.state.data.orEmpty(),
            lessonClick = {
                val intent = Intent(context, ArActivity::class.java).apply {
                    putExtra(ArActivity.MODEL, it)
                }
                context.startActivity(intent)
            }
        )
    }
}

@Composable
fun LessonsScreenDetails(
    lessons: List<Lesson>,
    lessonClick: (Lesson) -> Unit
) {
    CardsPager(list = lessons) { _, lesson, cardModifier, imageModifier ->
        ItemCard(
            text = lesson.name,
            modifier = cardModifier,
            imageModifier = imageModifier,
            image = R.drawable.ic_lesson,
            categoryClick = {
                lessonClick(lesson)
            }
        )
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun LessonsPreview() {
    ARStudyTheme {
        LessonsScreenDetails(
            lessons = List(5) { Lesson(it, "lesson #$it", emptyList()) },
            lessonClick = {})
    }
}