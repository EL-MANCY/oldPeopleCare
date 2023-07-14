package com.example.oldpeoplecareapp.model.entity

data class MedicineX(
    val _id: String,
    val image: Image,
    val audio: Audio,
    val name: String,
    val time: List<String>
)