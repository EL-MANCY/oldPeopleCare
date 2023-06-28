package com.example.oldpeoplecareapp.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "UserData")
data class SingleUserResponse(
    val image: Image,
    @PrimaryKey
    val _id: String,
    val createdAt: String,
    val dateOfBirth: String,
    val email: String,
    val fullname: String,
    val gender: String,
    val isAdmin: Boolean,
    val phone: String,
    val registAs: String,
    val updatedAt: String,
    val isInCircle:Boolean
)