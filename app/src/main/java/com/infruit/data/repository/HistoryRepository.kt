package com.infruit.data.repository

import android.util.Log
import com.infruit.api.RetrofitInstance
import com.infruit.data.TypesResponse
import com.infruit.data.model.history.CreateHistoryResponse
import com.infruit.data.model.history.GetHistoryResponse
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class HistoryRepository() {
    private val apiService = RetrofitInstance.getApiService()

    fun createHistory(token: String, image: File, id: RequestBody,
                      label: RequestBody, score: RequestBody,
                      recommendation: RequestBody,
                      callback: (TypesResponse<CreateHistoryResponse>) -> Unit) {

        val requestImageFile = image.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "image",
            image.name,
            requestImageFile
        )
        apiService.createHistory("Bearer $token", multipartBody, id, label, score, recommendation)
            .enqueue(object : Callback<CreateHistoryResponse> {
            override fun onResponse(
                call: Call<CreateHistoryResponse>,
                response: Response<CreateHistoryResponse>
            ) {
                if (response.isSuccessful) {
                    callback(TypesResponse.Success(response.body()))
                    Log.d("Create History Success", response.body()?.message.toString())
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.d("History Error", response.toString())
                    try {
                        val json = errorBody?.let { JSONObject(it) }
                        val errorMessage = json?.getString("message")
                        callback(TypesResponse.Error(errorMessage))
                    } catch (e: JSONException) {
                        callback(TypesResponse.Error(e.message))
                    }
                }
            }
            override fun onFailure(call: Call<CreateHistoryResponse>, t: Throwable) {
                callback(TypesResponse.Error(t.message))
            }
        })
    }

    fun getAllHistory(token: String, callback: (TypesResponse<GetHistoryResponse>) -> Unit) {
        apiService.getAllHistory("Bearer $token").enqueue(object : Callback<GetHistoryResponse> {
            override fun onResponse(
                call: Call<GetHistoryResponse>,
                response: Response<GetHistoryResponse>
            ) {
                if (response.isSuccessful) {
                    callback(TypesResponse.Success(response.body()))
                    Log.d("Get History Response Success", response.body()?.data.toString())
                } else {
                    val errorBody = response.errorBody()?.string()
                    try {
                        val json = errorBody?.let { JSONObject(it) }
                        val errorMessage = json?.getString("message")
                        callback(TypesResponse.Error(errorMessage))
                    } catch (e: JSONException) {
                        callback(TypesResponse.Error(e.message))
                    }
                }
            }
            override fun onFailure(call: Call<GetHistoryResponse>, t: Throwable) {
                callback(TypesResponse.Error(t.message))
            }
        })
    }
}