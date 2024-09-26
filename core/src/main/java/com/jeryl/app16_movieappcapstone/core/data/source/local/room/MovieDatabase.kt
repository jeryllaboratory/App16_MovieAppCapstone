package com.jeryl.app16_movieappcapstone.core.data.source.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.jeryl.app16_movieappcapstone.core.data.source.local.entity.MovieEntity


/**
 * Created by Jery I D Lenas on 12/09/2024.
 * Contact : jerylenas@gmail.com
 */

@Database(entities = [MovieEntity::class], version = 1, exportSchema = false)
abstract class MovieDatabase : RoomDatabase() {

    abstract fun movieDao(): MovieDao

}