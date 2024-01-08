package ua.com.honchar.arstudy.domain.repository

import ua.com.honchar.arstudy.domain.repository.model.Category
import ua.com.honchar.arstudy.domain.repository.model.Language
import ua.com.honchar.arstudy.domain.repository.model.Lesson
import ua.com.honchar.arstudy.domain.repository.model.Model
import ua.com.honchar.arstudy.domain.repository.model.Module
import ua.com.honchar.arstudy.domain.repository.model.User
import ua.com.honchar.arstudy.util.Resource

interface ArStudyRepository {

    suspend fun getCategories(langId: Int?): Resource<List<Category>>
    suspend fun getModelsByCategory(categoryId: Int?, langId: Int?): Resource<List<Model>>
    suspend fun getModulesByCategory(categoryId: Int, langId: Int?): Resource<List<Module>>
    suspend fun getModuleLessons(moduleId: Int, langId: Int?): Resource<List<Lesson>>
    suspend fun getLanguages(currentLang: String): Resource<List<Language>>
    suspend fun getLanguagesDb(): Resource<List<Language>>
    suspend fun getUserDb(): Resource<User?>
    suspend fun login(login: String, pass: String): Resource<User>
    suspend fun register(login: String, pass: String, name: String?): Resource<User>
    suspend fun exitApp()
    suspend fun saveSelectedLangId(langId: Int)
    suspend fun getSavedLangId(): Int
}