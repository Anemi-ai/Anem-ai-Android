package com.bangkit.anemai.data

import com.bangkit.anemai.data.model.AnemiaDetection
import com.bangkit.anemai.data.model.ArticlesResponse
import com.bangkit.anemai.data.model.ArticlesResponseItem
import com.bangkit.anemai.data.model.Data
import com.bangkit.anemai.data.model.LoginResponse
import com.bangkit.anemai.data.model.RegisterResponse

object DataDummy {
    fun generateRegisterResponse(): RegisterResponse = RegisterResponse(
        status = true,
        message = "Pengguna berhasil ditambahkan",
        data = Data(
            "Pulu",
            "0",
            "pulu@gmail.com"
        )
    )

    fun generateLoginResponse(): LoginResponse = LoginResponse(
        status = true,
        message = "Login berhasil",
        token = "asasa1qwidjmqwd.qw9djqd.q0wdj",
        data = Data(
            "Pulu",
            "0",
            "pulu@gmail.com"
        )
    )

    fun generateArticleResponse(): ArticlesResponse {
        val articleList = ArrayList<ArticlesResponseItem>()
        for (i in 0..10) {
            val story = ArticlesResponseItem(
                "https://res.cloudinary.com/drjnb5zxa/image/upload/v1698108753/cld-sample-5.jpg",
                "2024-05-31T15:08:39.868Z",
                "https://res.cloudinary.com/drjnb5zxa/image/upload/v1698108753/cld-sample-5.jpg",
                "pulu pulu",
                null,
                "id$i",
                null
            )
            articleList.add(story)
        }
        return ArticlesResponse(articleList)
    }
    
    fun generateArticleList(): List<ArticlesResponseItem> {
        val articleList = ArrayList<ArticlesResponseItem>()
        for (i in 0..10) {
            val story = ArticlesResponseItem(
                "https://res.cloudinary.com/drjnb5zxa/image/upload/v1698108753/cld-sample-5.jpg",
                "2024-05-31T15:08:39.868Z",
                "https://res.cloudinary.com/drjnb5zxa/image/upload/v1698108753/cld-sample-5.jpg",
                "pulu pulu",
                null,
                "id$i",
                null
            )
            articleList.add(story)
        }
        return articleList
    }

    fun generateAnemiDetectionList(): List<AnemiaDetection> {
        val detectionList = ArrayList<AnemiaDetection>()
        for (i in 0..5) {
            val detection = AnemiaDetection(
                "id$i",
                "0",
                "anemia",
                "https://res.cloudinary.com/drjnb5zxa/image/upload/v1698108753/cld-sample-5.jpg",
                "Saturday, 20 January 2020"
            )
            detectionList.add(detection)
        }

        for (i in 0..5) {
            val detection = AnemiaDetection(
                "id$i",
                "0",
                "normal",
                "https://res.cloudinary.com/drjnb5zxa/image/upload/v1698108753/cld-sample-5.jpg",
                "Saturday, 20 January 2020"
            )
            detectionList.add(detection)
        }
        return detectionList
    }
}