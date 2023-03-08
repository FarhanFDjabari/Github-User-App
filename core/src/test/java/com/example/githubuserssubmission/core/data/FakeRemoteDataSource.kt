package com.example.githubuserssubmission.core.data

import com.example.githubuserssubmission.core.data.source.remote.RemoteDataSource
import com.example.githubuserssubmission.core.data.source.remote.response.ApiResponse
import com.example.githubuserssubmission.core.data.source.remote.response.GithubUserResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeRemoteDataSource : RemoteDataSource {
    override suspend fun searchUserAsync(keyword: String): Flow<ApiResponse<List<GithubUserResponse>>> {
        return flow {
            emit(ApiResponse.Success(DataDummy.generateDummyUserListResponse()))
        }
    }

    override suspend fun getUserDetail(username: String): Flow<ApiResponse<GithubUserResponse>> {
        return flow {
            emit(ApiResponse.Success(DataDummy.generateDummyUserResponse()))
        }
    }

    override suspend fun getUserFollowers(username: String): Flow<ApiResponse<List<GithubUserResponse>>> {
        return flow {
            emit(ApiResponse.Success(DataDummy.generateDummyUserListResponse()))
        }
    }

    override suspend fun getUserFollowing(username: String): Flow<ApiResponse<List<GithubUserResponse>>> {
        return flow {
            emit(ApiResponse.Success(DataDummy.generateDummyUserListResponse()))
        }
    }
}