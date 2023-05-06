package com.example.finalproject.models

class User (
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val role: String = "client"
){
    override fun toString(): String {
        return "${firstName} ${lastName} ${email} ${role} "
    }
}