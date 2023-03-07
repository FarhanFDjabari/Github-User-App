package com.example.githubuserssubmission.ui.features.home.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.asLiveData
import com.example.githubuserssubmission.core.data.source.Resource
import com.example.githubuserssubmission.core.domain.model.GithubUser
import com.example.githubuserssubmission.core.domain.usecase.GithubUserUseCase
import com.example.githubuserssubmission.data.DataDummy
import com.example.githubuserssubmission.helper.MainDispatcherRule
import com.example.githubuserssubmission.helper.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.first
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
@OptIn(FlowPreview::class)
@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var githubUseCaseMock: GithubUserUseCase
    private lateinit var viewModel: MainViewModel

    @Before
    fun setUp() {
        viewModel = MainViewModel(githubUseCaseMock)
    }

    @Test
    fun `when search user should not null and return data`() = runTest {
        val expectedData = flowOf<Resource<List<GithubUser>>>(
            Resource.Success(DataDummy.generateDummyUserList())
        )

        `when`(githubUseCaseMock.searchUsers("test")).thenReturn(expectedData)

        val actualData = viewModel.searchUser("test").asLiveData().getOrAwaitValue()

        Mockito.verify(githubUseCaseMock).searchUsers("test")
        assertNotNull(actualData)
        assertTrue(actualData is Resource.Success)
        assertEquals(expectedData.first().data?.size, (actualData as Resource.Success).data?.size)
    }

    @Test
    fun `when user not found, size should zero`() = runTest {
        val expectedData = flowOf<Resource<List<GithubUser>>>(
            Resource.Success(listOf())
        )

        `when`(githubUseCaseMock.searchUsers("unknown user")).thenReturn(expectedData)
        val actualData = viewModel.searchUser("unknown user").asLiveData().getOrAwaitValue()

        Mockito.verify(githubUseCaseMock).searchUsers("unknown user")
        assertNotNull(actualData)
        assertTrue(actualData is Resource.Success)
        assertEquals(0, (actualData as Resource.Success).data?.size)
    }

    @Test
    fun `when network error, should return error`() = runTest {
        val expectedData = flowOf<Resource<List<GithubUser>>>(
            Resource.Error("No network")
        )

        `when`(githubUseCaseMock.searchUsers("test")).thenReturn(expectedData)
        val actualData = viewModel.searchUser("test").asLiveData().getOrAwaitValue()

        Mockito.verify(githubUseCaseMock).searchUsers("test")
        assertNotNull(actualData)
        assertTrue(actualData is Resource.Error)
    }
}
