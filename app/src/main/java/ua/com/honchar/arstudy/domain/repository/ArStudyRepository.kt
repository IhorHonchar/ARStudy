package ua.com.honchar.arstudy.domain.repository

import ua.com.honchar.arstudy.domain.model.Category
import ua.com.honchar.arstudy.domain.model.Language
import ua.com.honchar.arstudy.domain.model.Lesson
import ua.com.honchar.arstudy.domain.model.Model
import ua.com.honchar.arstudy.domain.model.Module
import ua.com.honchar.arstudy.domain.model.User

interface ArStudyRepository {

    suspend fun getCategories(): Resource<List<Category>>
    suspend fun getModelsByCategory(categoryId: Int?): Resource<List<Model>>
    suspend fun getModulesByCategory(categoryId: Int): Resource<List<Module>>
    suspend fun getModuleLessons(moduleId: Int): Resource<List<Lesson>>
    suspend fun getLanguages(currentLang: String): Resource<List<Language>>
    suspend fun getLanguagesDb(): Resource<List<Language>>
    suspend fun getUserDb(): Resource<User?>
    suspend fun login(login: String, pass: String): Resource<User>
    suspend fun register(login: String, pass: String, name: String?): Resource<User>
    suspend fun exitApp()
    suspend fun saveSelectedLangId(langId: Int)
    suspend fun getSavedLangId(): Int
    suspend fun saveSpeakInfo(speak: Boolean)
    suspend fun getSpeakInfo(): Boolean
}