package com.jeryl.app16_movieappcapstone.core.domain.model

import android.os.Parcelable
import com.jeryl.app16_movieappcapstone.core.data.source.remote.network.ApiConstant.BASE_URL_IMAGE
import kotlinx.parcelize.Parcelize


/**
 * Created by Jery I D Lenas on 10/09/2024.
 * Contact : jerylenas@gmail.com
 */

@Parcelize
data class MovieDetailModel(
    val originalLanguage: String,
    val title: String,
    val backdropPath: String?,
    val genres: List<GenresItem>,
    val id: Int,
    val voteCount: Int,
    val overview: String,
    val originalTitle: String,
    val runtime: Int,
    val posterPath: String?,
    val productionCompanies: List<ProductionCompaniesItem>?,
    val releaseDate: String,
    val voteAverage: Double,
    val adult: Boolean,
    val homepage: String,
    val status: String
) : Parcelable

@Parcelize
data class ProductionCountriesItem(
    val iso31661: String,
    val name: String
) : Parcelable

@Parcelize
data class SpokenLanguagesItem(
    val name: String,
    val iso6391: String,
    val englishName: String
) : Parcelable

@Parcelize
data class ProductionCompaniesItem(
    val logoPath: String,
    val name: String,
    val id: Int,
    val originCountry: String
) : Parcelable {
    fun getLogoUrl(): String = BASE_URL_IMAGE + logoPath
}

@Parcelize
data class BelongsToCollection(
    val backdropPath: String,
    val name: String,
    val id: Int,
    val posterPath: String
) : Parcelable

@Parcelize
data class GenresItem(
    val name: String,
    val id: Int
) : Parcelable
