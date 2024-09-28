package com.jeryl.app16_movieappcapstone.core.data.source.remote.network

import com.jeryl.app16_movieappcapstone.core.BuildConfig


/**
 * Created by Jery I D Lenas on 10/09/2024.
 * Contact : jerylenas@gmail.com
 */
object ApiConstant {
    val BASE_URL: String = if (BuildConfig.BASE_URL.isNotEmpty()) BuildConfig.BASE_URL else "https://default.api.com/"
    val ACCESS_TOKEN: String = if (BuildConfig.ACCESS_TOKEN.isNotEmpty()) BuildConfig.ACCESS_TOKEN else "default_access_token"
    val BASE_URL_IMAGE: String = if (BuildConfig.BASE_URL_IMAGE.isNotEmpty()) BuildConfig.BASE_URL_IMAGE else "https://default.image.com/"
}