package com.jeryl.app16_movieappcapstone.favorite.di

import com.jeryl.app16_movieappcapstone.favorite.FavoriteViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


/**
 * Created by Jery I D Lenas on 20/09/2024.
 * Contact : jerylenas@gmail.com
 */

val favoriteModule = module {
    viewModel { FavoriteViewModel(get()) }
}