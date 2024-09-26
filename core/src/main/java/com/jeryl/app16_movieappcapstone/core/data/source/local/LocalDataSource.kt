package com.jeryl.app16_movieappcapstone.core.data.source.local

import com.jeryl.app16_movieappcapstone.core.data.source.local.entity.MovieEntity
import com.jeryl.app16_movieappcapstone.core.data.source.local.room.MovieDao
import kotlinx.coroutines.flow.Flow

class LocalDataSource(private val movieDao: MovieDao) {

    fun getAllMovie(): Flow<List<MovieEntity>> = movieDao.getAllMovie()

    fun getFavoriteMovie(): Flow<List<MovieEntity>> = movieDao.getFovoriteMovie()

    fun getFavoriteMovieById(id: Int): Flow<MovieEntity> = movieDao.getFavoriteMovieById(id)

    suspend fun insertTourism(movieList: List<MovieEntity>) = movieDao.insertMovie(movieList)

    fun setFavoriteMovie(tourism: MovieEntity, newState: Boolean) {
        tourism.isFavorite = newState
        movieDao.updateFavoriteMovie(tourism)
    }
}