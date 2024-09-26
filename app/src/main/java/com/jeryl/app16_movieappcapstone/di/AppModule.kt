package com.jeryl.app16_movieappcapstone.di

import com.jeryl.app16_movieappcapstone.core.domain.usecase.MovieInteractor
import com.jeryl.app16_movieappcapstone.core.domain.usecase.MovieUseCase
import com.jeryl.app16_movieappcapstone.feature.detail.DetailMovieViewModel
import com.jeryl.app16_movieappcapstone.feature.home.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
/**
 * Created by Jery I D Lenas on 18/09/2024.
 * Contact : jerylenas@gmail.com
 */

val useCaseModule = module {
    factory<MovieUseCase> { MovieInteractor(get()) }
}

val viewModelModule = module {
    viewModel { HomeViewModel(get()) }
    viewModel { DetailMovieViewModel(get()) }
}