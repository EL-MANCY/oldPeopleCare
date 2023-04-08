package com.example.oldpeoplecareapp.model.entity

data class SingleUserResponse(
    val _id: String,
    val createdAt: String,
    val dateOfBirth: String,
    val email: String,
    val fullname: String,
    val gender: String,
    val isAdmin: Boolean,
    val phone: String,
    val registAs: String,
    val updatedAt: String
)