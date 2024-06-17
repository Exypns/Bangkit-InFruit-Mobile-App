package com.infruit.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.infruit.data.datastore.UserDataPreferences

class OnBoardViewModel(private val pref: UserDataPreferences): ViewModel() {
    fun getTokenData(): LiveData<String?> {
        return pref.getToken().asLiveData()
    }
}