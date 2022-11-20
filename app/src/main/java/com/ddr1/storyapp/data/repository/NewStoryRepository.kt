package com.ddr1.storyapp.data.repository

import com.ddr1.storyapp.data.remote.api.ApiService
import com.ddr1.storyapp.data.remote.response.NewStoryResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class NewStoryRepository @Inject constructor(
    private val apiService: ApiService,
) {

    suspend fun uploadStory(
        file: MultipartBody.Part,
        description: RequestBody
    ): Flow<Result<NewStoryResponse>> = flow {
        try {
            val response = apiService.uploadStory(file, description)
            emit(Result.success(response))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.failure(e))
        }
    }.flowOn(Dispatchers.IO)
}