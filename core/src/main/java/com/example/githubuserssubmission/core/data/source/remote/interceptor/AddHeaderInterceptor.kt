package com.example.githubuserssubmission.core.data.source.remote.interceptor

import com.example.githubuserssubmission.core.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class AddHeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
            .addHeader("Authorization", "token ${BuildConfig.GITHUB_API_KEY}")
        return chain.proceed(builder.build())
    }
}