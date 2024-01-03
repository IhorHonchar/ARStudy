package ua.com.honchar.arstudy.data.network.model.response

import com.google.gson.annotations.SerializedName

data class ModelResponse(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("modelPath")
    val modelPath: String?
)
