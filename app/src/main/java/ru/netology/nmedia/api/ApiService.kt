package ru.netology.nmedia.api

import okhttp3.MultipartBody
import retrofit2.*
import retrofit2.http.*
import ru.netology.nmedia.auth.*
import ru.netology.nmedia.handler.*

interface ApiService {
    @POST("users/push-tokens")
    suspend fun sendPushToken(@Body pushToken: PushToken): Response<Unit>
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