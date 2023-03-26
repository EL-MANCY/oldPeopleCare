package com.example.oldpeoplecareapp.model.entity

data class UserLogInInfo(val error : String,
                         var token:String,
                         var registAs: String,
                         val id :String,
                         )
data class ErrorResponse(val message: String)
