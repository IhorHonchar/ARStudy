package ua.com.honchar.arstudy.domain.repository

import ua.com.honchar.arstudy.domain.repository.model.Category
import ua.com.honchar.arstudy.util.Resource

interface ArStudyRepository {

    suspend fun getCategories(): Resource<List<Category>>
}