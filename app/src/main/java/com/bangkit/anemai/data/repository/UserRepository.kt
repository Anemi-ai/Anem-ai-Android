package com.bangkit.anemai.data.repository

import com.bangkit.anemai.data.model.LoginResult
import com.bangkit.anemai.data.model.RegisterResponse
import com.bangkit.anemai.data.pref.UserModel
import com.bangkit.anemai.data.pref.UserPreference
import com.bangkit.anemai.data.sevice.ApiConfig
import com.bangkit.anemai.data.sevice.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class UserRepository private constructor(
    private var apiService: ApiService,
    private val userPreference: UserPreference
){

    suspend fun register(name: String, birthDate: String, gender: String, email: String, password: String): RegisterResponse {
        return apiService.register(name, birthDate, gender, email, password)
    }

    suspend fun login(email: String, password: String): LoginResult {
        val response = apiService.login(email, password)
        if (!response.status!!) {
            val user = UserModel(email, response.token!!, true)
            saveSession(user)
            apiService = ApiConfig.getApiService()
            return response.loginResult!!
        } else {
            throw Exception(response.message)
        }
    }

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
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
            val apiService = ApiConfig.getApiService()
            return instance ?: synchronized(this) {
                UserRepository(apiService, userPreference).also { instance = it }
            }
        }
    }
}