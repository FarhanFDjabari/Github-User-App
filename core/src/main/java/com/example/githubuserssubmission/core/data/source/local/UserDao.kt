package com.example.githubuserssubmission.core.data.source.local

import androidx.room.*
import com.example.githubuserssubmission.core.data.source.local.entity.GithubUserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: List<GithubUserEntity>)

    @Update
    suspend fun update(user: GithubUserEntity)

    @Delete
    fun delete(user: GithubUserEntity)

    @Query("select exists(select * from githubuser where login = :username and is_favorite = 1)")
    fun isUserFavorite(username: String): Boolean

    @Query("select * from githubuser where login = :username")
    fun searchUser(username: String): Flow<List<GithubUserEntity>>

    @Query("select * from githubuser")
    fun getAllUsers(): Flow<List<GithubUserEntity>>

    @Query("select * from githubuser where is_favorite = 1")
    fun getFavoriteUsers(): Flow<List<GithubUserEntity>>

    @Query("select * from githubuser where login = :username")
    fun getUserDetail(username: String): Flow<GithubUserEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: GithubUserEntity)

    @Query("delete from githubuser where is_favorite = 0")
    fun deleteAll()
}