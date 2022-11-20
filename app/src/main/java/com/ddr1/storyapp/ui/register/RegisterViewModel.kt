package com.ddr1.storyapp.ui.register

import androidx.lifecycle.ViewModel
import com.ddr1.storyapp.data.remote.response.RegisterResponse
import com.ddr1.storyapp.data.repository.RegisterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerRepository: RegisterRepository
) : ViewModel() {

    private val state = MutableStateFlow<RegisterActivityState>(RegisterActivityState.Init)
    val mState: StateFlow<RegisterActivityState> get() = state

    private fun showToast(message: String) {
        state.value = RegisterActivityState.ShowToast(message)
    }

    suspend fun register(name: String, email: String, password: String): Flow<Result<RegisterResponse>> =
        registerRepository.register(name, email, password).catch {
            it.message?.let { it1 -> showToast(it1) }
        }


}

sealed class RegisterActivityState {
    object Init : RegisterActivityState()
    data class ShowToast(val message: String) : RegisterActivityState()
}