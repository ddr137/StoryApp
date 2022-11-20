package com.ddr1.storyapp.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ddr1.storyapp.DataDummy
import com.ddr1.storyapp.FakeApiService
import com.ddr1.storyapp.MainDispatcherRule
import com.ddr1.storyapp.data.remote.api.ApiService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class RegisterRepositoryTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var apiService: ApiService
    private lateinit var registerRepository: RegisterRepository
    private val dummyRegisterResponse = DataDummy.generateDummyRegisterResponse()
    private val dummyEmail = "emailTest@gmail.com"
    private val dummyPassword = "password"
    private val dummyName = "Dadan"

    @Before
    fun setUp() {
        apiService = FakeApiService()
        registerRepository = RegisterRepository(apiService)
    }

    @Test
    fun `Register successfully`() = runTest {
        val expectedResponse = dummyRegisterResponse

        registerRepository.register(dummyName, dummyEmail, dummyPassword).collect { result ->
            Assert.assertTrue(result.isSuccess)
            Assert.assertFalse(result.isFailure)

            result.onSuccess { actualResponse ->
                Assert.assertNotNull(actualResponse)
                assertEquals(expectedResponse, actualResponse)
            }
        }
    }
}