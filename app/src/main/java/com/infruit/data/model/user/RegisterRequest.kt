package com.infruit.data.model.user

data class RegisterRequest (
    val email: String,
    val password: String,
    val name: String
)