package com.jeryl.app16_movieappcapstone.core.data.source.remote

import android.util.Log
import com.jeryl.app16_movieappcapstone.core.data.source.remote.network.ApiResponse
import com.jeryl.app16_movieappcapstone.core.data.source.remote.network.ApiService
import com.jeryl.app16_movieappcapstone.core.data.source.remote.response.MovieDetailResponse
import com.jeryl.app16_movieappcapstone.core.data.source.remote.response.MovieItemResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class RemoteDataSource(private val apiService: ApiService) {

    fun getAllMovie(): Flow<ApiResponse<List<MovieItemResponse>>> {
        return flow {
            try {
                val response = apiService.getList()
                val dataArray = response.results
                if (dataArray.isNotEmpty()) {
                    emit(ApiResponse.Success(dataArray))
                } else {
                    emit(ApiResponse.Empty)
                }
            }catch (e: Exception){
                emit(ApiResponse.Error(e.toString()))
                Log.e("RemoteDataSource", e.toString())
            }
        }.flowOn(Dispatchers.IO)

    }

    fun searchMovie(query: String): Flow<ApiResponse<List<MovieItemResponse>>> {
        return flow {
            try {
                val response = apiService.searchMovies(query)
                val dataArray = response.results
                if (dataArray.isNotEmpty()) {
                    emit(ApiResponse.Success(dataArray))
                } else {
                    emit(ApiResponse.Empty)
                }
            }catch (e: Exception){
                emit(ApiResponse.Error(e.toString()))
                Log.e("RemoteDataSource", e.toString())
            }
        }.flowOn(Dispatchers.IO)

    }

    fun getDetailMovie(movieId: Int): Flow<ApiResponse<MovieDetailResponse>> {
        return flow {
            try {
                val response = apiService.detail(movieId)
                emit(ApiResponse.Success(response))
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
                Log.e("RemoteDataSource", e.toString())
            }
        }.flowOn(Dispatchers.IO)


    }

}

