package com.ddr1.storyapp.data.repository

import com.ddr1.storyapp.data.local.UserPreference
import com.ddr1.storyapp.data.remote.api.ApiService
import com.ddr1.storyapp.data.remote.response.LoginResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class LoginRepository @Inject constructor(
    private val apiService: ApiService,
    private val userPreference: UserPreference
) {

    fun getUserToken(): Flow<String?> = userPreference.getUserToken()

    suspend fun saveUserToken(token: String) {
        userPreference.saveUserToken(token)
    }

    suspend fun login(
        email: String,
        password: String
    ): Flow<Result<LoginResponse>> = flow {
        try {
            val response = apiService.login(email, password)
            emit(Result.success(response))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.failure(e))
        }
    }.flowOn(Dispatchers.IO)

}