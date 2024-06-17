package com.infruit.viewmodel.sign_in

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.infruit.data.TypesResponse
import com.infruit.data.model.user.RegisterRequest
import com.infruit.data.model.user.RegisterResponse
import com.infruit.data.repository.UserRepository

class RegisterViewModel: ViewModel() {
    private val userRepository = UserRepository()

    private val _registerResponse = MutableLiveData<TypesResponse<RegisterResponse>>()
    val registerResponse: LiveData<TypesResponse<RegisterResponse>> get() = _registerResponse

    fun registerUser(user: RegisterRequest) {
        _registerResponse.value = TypesResponse.Loading()
        userRepository.registerUser(user) { resource ->
            _registerResponse.value = resource
        }
    }
}