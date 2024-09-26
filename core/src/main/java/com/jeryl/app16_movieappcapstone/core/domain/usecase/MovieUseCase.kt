package com.jeryl.app16_movieappcapstone.core.domain.usecase

import com.jeryl.app16_movieappcapstone.core.data.ResourceState
import com.jeryl.app16_movieappcapstone.core.domain.model.MovieDetailModel
import com.jeryl.app16_movieappcapstone.core.domain.model.MovieModel
import kotlinx.coroutines.flow.Flow


/**
 * Created by Jery I D Lenas on 17/09/2024.
 * Contact : jerylenas@gmail.com
 */

interface MovieUseCase {
    fun getAllMovie(): Flow<ResourceState<List<MovieModel>>>
    fun getFavoriteMovie(): Flow<List<MovieModel>>
    fun setFavoriteMovie(tourism: MovieModel, state: Boolean)
    fun getDetailMovie(id: Int): Flow<ResourceState<MovieDetailModel>>
    fun getFavoriteMovieById(id: Int): Flow<MovieModel>
    fun searchMovie(query: String): Flow<ResourceState<List<MovieModel>>>
}