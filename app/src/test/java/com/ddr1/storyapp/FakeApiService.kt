package com.ddr1.storyapp

import com.ddr1.storyapp.data.remote.api.ApiService
import com.ddr1.storyapp.data.remote.response.LoginResponse
import com.ddr1.storyapp.data.remote.response.NewStoryResponse
import com.ddr1.storyapp.data.remote.response.RegisterResponse
import com.ddr1.storyapp.data.remote.response.StoriesResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody

class FakeApiService: ApiService {

    private val dummyRegisterResponse = DataDummy.generateDummyRegisterResponse()
    private val dummyLoginResponse = DataDummy.generateDummyLoginResponse()
    private val dummyNewStoryResponse = DataDummy.generateDummyNewStoryResponse()
    private val dummyStoriesResponse = DataDummy.generateDummyStoriesResponse()

    override suspend fun register(name: String, email: String, password: String): RegisterResponse {
        return dummyRegisterResponse
    }

    override suspend fun login(email: String, password: String): LoginResponse {
        return dummyLoginResponse
    }

    override suspend fun getAllStories(page: Int?, size: Int?, location: Int?): StoriesResponse {
        return dummyStoriesResponse
    }

    override suspend fun uploadStory(
        file: MultipartBody.Part,
        description: RequestBody
    ): NewStoryResponse {
       return dummyNewStoryResponse
    }
}