package com.example.githubuserssubmission.core.data.source.remote

import com.example.githubuserssubmission.core.data.source.remote.response.ApiResponses
import com.example.githubuserssubmission.core.data.source.remote.response.GithubUserResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiServices {
    @GET("search/users")
    suspend fun searchUserAsync(
        @Query("q") keyword: String
    ) : Response<ApiResponses<GithubUserResponse>>

    @GET("users/{username}")
    suspend fun getUserDetail(
        @Path("username") username: String
    ) : Response<GithubUserResponse>

    @GET("users/{username}/followers")
    suspend fun getUserFollowers(
        @Path("username") username: String
    ) : Response<List<GithubUserResponse>>

    @GET("users/{username}/following")
    suspend fun getUserFollowing(
        @Path("username") username: String
    ) : Response<List<GithubUserResponse>>
}