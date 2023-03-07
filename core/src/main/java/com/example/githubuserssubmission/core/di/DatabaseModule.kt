package com.example.githubuserssubmission.core.di

import android.content.Context
import androidx.room.Room
import com.example.githubuserssubmission.core.data.source.local.*
import com.example.githubuserssubmission.core.data.source.remote.ApiServices
import com.example.githubuserssubmission.core.data.source.remote.RemoteDataSource
import com.example.githubuserssubmission.core.data.source.remote.RemoteDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context) : UserRoomDatabase {
        val passphrase = SQLiteDatabase.getBytes("githubuserapp".toCharArray())
        val factory = SupportFactory(passphrase)

       return Room.databaseBuilder(
            context.applicationContext,
            UserRoomDatabase::class.java,
            "user_db"
        )
           .fallbackToDestructiveMigration()
           .openHelperFactory(factory)
           .build()
    }

    @Provides
    fun provideUserDao(database: UserRoomDatabase) : UserDao = database.userDao()

    @Singleton
    @Provides
    fun provideLocalDataSource(dao: UserDao, pref: SettingPreferences) : LocalDataSource =
        LocalDataSourceImpl(dao, pref)

    @Singleton
    @Provides
    fun provideRemoteDataSource(api: ApiServices) : RemoteDataSource =
        RemoteDataSourceImpl(api)
}