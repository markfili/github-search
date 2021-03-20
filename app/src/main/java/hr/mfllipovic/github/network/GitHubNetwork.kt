package hr.mfllipovic.github.network

import hr.mfllipovic.github.entities.SearchResults
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.QueryMap

private const val baseUrl = "https://api.github.com/"

private val service: GitHubNetwork by lazy {
    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor())
        .build()

    val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    retrofit.create(GitHubNetwork::class.java)
}

fun getNetworkService() = service


interface GitHubNetwork {

    @GET("/search/repositories")
    suspend fun searchRepositories(@QueryMap queryMap: Map<String, String>): SearchResults
}