package com.bangkit.anemai.data.sevice

import com.bangkit.anemai.data.model.ArticlesResponse
import com.bangkit.anemai.data.model.LoginResponse
import com.bangkit.anemai.data.model.RegisterResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<RegisterResponse>

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ) : Response<LoginResponse>

    @GET("stories")
    suspend fun getStories(): ArticlesResponse
}