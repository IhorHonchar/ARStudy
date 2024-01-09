package ua.com.honchar.arstudy.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import ua.com.honchar.arstudy.data.db.ArDao
import ua.com.honchar.arstudy.data.mappers.toDb
import ua.com.honchar.arstudy.data.mappers.toDomain
import ua.com.honchar.arstudy.data.network.ArStudyApi
import ua.com.honchar.arstudy.data.network.lesson.request.LessonsRequest
import ua.com.honchar.arstudy.data.network.model.request.ModelsRequest
import ua.com.honchar.arstudy.data.network.module.request.ModulesRequest
import ua.com.honchar.arstudy.data.network.user.request.UserRequest
import ua.com.honchar.arstudy.data.sharedPref.SharedPref
import ua.com.honchar.arstudy.domain.repository.ArStudyRepository
import ua.com.honchar.arstudy.domain.model.Category
import ua.com.honchar.arstudy.domain.model.Language
import ua.com.honchar.arstudy.domain.model.Lesson
import ua.com.honchar.arstudy.domain.model.Model
import ua.com.honchar.arstudy.domain.model.Module
import ua.com.honchar.arstudy.domain.model.User
import ua.com.honchar.arstudy.domain.repository.Resource
import javax.inject.Inject

class ArStudyRepositoryImpl @Inject constructor(
    private val api: ArStudyApi,
    private val dao: ArDao,
    private val sharedPref: SharedPref,
) : ArStudyRepository {

    override suspend fun getCategories(): Resource<List<Category>> {
        return executeRequest {
            val langId = getLangId()
            val response = api.getCategories(langId)
            response.map { it.toDomain() }
        }
    }

    override suspend fun getModelsByCategory(
        categoryId: Int?
    ): Resource<List<Model>> {
        return executeRequest {
            val langId = getLangId()
            val requestData = ModelsRequest(categoryId, langId)
            val response = api.getModelsByCategory(requestData)
            response.map { it.toDomain() }
        }
    }

    override suspend fun getModulesByCategory(
        categoryId: Int
    ): Resource<List<Module>> {
        return executeRequest {
            val langId = getLangId()
            val requestData = ModulesRequest(categoryId, langId)
            val response = api.getModulesByCategory(requestData)
            response.map { it.toDomain() }
        }
    }

    override suspend fun getModuleLessons(moduleId: Int): Resource<List<Lesson>> {
        return executeRequest {
            val langId = getLangId()
            val request = LessonsRequest(moduleId, langId)
            val response = api.getModuleLessons(request)
            response.map { it.toDomain() }
        }
    }

    override suspend fun getLanguages(currentLang: String): Resource<List<Language>> {
        return executeRequest {
            val response = api.getLanguages()
            val domainModels = response.map { it.toDomain() }
            coroutineScope {
                launch(Dispatchers.IO) {
                    val lang = domainModels.firstOrNull { it.code == currentLang }
                    lang?.let {
                        sharedPref.saveLangId(it.id)
                    }
                    val dbModels = domainModels.map { it.toDb() }
                    dao.insertAll(*dbModels.toTypedArray())
                }
            }
            domainModels
        }
    }

    override suspend fun getLanguagesDb(): Resource<List<Language>> {
        return executeRequest {
            val dbLanguages = dao.getLanguages()
            dbLanguages.map { it.toDomain() }
        }
    }

    override suspend fun getUserDb(): Resource<User?> {
        return executeRequest {
            val user = dao.getUserData()
            user?.toDomain()
        }
    }

    override suspend fun login(login: String, pass: String): Resource<User> {
        return executeRequest {
            val request = UserRequest(
                login = login,
                pass = pass,
                name = null
            )
            val response = api.login(request)
            val userDomain = response.toDomain()
            saveUser(userDomain)
            userDomain
        }
    }

    override suspend fun register(login: String, pass: String, name: String?): Resource<User> {
        return executeRequest {
            val request = UserRequest(
                login = login,
                pass = pass,
                name = name
            )
            val response = api.register(request)
            val userDomain = response.toDomain()
            saveUser(userDomain)
            userDomain
        }
    }

    override suspend fun exitApp() {
        dao.clearUser()
    }

    override suspend fun saveSelectedLangId(langId: Int) {
        sharedPref.saveLangId(langId)
    }

    override suspend fun getSavedLangId(): Int {
        return sharedPref.getLangId()
    }

    override suspend fun saveSpeakInfo(speak: Boolean) {
        sharedPref.saveSpeakInfo(speak)
    }

    override suspend fun getSpeakInfo(): Boolean {
        return sharedPref.getSpeakInfo()
    }

    private suspend fun saveUser(user: User) {
        coroutineScope {
            launch(Dispatchers.IO + Job()) {
                val userDb = user.toDb()
                dao.insertUser(userDb)
            }
        }
    }

    private suspend fun <T> executeRequest(block: suspend () -> T): Resource<T> {
        return try {
            Resource.Success(block.invoke())
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage.orEmpty())
        }
    }

    private suspend fun getLangId() = getSavedLangId().let {
        if (it == -1) null else it
    }
}