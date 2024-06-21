package com.infruit.data.model.history

data class HistoryRequest(
    val score: String? = null,
    val image: String? = null,
    val recommendation: String? = null,
    val id: Int? = null,
    val label: String? = null
)