package ua.com.honchar.arstudy.data.repository

import ua.com.honchar.arstudy.data.mappers.toDomain
import ua.com.honchar.arstudy.data.network.ArStudyApi
import ua.com.honchar.arstudy.data.network.model.request.ModelsRequest
import ua.com.honchar.arstudy.data.network.module.request.ModulesRequest
import ua.com.honchar.arstudy.domain.repository.ArStudyRepository
import ua.com.honchar.arstudy.domain.repository.model.Category
import ua.com.honchar.arstudy.domain.repository.model.Model
import ua.com.honchar.arstudy.domain.repository.model.Module
import ua.com.honchar.arstudy.util.Resource
import javax.inject.Inject

class ArStudyRepositoryImpl @Inject constructor(
    private val api: ArStudyApi
) : ArStudyRepository {

    override suspend fun getCategories(langId: Int?): Resource<List<Category>> {
        return executeRequest {
            val response = api.getCategories(langId)
            response.map { it.toDomain() }
        }
    }

    override suspend fun getModelsByCategory(categoryId: Int?, langId: Int?): Resource<List<Model>> {
        return executeRequest {
            val requestData = ModelsRequest(categoryId, langId)
            val response = api.getModelsByCategory(requestData)
            response.map { it.toDomain() }
        }
    }

    override suspend fun getModulesByCategory(
        categoryId: Int,
        langId: Int?
    ): Resource<List<Module>> {
        return executeRequest {
            val requestData = ModulesRequest(categoryId, langId)
            val response = api.getModulesByCategory(requestData)
            response.map { it.toDomain() }
        }
    }

    private suspend fun <T> executeRequest(block: suspend () -> T): Resource<T> {
        return try {
            Resource.Success(block.invoke())
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage.orEmpty())
        }
    }
}