package com.example.custommoviedb.network

import com.example.custommoviedb.models.MovieResponse
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

const val API_KEY = "d64e8adb74a8687e84dae7ae26eb3628"
const val BASE_URL = "https://api.themoviedb.org/3/"
const val POSTER_BASE_URL = "https://image.tmdb.org/t/p/w342"

const val FIRST_PAGE = 1
const val POST_PER_PAGE = 20

interface TheMovieDbService {
    
    @GET("movie/popular")
    fun getPopularMovieAsync(@Query("page") page : Int) : Deferred<MovieResponse>
}

object MovieNetwork {
    
    private val requestInterceptor = Interceptor { chain ->
        
        val url = chain.request()
            .url
            .newBuilder()
            .addQueryParameter("api_key" , API_KEY)
            .build()
        
        val request = chain.request()
            .newBuilder()
            .url(url)
            .build()
        
        return@Interceptor chain.proceed(request)
    }
    
    // set time out for retrofit
    private val okHttpClient = OkHttpClient().newBuilder()
        .addInterceptor(requestInterceptor)
        .connectTimeout(40 , TimeUnit.SECONDS)
        .readTimeout(60 , TimeUnit.SECONDS)
        .writeTimeout(60 , TimeUnit.SECONDS)
        .build()
    
    // Configure retrofit to parse JSON and use coroutines
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
    
    val movieNetService : TheMovieDbService = retrofit.create(TheMovieDbService::class.java)
    
}