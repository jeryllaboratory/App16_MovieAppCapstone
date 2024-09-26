package com.jeryl.app16_movieappcapstone.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.jeryl.app16_movieappcapstone.core.domain.usecase.MovieUseCase


/**
 * Created by Jery I D Lenas on 19/09/2024.
 * Contact : jerylenas@gmail.com
 */

class SearchViewModel(private val movieUseCase: MovieUseCase): ViewModel() {
    fun searchMovie(query: String) = movieUseCase.searchMovie(query).asLiveData()
}