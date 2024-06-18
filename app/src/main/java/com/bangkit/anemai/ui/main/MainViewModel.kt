package com.bangkit.anemai.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.bangkit.anemai.data.model.UserIdResponse
import com.bangkit.anemai.data.pref.UserModel
import com.bangkit.anemai.data.repository.ArticleRepository
import com.bangkit.anemai.data.repository.DetectionRepository
import com.bangkit.anemai.data.repository.UserRepository
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class MainViewModel(
    private val detectionRepository: DetectionRepository,
    private val userRepository: UserRepository,
    private val articleRepository: ArticleRepository
): ViewModel() {

    private val _userDetail = MutableLiveData<UserIdResponse>()
    val userDetail: LiveData<UserIdResponse> get() = _userDetail

    fun getSession(): LiveData<UserModel> {
        return userRepository.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            userRepository.logout()
        }
    }

    fun getDetailUser(userId: String) {
        viewModelScope.launch {
            try {
                val response = userRepository.getDetailUser(userId)
                _userDetail.value = response
            } catch (e: Exception) {
                // Handle exceptions
            }
        }
    }

    fun predict(multipart: MultipartBody.Part) = detectionRepository.predictAnemia(multipart)
    fun getHistory() = detectionRepository.fetchHistory()

    fun getArticles() = articleRepository.fetchArticles()
    fun getArticleById(articleId: String) = articleRepository.fetchArticleById(articleId)
}