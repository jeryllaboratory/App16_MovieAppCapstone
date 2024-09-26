package com.jeryl.app16_movieappcapstone.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.jeryl.app16_movieappcapstone.core.domain.usecase.MovieUseCase


/**
 * Created by Jery I D Lenas on 13/09/2024.
 * Contact : jerylenas@gmail.com
 */

class FavoriteViewModel(movieUsecase: MovieUseCase) : ViewModel() {
    val favoriteMovie = movieUsecase.getFavoriteMovie().asLiveData()

}
