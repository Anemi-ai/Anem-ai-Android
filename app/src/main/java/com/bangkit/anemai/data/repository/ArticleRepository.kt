package com.bangkit.anemai.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.bangkit.anemai.R
import com.bangkit.anemai.data.model.ArticleItem
import com.bangkit.anemai.data.model.ArticlesResponseItem
import com.bangkit.anemai.data.pref.UserPreference
import com.bangkit.anemai.data.sevice.ApiConfig
import com.bangkit.anemai.data.sevice.ApiService
import com.bangkit.anemai.utils.Result
import kotlinx.coroutines.flow.first
import org.json.JSONObject

class ArticleRepository(
    private val application: Application,
    private val apiService: ApiService
) {
    fun fetchArticles() : LiveData<Result<List<ArticleItem>>> = liveData {
        emit(Result.Loading)

        try {
            val response = apiService.getArticles()

            if (response.isSuccessful) {
                val responseBody = response.body()
                val articleList = ArrayList<ArticleItem>()
                responseBody?.forEach {
                    val item = ArticleItem(
                        it.id,
                        it.title,
                        it.imageUrl
                    )
                    articleList.add(item)
                }

                emit(Result.Success(articleList))
            } else {
                val errorBody = response.errorBody()?.string()
                val jsonObject = errorBody?.let { JSONObject(it) }

                emit(Result.Error(jsonObject?.getString("error") ?: "Unknown Error"))
            }
        } catch (e: Exception) {
            emit(Result.Error(application.getString(R.string.error_try_again)))
        }
    }

    fun fetchArticleById(id: String): LiveData<Result<ArticlesResponseItem>> = liveData {
        emit(Result.Loading)

        try {
            val response = apiService.getArticleById(id)

            if (response.isSuccessful) {
                val responseBody = response.body()
                val articleItem = ArticlesResponseItem(
                    responseBody?.sourceUrl,
                    responseBody?.createdAt,
                    responseBody?.imageUrl,
                    responseBody?.description,
                    responseBody?.id,
                    responseBody?.title,
                    responseBody?.content
                )

                emit(Result.Success(articleItem))
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
        private var instance: ArticleRepository? = null

        suspend fun getInstance(userPreference: UserPreference, application: Application): ArticleRepository {
            val user = userPreference.getSession().first()
            val token = user.token
            val apiService = ApiConfig.getApiService(token)
            return instance ?: synchronized(this) {
                ArticleRepository(application, apiService)
            }
        }
    }
}