package com.example.githubuserssubmission.core.domain.usecase

import com.example.githubuserssubmission.core.data.source.Resource
import com.example.githubuserssubmission.core.domain.model.GithubUser
import com.example.githubuserssubmission.core.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GithubUserInteractor @Inject constructor (private val userRepository: UserRepository) : GithubUserUseCase {
    override fun searchUsers(keyword: String): Flow<Resource<List<GithubUser>>> {
        return userRepository.searchUsers(keyword)
    }

    override fun getUserFollowing(username: String): Flow<Resource<List<GithubUser>>> {
        return userRepository.getUserFollowing(username)
    }

    override fun getUserFollowers(username: String): Flow<Resource<List<GithubUser>>> {
        return userRepository.getUserFollowers(username)
    }

    override fun getUserDetail(username: String): Flow<Resource<GithubUser?>> {
        return userRepository.getUserDetail(username)
    }

    override fun getFavoriteUsers(): Flow<List<GithubUser>> {
        return userRepository.getFavoriteUsers()
    }

    override suspend fun setFavoriteUser(githubUser: GithubUser, state: Boolean) {
        return userRepository.update(githubUser, state)
    }

    override fun getThemeSetting(): Flow<Boolean> {
        return userRepository.getThemeSetting()
    }

    override suspend fun saveThemeSetting(isDarkMode: Boolean) {
        return userRepository.saveThemeSetting(isDarkMode)
    }
}