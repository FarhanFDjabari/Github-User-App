package com.example.githubuserssubmission.core.domain.repository

import com.example.githubuserssubmission.core.data.source.Resource
import com.example.githubuserssubmission.core.domain.model.GithubUser
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun searchUsers(keyword: String): Flow<Resource<List<GithubUser>>>
    fun getUserFollowing(username: String): Flow<Resource<List<GithubUser>>>
    fun getUserFollowers(username: String): Flow<Resource<List<GithubUser>>>
    fun getUserDetail(username: String): Flow<Resource<GithubUser?>>
    fun getFavoriteUsers(): Flow<List<GithubUser>>
    suspend fun update(user: GithubUser, isFavoriteState: Boolean)
    fun getThemeSetting() : Flow<Boolean>
    suspend fun saveThemeSetting(isDarkMode: Boolean)
}