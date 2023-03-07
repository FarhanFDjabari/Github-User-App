package com.example.githubuserssubmission.ui.features.detail.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.githubuserssubmission.core.data.source.Resource
import com.example.githubuserssubmission.core.domain.model.GithubUser
import com.example.githubuserssubmission.core.domain.usecase.GithubUserUseCase
import com.example.githubuserssubmission.data.DataDummy
import com.example.githubuserssubmission.helper.MainDispatcherRule
import com.example.githubuserssubmission.helper.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert
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
class GithubUserDetailViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var githubUseCaseMock: GithubUserUseCase
    private lateinit var viewModel: GithubUserDetailViewModel

    @Before
    fun setUp() {
        viewModel = GithubUserDetailViewModel(githubUseCaseMock)
    }

    @Test
    fun `when get user should not null and return data`() = runTest {
        val expectedData = flowOf<Resource<GithubUser?>>(
            Resource.Success(DataDummy.generateDummyUser())
        )

        `when`(githubUseCaseMock.getUserDetail("test")).thenReturn(expectedData)

        val actualData = viewModel.getUserDetail("test").getOrAwaitValue()

        Mockito.verify(githubUseCaseMock).getUserDetail("test")
        Assert.assertNotNull(actualData)
        Assert.assertTrue(actualData is Resource.Success)
        Assert.assertEquals(
            expectedData.first().data?.id,
            (actualData as Resource.Success).data?.id
        )
    }

    @Test
    fun `when get following user should not null and return data`() = runTest {
        val expectedData = flowOf<Resource<List<GithubUser>>>(
            Resource.Success(DataDummy.generateDummyUserList())
        )

        `when`(githubUseCaseMock.getUserFollowing("test")).thenReturn(expectedData)

        val actualData = viewModel.getUserFollowing("test").getOrAwaitValue()

        Mockito.verify(githubUseCaseMock).getUserFollowing("test")
        Assert.assertNotNull(actualData)
        Assert.assertTrue(actualData is Resource.Success)
    }

    @Test
    fun `when get follower user should not null and return data`() = runTest {
        val expectedData = flowOf<Resource<List<GithubUser>>>(
            Resource.Success(DataDummy.generateDummyUserList())
        )

        `when`(githubUseCaseMock.getUserFollowers("test")).thenReturn(expectedData)

        val actualData = viewModel.getUserFollowers("test").getOrAwaitValue()

        Mockito.verify(githubUseCaseMock).getUserFollowers("test")
        Assert.assertNotNull(actualData)
        Assert.assertTrue(actualData is Resource.Success)
    }

    @Test
    fun `when network error, should return error`() = runTest {
        val getUserDetailExpectedData = flowOf<Resource<GithubUser?>>(
            Resource.Error("No network")
        )
        val getFollowerFollowingUserExpectedData = flowOf<Resource<List<GithubUser>>>(
            Resource.Error("No network")
        )

        `when`(githubUseCaseMock.getUserDetail("test")).thenReturn(getUserDetailExpectedData)
        `when`(githubUseCaseMock.getUserFollowing("test")).thenReturn(getFollowerFollowingUserExpectedData)
        `when`(githubUseCaseMock.getUserFollowers("test")).thenReturn(getFollowerFollowingUserExpectedData)

        val actualDataGetUser = viewModel.getUserDetail("test").getOrAwaitValue()
        val actualDataGetUserFollowing = viewModel.getUserFollowing("test").getOrAwaitValue()
        val actualDataGetUserFollower = viewModel.getUserFollowers("test").getOrAwaitValue()

        Mockito.verify(githubUseCaseMock).getUserDetail("test")
        Mockito.verify(githubUseCaseMock).getUserFollowing("test")
        Mockito.verify(githubUseCaseMock).getUserFollowers("test")

        Assert.assertNotNull(actualDataGetUser)
        Assert.assertNotNull(actualDataGetUserFollowing)
        Assert.assertNotNull(actualDataGetUserFollower)

        Assert.assertTrue(actualDataGetUser is Resource.Error)
        Assert.assertTrue(actualDataGetUserFollowing is Resource.Error)
        Assert.assertTrue(actualDataGetUserFollower is Resource.Error)
    }
}