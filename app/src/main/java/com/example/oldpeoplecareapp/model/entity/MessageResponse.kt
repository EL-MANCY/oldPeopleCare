package com.example.oldpeoplecareapp.model.entity

data class MessageResponse(
    val __v: Int,
    val _id: String,
    val content: String,
    val receiver: String,
    val sender: String,
    val timestamp: String
)