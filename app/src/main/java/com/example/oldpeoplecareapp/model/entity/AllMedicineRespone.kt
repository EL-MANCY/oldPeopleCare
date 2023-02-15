package com.example.oldpeoplecareapp.model.entity


    data class AllMedicineRespone(
        var _id: String,
        var name: String,
        var imgUrl: String,
        var recordUrl: String,
        var type: String,
        var date: String,
        var time: String,
        var repeatDays: Int,
        var description: String,
        var userId: String,
        var createdAt: String,
        var updatedAt: String,
        var __v: Int
    )
