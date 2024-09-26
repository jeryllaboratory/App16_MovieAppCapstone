package com.jeryl.app16_movieappcapstone.core.di

import androidx.room.Room
import com.jeryl.app16_movieappcapstone.core.BuildConfig
import com.jeryl.app16_movieappcapstone.core.data.source.local.LocalDataSource
import com.jeryl.app16_movieappcapstone.core.data.source.local.room.MovieDatabase
import com.jeryl.app16_movieappcapstone.core.data.source.remote.RemoteDataSource
import com.jeryl.app16_movieappcapstone.core.data.source.remote.network.ApiConstant
import com.jeryl.app16_movieappcapstone.core.data.source.remote.network.ApiService
import com.jeryl.app16_movieappcapstone.core.domain.repository.IMovieRepository
import com.jeryl.app16_movieappcapstone.core.utils.AppExecutors
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


/**
 * Created by Jery I D Lenas on 18/09/2024.
 * Contact : jerylenas@gmail.com
 */


val databaseModule = module {
    factory { get<MovieDatabase>().movieDao() }
    single {
        Room.databaseBuilder(
            androidContext(),
            MovieDatabase::class.java, "Movie.db"
        ).fallbackToDestructiveMigration().build()
    }
}

val authInterceptor = Interceptor { chain ->
    val req = chain.request()
    val requestHeaders = req.newBuilder()
        .addHeader("Authorization", "Bearer ${ApiConstant.ACCESS_TOKEN}")
        .build()
    chain.proceed(requestHeaders)
}

    val loggingInterceptor = if (BuildConfig.DEBUG) {
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    } else {
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
    }

    val networkModule = module {
        single {
            OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(authInterceptor)
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .build()
        }
        single {
            val retrofit = Retrofit.Builder()
                .baseUrl(ApiConstant.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(get())
                .build()
            retrofit.create(ApiService::class.java)
        }
    }

    val repositoryModule = module {
        single { LocalDataSource(get()) }
        single { RemoteDataSource(get()) }
        factory { AppExecutors() }
        single<IMovieRepository> { com.jeryl.app16_movieappcapstone.core.data.MovieRepository(get(), get(), get()) }
    }
