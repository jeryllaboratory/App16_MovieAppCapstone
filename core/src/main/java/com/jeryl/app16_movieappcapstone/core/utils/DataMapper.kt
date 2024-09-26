package com.jeryl.app16_movieappcapstone.core.utils

import com.jeryl.app16_movieappcapstone.core.data.source.local.entity.MovieEntity
import com.jeryl.app16_movieappcapstone.core.data.source.remote.response.GenresItemResponse
import com.jeryl.app16_movieappcapstone.core.data.source.remote.response.MovieDetailResponse
import com.jeryl.app16_movieappcapstone.core.data.source.remote.response.MovieItemResponse
import com.jeryl.app16_movieappcapstone.core.data.source.remote.response.ProductionCompaniesItemResponse
import com.jeryl.app16_movieappcapstone.core.domain.model.GenresItem
import com.jeryl.app16_movieappcapstone.core.domain.model.MovieDetailModel
import com.jeryl.app16_movieappcapstone.core.domain.model.MovieModel
import com.jeryl.app16_movieappcapstone.core.domain.model.ProductionCompaniesItem

object DataMapper {
    fun mapResponsesToEntities(input: List<MovieItemResponse>): List<MovieEntity> {
        val movieList = ArrayList<MovieEntity>()
        input.map {
            val posterPath = it.posterPath ?: ""
            val backdropPath = it.backdropPath ?: ""
            val movie = MovieEntity(
                id = it.id,
                title = it.title,
                overview = it.overview,
                releaseDate = it.releaseDate,
                posterPath = posterPath,
                isFavorite = false,
                voteAverage = it.voteAverage,
                voteCount = it.voteCount,
                originalLanguage = it.originalLanguage,
                originalTitle = it.originalTitle,
                popularity = it.popularity,
                backdropPath = backdropPath,
                adult = it.adult,
            )
            movieList.add(movie)
        }
        return movieList
    }

    fun mapResponsesToDomain(input: List<MovieItemResponse>): List<MovieModel> {
        val movieList = ArrayList<MovieModel>()
        input.map {
        val posterPath = it.posterPath ?: ""
        val backdropPath = it.backdropPath ?: ""
            val movie = MovieModel(
                id = it.id,
                title = it.title,
                overview = it.overview,
                releaseDate = it.releaseDate,
                posterPath = posterPath,
                isFavorite = false,
                voteAverage = it.voteAverage,
                voteCount = it.voteCount,
                originalLanguage = it.originalLanguage,
                originalTitle = it.originalTitle,
                popularity = it.popularity,
                backdropPath = backdropPath,
                adult = it.adult,
            )
            movieList.add(movie)
        }
        return movieList
    }

    fun mapEntityToDomain(input: List<MovieEntity>): List<MovieModel> {
        return input.map {
            MovieModel(
                id = it.id,
                title = it.title,
                overview = it.overview,
                releaseDate = it.releaseDate,
                posterPath = it.posterPath,
                voteAverage = it.voteAverage,
                voteCount = it.voteCount,
                originalLanguage = it.originalLanguage,
                originalTitle = it.originalTitle,
                popularity = it.popularity,
                backdropPath = it.backdropPath,
                adult = it.adult,
                isFavorite = it.isFavorite
            )
        }
    }

    fun mapEntityToDomainById(input: MovieEntity?): MovieModel {
        return if (input != null) {
            MovieModel(
                id = input.id,
                title = input.title,
                overview = input.overview,
                releaseDate = input.releaseDate,
                posterPath = input.posterPath,
                voteAverage = input.voteAverage,
                voteCount = input.voteCount,
                originalLanguage = input.originalLanguage,
                originalTitle = input.originalTitle,
                popularity = input.popularity,
                backdropPath = input.backdropPath,
                adult = input.adult,
                isFavorite = input.isFavorite
            )
        } else {
            MovieModel(
                id = 0,
                title = "Unknown",
                overview = "No overview available",
                releaseDate = "Unknown",
                posterPath = "",
                voteAverage = 0.0,
                voteCount = 0,
                originalLanguage = "Unknown",
                originalTitle = "Unknown",
                popularity = 0.0,
                backdropPath = "",
                adult = false,
                isFavorite = false
            )
        }
    }

    fun mapDomainToEntity(input: MovieModel): MovieEntity {
        val posterPath = input.posterPath ?: ""
        val backdropPath = input.backdropPath ?: ""
       return MovieEntity(
            id = input.id,
            title = input.title,
            overview = input.overview,
            releaseDate = input.releaseDate,
            posterPath = posterPath,
            voteAverage = input.voteAverage,
            voteCount = input.voteCount,
            originalLanguage = input.originalLanguage,
            originalTitle = input.originalTitle,
            popularity = input.popularity,
            backdropPath = backdropPath,
            adult = input.adult
        )
    }


    fun mapDetailToDomain(movieDetailResponse: MovieDetailResponse): MovieDetailModel {
        val productionCompanies = movieDetailResponse.productionCompanies?.map { mapProductionCompanyToDomain(it) }
        val genres =  movieDetailResponse.genres.map { mapGenreItemToDomain(it) }

        val posterPath = movieDetailResponse.posterPath ?: ""
        val backdropPath = movieDetailResponse.backdropPath ?: ""

        return MovieDetailModel(
            originalLanguage = movieDetailResponse.originalLanguage,
            title = movieDetailResponse.title,
            backdropPath = backdropPath,
            genres = genres,
            id = movieDetailResponse.id,
            voteCount = movieDetailResponse.voteCount,
            overview = movieDetailResponse.overview,
            originalTitle = movieDetailResponse.originalTitle,
            runtime = movieDetailResponse.runtime,
            posterPath = posterPath,
            productionCompanies = productionCompanies,
            releaseDate = movieDetailResponse.releaseDate,
            voteAverage = movieDetailResponse.voteAverage,
            adult = movieDetailResponse.adult,
            homepage = movieDetailResponse.homepage,
            status = movieDetailResponse.status
        )
    }

    private fun mapGenreItemToDomain(genreItem: GenresItemResponse): GenresItem {
        return GenresItem(
            id = genreItem.id,
            name = genreItem.name
        )
    }

    private fun mapProductionCompanyToDomain(productionCompany: ProductionCompaniesItemResponse): ProductionCompaniesItem {
        val logoPath = productionCompany.logoPath ?: ""
        return ProductionCompaniesItem(
            logoPath = logoPath,
            name = productionCompany.name,
            id = productionCompany.id,
            originCountry = productionCompany.originCountry
        )
    }

}
