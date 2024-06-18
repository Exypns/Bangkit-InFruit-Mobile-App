package com.infruit.data.repository

import com.infruit.api.RetrofitInstance
import com.infruit.data.TypesResponse
import com.infruit.data.model.user.LoginRequest
import com.infruit.data.model.user.LoginResponse
import com.infruit.data.model.user.RegisterRequest
import com.infruit.data.model.user.RegisterResponse
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository {
    private val apiService = RetrofitInstance.getApiService()

    fun loginUser(userData: LoginRequest, callback: (TypesResponse<LoginResponse>) -> Unit) {
        apiService.loginUser(userData).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                if (response.isSuccessful) {
                    callback(TypesResponse.Success(response.body()))
                } else {
                    val errorBody = response.errorBody()?.string()
                    try {
                        val json = JSONObject(errorBody)
                        val errorMessage = json.getString("message")
                        callback(TypesResponse.Error(errorMessage))

                    } catch (e: JSONException) {
                        callback(TypesResponse.Error(e.message))
                    }
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                callback(TypesResponse.Error(t.message))
            }
        })
    }

    fun registerUser(userData: RegisterRequest, callback: (TypesResponse<RegisterResponse>) -> Unit) {
        apiService.registerUser(userData).enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                if (response.isSuccessful) {
                    callback(TypesResponse.Success(response.body()))
                } else {
                    val errorBody = response.errorBody()?.string()
                    try {
                        val json = JSONObject(errorBody)
                        val errorMessage = json.getString("message")
                        callback(TypesResponse.Error(errorMessage))

                    } catch (e: JSONException) {
                        callback(TypesResponse.Error(e.message))
                    }
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                callback(TypesResponse.Error(t.message))
            }
        })
    }

}