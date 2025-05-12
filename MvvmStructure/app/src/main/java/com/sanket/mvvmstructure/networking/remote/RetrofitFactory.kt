package com.sanket.mvvmstructure.networking.remote

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

object RetrofitFactory {
    @OptIn(ExperimentalSerializationApi::class)
    fun createRetrofitInstance(): Retrofit {
        val json = Json {
            ignoreUnknownKeys = true
            prettyPrint = true
            isLenient = true
        }
        return Retrofit.Builder()
            .baseUrl("https://i-film.co.uk/bapi/stops/")
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    }
}