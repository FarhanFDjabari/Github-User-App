package com.example.githubuserssubmission.ui.features.detail.viewModel

import androidx.lifecycle.*
import com.example.githubuserssubmission.core.domain.model.GithubUser
import com.example.githubuserssubmission.core.domain.usecase.GithubUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GithubUserDetailViewModel @Inject constructor(
    private val githubUserUseCase: GithubUserUseCase,
) : ViewModel() {

    fun getThemeSettings(): LiveData<Boolean> {
        return githubUserUseCase.getThemeSetting().asLiveData()
    }

    fun setThemeSetting(isDarkMode: Boolean) {
        viewModelScope.launch {
            githubUserUseCase.saveThemeSetting(isDarkMode)
        }
    }

    fun editFavoriteState(isFavorite: Boolean, user: GithubUser) {
        viewModelScope.launch {
            githubUserUseCase.setFavoriteUser(user, !isFavorite)
        }
    }

    fun getUserDetail(username: String) = githubUserUseCase.getUserDetail(username).asLiveData()

    fun getUserFollowing(username: String) = githubUserUseCase.getUserFollowing(username).asLiveData()

    fun getUserFollowers(username: String) = githubUserUseCase.getUserFollowers(username).asLiveData()
}