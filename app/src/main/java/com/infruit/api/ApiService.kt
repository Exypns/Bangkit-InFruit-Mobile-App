package com.infruit.api

import com.infruit.data.model.history.CreateHistoryResponse
import com.infruit.data.model.history.GetHistoryResponse
import com.infruit.data.model.user.LoginRequest
import com.infruit.data.model.user.LoginResponse
import com.infruit.data.model.user.RegisterRequest
import com.infruit.data.model.user.RegisterResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
    @POST("auth/register")
    @Headers("Content-Type: application/json")
    fun registerUser(
        @Body userData: RegisterRequest
    ): Call<RegisterResponse>

    @POST("auth/login")
    @Headers("Content-Type: application/json")
    fun loginUser(
        @Body userData: LoginRequest
    ): Call<LoginResponse>

    @Multipart
    @POST("history/create")
    fun createHistory(
        @Header("Authorization") token: String,
        @Part image: MultipartBody.Part,
        @Part("id") id: RequestBody,
        @Part("label") label: RequestBody,
        @Part("score") score: RequestBody,
        @Part("recommendation") recommendation: RequestBody
    ): Call<CreateHistoryResponse>

    @GET("history")
    @Headers("Content-Type: application/json")
    fun getAllHistory(
        @Header("Authorization") token: String
    ): Call<GetHistoryResponse>
}