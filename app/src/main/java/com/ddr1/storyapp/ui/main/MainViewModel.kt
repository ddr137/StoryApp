package com.ddr1.storyapp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ddr1.storyapp.data.local.entity.StoriesEntity
import com.ddr1.storyapp.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {

    fun getUserToken(): Flow<String?> = mainRepository.getUserToken()

    fun saveAuthToken(token: String) {
        viewModelScope.launch {
            mainRepository.saveUserToken(token)
        }
    }

    fun getAllStories(): LiveData<PagingData<StoriesEntity>> =
        mainRepository.getAllStories().cachedIn(viewModelScope)
}