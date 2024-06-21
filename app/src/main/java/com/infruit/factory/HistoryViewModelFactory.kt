package com.infruit.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.infruit.data.datastore.UserDataPreferences
import com.infruit.viewmodel.history.HistoryViewModel

class HistoryViewModelFactory(private val pref: UserDataPreferences): ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HistoryViewModel::class.java)) {
            return HistoryViewModel(pref) as T
        }
        throw IllegalArgumentException("Unchecked Viemodel Class: " + modelClass.name)
    }


}