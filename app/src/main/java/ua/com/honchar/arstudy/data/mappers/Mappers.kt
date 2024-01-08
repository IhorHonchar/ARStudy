package ua.com.honchar.arstudy.data.mappers

import ua.com.honchar.arstudy.data.db.languages.DbLanguage
import ua.com.honchar.arstudy.data.db.user.DbUser
import ua.com.honchar.arstudy.data.network.categories.response.CategoriesResponse
import ua.com.honchar.arstudy.data.network.language.response.LanguageResponse
import ua.com.honchar.arstudy.data.network.lesson.response.LessonPartResponse
import ua.com.honchar.arstudy.data.network.lesson.response.LessonResponse
import ua.com.honchar.arstudy.data.network.model.response.ModelResponse
import ua.com.honchar.arstudy.data.network.module.response.ModuleResponse
import ua.com.honchar.arstudy.data.network.user.response.UserResponse
import ua.com.honchar.arstudy.domain.repository.model.Category
import ua.com.honchar.arstudy.domain.repository.model.Language
import ua.com.honchar.arstudy.domain.repository.model.Lesson
import ua.com.honchar.arstudy.domain.repository.model.LessonPart
import ua.com.honchar.arstudy.domain.repository.model.Model
import ua.com.honchar.arstudy.domain.repository.model.Module
import ua.com.honchar.arstudy.domain.repository.model.User

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
    categoryName = categoryName,
    fileName = fileName.orEmpty()
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

fun LanguageResponse.toDomain() = Language(
    id = id ?: 0,
    code = code.orEmpty()
)

fun Language.toDb() = DbLanguage(
    id = id,
    code = code
)

fun DbLanguage.toDomain() = Language(
    id = id,
    code = code
)

fun UserResponse.toDomain() = User(
    login = login.orEmpty(),
    name = name.orEmpty()
)

fun User.toDb() = DbUser(
    id = 0,
    name = name,
    login = login
)

fun DbUser.toDomain() = User(
    login = login,
    name = name
)