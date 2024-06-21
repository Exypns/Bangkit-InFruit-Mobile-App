package com.infruit.viewmodel.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.infruit.data.TypesResponse
import com.infruit.data.datastore.UserDataPreferences
import com.infruit.data.model.history.CreateHistoryResponse
import com.infruit.data.model.history.DetailHistoryResponse
import com.infruit.data.model.history.GetHistoryResponse
import com.infruit.data.repository.HistoryRepository
import okhttp3.RequestBody
import java.io.File

class HistoryViewModel(private val pref: UserDataPreferences): ViewModel() {
    private val historyRepository = HistoryRepository()

    private val _createHistoryResponse = MutableLiveData<TypesResponse<CreateHistoryResponse>>()
    val createHistoryResponse: LiveData<TypesResponse<CreateHistoryResponse>> get() = _createHistoryResponse

    private val _listHistory = MutableLiveData<TypesResponse<GetHistoryResponse>>()
    val listHistory: LiveData<TypesResponse<GetHistoryResponse>> = _listHistory

    private val _detailHistory = MutableLiveData<TypesResponse<DetailHistoryResponse>>()
    val detailHistory: LiveData<TypesResponse<DetailHistoryResponse>> = _detailHistory

    fun createHistory(token: String, image: File, id: RequestBody,
                      label: RequestBody, score: RequestBody, recommendation: RequestBody) {
        _createHistoryResponse.value = TypesResponse.Loading()
        historyRepository.createHistory(token, image, id, label, score, recommendation) {
            _createHistoryResponse.value = it
        }
    }

    fun getAllHistory(token: String) {
        _listHistory.value = TypesResponse.Loading()
        historyRepository.getAllHistory(token) {
            _listHistory.value = it
        }
    }

    fun getToken(): LiveData<String?> {
        return pref.getToken().asLiveData()
    }
}