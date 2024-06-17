package com.bangkit.anemai.data.model

import com.google.gson.annotations.SerializedName

data class UserIdResponse(

	@field:SerializedName("user")
	val userResult: UserResult? = null
)

data class UserResult(

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("password")
	val password: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("email")
	val email: String? = null
)
