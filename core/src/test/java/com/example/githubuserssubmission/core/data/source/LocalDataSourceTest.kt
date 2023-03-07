package com.example.githubuserssubmission.core.data.source

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.asLiveData
import com.example.githubuserssubmission.core.data.DataDummy
import com.example.githubuserssubmission.core.data.source.local.LocalDataSource
import com.example.githubuserssubmission.core.data.source.local.LocalDataSourceImpl
import com.example.githubuserssubmission.core.data.source.local.SettingPreferences
import com.example.githubuserssubmission.core.data.source.local.UserDao
import com.example.githubuserssubmission.core.helper.MainDispatcherRule
import com.example.githubuserssubmission.core.helper.getOrAwaitValue
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
class LocalDataSourceTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var daoMock: UserDao
    @Mock
    private lateinit var prefsMock: SettingPreferences
    private lateinit var dataSource: LocalDataSource

    @Before
    fun setUp() {
        dataSource = LocalDataSourceImpl(daoMock, prefsMock)
    }

    @Test
    fun `when get favorite users, should not null and return data`() = runTest {
        val expectedData = flowOf(
            DataDummy.generateDummyUserListEntity()
        )

        `when`(daoMock.getFavoriteUsers()).thenReturn(expectedData)

        val actualData = dataSource.getFavoriteUsers().asLiveData().getOrAwaitValue()

        Mockito.verify(daoMock).getFavoriteUsers()
        Assert.assertNotNull(actualData)
        Assert.assertEquals(expectedData.first().size, actualData.size)
    }

    @Test
    fun `when get user detail, should not null and return data`() = runTest {
        val expectedData = flowOf(
            DataDummy.generateDummyEntity()
        )

        `when`(daoMock.getUserDetail("test")).thenReturn(expectedData)

        val actualData = dataSource.getUserDetail("test").asLiveData().getOrAwaitValue()

        Mockito.verify(daoMock).getUserDetail("test")
        Assert.assertNotNull(actualData)
        Assert.assertEquals(expectedData.first().id, actualData?.id)
    }
}