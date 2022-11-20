package com.ddr1.storyapp.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.ListUpdateCallback
import com.ddr1.storyapp.*
import com.ddr1.storyapp.data.local.UserPreference
import com.ddr1.storyapp.data.local.database.StoryDatabase
import com.ddr1.storyapp.data.remote.api.ApiService
import com.ddr1.storyapp.ui.main.StoriesAdapter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@ExperimentalPagingApi
@RunWith(MockitoJUnitRunner.Silent::class)
class MainRepositoryTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var userPreference: UserPreference

    private lateinit var mainRepository: MainRepository

    @Mock
    private lateinit var mainRepositoryMock: MainRepository

    @Mock
    private lateinit var apiService: ApiService

    @Mock
    private lateinit var appDatabase: StoryDatabase

    private val dummyStoriesResponse = DataDummy.generateDummyStoriesResponse()
    private val dummyStoriesEntity = DataDummy.generateDummyStoriesRepoEntity()
    private val dummyToken = "token"

    @Before
    fun setUp() {
        apiService = FakeApiService()
        mainRepository = MainRepository(appDatabase, userPreference, apiService)
    }

    @Test
    fun `When Get GetAllStories Should Not Null and Return Success`() = runTest {
        val data = PagedTestDataSource.snapshot(dummyStoriesEntity)
        val expectedResult = MutableLiveData(data)

        `when`(mainRepositoryMock.getAllStories()).thenReturn(expectedResult)

        val actualStories = mainRepositoryMock.getAllStories().getOrAwaitValue()
        val differ = AsyncPagingDataDiffer(
            diffCallback = StoriesAdapter.DiffCallback,
            updateCallback = noopListUpdateCallback,
            mainDispatcher = mainDispatcherRule.testDispatcher,
            workerDispatcher = mainDispatcherRule.testDispatcher
        )
        differ.submitData(actualStories)
        assertNotNull(differ.snapshot())
        assertEquals(
            dummyStoriesResponse.listStory.size,
            differ.snapshot().size
        )
    }

    @Test
    fun `When Get GetAllStories with location Should Not Null and Return Success`() = runTest {
        val expectedResult = flowOf(Result.success(dummyStoriesResponse))

        `when`(mainRepositoryMock.getAllStoriesWithLocation()).thenReturn(expectedResult)

        mainRepository.getAllStoriesWithLocation().collect { result ->
            assertTrue(result.isSuccess)
            assertFalse(result.isFailure)

            result.onSuccess { actualResponse ->
                assertNotNull(actualResponse)
                assertEquals(dummyStoriesResponse, actualResponse)
            }
        }
    }

    @Test
    fun `Save Token successfully`() = runTest {
        mainRepository.saveUserToken(dummyToken)
        Mockito.verify(userPreference).saveUserToken(dummyToken)
    }

    @Test
    fun `Get Token successfully`() = runTest {
        val expectedToken = flowOf(dummyToken)

        `when`(userPreference.getUserToken()).thenReturn(expectedToken)

        mainRepository.getUserToken().collect { actualToken ->
            assertNotNull(actualToken)
            assertEquals(dummyToken, actualToken)
        }

        Mockito.verify(userPreference).getUserToken()
    }

    private val noopListUpdateCallback = object : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {}
        override fun onRemoved(position: Int, count: Int) {}
        override fun onMoved(fromPosition: Int, toPosition: Int) {}
        override fun onChanged(position: Int, count: Int, payload: Any?) {}
    }

}