package com.ddr1.storyapp.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ddr1.storyapp.DataDummy
import com.ddr1.storyapp.FakeApiService
import com.ddr1.storyapp.MainDispatcherRule
import com.ddr1.storyapp.data.local.UserPreference
import com.ddr1.storyapp.data.remote.api.ApiService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Assert.assertEquals
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
class LoginRepositoryTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var userPreference: UserPreference

    private lateinit var apiService: ApiService
    private lateinit var loginRepository: LoginRepository

    private val dummyLoginResponse = DataDummy.generateDummyLoginResponse()
    private val dummyEmail = "emailTest@gmail.com"
    private val dummyPassword = "password"
    private val dummyToken = "token"

    @Before
    fun setUp() {
        apiService = FakeApiService()
        loginRepository = LoginRepository(apiService, userPreference)
    }

    @Test
    fun `Login successfully`() = runTest {
        val expectedResponse = dummyLoginResponse

        loginRepository.login(dummyEmail, dummyPassword).collect { result ->
            Assert.assertTrue(result.isSuccess)
            Assert.assertFalse(result.isFailure)

            result.onSuccess { actualResponse ->
                Assert.assertNotNull(actualResponse)
                assertEquals(expectedResponse, actualResponse)
            }
        }
    }

    @Test
    fun `Save Token successfully`() = runTest {
        loginRepository.saveUserToken(dummyToken)
        Mockito.verify(userPreference).saveUserToken(dummyToken)
    }

    @Test
    fun `Get Token successfully`() = runTest {
        val expectedToken = flowOf(dummyToken)

        `when`(userPreference.getUserToken()).thenReturn(expectedToken)

        loginRepository.getUserToken().collect { actualToken ->
            Assert.assertNotNull(actualToken)
            assertEquals(dummyToken, actualToken)
        }

        Mockito.verify(userPreference).getUserToken()
    }
}