package com.nurfauzi.storyapp.data.network.api

import com.nurfauzi.storyapp.data.network.responses.FileUploadResponse
import com.nurfauzi.storyapp.data.network.responses.LoginResponse
import com.nurfauzi.storyapp.data.network.responses.RegisterResponse
import com.nurfauzi.storyapp.data.network.responses.StoriesResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisterResponse

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @GET("stories")
    suspend fun getAllStories(
        @Header("Authorization") token: String
    ): StoriesResponse

    @GET("stories")
    suspend fun getAllStoriesWithLocation(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("location") location: Int? = null
    ): StoriesResponse

    @Multipart
    @POST("stories")
    suspend fun uploadImage(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Header("Authorization") token: String
    ): FileUploadResponse

    @GET("stories")
    suspend fun getAllStoriesMap(
        @Query("location") location: Int,
        @Header("Authorization") token: String
    ): StoriesResponse
}