package com.ddr1.storyapp.data.remote.response

data class LoginResponse(
    val error: Boolean,
    val loginResult: LoginResult,
    val message: String
) {
    data class LoginResult(
        val name: String,
        val token: String,
        val userId: String
    )
}