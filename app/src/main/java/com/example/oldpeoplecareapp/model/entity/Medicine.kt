package com.example.oldpeoplecareapp.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Medicine")
data class Medicine(
    @PrimaryKey val i:Int,
    val medicine: MedicineX,
    val state: String
)