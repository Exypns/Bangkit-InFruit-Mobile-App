package com.infruit.data.model.user

import com.google.gson.annotations.SerializedName

data class LoginResponse(

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("data")
    val data: LoginData? = null
)

data class LoginData(

    @field:SerializedName("token")
    val token: String? = null
)