package com.example.oldpeoplecareapp.model.local

import androidx.room.*
import com.example.oldpeoplecareapp.model.entity.*
import com.example.oldpeoplecareapp.ui.CaregiverPath.CaregiverHome.UiModel.MedicineUiModel
import com.google.gson.Gson

import com.google.gson.reflect.TypeToken
import retrofit2.Response
import retrofit2.http.Header
import retrofit2.http.Path


@Dao
interface DataDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun postMedicine(
      allMedicineResponseItem: List<AllMedicineResponseItem>
    )

    @Query("select * from AllAlarms")
    suspend fun getAllMedicine(): List<AllMedicineResponseItem>
    ///////////////////////////////////////////////////////////////

    @Query("select * from Medicine Where state=='Waiting'")
    suspend fun getUpcomingWaiting(
    ): List<Medicine>

    @Query("select * from Medicine Where state=='Missed'")
    suspend fun getUpcomingMissed(
    ): List<Medicine>

    @Query("select * from Medicine Where state=='Completed'")
    suspend fun getUpcomingCompleted(
    ):List<Medicine>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addUpComming(
        medicine: List<Medicine>
    )

    ///////////////////////////////////////////////////////////////////

    @Query("select * from Notify")
    suspend fun getAllNotification():List<notificationData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNotify(
        medicine: List<notificationData>
    )

    /////////////////////////////////////////////////////////////////////

    @Query("select * from CirclePatient")
    suspend fun getPatientCircle():List<Circles>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPatientCircle(
        medicine: List<Circles>
    )
    //////////////////////////////////////////////////////

    @Query("select * from MedicineCareGiver")
    suspend fun getPatients():List<MedicineUiModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPatientsCareGiver(medicine: List<MedicineUiModel>)

    ////////////////////////////////////////////////////////

    @Query("select * from UserData")
    suspend fun getSingleUser():SingleUserResponse

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun postSingleUser(user:SingleUserResponse)


}

