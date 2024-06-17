package com.bangkit.anemai.data.sevice

import com.bangkit.anemai.data.model.DetectionResponse
import com.bangkit.anemai.data.model.ArticlesResponse
import com.bangkit.anemai.data.model.LoginResponse
import com.bangkit.anemai.data.model.RegisterResponse
import com.bangkit.anemai.data.model.UserIdResponse
import com.bangkit.anemai.data.model.UserResult
import com.google.gson.JsonObject
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

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

    @POST("auth/login")
    suspend fun login(
        @Body raw: JsonObject
    ): LoginResponse

    @GET("stories")
    suspend fun getStories(): ArticlesResponse

    @GET("users/{id}")
    suspend fun getUserDetail(
        @Path("id") id: String
    ): UserIdResponse

    @Multipart
    @POST("predict")
    suspend fun predictAnemia(
        @Part("user_id") userId: String,
        @Part file: MultipartBody.Part
    ): Response<DetectionResponse>

    @GET("history/{id}")
    suspend fun getHistoryById(
        @Path("id") userId: String
    ): Response<List<DetectionResponse>>
}