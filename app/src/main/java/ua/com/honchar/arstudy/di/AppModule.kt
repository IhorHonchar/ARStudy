package ua.com.honchar.arstudy.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ua.com.honchar.arstudy.data.db.ArDao
import ua.com.honchar.arstudy.data.db.Database
import ua.com.honchar.arstudy.data.network.ArStudyApi
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideArStudyApi(): ArStudyApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(ArStudyApi.BASE_URL)
            .build()
            .create(ArStudyApi::class.java)
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): Database {
        return Room.databaseBuilder(
            context,
            Database::class.java,
            "ar-database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideArDao(database: Database): ArDao {
        return database.arDao()
    }
}