package com.jeryl.app16_movieappcapstone.core.domain.usecase

import com.jeryl.app16_movieappcapstone.core.data.ResourceState
import com.jeryl.app16_movieappcapstone.core.domain.model.MovieDetailModel
import com.jeryl.app16_movieappcapstone.core.domain.model.MovieModel
import com.jeryl.app16_movieappcapstone.core.domain.repository.IMovieRepository
import kotlinx.coroutines.flow.Flow


/**
 * Created by Jery I D Lenas on 17/09/2024.
 * Contact : jerylenas@gmail.com
 */

class MovieInteractor(private val movieRepository: IMovieRepository) : MovieUseCase {
    override fun getAllMovie(): Flow<ResourceState<List<MovieModel>>> {
        return movieRepository.getAllMovie()
    }

    override fun getFavoriteMovie(): Flow<List<MovieModel>> {
        return movieRepository.getFavoriteMovie()
    }

    override fun setFavoriteMovie(tourism: MovieModel, state: Boolean) {
        return movieRepository.setFavoriteMovie(tourism, state)
    }

    override fun getDetailMovie(id: Int): Flow<ResourceState<MovieDetailModel>> {
        return movieRepository.getDetailMovie(id)
    }

    override fun getFavoriteMovieById(id: Int): Flow<MovieModel> {
        return movieRepository.getFavoriteMovieById(id)
    }

    override fun searchMovie(query: String): Flow<ResourceState<List<MovieModel>>> {
        return movieRepository.searchMovie(query)
    }
}