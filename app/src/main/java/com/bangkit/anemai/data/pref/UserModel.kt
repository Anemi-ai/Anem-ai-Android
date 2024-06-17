package com.bangkit.anemai.data.pref

data class UserModel(
    val id: String,
    val email: String,
    val token: String,
    val isLogin: Boolean = false
)
