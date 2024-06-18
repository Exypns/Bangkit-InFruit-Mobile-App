package com.infruit.api

import com.infruit.data.model.user.LoginRequest
import com.infruit.data.model.user.LoginResponse
import com.infruit.data.model.user.RegisterRequest
import com.infruit.data.model.user.RegisterResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("auth/register")
    fun registerUser(
        @Body userData: RegisterRequest
    ): Call<RegisterResponse>

    @POST("auth/login")
    fun loginUser(
        @Body userData: LoginRequest
    ): Call<LoginResponse>

}