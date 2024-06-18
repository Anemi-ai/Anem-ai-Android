package com.bangkit.anemai.data.repository

import com.bangkit.anemai.data.model.LoginResult
import com.bangkit.anemai.data.model.RegisterResponse
import com.bangkit.anemai.data.model.UserIdResponse
import com.bangkit.anemai.data.model.UserResult
import com.bangkit.anemai.data.pref.UserModel
import com.bangkit.anemai.data.pref.UserPreference
import com.bangkit.anemai.data.sevice.ApiConfig
import com.bangkit.anemai.data.sevice.ApiService
import com.google.gson.JsonObject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class UserRepository private constructor(
    private var apiService: ApiService,
    private val userPreference: UserPreference
){

    suspend fun register(name: String, birthDate: String, gender: String, email: String, password: String): RegisterResponse {
        val param = JsonObject().apply {
            addProperty("name", name)
            addProperty("birthDate", birthDate)
            addProperty("gender", gender)
            addProperty("email", email)
            addProperty("password", password)
        }
        val response = apiService.register(param)
        if (response.status!!) {
            return response
        } else {
            throw Exception(response.message)
        }
    }

    suspend fun login(email: String, password: String): LoginResult {
        val param = JsonObject().apply {
            addProperty("email", email)
            addProperty("password", password)
        }
        val response = apiService.login(param)
        if (response.status!!) {
            val user = UserModel(response.loginResult!!.id!!, email, response.token!!, true)
            saveSession(user)
            apiService = ApiConfig.getApiService(user.token)
            return response.loginResult
        } else {
            throw Exception(response.message)
        }
    }

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    suspend fun getDetailUser(id: String): UserIdResponse {
        return apiService.getUserDetail(id)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null

        suspend fun getInstance(userPreference: UserPreference): UserRepository {
            val user = userPreference.getSession().first()
            val token = user.token
            val apiService = ApiConfig.getApiService(token)
            return instance ?: synchronized(this) {
                UserRepository(apiService, userPreference).also { instance = it }
            }
        }
    }
}