package ua.com.honchar.arstudy.data.network.module.request

import com.google.gson.annotations.SerializedName

data class ModulesRequest(
    @SerializedName("categoryId")
    val categoryId: Int,
    @SerializedName("langId")
    val langId: Int?
)