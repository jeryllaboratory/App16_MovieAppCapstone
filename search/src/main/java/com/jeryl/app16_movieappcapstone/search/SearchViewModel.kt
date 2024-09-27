package com.jeryl.app16_movieappcapstone.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.jeryl.app16_movieappcapstone.core.domain.usecase.MovieUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest


/**
 * Created by Jery I D Lenas on 19/09/2024.
 * Contact : jerylenas@gmail.com
 */

class SearchViewModel(private val movieUseCase: MovieUseCase) : ViewModel() {
    private val queryChannel = MutableStateFlow("")

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val searchResult = queryChannel
        .debounce(300)
        .distinctUntilChanged()
        .filter { it.trim().isNotEmpty() }
        .flatMapLatest { query -> movieUseCase.searchMovie(query) }
        .asLiveData()

    fun searchMovie(query: String) {
        queryChannel.value = query
    }
}