package ua.com.honchar.arstudy.domain.repository

import ua.com.honchar.arstudy.domain.repository.model.Category
import ua.com.honchar.arstudy.domain.repository.model.Model
import ua.com.honchar.arstudy.domain.repository.model.Module
import ua.com.honchar.arstudy.util.Resource

interface ArStudyRepository {

    suspend fun getCategories(langId: Int?): Resource<List<Category>>
    suspend fun getModelsByCategory(categoryId: Int?, langId: Int?): Resource<List<Model>>
    suspend fun getModulesByCategory(categoryId: Int, langId: Int?): Resource<List<Module>>
}