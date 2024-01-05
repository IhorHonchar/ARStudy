package ua.com.honchar.arstudy.domain.repository.model

data class Model(
    val id: Int,
    val name: String,
    val modelPath: String,
    val categoryId: Int?,
    val categoryName: String?
)
