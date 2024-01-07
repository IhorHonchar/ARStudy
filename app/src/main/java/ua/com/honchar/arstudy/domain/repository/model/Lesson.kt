package ua.com.honchar.arstudy.domain.repository.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Lesson(
    val id: Int,
    val name: String,
    val lessonParts: List<LessonPart>,
): Parcelable

@Parcelize
data class LessonPart(
    val id: Int,
    val text: String,
    val model: Model?
): Parcelable
