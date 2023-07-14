package com.example.oldpeoplecareapp.model.entity

data class CaregiverHomeResponseItem(
    val __v: Int,
    val _id: String,
    val createdAt: String,
    val medicines: List<Medicines>,
    val user: User
)

data class ImageCare(
    val public_id: String,
    val url: String
)

data class Medicines(
    val medicine: MedItem,
    val state: String
)

data class MedItem(
    val _id: String,
    val image: Image,
    val audio: Audio,
    val name: String,
    val time: List<String>
)

data class User(
    val _id: String,
    val fullname: String,
    val image: ImageCare
)