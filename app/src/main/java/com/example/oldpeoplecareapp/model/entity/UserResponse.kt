package com.example.oldpeoplecareapp.model.entity


data class UserResponse(
    var fullname: String,
    var email: String,
    var phone: Int,
    var dateOfBirth: String,
    var gender: String,
    var registAs: String,
    var password: String,
    var isAdmin:Boolean,
    var _id:String,
    var createdAt:String,
    var updatedAt:String,
    var __v:Int
    )
