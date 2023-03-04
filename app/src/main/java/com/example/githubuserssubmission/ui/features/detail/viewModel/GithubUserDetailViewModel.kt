package com.example.githubuserssubmission.ui.features.detail.viewModel

import androidx.lifecycle.*
import com.example.githubuserssubmission.core.domain.model.GithubUser
import com.example.githubuserssubmission.core.domain.usecase.GithubUserUseCase
import com.example.githubuserssubmission.ui.features.setting.SettingPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GithubUserDetailViewModel @Inject constructor(
    private val githubUserUseCase: GithubUserUseCase,
    private val pref: SettingPreferences
) : ViewModel() {

    companion object {
        private const val TAG = "GithubUserDetailViewModel"
    }

    fun getThemeSettings(): LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
    }

    fun setThemeSetting(isDarkMode: Boolean) {
        viewModelScope.launch {
            pref.saveThemeSetting(isDarkMode)
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