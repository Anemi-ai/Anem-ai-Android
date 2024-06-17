package com.bangkit.anemai.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.anemai.data.repository.DetectionRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class MainViewModel(private val detectionRepository: DetectionRepository): ViewModel() {

    private val _isReady = MutableStateFlow(false)
    val isReady = _isReady.asStateFlow()

    init {
        viewModelScope.launch {
            delay(3000)
            _isReady.value = true
        }
    }

    fun predict(multipart: MultipartBody.Part) = detectionRepository.predictAnemia(multipart)
    fun getHistory() = detectionRepository.fetchHistory()
}