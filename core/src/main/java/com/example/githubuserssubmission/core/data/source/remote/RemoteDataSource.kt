package com.example.githubuserssubmission.core.data.source.remote

import android.util.Log
import com.example.githubuserssubmission.core.data.source.remote.response.ApiResponse
import com.example.githubuserssubmission.core.data.source.remote.response.GithubUserResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Singleton

interface RemoteDataSource {
    suspend fun searchUserAsync(keyword: String) : Flow<ApiResponse<List<GithubUserResponse>>>
    suspend fun getUserDetail(username: String): Flow<ApiResponse<GithubUserResponse>>
    suspend fun getUserFollowers(username: String) : Flow<ApiResponse<List<GithubUserResponse>>>
    suspend fun getUserFollowing(username: String) : Flow<ApiResponse<List<GithubUserResponse>>>
}

@Singleton
class RemoteDataSourceImpl constructor(private val apiServices: ApiServices) : RemoteDataSource {
    override suspend fun searchUserAsync(keyword: String) : Flow<ApiResponse<List<GithubUserResponse>>> {
        return flow {
            try {
                val response = apiServices.searchUserAsync(keyword)
                if (!response.isSuccessful) {
                    emit(ApiResponse.Error(response.message()))
                }
                val dataArray = response.body()?.items
                if (dataArray?.isNotEmpty() == true) {
                    emit(ApiResponse.Success(dataArray))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
                Log.e("RemoteDataSource", "searchUserAsync: $e")
            }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun getUserDetail(username: String): Flow<ApiResponse<GithubUserResponse>> {
        return flow {
            try {
                val response = apiServices.getUserDetail(username)
                if (!response.isSuccessful) {
                    emit(ApiResponse.Error(response.message()))
                }
                val dataArray = response.body()
                if (dataArray != null) {
                    emit(ApiResponse.Success(dataArray))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
                Log.e("RemoteDataSource", "getUserDetail: $e")
            }
        }.flowOn(Dispatchers.Default)
    }

    override suspend fun getUserFollowers(username: String) : Flow<ApiResponse<List<GithubUserResponse>>> {
        return flow {
            try {
                val response = apiServices.getUserFollowers(username)
                if (!response.isSuccessful) {
                    emit(ApiResponse.Error(response.message()))
                }
                val dataArray = response.body()
                if (dataArray?.isNotEmpty() == true) {
                    emit(ApiResponse.Success(dataArray))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
                Log.e("RemoteDataSource", "getUserFollowers: $e")
            }
        }.flowOn(Dispatchers.Default)
    }

    override suspend fun getUserFollowing(username: String) : Flow<ApiResponse<List<GithubUserResponse>>> {
        return flow {
            try {
                val response = apiServices.getUserFollowing(username)
                if (!response.isSuccessful) {
                    emit(ApiResponse.Error(response.message()))
                }
                val dataArray = response.body()
                if (dataArray?.isNotEmpty() == true) {
                    emit(ApiResponse.Success(dataArray))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
                Log.e("RemoteDataSource", "getUserFollowing: $e")
            }
        }.flowOn(Dispatchers.Default)
    }
}
