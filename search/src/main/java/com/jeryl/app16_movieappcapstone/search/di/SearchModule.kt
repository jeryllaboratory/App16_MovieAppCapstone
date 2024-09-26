package com.jeryl.app16_movieappcapstone.search.di

import com.jeryl.app16_movieappcapstone.search.SearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


/**
 * Created by Jery I D Lenas on 19/09/2024.
 * Contact : jerylenas@gmail.com
 */

val searchModule = module {
    viewModel { SearchViewModel(get()) }
}