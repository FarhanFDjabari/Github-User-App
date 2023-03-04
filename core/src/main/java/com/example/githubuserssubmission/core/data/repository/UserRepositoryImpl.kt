package com.example.githubuserssubmission.core.data.repository

import com.example.githubuserssubmission.core.data.source.Resource
import com.example.githubuserssubmission.core.data.source.local.LocalDataSource
import com.example.githubuserssubmission.core.data.source.remote.RemoteDataSource
import com.example.githubuserssubmission.core.data.source.remote.response.ApiResponse
import com.example.githubuserssubmission.core.domain.model.GithubUser
import com.example.githubuserssubmission.core.domain.repository.UserRepository
import com.example.githubuserssubmission.core.helper.DataMapper
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
) : UserRepository {

    override fun searchUsers(keyword: String): Flow<Resource<List<GithubUser>>> = flow {
        emit(Resource.Loading())
        when (val response = remoteDataSource.searchUserAsync(keyword).first()) {
            is ApiResponse.Success -> {
                val result = DataMapper.mapResponsesToEntities(response.data)
                val data = DataMapper.mapEntitiesToDomain(result)
                emit(Resource.Success(data))
            }
            is ApiResponse.Empty -> {
                val result = DataMapper.mapResponsesToEntities(listOf())
                val data = DataMapper.mapEntitiesToDomain(result)
                emit(Resource.Success(data))
            }
            is ApiResponse.Error -> {
                emit(Resource.Error(response.errorMessage))
            }
        }
    }

    override fun getUserFollowing(username: String): Flow<Resource<List<GithubUser>>> = flow {
        emit(Resource.Loading())
        when (val response = remoteDataSource.getUserFollowing(username).first()) {
            is ApiResponse.Success -> {
                val result = DataMapper.mapResponsesToEntities(response.data)
                val data = DataMapper.mapEntitiesToDomain(result)
                emit(Resource.Success(data))
            }
            is ApiResponse.Empty -> {
                val result = DataMapper.mapResponsesToEntities(listOf())
                val data = DataMapper.mapEntitiesToDomain(result)
                emit(Resource.Success(data))
            }
            is ApiResponse.Error -> {
                emit(Resource.Error(response.errorMessage))
            }
        }
    }

    override fun getUserFollowers(username: String): Flow<Resource<List<GithubUser>>> = flow {
        emit(Resource.Loading())
        when (val response = remoteDataSource.getUserFollowers(username).first()) {
            is ApiResponse.Success -> {
                val result = DataMapper.mapResponsesToEntities(response.data)
                val data = DataMapper.mapEntitiesToDomain(result)
                emit(Resource.Success(data))
            }
            is ApiResponse.Empty -> {
                val result = DataMapper.mapResponsesToEntities(listOf())
                val data = DataMapper.mapEntitiesToDomain(result)
                emit(Resource.Success(data))
            }
            is ApiResponse.Error -> {
                emit(Resource.Error(response.errorMessage))
            }
        }
    }

    override fun getUserDetail(username: String): Flow<Resource<GithubUser?>> = flow {
        emit(Resource.Loading())
        if (localDataSource.getUserDetail(username).first() == null) {
            when (val response = remoteDataSource.getUserDetail(username).last()) {
                is ApiResponse.Success -> {
                    localDataSource.insertUser(DataMapper.mapResponseToEntities(response.data))
                    emitAll(
                        localDataSource.getUserDetail(username).map {
                            Resource.Success(it?.let {detail ->
                                DataMapper.mapEntityToDomain(detail)
                            })
                        }
                    )
                }
                is ApiResponse.Empty -> {
                    emitAll(
                        localDataSource.getUserDetail(username).map {
                            Resource.Success(it?.let {detail ->
                                DataMapper.mapEntityToDomain(detail)
                            })
                        }
                    )
                }
                is ApiResponse.Error -> {
                    emit(Resource.Error(response.errorMessage))
                }
            }
        } else {
            emitAll(
                localDataSource.getUserDetail(username).map {
                    Resource.Success(it?.let {detail ->
                        DataMapper.mapEntityToDomain(detail)
                    })
                }
            )
        }
    }

    override fun getFavoriteUsers(): Flow<List<GithubUser>> {
        return localDataSource.getFavoriteUsers().map {
            DataMapper.mapEntitiesToDomain(it)
        }
    }

    override suspend fun update(user: GithubUser, isFavoriteState: Boolean) {
        val userEntity = DataMapper.mapDomainToEntity(user)
        localDataSource.updateFavoriteUser(userEntity, isFavoriteState)
    }

    override fun getThemeSetting(): Flow<Boolean> {
        return localDataSource.getThemeSetting()
    }

    override suspend fun saveThemeSetting(isDarkMode: Boolean) {
        localDataSource.saveThemeSetting(isDarkMode)
    }
}