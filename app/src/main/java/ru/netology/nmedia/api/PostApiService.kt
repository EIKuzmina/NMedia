package ru.netology.nmedia.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import ru.netology.nmedia.BuildConfig
import ru.netology.nmedia.handler.Post

private const val BASE_URL = "${BuildConfig.BASE_URL}/api/slow/"
interface PostApiService {
    @GET("posts")
    fun getAll(): Call<List<Post>>
    @POST("posts/{id}/likes")
    fun likeById(@Path("id") id: Int): Call<Post>
    @DELETE("posts/{id}/likes")
    fun disLikeById(@Path("id") id: Int): Call<Post>
    @DELETE("posts/{id}")
    fun removeById(@Path("id") id: Int): Call<Unit>
    @POST("posts")
    fun save(@Body post: Post): Call<Post>
}
val logger = HttpLoggingInterceptor().apply {
    if (BuildConfig.DEBUG) {
        level = HttpLoggingInterceptor.Level.BODY
    }
}
val client = OkHttpClient.Builder().addInterceptor(logger).build()

private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .client(client)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

object PostsApi {
    val retrofitService : PostApiService by lazy {
        retrofit.create(PostApiService::class.java)
    }
}