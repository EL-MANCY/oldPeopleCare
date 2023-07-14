package com.example.oldpeoplecareapp.ui.CaregiverPath.CaregiverHome.UiModel

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "MedicineCareGiver")
data class MedicineUiModel(
    val name: String,
    @PrimaryKey
    val med:String,
    val medId:String,
    val userId:String,
    val time:String,
    val imgUrlMed: String,
    val imgUrlUser:String,
    val state:String
)