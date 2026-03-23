package com.ejemplo.myapp.di

import android.content.Context
import com.ejemplo.myapp.data.local.AppDatabase
import com.ejemplo.myapp.data.local.dao.FitnessDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getDatabase(context)
    }

    @Provides
    fun provideFitnessDao(database: AppDatabase): FitnessDao {
        return database.fitnessDao()
    }
}
