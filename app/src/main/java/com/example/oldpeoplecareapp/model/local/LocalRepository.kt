package com.example.oldpeoplecareapp.model.local

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.oldpeoplecareapp.model.entity.AllMedicineResponseItem
import com.example.oldpeoplecareapp.model.entity.Circles
import com.example.oldpeoplecareapp.model.entity.Medicine
import com.example.oldpeoplecareapp.model.entity.notificationData

interface LocalRepository {

    suspend fun postMedicine(allMedicineResponseItem: List<AllMedicineResponseItem>)

    suspend fun getAllMedicine(): List<AllMedicineResponseItem>

    /////////////////////////////////////////////////////////////////////////////

    suspend fun getUpcomingWaiting(): List<Medicine>

    suspend fun getUpcomingMissed(): List<Medicine>

    suspend fun getUpcomingCompleted():List<Medicine>

    suspend fun addUpComming(medicine: List<Medicine>)

    ///////////////////////////////////////////////////////

    suspend fun getAllNotification():List<notificationData>

    suspend fun addNotify(medicine: List<notificationData>)

    ////////////////////////////////////////////////////////////

    suspend fun getPatientCircle():List<Circles>?

    suspend fun addPatientCircle(medicine: List<Circles>)

}