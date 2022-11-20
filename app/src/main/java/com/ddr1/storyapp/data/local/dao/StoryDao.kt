package com.ddr1.storyapp.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ddr1.storyapp.data.local.entity.StoriesEntity

@Dao
interface StoryDao {

    @Query("DELETE FROM story")
    fun deleteAll()

    @Query("SELECT * FROM story")
    fun getAllStories(): PagingSource<Int, StoriesEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStory(vararg story: StoriesEntity)
}