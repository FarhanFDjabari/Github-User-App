package com.example.githubuserssubmission.core.data.source.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Entity(tableName = "githubuser")
@Parcelize
data class GithubUserEntity (

        @ColumnInfo(name= "gists_url")
        val gistsUrl: String? = null,

        @ColumnInfo(name= "repos_url")
        val reposUrl: String? = null,

        @ColumnInfo(name= "following_url")
        val followingUrl: String? = null,

        @ColumnInfo(name= "twitter_username")
        val twitterUsername: String? = null,

        @ColumnInfo(name= "bio")
        val bio: String? = null,

        @ColumnInfo(name= "created_at")
        val createdAt: String? = null,

        @ColumnInfo(name= "login")
        val login: String? = null,

        @ColumnInfo(name= "type")
        val type: String? = null,

        @ColumnInfo(name= "blog")
        val blog: String? = null,

        @ColumnInfo(name= "subscriptions_url")
        val subscriptionsUrl: String? = null,

        @ColumnInfo(name= "updated_at")
        val updatedAt: String? = null,

        @ColumnInfo(name= "site_admin")
        val siteAdmin: Boolean? = null,

        @ColumnInfo(name= "company")
        val company: String? = null,

        @PrimaryKey
        @ColumnInfo(name= "id")
        val id: Int = 0,

        @ColumnInfo(name= "public_repos")
        val publicRepos: Int? = null,

        @ColumnInfo(name= "gravatar_id")
        val gravatarId: String? = null,

        @ColumnInfo(name= "email")
        val email: String? = null,

        @ColumnInfo(name= "organizations_url")
        val organizationsUrl: String? = null,

        @ColumnInfo(name= "hireable")
        val hireable: Boolean? = null,

        @ColumnInfo(name= "starred_url")
        val starredUrl: String? = null,

        @ColumnInfo(name= "followers_url")
        val followersUrl: String? = null,

        @ColumnInfo(name= "public_gists")
        val publicGists: Int? = null,

        @ColumnInfo(name= "url")
        @field:SerializedName("url")
        val url: String? = null,

        @ColumnInfo(name= "received_events_url")
        val receivedEventsUrl: String? = null,

        @ColumnInfo(name= "followers")
        val followers: Int? = null,

        @ColumnInfo(name= "avatar_url")
        val avatarUrl: String? = null,

        @ColumnInfo(name= "events_url")
        val eventsUrl: String? = null,

        @ColumnInfo(name= "html_url")
        val htmlUrl: String? = null,

        @ColumnInfo(name= "following")
        val following: Int? = null,

        @ColumnInfo(name= "name")
        val name: String? = null,

        @ColumnInfo(name= "location")
        val location: String? = null,

        @ColumnInfo(name= "node_id")
        val nodeId: String? = null,

        @ColumnInfo(name = "is_favorite")
        var isFavorite: Boolean? = false
) : Parcelable