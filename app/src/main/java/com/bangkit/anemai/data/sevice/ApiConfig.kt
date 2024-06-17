package com.bangkit.anemai.data.sevice

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.bangkit.anemai.BuildConfig
import java.io.File
import java.io.FileInputStream
import java.util.Properties

class ApiConfig {
    companion object {
//        private val properties = Properties().apply {
//            load(FileInputStream(File("local.properties")))
//        }
//        private val inputStream = FileInputStream("local.properties")
        fun getApiService(token: String? = null): ApiService {
//            properties.load(inputStream)
//            val baseUrlGeneral = properties.getProperty("BASE_URL_GENERAL")
            val baseUrlGeneral = BuildConfig.BASE_URL_GENERAL

            val loggingInterceptor = if(BuildConfig.DEBUG) {
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            } else {
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
            }

            val clientBuilder = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)

            if (token != null) {
                clientBuilder.addInterceptor { chain ->
                    val originalRequest = chain.request()
                    val requestBuilder = originalRequest.newBuilder()
                        .addHeader("Authorization", "Bearer $token")
                        .method(originalRequest.method, originalRequest.body)
                    val request = requestBuilder.build()
                    chain.proceed(request)
                }
            }

            val client = clientBuilder.build()

            val retrofit = Retrofit.Builder()
                .baseUrl(baseUrlGeneral)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            return retrofit.create(ApiService::class.java)
        }

        fun getMLApiService(): ApiService {
//            properties.load(inputStream)
//            val baseUrlGeneral = properties.getProperty("BASE_URL_ML")
            val baseUrlGeneral = BuildConfig.BASE_URL_ML

            val loggingInterceptor = if(BuildConfig.DEBUG) {
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            } else {
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
            }


            val client = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl(baseUrlGeneral)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            return retrofit.create(ApiService::class.java)
        }
    }
}