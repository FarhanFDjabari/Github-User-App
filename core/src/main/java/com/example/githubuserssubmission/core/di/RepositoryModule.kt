package com.example.githubuserssubmission.core.di

import com.example.githubuserssubmission.core.data.repository.UserRepositoryImpl
import com.example.githubuserssubmission.core.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module(includes = [NetworkModule::class, DatabaseModule::class])
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun provideRepository(userRepositoryImpl: UserRepositoryImpl) : UserRepository

}