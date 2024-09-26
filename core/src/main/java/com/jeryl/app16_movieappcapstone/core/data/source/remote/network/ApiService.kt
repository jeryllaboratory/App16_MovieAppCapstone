package com.jeryl.app16_movieappcapstone.core.data.source.remote.network

import com.jeryl.app16_movieappcapstone.core.data.source.remote.response.MovieDetailResponse
import com.jeryl.app16_movieappcapstone.core.data.source.remote.response.MovieListPopularResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


/**
 * Created by Jery I D Lenas on 17/09/2024.
 * Contact : jerylenas@gmail.com
 */

interface ApiService {
    @GET("movie/popular")
    suspend fun getList(): MovieListPopularResponse

    @GET("movie/{movieId}")
   suspend fun detail(
        @Path("movieId") movieId: Int,
    ): MovieDetailResponse

    @GET("search/movie")
    suspend fun searchMovies(
        @Query("query") query: String,
        @Query("page") page: Int = 1,
        @Query("language") language: String = "en-US"
    ): MovieListPopularResponse
}