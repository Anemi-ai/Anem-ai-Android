package com.bangkit.anemai.data.model

import com.google.gson.annotations.SerializedName

class AnemiaDetection (
        @field:SerializedName("id")
        val id: String? = null,

        @field:SerializedName("idUser")
        val idUser: String? = null,

        @field:SerializedName("result")
        val result: String? = null,

        @field:SerializedName("imageUrl")
        val imageUrl: String? = null,

        @field:SerializedName("createdAt")
        val createdAt: String? = null,
    )