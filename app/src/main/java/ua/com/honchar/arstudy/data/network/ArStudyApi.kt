package ua.com.honchar.arstudy.data.network

import retrofit2.http.GET
import retrofit2.http.Path
import ua.com.honchar.arstudy.data.network.categories.response.CategoriesResponse
import ua.com.honchar.arstudy.data.network.model.response.ModelResponse

interface ArStudyApi {

    @GET("/categories")
    suspend fun getCategories(): List<CategoriesResponse>

    @GET("/models/{id}")
    suspend fun getModelsByCategory(
        @Path("id") id: Int
    ): List<ModelResponse>

    companion object {
        const val BASE_URL = "http://192.168.0.103:8080"
    }
}