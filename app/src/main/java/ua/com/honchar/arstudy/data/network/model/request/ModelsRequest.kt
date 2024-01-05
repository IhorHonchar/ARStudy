package ua.com.honchar.arstudy.data.network.model.request

import com.google.gson.annotations.SerializedName

data class ModelsRequest(
    @SerializedName("categoryId")
    val categoryId: Int?,
    @SerializedName("langId")
    val langId: Int?
)
