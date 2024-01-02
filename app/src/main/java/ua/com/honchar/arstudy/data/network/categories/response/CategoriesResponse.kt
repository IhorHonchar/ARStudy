package ua.com.honchar.arstudy.data.network.categories.response

import com.google.gson.annotations.SerializedName

data class CategoriesResponse(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("order")
    val order: Int?,
    @SerializedName("imagePath")
    val imagePath: String?
)
