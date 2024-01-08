package ua.com.honchar.arstudy.domain.repository.model

import android.os.Parcelable
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
data class Lesson(
    val id: Int,
    val name: String,
    val lessonParts: List<LessonPart>,
) : Parcelable {

    val moreThanOneLessonPart: Boolean
        get() = lessonParts.size > 1

    val firstLessonPartModel: Model?
        get() = lessonParts.firstOrNull()?.model

    @IgnoredOnParcel
    private var lessonPartIndex = 0

    fun getNextLessonPart(): LessonPart? {
        return if (lessonPartIndex != lessonParts.lastIndex) {
            lessonParts[++lessonPartIndex]
        } else {
            null
        }
    }

    fun getPrevLessonPart(): LessonPart? {
        return if (lessonPartIndex != 0) {
            lessonParts[--lessonPartIndex]
        } else {
            null
        }
    }
}

@Parcelize
data class LessonPart(
    val id: Int,
    val text: String,
    val model: Model?
) : Parcelable
