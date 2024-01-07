package ua.com.honchar.arstudy.data.mappers

import ua.com.honchar.arstudy.data.network.categories.response.CategoriesResponse
import ua.com.honchar.arstudy.data.network.lesson.response.LessonPartResponse
import ua.com.honchar.arstudy.data.network.lesson.response.LessonResponse
import ua.com.honchar.arstudy.data.network.model.response.ModelResponse
import ua.com.honchar.arstudy.data.network.module.response.ModuleResponse
import ua.com.honchar.arstudy.domain.repository.model.Category
import ua.com.honchar.arstudy.domain.repository.model.Lesson
import ua.com.honchar.arstudy.domain.repository.model.LessonPart
import ua.com.honchar.arstudy.domain.repository.model.Model
import ua.com.honchar.arstudy.domain.repository.model.Module

fun CategoriesResponse.toDomain() = Category(
    id = id ?: 0,
    name = name.orEmpty(),
    imagePath = imagePath.orEmpty(),
    order = order ?: 0
)

fun ModelResponse.toDomain() = Model(
    id = id ?: 0,
    name = name.orEmpty(),
    modelPath = modelPath.orEmpty(),
    categoryId = categoryId,
    categoryName = categoryName
)

fun ModuleResponse.toDomain() = Module(
    id = id ?: 0,
    name = name.orEmpty(),
    info = info.orEmpty()
)

fun LessonResponse.toDomain() = Lesson(
    id = id ?: 0,
    name = name.orEmpty(),
    lessonParts = lessonParts?.map { it.toDomain() }.orEmpty()
)

fun LessonPartResponse.toDomain() = LessonPart(
    id = id ?: 0,
    text = text.orEmpty(),
    model = model?.toDomain()
)