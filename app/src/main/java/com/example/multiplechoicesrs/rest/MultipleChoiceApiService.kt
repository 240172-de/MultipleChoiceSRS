package com.example.multiplechoicesrs.rest

import com.example.multiplechoicesrs.model.DecksJson
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.http.GET
import java.util.concurrent.TimeUnit

//To run:
//Add IP to network_security_config.xml
//Set IP here
//Start server via php artisan serve --host IP --port 8000
private const val IP_ADDRESS = "192.168.91.31"
private const val PORT = "8000"
private const val BASE_URL = "http://$IP_ADDRESS:$PORT"


private val interceptor = HttpLoggingInterceptor().apply {
    this.level = HttpLoggingInterceptor.Level.BODY
}

private val client = OkHttpClient.Builder().apply {
    this.addInterceptor(interceptor)
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(20,TimeUnit.SECONDS)
        .writeTimeout(25,TimeUnit.SECONDS)

}.build()

private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .client(client)
    .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
    .build()

interface MultipleChoiceApiService {
    @GET("api/decks/")
    suspend fun getDecks(): DecksJson
}

object MultipleChoiceApi {
    val retrofitService : MultipleChoiceApiService by lazy {
        retrofit.create(MultipleChoiceApiService::class.java)
    }
}

