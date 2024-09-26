package com.jeryl.app16_movieappcapstone.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.jeryl.app16_movieappcapstone.core.domain.usecase.MovieUseCase


/**
 * Created by Jery I D Lenas on 12/09/2024.
 * Contact : jerylenas@gmail.com
 */

class HomeViewModel(movieUseCase: MovieUseCase) : ViewModel() {
    val movie = movieUseCase.getAllMovie().asLiveData()
}