package com.id.destinasyik.data.retrofit

data class LoginResponse(
    val message: String,
    val user: UserData,
    val token: String
)

data class UserData(
    val user_id: Int,
    val username: String,
    val name: String,
    val password: String,
    val age: Int,
    val email: String,
    val city: String,
    val prefered_category: String
)