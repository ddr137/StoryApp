package com.ddr1.storyapp.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.*
import com.ddr1.storyapp.data.StoryRemoteMediator
import com.ddr1.storyapp.data.local.UserPreference
import com.ddr1.storyapp.data.local.database.StoryDatabase
import com.ddr1.storyapp.data.local.entity.StoriesEntity
import com.ddr1.storyapp.data.remote.api.ApiService
import com.ddr1.storyapp.data.remote.response.StoriesResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val storyDatabase: StoryDatabase,
    private val userPreference: UserPreference,
    private val apiService: ApiService
) {

    fun getUserToken(): Flow<String?> = userPreference.getUserToken()

    suspend fun saveUserToken(token: String) {
        userPreference.saveUserToken(token)
    }

    @OptIn(ExperimentalPagingApi::class)
    fun getAllStories(): LiveData<PagingData<StoriesEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
            ),
            remoteMediator = StoryRemoteMediator(
                storyDatabase,
                apiService
            ),
            pagingSourceFactory = {
                storyDatabase.storyDao().getAllStories()
            }
        ).liveData
    }

    suspend fun getAllStoriesWithLocation(): Flow<Result<StoriesResponse>> = flow {
        val response = apiService.getAllStories(size = 20, location = 1)
        try {
            emit(Result.success(response))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.failure(e))
        }
    }.flowOn(Dispatchers.IO)
}