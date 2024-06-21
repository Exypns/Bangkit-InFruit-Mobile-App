package com.infruit.data.model.history

import com.google.gson.annotations.SerializedName


data class HistoryData(
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
