package com.bangkit.anemai.data.repository

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.bangkit.anemai.R
import com.bangkit.anemai.data.model.DetectionResponse
import com.bangkit.anemai.data.pref.UserPreference
import com.bangkit.anemai.data.sevice.ApiConfig
import com.bangkit.anemai.data.sevice.ApiService
import com.bangkit.anemai.utils.Result
import kotlinx.coroutines.flow.first
import okhttp3.MultipartBody
import org.json.JSONObject

class DetectionRepository(
    private val application: Application,
    private val apiService: ApiService,
    private val preference: UserPreference
) {

    fun predictAnemia(multipart: MultipartBody.Part): LiveData<Result<DetectionResponse>> = liveData {
        emit(Result.Loading)

        try {
            val userId = preference.getSession().first().id
            val response = apiService.predictAnemia(userId = userId, multipart)
            Log.d("DetectionReposPredict", userId)

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
            val userId = preference.getSession().first().id
            val response = apiService.getHistoryById(userId)

            if (response.isSuccessful) {
                val responseBody = response.body()
                val historyList = ArrayList<DetectionResponse>()
                responseBody?.forEach {
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
        fun getInstance(application: Application, preference: UserPreference): DetectionRepository {
            val apiService = ApiConfig.getMLApiService()
            return instance ?: synchronized(this) {
                instance ?: DetectionRepository(application, apiService, preference)
            }.also { instance = it }
        }
    }
}