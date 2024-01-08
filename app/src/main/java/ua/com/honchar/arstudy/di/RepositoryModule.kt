package ua.com.honchar.arstudy.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ua.com.honchar.arstudy.data.db.ArDao
import ua.com.honchar.arstudy.data.db.Database
import ua.com.honchar.arstudy.data.repository.ArStudyRepositoryImpl
import ua.com.honchar.arstudy.domain.repository.ArStudyRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindArStudyRepository(
        arStudyRepositoryImpl: ArStudyRepositoryImpl
    ): ArStudyRepository
}