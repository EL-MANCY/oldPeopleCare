package com.example.oldpeoplecareapp.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "Notify")
data class notificationData(
    var sender: Sender,
    var receiver: Reciever,

    @PrimaryKey var _id: String,
    var type: String,
    var read: String,
    var description: String,
    var caregiverRole: String?,
    var createdAt: String,
    var updatedAt: String,
    var __v: Int
    )

    data class Sender(
        var id: IDnotify,
        var name: String
    )

    data class IDnotify(
        var image:imageNotify,
        var id: String
    )
data class imageNotify(
    var public_id:String,
    var url :String
)

    data class Reciever(
        var id: String,
        var name: String
    )
