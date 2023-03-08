package com.example.githubuserssubmission.core.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.asLiveData
import com.example.githubuserssubmission.core.data.DataDummy
import com.example.githubuserssubmission.core.data.FakeLocalDataSource
import com.example.githubuserssubmission.core.data.FakeRemoteDataSource
import com.example.githubuserssubmission.core.data.source.Resource
import com.example.githubuserssubmission.core.data.source.local.LocalDataSource
import com.example.githubuserssubmission.core.data.source.remote.RemoteDataSource
import com.example.githubuserssubmission.core.data.source.remote.response.ApiResponse
import com.example.githubuserssubmission.core.domain.repository.UserRepository
import com.example.githubuserssubmission.core.helper.DataMapper
import com.example.githubuserssubmission.core.helper.MainDispatcherRule
import com.example.githubuserssubmission.core.helper.getOrAwaitValue
import com.example.githubuserssubmission.core.helper.observeForTesting
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class GithubRepositoryTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var localDataSource: LocalDataSource
    private lateinit var remoteDataSource: RemoteDataSource
    private lateinit var repository: UserRepository

    @Before
    fun setUp() {
        localDataSource = FakeLocalDataSource()
        remoteDataSource = FakeRemoteDataSource()
        repository = UserRepositoryImpl(remoteDataSource, localDataSource)
    }

    @Test
    fun `when search user should success and return data`() = runTest {
        val expectedData = flowOf(
            ApiResponse.Success(DataDummy.generateDummyUserListResponse())
        )

        val actualData = repository.searchUsers("test").asLiveData()

        actualData.observeForTesting {
            Assert.assertNotNull(actualData.value)
            Assert.assertTrue(actualData.value is Resource.Success)
            Assert.assertEquals(expectedData.first().data.size, actualData.value?.data?.size)
            Assert.assertEquals(expectedData.first().data.first().id, actualData.value?.data?.first()?.id)
        }
    }

    @Test
    fun `when get user if user detail already in db should success and return local data`() = runTest {
        val expectedData = Resource.Success(
            DataDummy.generateDummyUser()
        )

        localDataSource.insertUser(DataDummy.generateDummyEntity())

        val actualData = repository.getUserDetail("testuser").asLiveData()

        actualData.observeForTesting {
            Assert.assertNotNull(actualData.value)
            Assert.assertTrue(actualData.value is Resource.Success)
            Assert.assertEquals(expectedData.data?.id, actualData.value?.data?.id)
        }
    }

    @Test
    fun `when get user if user detail not in db should success and return remote data`() = runTest {
        val expectedRemoteData = flowOf(
            ApiResponse.Success(DataDummy.generateDummyUserResponse())
        )

        val actualData = repository.getUserDetail("testuser").asLiveData()

        actualData.observeForTesting {
            Assert.assertNotNull(actualData.value)
            Assert.assertTrue(actualData.value is Resource.Success)
            Assert.assertEquals(expectedRemoteData.first().data.id, actualData.value?.data?.id)
        }
    }

    @Test
    fun `when get favorite users should success and return data`() = runTest {
        val expectedData = flowOf(
            mutableListOf(DataDummy.generateDummyUser())
        )

        localDataSource.updateFavoriteUser(DataDummy.generateDummyEntity(), true)

        val actualData = repository.getFavoriteUsers().asLiveData()

        actualData.observeForTesting {
            Assert.assertNotNull(actualData.value)
            Assert.assertEquals(expectedData.first().size, actualData.value?.size)
        }
    }

    @Test
    fun `when favorite user should exist in favorite list`() = runTest {
        val sampleUser = DataDummy.generateDummyUserListEntity()[0]
        localDataSource.updateFavoriteUser(sampleUser, true)

        val actualData = repository.getFavoriteUsers().asLiveData()
        actualData.observeForTesting {
            Assert.assertTrue(actualData.value?.contains(DataMapper.mapEntityToDomain(sampleUser)) == true)
            Assert.assertTrue(sampleUser.isFavorite == true)
        }
    }

    @Test
    fun `when favorite user removed should not exist in favorite list`() = runTest {
        val sampleUser = DataDummy.generateDummyUserListEntity()[0]
        localDataSource.updateFavoriteUser(sampleUser, false)

        val actualData = repository.getFavoriteUsers().asLiveData()
        actualData.observeForTesting {
            Assert.assertFalse(actualData.value?.contains(DataMapper.mapEntityToDomain(sampleUser)) == true)
            Assert.assertTrue(sampleUser.isFavorite == false)
        }
    }

    @Test
    fun `when theme change, isDarkMode should change as well`() = runTest {
        localDataSource.saveThemeSetting(true)
        val actualData = repository.getThemeSetting().asLiveData().getOrAwaitValue()
        Assert.assertNotNull(actualData)
        Assert.assertTrue(actualData)
    }
}