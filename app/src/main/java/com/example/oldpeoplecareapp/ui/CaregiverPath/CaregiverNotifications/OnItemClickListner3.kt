package com.example.oldpeoplecareapp.ui.CaregiverPath.CaregiverNotifications

import com.example.oldpeoplecareapp.model.entity.notificationData

interface OnItemClickListner3 {
    fun accepted(item: notificationData)
    fun rejected(item: notificationData)
}