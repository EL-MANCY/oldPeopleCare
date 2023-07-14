package com.example.oldpeoplecareapp.ui.CaregiverPath.CaregiverHome

import com.example.oldpeoplecareapp.model.entity.AllMedicineRespone
import com.example.oldpeoplecareapp.model.entity.AllMedicineResponseItem
import com.example.oldpeoplecareapp.model.entity.Medicine
import com.example.oldpeoplecareapp.ui.CaregiverPath.CaregiverHome.UiModel.MedicineUiModel

interface OnMedClickListener {
    fun onItemClick(info: MedicineUiModel)

}