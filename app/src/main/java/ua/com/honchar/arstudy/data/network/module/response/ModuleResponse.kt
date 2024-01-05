package ua.com.honchar.arstudy.data.network.module.response

import com.google.gson.annotations.SerializedName

data class ModuleResponse(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("info")
    val info: String?
)
