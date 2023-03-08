package com.example.githubuserssubmission.core.domain

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.asLiveData
import com.example.githubuserssubmission.core.data.DataDummy
import com.example.githubuserssubmission.core.data.source.Resource
import com.example.githubuserssubmission.core.domain.model.GithubUser
import com.example.githubuserssubmission.core.domain.repository.UserRepository
import com.example.githubuserssubmission.core.domain.usecase.GithubUserInteractor
import com.example.githubuserssubmission.core.domain.usecase.GithubUserUseCase
import com.example.githubuserssubmission.core.helper.MainDispatcherRule
import com.example.githubuserssubmission.core.helper.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class GithubUserUseCaseTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var repository: UserRepository
    private lateinit var useCase: GithubUserUseCase

    @Before
    fun setUp() {
        useCase = GithubUserInteractor(repository)
    }

    @Test
    fun `when search user should success and return data`() = runTest {
        val expectedData = flowOf(
            Resource.Success(DataDummy.generateDummyUserList())
        )

        `when`(repository.searchUsers("test")).thenReturn(expectedData)

        val actualData = useCase.searchUsers("test").asLiveData().getOrAwaitValue()

        verify(repository).searchUsers("test")
        Assert.assertNotNull(actualData.data)
        Assert.assertTrue(actualData is Resource.Success)
        Assert.assertEquals(expectedData.first().data?.size, actualData.data?.size)
    }

    @Test
    fun `when user following should success and return data`() = runTest {
        val expectedData = flowOf(
            Resource.Success(DataDummy.generateDummyUserList())
        )

        `when`(repository.getUserFollowing("test")).thenReturn(expectedData)

        val actualData = useCase.getUserFollowing("test").asLiveData().getOrAwaitValue()

        verify(repository).getUserFollowing("test")
        Assert.assertNotNull(actualData.data)
        Assert.assertTrue(actualData is Resource.Success)
        Assert.assertEquals(expectedData.first().data?.size, actualData.data?.size)
    }

    @Test
    fun `when user followers should success and return data`() = runTest {
        val expectedData = flowOf(
            Resource.Success(DataDummy.generateDummyUserList())
        )

        `when`(repository.getUserFollowers("test")).thenReturn(expectedData)

        val actualData = useCase.getUserFollowers("test").asLiveData().getOrAwaitValue()

        verify(repository).getUserFollowers("test")
        Assert.assertNotNull(actualData.data)
        Assert.assertTrue(actualData is Resource.Success)
        Assert.assertEquals(expectedData.first().data?.size, actualData.data?.size)
    }

    @Test
    fun `when user detail should success and return data`() = runTest {
        val expectedData : Flow<Resource<GithubUser?>> = flowOf(
            Resource.Success(DataDummy.generateDummyUser())
        )

        `when`(repository.getUserDetail("test")).thenReturn(expectedData)

        val actualData = useCase.getUserDetail("test").asLiveData().getOrAwaitValue()

        verify(repository).getUserDetail("test")
        Assert.assertNotNull(actualData.data)
        Assert.assertTrue(actualData is Resource.Success)
        Assert.assertEquals(expectedData.first().data?.id, actualData.data?.id)
    }

    @Test
    fun `when get favorite users should success and return data`() = runTest {
        val expectedData = flowOf(
            DataDummy.generateDummyUserList()
        )

        `when`(repository.getFavoriteUsers()).thenReturn(expectedData)

        val actualData = useCase.getFavoriteUsers().asLiveData().getOrAwaitValue()

        verify(repository).getFavoriteUsers()
        Assert.assertNotNull(actualData)
        Assert.assertEquals(expectedData.first().size, actualData.size)
    }
}