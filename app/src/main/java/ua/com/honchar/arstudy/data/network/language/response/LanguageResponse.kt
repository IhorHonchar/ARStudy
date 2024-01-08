package ua.com.honchar.arstudy.data.network.language.response

import com.google.gson.annotations.SerializedName

data class LanguageResponse(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("code")
    val code: String?
)
