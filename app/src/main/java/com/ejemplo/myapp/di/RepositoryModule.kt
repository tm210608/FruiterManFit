package com.ejemplo.myapp.di

import com.ejemplo.myapp.data.local.dao.FitnessDao
import com.ejemplo.myapp.data.repository.FitnessRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideFitnessRepository(fitnessDao: FitnessDao): FitnessRepository {
        return FitnessRepository(fitnessDao)
    }
}
