package ua.com.honchar.arstudy.data.network

import retrofit2.http.GET
import ua.com.honchar.arstudy.data.network.categories.response.CategoriesResponse

interface ArStudyApi {

    @GET("/categories")
    suspend fun getCategories(): List<CategoriesResponse>


    companion object {
        const val BASE_URL = "http://192.168.0.103:8080"
    }
}