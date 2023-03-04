package com.example.githubuserssubmission.core.di

import com.example.githubuserssubmission.core.data.source.remote.ApiServices
import com.example.githubuserssubmission.core.data.source.remote.interceptor.AddHeaderInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun provideOkHttpClient() : OkHttpClient = OkHttpClient.Builder()
        .addNetworkInterceptor(AddHeaderInterceptor())
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .connectTimeout(120, TimeUnit.SECONDS)
        .readTimeout(120, TimeUnit.SECONDS)
        .build()

    @Provides
    fun provideRetrofitService(client: OkHttpClient) : ApiServices {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(ApiServices::class.java)
    }

}