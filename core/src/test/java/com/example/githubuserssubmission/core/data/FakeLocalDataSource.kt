package com.example.githubuserssubmission.core.data

import com.example.githubuserssubmission.core.data.source.local.LocalDataSource
import com.example.githubuserssubmission.core.data.source.local.entity.GithubUserEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeLocalDataSource : LocalDataSource {
    private var userList: MutableList<GithubUserEntity> = mutableListOf()
    private var isDarkMode: Boolean = false

    override fun getFavoriteUsers(): Flow<List<GithubUserEntity>> {
        val favoriteList = userList.filter { it.isFavorite == true }
        return flow {
            emit(favoriteList)
        }
    }

    override fun getUserDetail(username: String): Flow<GithubUserEntity?> {
        val userDetail = userList.find { item -> item.login == username }
        return flow {
            emit(userDetail)
        }
    }

    override suspend fun insertUser(user: GithubUserEntity) {
        userList.add(user)
    }

    override suspend fun updateFavoriteUser(user: GithubUserEntity, newState: Boolean) {
        if (userList.contains(user)) {
            val newData = userList[userList.indexOf(user)].apply {
                isFavorite = newState
            }
            userList.remove(user)
            userList.add(newData)
        } else {
            val newData = user.apply {
                isFavorite = newState
            }
            userList.add(newData)
        }
    }

    override suspend fun saveThemeSetting(isDarkMode: Boolean) {
        this.isDarkMode = isDarkMode
    }

    override fun getThemeSetting(): Flow<Boolean> {
        return flow {
            emit(isDarkMode)
        }
    }
}