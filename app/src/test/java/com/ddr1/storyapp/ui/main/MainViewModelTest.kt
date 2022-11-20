package com.ddr1.storyapp.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.recyclerview.widget.ListUpdateCallback
import com.ddr1.storyapp.DataDummy
import com.ddr1.storyapp.MainDispatcherRule
import com.ddr1.storyapp.PagedTestDataSource
import com.ddr1.storyapp.data.local.entity.StoriesEntity
import com.ddr1.storyapp.data.repository.MainRepository
import com.ddr1.storyapp.getOrAwaitValue
import com.ddr1.storyapp.ui.location.MapsViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var mainRepository: MainRepository
    private lateinit var mainViewModel: MainViewModel

    private val dummyStories = DataDummy.generateDummyStoriesEntity()
    private val dummyToken = "token"

    @Before
    fun setup() {
        mainViewModel = MainViewModel(mainRepository)
    }

    @Test
    fun `When Get GetAllStories Should Not Null and Return Success`() = runTest {
        val data = PagedTestDataSource.snapshot(dummyStories)
        val stories = MutableLiveData<PagingData<StoriesEntity>>()
        stories.value = data
        `when`(mainRepository.getAllStories()).thenReturn(stories)
        val actualStories = mainViewModel.getAllStories().getOrAwaitValue()
        val differ = AsyncPagingDataDiffer(
            diffCallback = StoriesAdapter.DiffCallback,
            updateCallback = noopListUpdateCallback,
            mainDispatcher = mainDispatcherRule.testDispatcher,
            workerDispatcher = mainDispatcherRule.testDispatcher
        )
        differ.submitData(actualStories)
        assertNotNull(differ.snapshot())
        assertEquals(dummyStories.size, differ.snapshot().size)
    }

    @Test
    fun `Save token successfully`() = runTest {
        mainViewModel.saveAuthToken(dummyToken)
        Mockito.verify(mainRepository).saveUserToken(dummyToken)
    }

    @Test
    fun `Get token successfully`() = runTest {
        val expectedToken = flowOf(dummyToken)
        `when`(mainRepository.getUserToken()).thenReturn(expectedToken)
        mainViewModel.getUserToken().collect {
            assertNotNull(it)
            assertEquals(dummyToken, it)
        }
        Mockito.verify(mainRepository).getUserToken()
    }

    private val noopListUpdateCallback = object : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {}
        override fun onRemoved(position: Int, count: Int) {}
        override fun onMoved(fromPosition: Int, toPosition: Int) {}
        override fun onChanged(position: Int, count: Int, payload: Any?) {}
    }

}