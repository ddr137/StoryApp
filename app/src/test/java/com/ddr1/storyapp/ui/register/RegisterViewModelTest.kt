package com.ddr1.storyapp.ui.register

import com.ddr1.storyapp.DataDummy
import com.ddr1.storyapp.MainDispatcherRule
import com.ddr1.storyapp.data.repository.RegisterRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
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
class RegisterViewModelTest {

    @get:Rule
    var mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var registerRepository: RegisterRepository
    private lateinit var registerViewModel: RegisterViewModel

    private val dummyRegisterResponse = DataDummy.generateDummyRegisterResponse()
    private val dummyEmail = "emailTest@gmail.com"
    private val dummyPassword = "password"
    private val dummyName = "Dadan"

    @Before
    fun setUp() {
        registerViewModel = RegisterViewModel(registerRepository)
    }

    @Test
    fun `Register successfully`(): Unit = runTest {
        val expectedResponse = flowOf(Result.success(dummyRegisterResponse))

        `when`(registerRepository.register(dummyName, dummyEmail, dummyPassword)).thenReturn(
            expectedResponse
        )

        registerViewModel.register(dummyName, dummyEmail, dummyPassword).collect { result ->

            result.onSuccess { actualResponse ->
                assertNotNull(actualResponse)
                assertSame(dummyRegisterResponse, actualResponse)
            }
        }

        Mockito.verify(registerRepository).register(dummyName, dummyEmail, dummyPassword)
    }

}