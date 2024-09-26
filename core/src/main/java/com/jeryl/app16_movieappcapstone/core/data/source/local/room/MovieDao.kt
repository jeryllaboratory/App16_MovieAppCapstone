package com.jeryl.app16_movieappcapstone.core.data.source.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.jeryl.app16_movieappcapstone.core.data.source.local.entity.MovieEntity
import kotlinx.coroutines.flow.Flow


/**
 * Created by Jery I D Lenas on 12/09/2024.
 * Contact : jerylenas@gmail.com
 */
@Dao
interface MovieDao {

    @Query("SELECT * FROM movies")
    fun getAllMovie(): Flow<List<MovieEntity>>

    @Query("SELECT * FROM movies where isFavorite = 1")
    fun getFovoriteMovie(): Flow<List<MovieEntity>>

    @Query("SELECT * FROM movies where id = :id")
    fun getFavoriteMovieById(id: Int): Flow<MovieEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(tourism: List<MovieEntity>)

    @Update
    fun updateFavoriteMovie(tourism: MovieEntity)

}