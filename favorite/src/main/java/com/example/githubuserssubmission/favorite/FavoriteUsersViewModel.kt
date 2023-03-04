package com.example.githubuserssubmission.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.githubuserssubmission.core.domain.usecase.GithubUserUseCase
import com.example.githubuserssubmission.ui.features.setting.SettingPreferences
import kotlinx.coroutines.launch

class FavoriteUsersViewModel (
    githubUserUseCase: GithubUserUseCase
) : ViewModel() {

    val getFavoriteUsers = githubUserUseCase.getFavoriteUsers().asLiveData()

}