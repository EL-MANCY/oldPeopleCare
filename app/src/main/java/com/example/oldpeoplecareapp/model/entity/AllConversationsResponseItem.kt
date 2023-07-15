package com.example.oldpeoplecareapp.model.entity

data class AllConversationsResponseItem(
    val _id: String,
    val participants: List<Participant>,
    val messages: List<Messages>
)

data class Messages(
    val _id: String,
    val sender: String,
    val receiver: String,
    val content: String,
    val timestamp: String,
    val __v: Int
)