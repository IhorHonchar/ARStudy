package ua.com.honchar.arstudy.data.network.user.response

import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName("login")
    val login: String?,
    @SerializedName("name")
    val name: String?
)
