package com.example.oldpeoplecareapp.ui.PatientPath.patientHome

import com.example.oldpeoplecareapp.model.entity.AllMedicineRespone
import com.example.oldpeoplecareapp.model.entity.AllMedicineResponseItem
import com.example.oldpeoplecareapp.model.entity.Medicine

interface OnItemClickListener {
    fun onItemClick(info: Medicine)
    fun onStateClick(info: Medicine)

}