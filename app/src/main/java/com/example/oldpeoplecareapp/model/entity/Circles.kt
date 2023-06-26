package com.example.oldpeoplecareapp.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "CirclePatient")
data class Circles(@PrimaryKey val id:ID, val role:String)
data class ID(val image: chatImage, val _id:String, val fullname:String, val phone:String)
data class Image(val public_id:String,val url:String)