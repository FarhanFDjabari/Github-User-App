package com.example.githubuserssubmission.core.data.source.local

import com.example.githubuserssubmission.core.data.source.local.entity.GithubUserEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalDataSource @Inject constructor(
    private val userDao: UserDao,
    private val pref: SettingPreferences
)  {

    fun getFavoriteUsers(): Flow<List<GithubUserEntity>> = userDao.getFavoriteUsers()

    fun getUserDetail(username: String): Flow<GithubUserEntity?> = userDao.getUserDetail(username)

    suspend fun insertUser(user: GithubUserEntity) = userDao.insertUser(user)

    suspend fun updateFavoriteUser(user: GithubUserEntity, newState: Boolean) {
        user.isFavorite = newState
        userDao.update(user)
    }

    suspend fun saveThemeSetting(isDarkMode: Boolean) = pref.saveThemeSetting(isDarkMode)

    fun getThemeSetting(): Flow<Boolean> = pref.getThemeSetting()
}