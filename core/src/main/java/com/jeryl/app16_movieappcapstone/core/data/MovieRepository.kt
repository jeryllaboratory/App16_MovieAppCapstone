package com.jeryl.app16_movieappcapstone.core.data

import android.util.Log
import com.jeryl.app16_movieappcapstone.core.data.source.local.LocalDataSource
import com.jeryl.app16_movieappcapstone.core.data.source.remote.RemoteDataSource
import com.jeryl.app16_movieappcapstone.core.data.source.remote.network.ApiResponse
import com.jeryl.app16_movieappcapstone.core.data.source.remote.response.MovieItemResponse
import com.jeryl.app16_movieappcapstone.core.domain.model.MovieDetailModel
import com.jeryl.app16_movieappcapstone.core.domain.model.MovieModel
import com.jeryl.app16_movieappcapstone.core.domain.repository.IMovieRepository
import com.jeryl.app16_movieappcapstone.core.utils.AppExecutors
import com.jeryl.app16_movieappcapstone.core.utils.DataMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class MovieRepository(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val appExecutors: AppExecutors
): IMovieRepository {

    override fun getAllMovie(): Flow<ResourceState<List<MovieModel>>> =
        object : NetworkBoundResource<List<MovieModel>, List<MovieItemResponse>>() {
            override fun loadFromDB(): Flow<List<MovieModel>> {
                return localDataSource.getAllMovie().map { DataMapper.mapEntityToDomain(it) }
            }

            override fun shouldFetch(data: List<MovieModel>?): Boolean =
                data.isNullOrEmpty()

            override suspend fun createCall(): Flow<ApiResponse<List<MovieItemResponse>>> =
                remoteDataSource.getAllMovie()

            override suspend fun saveCallResult(data: List<MovieItemResponse>) {
                val movieEntityList = DataMapper.mapResponsesToEntities(data)
                localDataSource.insertTourism(movieEntityList)
            }
        }.asFlow()

    override fun getFavoriteMovie(): Flow<List<MovieModel>> {
        return localDataSource.getFavoriteMovie().map { DataMapper.mapEntityToDomain(it) }
    }

    override fun getFavoriteMovieById(id: Int): Flow<MovieModel> {
        return localDataSource.getFavoriteMovieById(id).map { DataMapper.mapEntityToDomainById(it) }
    }

    override fun searchMovie(query: String): Flow<ResourceState<List<MovieModel>>>  = flow {
        emit(ResourceState.Loading())
        when (val apiResponse = remoteDataSource.searchMovie(query).first()) {
            is ApiResponse.Success -> {
                val movieList = DataMapper.mapResponsesToDomain(apiResponse.data)
                emit(ResourceState.Success(movieList))
            }
             is ApiResponse.Empty -> {
                 emit(ResourceState.Error("Empty response"))
             }
            is ApiResponse.Error -> {
                emit(ResourceState.Error(apiResponse.errorMessage))
            }
        }
    }

    override fun setFavoriteMovie(movie: MovieModel, state: Boolean) {
        val movieEntity = DataMapper.mapDomainToEntity(movie)
        appExecutors.diskIO().execute { localDataSource.setFavoriteMovie(movieEntity, state) }
    }

    override fun getDetailMovie(id: Int): Flow<ResourceState<MovieDetailModel>> = flow {
        emit(ResourceState.Loading())
        when (val apiResponse = remoteDataSource.getDetailMovie(id).first()) {
            is ApiResponse.Success -> {
                Log.d("MovieRepository", "getDetailMovie: ${apiResponse.data}")
                val movieDetailModel = DataMapper.mapDetailToDomain(apiResponse.data)
                emit(ResourceState.Success(movieDetailModel))
            }
            is ApiResponse.Empty -> {
                emit(ResourceState.Error("Empty response"))
            }
            is ApiResponse.Error -> {
                Log.e("MovieRepository", "Error fetching movie details: ${apiResponse.errorMessage}")
                emit(ResourceState.Error(apiResponse.errorMessage))
            }
        }
    }
}

