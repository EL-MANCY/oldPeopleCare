package com.example.oldpeoplecareapp.model.local

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.oldpeoplecareapp.model.entity.*
import com.example.oldpeoplecareapp.ui.CaregiverPath.CaregiverHome.UiModel.MedicineUiModel

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

    ////////////////////////////////////////////////////////////

    suspend fun getPatients():List<MedicineUiModel>

    suspend fun addPatientsCareGiver(medicine: List<MedicineUiModel>)

    //////////////////////////////////////////////////////////////////

    suspend fun getSingleUser(): SingleUserResponse

    suspend fun postSingleUser(user:SingleUserResponse)


}