package ua.com.honchar.arstudy.domain.repository.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Model(
    val id: Int,
    val name: String,
    val modelPath: String,
    val categoryId: Int?,
    val categoryName: String?,
    val fileName: String
): Parcelable
