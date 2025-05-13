package com.beka.jaqsyadet.di

import android.content.Context
import androidx.work.WorkManager
import com.beka.jaqsyadet.data.AppDatabase
import com.beka.jaqsyadet.data.dao.HabitDao
import com.beka.jaqsyadet.data.dao.HabitCheckDao
import com.beka.jaqsyadet.data.repository.HabitRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getDatabase(context)
    }

    @Provides
    @Singleton
    fun provideHabitDao(database: AppDatabase): HabitDao {
        return database.habitDao()
    }

    @Provides
    @Singleton
    fun provideHabitCheckDao(database: AppDatabase): HabitCheckDao {
        return database.habitCheckDao()
    }

    @Provides
    @Singleton
    fun provideHabitRepository(
        habitDao: HabitDao,
        habitCheckDao: HabitCheckDao
    ): HabitRepository {
        return HabitRepository(habitDao, habitCheckDao)
    }

    @Provides
    @Singleton
    fun provideWorkManager(@ApplicationContext context: Context): WorkManager {
        return WorkManager.getInstance(context)
    }
} 