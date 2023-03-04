package com.example.githubuserssubmission.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.githubuserssubmission.core.domain.usecase.GithubUserUseCase
import javax.inject.Inject

class FavoriteViewModelFactory @Inject constructor(
    private val githubUserUseCase: GithubUserUseCase,
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        when {
            modelClass.isAssignableFrom(FavoriteUsersViewModel::class.java) -> {
                FavoriteUsersViewModel(githubUserUseCase) as T
            }
            else -> throw Throwable("Unknown ViewModel class: " + modelClass.name)
        }
}