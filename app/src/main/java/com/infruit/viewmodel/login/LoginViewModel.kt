package com.infruit.viewmodel.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infruit.data.TypesResponse
import com.infruit.data.datastore.UserDataPreferences
import com.infruit.data.model.user.LoginRequest
import com.infruit.data.model.user.LoginResponse
import com.infruit.data.repository.UserRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val pref: UserDataPreferences) : ViewModel() {
    private val userRepository = UserRepository()

    private val _loginResponse = MutableLiveData<TypesResponse<LoginResponse>>()
    val loginResponse: LiveData<TypesResponse<LoginResponse>> get() = _loginResponse

    fun loginUser(user: LoginRequest) {
        _loginResponse.value = TypesResponse.Loading()
        userRepository.loginUser(user) { resource ->
            _loginResponse.value = resource
        }
    }

    fun saveUserData(token: String) {
        viewModelScope.launch {
            pref.saveUserData(token)
        }
    }
}