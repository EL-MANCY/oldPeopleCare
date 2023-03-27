package com.example.oldpeoplecareapp.ui.CaregiverPath.CaregiverNotifications

import android.widget.Button
import com.example.oldpeoplecareapp.model.entity.notificationData

interface OnItemClickListner3 {
    fun accepted(item: notificationData)
    fun rejected(item: notificationData)
}