package com.ddr1.storyapp.ui.login

import androidx.lifecycle.ViewModel
import com.ddr1.storyapp.data.remote.response.LoginResponse
import com.ddr1.storyapp.data.repository.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository
) : ViewModel() {

    private val state = MutableStateFlow<LoginActivityState>(LoginActivityState.Init)
    val mState: StateFlow<LoginActivityState> get() = state

    private fun showToast(message: String) {
        state.value = LoginActivityState.ShowToast(message)
    }

    suspend fun login(email: String, password: String): Flow<Result<LoginResponse>> =
        loginRepository.login(email, password).catch {
            it.message?.let { it1 -> showToast(it1) }
        }

    fun getUserToken(): Flow<String?> = loginRepository.getUserToken()

    suspend fun saveAuthToken(token: String) {
        loginRepository.saveUserToken(token)
    }

}

sealed class LoginActivityState {
    object Init : LoginActivityState()
    data class ShowToast(val message: String) : LoginActivityState()
}