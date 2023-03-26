package com.example.oldpeoplecareapp.model.entity

import java.util.*


data class MedicineResponse(
    val error : String,
    var name: String,
    var imgUrl: String,
    var recordUrl: String,
    var type: String,
    var description: String,
    var time: MutableList<String>,
    var weakly: MutableList<String>,
    var _id: String,
    var lastUpdatedUserID :String,
    var createdAt: String,
    var updatedAt: String,
    var __v: Int
    )
