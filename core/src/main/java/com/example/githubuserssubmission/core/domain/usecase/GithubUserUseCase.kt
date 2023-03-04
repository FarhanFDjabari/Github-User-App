package com.example.githubuserssubmission.core.domain.usecase

import com.example.githubuserssubmission.core.data.source.Resource
import com.example.githubuserssubmission.core.domain.model.GithubUser
import kotlinx.coroutines.flow.Flow

interface GithubUserUseCase {
        fun searchUsers(keyword: String): Flow<Resource<List<GithubUser>>>
        fun getUserFollowing(username: String): Flow<Resource<List<GithubUser>>>
        fun getUserFollowers(username: String): Flow<Resource<List<GithubUser>>>
        fun getUserDetail(username: String): Flow<Resource<GithubUser?>>
        fun getFavoriteUsers(): Flow<List<GithubUser>>
        suspend fun setFavoriteUser(githubUser: GithubUser, state: Boolean)
}