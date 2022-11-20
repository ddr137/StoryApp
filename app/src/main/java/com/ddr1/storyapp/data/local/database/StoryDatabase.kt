package com.ddr1.storyapp.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ddr1.storyapp.data.local.dao.RemoteKeysDao
import com.ddr1.storyapp.data.local.dao.StoryDao
import com.ddr1.storyapp.data.local.entity.RemoteKeys
import com.ddr1.storyapp.data.local.entity.StoriesEntity

@Database(
    entities = [StoriesEntity::class, RemoteKeys::class],
    version = 1,
    exportSchema = false
)
abstract class StoryDatabase : RoomDatabase() {
    abstract fun storyDao(): StoryDao
    abstract fun remoteKeysDao(): RemoteKeysDao
}