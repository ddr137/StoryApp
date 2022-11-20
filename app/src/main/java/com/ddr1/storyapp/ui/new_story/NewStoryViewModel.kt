package com.ddr1.storyapp.ui.new_story

import androidx.lifecycle.ViewModel
import com.ddr1.storyapp.data.remote.response.NewStoryResponse
import com.ddr1.storyapp.data.repository.NewStoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class NewStoryViewModel @Inject constructor(
    private val newStoryRepository: NewStoryRepository
) : ViewModel() {

    private val state = MutableStateFlow<NewStoryActivityState>(NewStoryActivityState.Init)
    val mState: StateFlow<NewStoryActivityState> get() = state

    private fun showToast(message: String) {
        state.value = NewStoryActivityState.ShowToast(message)
    }

    suspend fun uploadStory(
        file: MultipartBody.Part,
        description: RequestBody
    ): Flow<Result<NewStoryResponse>> = newStoryRepository.uploadStory(file, description).catch {
        it.message?.let { it1 -> showToast(it1) }
    }

}

sealed class NewStoryActivityState {
    object Init : NewStoryActivityState()
    data class ShowToast(val message: String) : NewStoryActivityState()
}