package com.ddr1.storyapp.ui.login

import com.ddr1.storyapp.DataDummy
import com.ddr1.storyapp.MainDispatcherRule
import com.ddr1.storyapp.data.repository.LoginRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
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
@RunWith(MockitoJUnitRunner::class)
class LoginViewModelTest {

    @get:Rule
    var mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var loginRepository: LoginRepository
    private lateinit var loginViewModel: LoginViewModel

    private val dummyLoginResponse = DataDummy.generateDummyLoginResponse()
    private val dummyEmail = "emailTest@gmail.com"
    private val dummyPassword = "password"
    private val dummyToken = "token"

    @Before
    fun setUp() {
        loginViewModel = LoginViewModel(loginRepository)
    }

    @Test
    fun `Login successfully`() = runTest {
        val expectedResponse = flow {
            emit(Result.success(dummyLoginResponse))
        }

        `when`(loginRepository.login(dummyEmail, dummyPassword)).thenReturn(expectedResponse)

        loginViewModel.login(dummyEmail, dummyPassword).collect { result ->
            result.onSuccess { actualResponse ->
                assertNotNull(actualResponse)
                assertSame(dummyLoginResponse, actualResponse)
            }
        }

        Mockito.verify(loginRepository).login(dummyEmail, dummyPassword)
    }

    @Test
    fun `Save token successfully`() = runTest {
        loginViewModel.saveAuthToken(dummyToken)
        Mockito.verify(loginRepository).saveUserToken(dummyToken)
    }

    @Test
    fun `Get token successfully`() = runTest {
        val expectedToken = flowOf(dummyToken)
        `when`(loginRepository.getUserToken()).thenReturn(expectedToken)
        loginViewModel.getUserToken().collect {
            assertNotNull(it)
            assertEquals(dummyToken, it)
        }
        Mockito.verify(loginRepository).getUserToken()
    }
}