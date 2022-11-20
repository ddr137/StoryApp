package com.ddr1.storyapp.di

import android.content.Context
import androidx.room.Room
import com.ddr1.storyapp.data.local.dao.RemoteKeysDao
import com.ddr1.storyapp.data.local.dao.StoryDao
import com.ddr1.storyapp.data.local.database.StoryDatabase
import com.ddr1.storyapp.utils.AppConstants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): StoryDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context,
                StoryDatabase::class.java,
                AppConstants.DATABASE_FILE_NAME
            )
                .fallbackToDestructiveMigration()
                .build()
                .also { INSTANCE = it }
            instance
        }
    }

    @Provides
    fun provideStoryDao(database: StoryDatabase): StoryDao =
        database.storyDao()

    @Provides
    fun provideRemoteKeysDao(database: StoryDatabase): RemoteKeysDao =
        database.remoteKeysDao()

    companion object {
        @Volatile
        private var INSTANCE: StoryDatabase? = null
    }

}