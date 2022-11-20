package com.ddr1.storyapp.ui.new_story

import com.ddr1.storyapp.DataDummy
import com.ddr1.storyapp.MainDispatcherRule
import com.ddr1.storyapp.data.repository.NewStoryRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertSame
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
class NewStoryViewModelTest {

    @get:Rule
    var mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var newStoryRepository: NewStoryRepository
    private lateinit var newStoryViewModel: NewStoryViewModel

    private val dummyImage = DataDummy.generateDummyImageFile()
    private val dummyDescription = DataDummy.generateDummyDescriptionRequestBody()
    private val dummyNewStoryResponse = DataDummy.generateDummyNewStoryResponse()

    @Before
    fun setup() {
        newStoryViewModel = NewStoryViewModel(newStoryRepository)
    }

    @Test
    fun `Upload file successfully`() = runTest {
        val expectedResponse = flow {
            emit(Result.success(dummyNewStoryResponse))
        }

        `when`(
            newStoryRepository.uploadStory(
                dummyImage,
                dummyDescription
            )
        ).thenReturn(expectedResponse)

        newStoryViewModel.uploadStory(
            dummyImage,
            dummyDescription
        ).collect { result ->

            result.onSuccess { actualResponse ->
                assertNotNull(actualResponse)
                assertSame(dummyNewStoryResponse, actualResponse)
            }
        }

        Mockito.verify(newStoryRepository).uploadStory(
            dummyImage,
            dummyDescription
        )
    }
}