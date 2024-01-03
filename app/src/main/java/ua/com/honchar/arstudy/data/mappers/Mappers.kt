package ua.com.honchar.arstudy.data.mappers

import ua.com.honchar.arstudy.data.network.categories.response.CategoriesResponse
import ua.com.honchar.arstudy.data.network.model.response.ModelResponse
import ua.com.honchar.arstudy.domain.repository.model.Category
import ua.com.honchar.arstudy.domain.repository.model.Model

fun CategoriesResponse.toDomain() = Category(
    id = id ?: 0,
    name = name.orEmpty(),
    imagePath = imagePath.orEmpty(),
    order = order ?: 0
)

fun ModelResponse.toDomain() = Model(
    id = id ?: 0,
    name = name.orEmpty(),
    modelPath = modelPath.orEmpty()
)