package com.bangkit.anemai.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.bangkit.anemai.data.model.DetectionResponse
import com.bangkit.anemai.data.sevice.ApiService
import com.bangkit.anemai.utils.Result
import okhttp3.MultipartBody
import org.json.JSONObject

class DetectionRepository(
    private val apiService: ApiService,
//    private val preference
) {

    fun predictAnemia(multipart: MultipartBody.Part): LiveData<Result<DetectionResponse>> = liveData {
        emit(Result.Loading)
        val userId = "1212"

        try {
            val response = apiService.predictAnemia(userId = userId, multipart)

            if (response.isSuccessful) {
                val responseBody = response.body()
                val predictResponse = DetectionResponse(
                    responseBody?.id,
                    responseBody?.result,
                    responseBody?.imageUrl,
                    responseBody?.createdAt,
                    responseBody?.akurasi,
                    responseBody?.deskripsi,
                    responseBody?.informasiTambahan
                )

                emit(Result.Success(predictResponse))
            } else {
                val errorBody = response.errorBody()?.string()
                val jsonObject = errorBody?.let { JSONObject(it) }

                emit(Result.Error(jsonObject?.getString("message") ?: "Unknown Error"))
            }
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    companion object {
        @Volatile
        private var instance: DetectionRepository? = null
        fun getInstance(
            apiService: ApiService
        ): DetectionRepository =
            instance ?: synchronized(this) {
                instance ?: DetectionRepository(apiService)
            }.also { instance = it }
    }
}