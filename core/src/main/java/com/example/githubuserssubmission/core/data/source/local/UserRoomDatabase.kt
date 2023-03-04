package com.example.githubuserssubmission.core.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.githubuserssubmission.core.data.source.local.entity.GithubUserEntity

@Database(entities = [GithubUserEntity::class], version = 1, exportSchema = false)
abstract class UserRoomDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}