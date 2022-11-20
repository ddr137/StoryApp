package com.ddr1.storyapp.ui.location

import androidx.lifecycle.ViewModel
import com.ddr1.storyapp.data.remote.response.StoriesResponse
import com.ddr1.storyapp.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class MapsViewModel
@Inject constructor(private val mainRepository: MainRepository) :
    ViewModel() {
    suspend fun getAllStoriesWithLocation(): Flow<Result<StoriesResponse>> =
        mainRepository.getAllStoriesWithLocation()
}