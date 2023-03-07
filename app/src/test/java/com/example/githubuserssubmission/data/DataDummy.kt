package com.example.githubuserssubmission.data

import com.example.githubuserssubmission.core.domain.model.GithubUser

object DataDummy {
    fun generateDummyUserList(): List<GithubUser> {
        val users: MutableList<GithubUser> = arrayListOf()
        repeat(10) {
            users.add(
                GithubUser(
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

    fun generateDummyUser(): GithubUser {
        return GithubUser(
            id = 1,
            name = "test user",
            email = "test.user@email.com",
            bio = "",
            login = "testuser"
        )
    }
}