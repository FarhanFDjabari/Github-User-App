package com.example.githubuserssubmission.di

import com.example.githubuserssubmission.core.domain.usecase.GithubUserUseCase
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface FavoriteModuleDependencies {

    fun githubUserUseCase(): GithubUserUseCase
}