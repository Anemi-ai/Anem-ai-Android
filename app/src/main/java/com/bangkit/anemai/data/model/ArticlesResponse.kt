package com.bangkit.anemai.data.model

import com.google.gson.annotations.SerializedName

data class ArticlesResponse(

	@field:SerializedName("ArticlesResponse")
	val articlesResponse: List<ArticlesResponseItem> = emptyList()
)

data class ArticlesResponseItem(

	@field:SerializedName("sourceUrl")
	val sourceUrl: String? = null,

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("image")
	val imageUrl: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("content")
	val content: String? = null
)

data class ArticleItem(

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("image")
	val imageUrl: String? = null,

	@field:SerializedName("createdAt")
	val createdAt: String? = null

)