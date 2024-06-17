package com.bangkit.anemai.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.bangkit.anemai.R
import com.bangkit.anemai.data.model.DetectionResponse
import com.bangkit.anemai.data.sevice.ApiService
import com.bangkit.anemai.utils.Result
import okhttp3.MultipartBody
import org.json.JSONObject

class DetectionRepository(
    private val application: Application,
    private val apiService: ApiService,
//    private val preference
) {

    private val userId = "12122"
    fun predictAnemia(multipart: MultipartBody.Part): LiveData<Result<DetectionResponse>> = liveData {
        emit(Result.Loading)

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

                emit(Result.Error(jsonObject?.getString("error") ?: "Unknown Error"))
            }
        } catch (e: Exception) {
            emit(Result.Error(application.getString(R.string.error_try_again)))
        }
    }

    fun fetchHistory(): LiveData<Result<List<DetectionResponse>>> = liveData {
        emit(Result.Loading)

        try {
            val response = apiService.getHistoryById(userId)

            if (response.isSuccessful) {
                val responseBody = response.body()
                val historyList = ArrayList<DetectionResponse>()
                responseBody?.forEach { it ->
                    val item = DetectionResponse(
                        it.id,
                        it.result,
                        it.imageUrl,
                        it.createdAt,
                        it.akurasi,
                        it.deskripsi,
                        it.informasiTambahan
                    )
                    historyList.add(item)
                }

                emit(Result.Success(historyList))
            } else {
                val errorBody = response.errorBody()?.string()
                val jsonObject = errorBody?.let { JSONObject(it) }

                emit(Result.Error(jsonObject?.getString("error") ?: "Unknown Error"))
            }

        } catch (e: Exception) {
            emit(Result.Error(application.getString(R.string.error_try_again)))
        }
    }

    companion object {
        @Volatile
        private var instance: DetectionRepository? = null
        fun getInstance(
            application: Application,
            apiService: ApiService
        ): DetectionRepository =
            instance ?: synchronized(this) {
                instance ?: DetectionRepository(application, apiService)
            }.also { instance = it }
    }
}