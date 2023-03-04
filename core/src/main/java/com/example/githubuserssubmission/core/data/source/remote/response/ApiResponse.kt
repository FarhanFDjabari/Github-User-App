package com.example.githubuserssubmission.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class ApiResponses<T> (
    @field:SerializedName("items")
    val items: List<T> = listOf(),
)

sealed class ApiResponse<out R> {
    data class Success<out T>(val data: T) : ApiResponse<T>()
    data class Error(val errorMessage: String) : ApiResponse<Nothing>()
    object Empty : ApiResponse<Nothing>()
}