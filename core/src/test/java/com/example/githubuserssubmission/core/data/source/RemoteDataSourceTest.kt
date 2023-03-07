package com.example.githubuserssubmission.core.data.source

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.asLiveData
import com.example.githubuserssubmission.core.data.DataDummy
import com.example.githubuserssubmission.core.data.source.remote.ApiServices
import com.example.githubuserssubmission.core.data.source.remote.RemoteDataSource
import com.example.githubuserssubmission.core.data.source.remote.RemoteDataSourceImpl
import com.example.githubuserssubmission.core.data.source.remote.response.ApiResponse
import com.example.githubuserssubmission.core.data.source.remote.response.ApiResponses
import com.example.githubuserssubmission.core.data.source.remote.response.GithubUserResponse
import com.example.githubuserssubmission.core.helper.MainDispatcherRule
import com.example.githubuserssubmission.core.helper.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class RemoteDataSourceTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var apiServicesMock: ApiServices
    private lateinit var dataSource: RemoteDataSource

    @Before
    fun setUp() {
        dataSource = RemoteDataSourceImpl(apiServicesMock)
    }

    @Test
    fun `when search user should success and return list of users`() = runTest {
        val expectedData = Response.success(
            ApiResponses(
                items = DataDummy.generateDummyUserListResponse()
            )
        )

        `when`(apiServicesMock.searchUserAsync("test")).thenReturn(expectedData)

        val actualData = dataSource.searchUserAsync("test").asLiveData().getOrAwaitValue()

        Mockito.verify(apiServicesMock).searchUserAsync("test")
        Assert.assertNotNull(actualData)
        Assert.assertTrue(actualData is ApiResponse.Success)
        Assert.assertEquals(expectedData.body()?.items?.size, (actualData as ApiResponse.Success).data.size)
    }

    @Test
    fun `when get user detail should success and return user data`() = runTest {
        val expectedData = Response.success(
            DataDummy.generateDummyUserResponse()
        )

        `when`(apiServicesMock.getUserDetail("test")).thenReturn(expectedData)

        val actualData = dataSource.getUserDetail("test").asLiveData().getOrAwaitValue()

        Mockito.verify(apiServicesMock).getUserDetail("test")
        Assert.assertNotNull(actualData)
        Assert.assertTrue(actualData is ApiResponse.Success)
        Assert.assertEquals(expectedData.body()?.id, (actualData as ApiResponse.Success).data.id)
    }

    @Test
    fun `when get user follower should success and return list of followers`() = runTest {
        val expectedData = Response.success(
            DataDummy.generateDummyUserListResponse()
        )

        `when`(apiServicesMock.getUserFollowers("test")).thenReturn(expectedData)

        val actualData = dataSource.getUserFollowers("test").asLiveData().getOrAwaitValue()

        Mockito.verify(apiServicesMock).getUserFollowers("test")
        Assert.assertNotNull(actualData)
        Assert.assertTrue(actualData is ApiResponse.Success)
        Assert.assertEquals(expectedData.body()?.size, (actualData as ApiResponse.Success).data.size)
    }

    @Test
    fun `when get user following should success and return list of following`() = runTest {
        val expectedData = Response.success(
            DataDummy.generateDummyUserListResponse()
        )

        `when`(apiServicesMock.getUserFollowing("test")).thenReturn(expectedData)

        val actualData = dataSource.getUserFollowing("test").asLiveData().getOrAwaitValue()

        Mockito.verify(apiServicesMock).getUserFollowing("test")
        Assert.assertNotNull(actualData)
        Assert.assertTrue(actualData is ApiResponse.Success)
        Assert.assertEquals(expectedData.body()?.size, (actualData as ApiResponse.Success).data.size)
    }

    @Test
    fun `when network error, should return error`() = runTest {
        val searchUserExpectedData = Response.error<ApiResponses<GithubUserResponse>>(
            404, "Site not found".toResponseBody()
        )
        val userDetailExpectedData = Response.error<GithubUserResponse>(
            404, "Site not found".toResponseBody()
        )
        val userFollowerExpectedData = Response.error<List<GithubUserResponse>>(
            404, "Site not found".toResponseBody()
        )
        val userFollowingExpectedData = Response.error<List<GithubUserResponse>>(
            404, "Site not found".toResponseBody()
        )

        `when`(apiServicesMock.searchUserAsync("test")).thenReturn(searchUserExpectedData)
        `when`(apiServicesMock.getUserDetail("test")).thenReturn(userDetailExpectedData)
        `when`(apiServicesMock.getUserFollowers("test")).thenReturn(userFollowerExpectedData)
        `when`(apiServicesMock.getUserFollowing("test")).thenReturn(userFollowingExpectedData)
        
        val searchUserActualData = dataSource.searchUserAsync("test").asLiveData().getOrAwaitValue() 
        val userDetailActualData = dataSource.getUserDetail("test").asLiveData().getOrAwaitValue()
        val userFollowerActualData = dataSource.getUserFollowers("test").asLiveData().getOrAwaitValue()
        val userFollowingActualData = dataSource.getUserFollowing("test").asLiveData().getOrAwaitValue()
        
        Mockito.verify(apiServicesMock).searchUserAsync("test")
        Mockito.verify(apiServicesMock).getUserDetail("test")
        Mockito.verify(apiServicesMock).getUserFollowers("test")
        Mockito.verify(apiServicesMock).getUserFollowing("test")

        Assert.assertNotNull(searchUserActualData)
        Assert.assertNotNull(userDetailActualData)
        Assert.assertNotNull(userFollowerActualData)
        Assert.assertNotNull(userFollowingActualData)

        Assert.assertTrue(searchUserActualData is ApiResponse.Error)
        Assert.assertTrue(userDetailActualData is ApiResponse.Error)
        Assert.assertTrue(userFollowerActualData is ApiResponse.Error)
        Assert.assertTrue(userFollowingActualData is ApiResponse.Error)
    }
}