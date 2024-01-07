package ua.com.honchar.arstudy.data.network.lesson.request

import com.google.gson.annotations.SerializedName

data class LessonsRequest(
    @SerializedName("moduleId")
    val moduleId: Int,
    @SerializedName("langId")
    val langId: Int?
)
