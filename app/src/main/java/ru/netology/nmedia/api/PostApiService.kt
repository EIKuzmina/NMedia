package ru.netology.nmedia.api

import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import ru.netology.nmedia.BuildConfig
import ru.netology.nmedia.handler.*

private const val BASE_URL = "${BuildConfig.BASE_URL}/api/slow/"
interface PostApiService {
    @GET("posts")
    suspend fun getAll(): Response<List<Post>>
    @GET("posts/{id}/newer")
    suspend fun getNewer(@Path("id") id: Int): Response<List<Post>>
    @POST("posts/{id}/likes")
    suspend fun likeById(@Path("id") id: Int): Response<Post>
    @DELETE("posts/{id}/likes")
    suspend fun disLikeById(@Path("id") id: Int): Response<Post>
    @DELETE("posts/{id}")
    suspend fun removeById(@Path("id") id: Int): Response<Unit>
    @POST("posts")
    suspend fun save(@Body post: Post): Response<Post>
    @Multipart
    @POST("media")
    suspend fun upload(@Part media: MultipartBody.Part): Response<Media>
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