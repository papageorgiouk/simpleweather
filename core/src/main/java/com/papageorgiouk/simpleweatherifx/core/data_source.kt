package com.papageorgiouk.simpleweatherifx.core

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.flow.Flow
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.io.IOException

interface NetworkDataSource {

    suspend fun getForCity(city: String): Result<List<WeatherDataModel>?>

}

interface DatabaseDataSource<T> {

    fun getHistory(): Flow<List<T>>

    suspend fun getByPrimaryKey(key: Int): T

    suspend fun add(data: T)

    suspend fun remove(data: T)

}

class WeatherBit(apiKey: String) : NetworkDataSource {

    interface WeatherBitApi {

        @GET("/v2.0/current")
        suspend fun byCity(@Query("city") city: String, @Query("country") country: String = "US"): Response<WeatherbitResponse>

    }

    object WeatherBitService {

        private var key: String? = null

        fun withApiKey(key: String): WeatherBitService {
            this.key = key
            return this
        }

        //  deserialiser
        private val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        //  interceptor for appending api key in url params...
        val apiKeyInterceptor = Interceptor {
            val original = it.request()
            val newUrl = original.url().newBuilder().addQueryParameter("key", key).build()
            val newRequest = original.newBuilder().url(newUrl).build()
            it.proceed(newRequest)
        }

        //  ...add it in the client...
        val client = OkHttpClient.Builder()
            .addInterceptor(apiKeyInterceptor)
            .build()

        //  ...and use the client in retrofit, along with the deserialiser
        private val retrofit = Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(client)
            .baseUrl("https://api.weatherbit.io")
            .build()

        internal val service: WeatherBitApi
            get() {
                if (key == null) throw IllegalAccessError("WeatherBit api key has not been set")
                return retrofit.create(WeatherBitApi::class.java)
            }

    }

    private val service = WeatherBitService.withApiKey(apiKey).service

    override suspend fun getForCity(city: String): Result<List<WeatherDataModel>?> {
        val response = service.byCity(city)

        return if (response.isSuccessful) Result.success(response.body()?.data)
        else Result.failure(IOException("Error fetching data from network"))
    }

}