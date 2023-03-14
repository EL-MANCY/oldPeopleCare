package com.example.oldpeoplecareapp.model.entity

data class Circles(val id:ID,val role:String)
data class ID(val image: Image,val _id:String, val fullname:String,val phone:String)
data class Image(val public_id:String,val url:String)