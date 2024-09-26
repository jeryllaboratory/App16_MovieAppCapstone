package com.jeryl.app16_movieappcapstone.feature.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.jeryl.app16_movieappcapstone.core.domain.model.MovieModel
import com.jeryl.app16_movieappcapstone.core.domain.usecase.MovieUseCase


/**
 * Created by Jery I D Lenas on 13/09/2024.
 * Contact : jerylenas@gmail.com
 */

class DetailMovieViewModel(private val movieUsecase: MovieUseCase) : ViewModel() {
    fun getDetailMovie(movieId: Int) = movieUsecase.getDetailMovie(movieId).asLiveData()
    fun setFavoriteTourism(movie: MovieModel, newStatus: Boolean) = movieUsecase.setFavoriteMovie(movie, newStatus)
    fun getFavoriteById(movieId: Int) = movieUsecase.getFavoriteMovieById(movieId).asLiveData()
}
