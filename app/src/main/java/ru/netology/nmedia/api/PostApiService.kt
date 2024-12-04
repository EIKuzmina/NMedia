package ru.netology.nmedia.api

import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import ru.netology.nmedia.BuildConfig
import ru.netology.nmedia.auth.*
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
    @FormUrlEncoded
    @POST("users/authentication")
    suspend fun updateUser(@Field("login") login: String,
                           @Field("pass") pass: String): Response<AuthState>
    @FormUrlEncoded
    @POST("users/registration")
    suspend fun registrationUser(@Field("name") name: String,
                                 @Field("login") login: String,
                                 @Field("pass") pass: String): Response<AuthState>
}
val logger = HttpLoggingInterceptor().apply {
    if (BuildConfig.DEBUG) {
        level = HttpLoggingInterceptor.Level.BODY
    }
}
private val okhttp = OkHttpClient.Builder()
    .addInterceptor(logger)
    .addInterceptor { chain ->
        AppAuth.getInstance().authStateFlow.value.token?.let { token ->
            val newRequest = chain.request().newBuilder()
                .addHeader("Authorization", token)
                .build()
            return@addInterceptor chain.proceed(newRequest)
        }
        chain.proceed(chain.request())
    }
    .build()

private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .client(okhttp)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

object PostsApi {
    val retrofitService : PostApiService by lazy {
        retrofit.create(PostApiService::class.java)
    }
}