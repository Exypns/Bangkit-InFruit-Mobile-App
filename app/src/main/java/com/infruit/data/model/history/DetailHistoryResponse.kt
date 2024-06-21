package com.infruit.data.model.history

import com.google.gson.annotations.SerializedName

data class DetailHistoryResponse(

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class Data(

	@field:SerializedName("score")
	val score: String? = null,

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("recommendation")
	val recommendation: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("label")
	val label: String? = null
)
