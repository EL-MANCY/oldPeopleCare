package com.example.oldpeoplecareapp.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "AllAlarms")
data class AllMedicineResponseItem(
    val __v: Int,
    @PrimaryKey
    val _id: String,
    val createdAt: String,
    val description: String,
    val image: Image?,
    val lastUpdatedUserID: String,
    val name: String,
    val audio: Audio?,
    val time: List<String>,
    val type: String,
    val updatedAt: String,
    val weakly: List<String>
){
    fun getImgUrl(): String? {
        return image?.url
    }
}