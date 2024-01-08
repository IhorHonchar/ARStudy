package ua.com.honchar.arstudy.data.network.user.request

import com.google.gson.annotations.SerializedName

data class UserRequest(
    @SerializedName("login")
    val login: String,
    @SerializedName("pass")
    val pass: String,
    @SerializedName("name")
    val name: String?
)
