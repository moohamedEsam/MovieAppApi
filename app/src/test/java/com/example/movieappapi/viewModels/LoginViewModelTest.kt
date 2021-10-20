package com.example.movieappapi.viewModels

import com.example.movieappapi.dataModels.Credentials
import com.example.movieappapi.repository.Repository
import com.example.movieappapi.utils.Resource
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner.StrictStubs::class)
@ExperimentalCoroutinesApi
class LoginViewModelTest {

    @Mock
    private lateinit var repository: Repository

    private lateinit var loginViewModel: LoginViewModel


    private val dispatcher = TestCoroutineDispatcher()

    @Before
    fun setup() {
        loginViewModel = LoginViewModel(repository)
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `valid credentials with empty strings return false`() {
        val credentials = Credentials("", "")
        val result = loginViewModel.validCredentials(credentials)
        assertThat(result).isFalse()
    }

    @Test
    fun `valid credentials with blank strings return false`() {
        val credentials = Credentials("         ", "        ")
        val result = loginViewModel.validCredentials(credentials)
        assertThat(result).isFalse()
    }

    @Test
    fun `valid credentials with empty password return false`() {
        val credentials = Credentials("mohamed", "")
        val result = loginViewModel.validCredentials(credentials)
        assertThat(result).isFalse()
    }

    @Test
    fun `valid credentials with valid strings return false`() {
        val credentials = Credentials("mohamed", "123")
        val result = loginViewModel.validCredentials(credentials)
        assertThat(result).isTrue()
    }

    @Test
    fun `sign in not valid credentials`() = runBlockingTest {
        val credentials = Credentials("", "")
        loginViewModel.signIn(credentials)
        assertThat(loginViewModel.userState.value).isInstanceOf(Resource.Error::class.java)
    }

    @Test
    fun `sign in with valid credentials`() = runBlockingTest {
        val credentials = Credentials("mohamed", "123")
        `when`(repository.signIn(credentials)).thenReturn(true)
        loginViewModel.signIn(credentials)
        assertThat(loginViewModel.userState.value).isInstanceOf(Resource.Success::class.java)
    }

}