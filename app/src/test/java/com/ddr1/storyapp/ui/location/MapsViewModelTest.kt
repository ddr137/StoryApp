package com.ddr1.storyapp.ui.location

import com.ddr1.storyapp.DataDummy
import com.ddr1.storyapp.MainDispatcherRule
import com.ddr1.storyapp.data.repository.MainRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MapsViewModelTest {

    @get:Rule
    var mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var mainRepository: MainRepository
    private lateinit var mapsViewModel: MapsViewModel

    private val dummyStories = DataDummy.generateDummyStoriesResponse()

    @Before
    fun setup() {
        mapsViewModel = MapsViewModel(mainRepository)
    }

    @Test
    fun `When Get GetAllStories with location Should Not Null and Return Success`() = runTest {
        val expectedResponse = flowOf(Result.success(dummyStories))
        `when`(mainRepository.getAllStoriesWithLocation()).thenReturn(expectedResponse)
        mapsViewModel.getAllStoriesWithLocation().collect { actualResponse ->
            actualResponse.onSuccess { storiesResponse ->
                assertNotNull(storiesResponse)
                Assert.assertSame(storiesResponse, dummyStories)
            }
        }
        Mockito.verify(mainRepository).getAllStoriesWithLocation()
    }
}