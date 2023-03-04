package com.example.githubuserssubmission.favorite

import android.content.Context
import com.example.githubuserssubmission.di.FavoriteModuleDependencies
import dagger.BindsInstance
import dagger.Component
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@Component(dependencies = [FavoriteModuleDependencies::class])
interface FavoriteComponent {

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    fun inject(activity: FavoriteUsersActivity)

    @Component.Builder
    interface Builder {
        fun context(@BindsInstance context: Context) : Builder
        fun appDependencies(favoriteModuleDependencies: FavoriteModuleDependencies): Builder
        fun build(): FavoriteComponent
    }
}