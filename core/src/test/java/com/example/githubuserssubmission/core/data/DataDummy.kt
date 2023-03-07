package com.example.githubuserssubmission.core.data

import com.example.githubuserssubmission.core.data.source.local.entity.GithubUserEntity
import com.example.githubuserssubmission.core.data.source.remote.response.GithubUserResponse

object DataDummy {

    fun generateDummyUserListResponse(): List<GithubUserResponse> {
        val users: MutableList<GithubUserResponse> = arrayListOf()
        repeat(10) {
            users.add(
                GithubUserResponse(
                    id = it,
                    name = "test user $it",
                    email = "test.user$it@email.com",
                    bio = "",
                    login = "testuser$it"
                )
            )
        }
        return users
    }

    fun generateDummyUserListEntity(): List<GithubUserEntity> {
        val users: MutableList<GithubUserEntity> = arrayListOf()
        repeat(10) {
            users.add(
                GithubUserEntity(
                    id = it,
                    name = "test user $it",
                    email = "test.user$it@email.com",
                    bio = "",
                    login = "testuser$it",
                    isFavorite = false
                )
            )
        }
        return users
    }

    fun generateDummyEntity(): GithubUserEntity {
        return GithubUserEntity(
            id = 1,
            name = "test user",
            email = "test.user@email.com",
            bio = "",
            login = "testuser",
            isFavorite = false,
        )
    }

    fun generateDummyUserResponse(): GithubUserResponse {
        return GithubUserResponse(
            id = 1,
            name = "test user",
            email = "test.user@email.com",
            bio = "",
            login = "testuser"
        )
    }
}