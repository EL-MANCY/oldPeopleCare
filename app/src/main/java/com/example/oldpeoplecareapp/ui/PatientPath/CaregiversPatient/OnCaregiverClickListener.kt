package com.example.oldpeoplecareapp.ui.PatientPath.CaregiversPatient

import com.example.oldpeoplecareapp.model.entity.Circles

interface OnCaregiverClickListener {
    fun onItemClick(info: Circles)
    fun onChatClick(info: Circles)
}