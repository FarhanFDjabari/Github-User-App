package com.example.githubuserssubmission.core.di

import android.content.Context
import androidx.room.Room
import com.example.githubuserssubmission.core.data.source.local.UserDao
import com.example.githubuserssubmission.core.data.source.local.UserRoomDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context) : UserRoomDatabase =
        Room.databaseBuilder(
            context.applicationContext,
            UserRoomDatabase::class.java,
            "user_db"
        ).fallbackToDestructiveMigration().build()

    @Provides
    fun provideUserDao(database: UserRoomDatabase) : UserDao = database.userDao()
}