package com.example.githubuserssubmission.core.data.source.local

import com.example.githubuserssubmission.core.data.source.local.entity.GithubUserEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Singleton

interface LocalDataSource {
    fun getFavoriteUsers(): Flow<List<GithubUserEntity>>
    fun getUserDetail(username: String): Flow<GithubUserEntity?>
    suspend fun insertUser(user: GithubUserEntity)
    suspend fun updateFavoriteUser(user: GithubUserEntity, newState: Boolean)
    suspend fun saveThemeSetting(isDarkMode: Boolean)
    fun getThemeSetting(): Flow<Boolean>
}

@Singleton
class LocalDataSourceImpl constructor(
    private val userDao: UserDao,
    private val pref: SettingPreferences
) : LocalDataSource {

    override fun getFavoriteUsers(): Flow<List<GithubUserEntity>> = userDao.getFavoriteUsers()

    override fun getUserDetail(username: String): Flow<GithubUserEntity?> = userDao.getUserDetail(username)

    override suspend fun insertUser(user: GithubUserEntity) = userDao.insertUser(user)

    override suspend fun updateFavoriteUser(user: GithubUserEntity, newState: Boolean) {
        user.isFavorite = newState
        userDao.update(user)
    }

    override suspend fun saveThemeSetting(isDarkMode: Boolean) = pref.saveThemeSetting(isDarkMode)

    override fun getThemeSetting(): Flow<Boolean> = pref.getThemeSetting()
}