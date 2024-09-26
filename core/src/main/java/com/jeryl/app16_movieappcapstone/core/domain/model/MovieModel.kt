package com.jeryl.app16_movieappcapstone.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


/**
 * Created by Jery I D Lenas on 10/09/2024.
 * Contact : jerylenas@gmail.com
 */

@Parcelize
data class MovieModel(
    val id: Int,
    val overview: String,
    val originalLanguage: String,
    val originalTitle: String,
    val title: String,
    val posterPath: String?,
    val backdropPath: String?,
    val releaseDate: String,
    val popularity: Double,
    val voteAverage: Double,
    val adult: Boolean,
    val voteCount: Int,
    var isFavorite: Boolean
): Parcelable