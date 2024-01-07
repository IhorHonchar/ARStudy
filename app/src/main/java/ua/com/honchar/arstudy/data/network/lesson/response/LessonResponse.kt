package ua.com.honchar.arstudy.data.network.lesson.response

import com.google.gson.annotations.SerializedName
import ua.com.honchar.arstudy.data.network.model.response.ModelResponse

data class LessonResponse(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("lessonParts")
    val lessonParts: List<LessonPartResponse>?
)

data class LessonPartResponse(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("text")
    val text: String?,
    @SerializedName("model")
    val model: ModelResponse?
)
