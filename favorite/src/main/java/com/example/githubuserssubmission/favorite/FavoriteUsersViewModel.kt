package com.example.githubuserssubmission.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.githubuserssubmission.core.domain.usecase.GithubUserUseCase

class FavoriteUsersViewModel (
    githubUserUseCase: GithubUserUseCase
) : ViewModel() {

    val getFavoriteUsers = githubUserUseCase.getFavoriteUsers().asLiveData()

}