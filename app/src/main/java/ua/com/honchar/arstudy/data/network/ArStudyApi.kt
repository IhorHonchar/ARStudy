package ua.com.honchar.arstudy.data.network

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import ua.com.honchar.arstudy.data.network.categories.response.CategoriesResponse
import ua.com.honchar.arstudy.data.network.model.request.ModelsRequest
import ua.com.honchar.arstudy.data.network.model.response.ModelResponse
import ua.com.honchar.arstudy.data.network.module.request.ModulesRequest
import ua.com.honchar.arstudy.data.network.module.response.ModuleResponse

interface ArStudyApi {

    @GET("/categories/{langId}")
    suspend fun getCategories(@Path("langId") langId: Int?): List<CategoriesResponse>

    @POST("/models")
    suspend fun getModelsByCategory(@Body request: ModelsRequest): List<ModelResponse>

    @POST("/modules")
    suspend fun getModulesByCategory(@Body request: ModulesRequest): List<ModuleResponse>

    companion object {
        const val BASE_URL = "http://192.168.0.103:8080"
    }
}