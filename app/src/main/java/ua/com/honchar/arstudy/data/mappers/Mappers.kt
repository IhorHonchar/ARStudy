package ua.com.honchar.arstudy.data.mappers

import ua.com.honchar.arstudy.data.network.categories.response.CategoriesResponse
import ua.com.honchar.arstudy.domain.repository.model.Category

fun CategoriesResponse.toDomain() = Category(
    id = id ?: 0,
    name = name.orEmpty(),
    imagePath = imagePath.orEmpty(),
    order = order ?: 0
)