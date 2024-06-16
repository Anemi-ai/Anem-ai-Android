package com.bangkit.anemai.data.sevice

import com.bangkit.anemai.data.model.DetectionResponse
import com.bangkit.anemai.data.model.ArticlesResponse
import com.bangkit.anemai.data.model.LoginResponse
import com.bangkit.anemai.data.model.RegisterResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {

    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("birthDate") birthDate: String,
        @Field("gender") gender: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisterResponse

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ) : LoginResponse

    @GET("stories")
    suspend fun getStories(): ArticlesResponse

    @Multipart
    @POST("predict")
    suspend fun predictAnemia(
        @Part("user_id") userId: String,
        @Part file: MultipartBody.Part
    ): Response<DetectionResponse>
}