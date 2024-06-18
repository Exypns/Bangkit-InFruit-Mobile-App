package com.infruit.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.infruit.data.datastore.UserDataPreferences
import com.infruit.viewmodel.OnBoardViewModel

class OnBoardViewModelFactory(private val pref: UserDataPreferences) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OnBoardViewModel::class.java)) {
            return OnBoardViewModel(pref) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}