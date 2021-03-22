package hr.mfllipovic.github.network

import hr.mfllipovic.github.entities.SearchResults
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.QueryMap

const val baseUrl = "https://api.github.com/"

interface GitHubNetwork {

    @GET("/search/repositories")
    suspend fun searchRepositories(@QueryMap queryMap: Map<String, String>): SearchResults
}