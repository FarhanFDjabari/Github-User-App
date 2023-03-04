package com.example.githubuserssubmission.di

import com.example.githubuserssubmission.core.domain.usecase.GithubUserInteractor
import com.example.githubuserssubmission.core.domain.usecase.GithubUserUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun provideUserUseCase(userInteractor: GithubUserInteractor) : GithubUserUseCase
}