package com.example.oldpeoplecareapp.model.local

import com.example.oldpeoplecareapp.model.entity.*
import com.example.oldpeoplecareapp.ui.CaregiverPath.CaregiverHome.UiModel.MedicineUiModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LocalRepositoryImpl(private val db:OldCareDB) : LocalRepository {
    override suspend fun postMedicine(
        allMedicineResponseItem: List<AllMedicineResponseItem>

    ){
        return withContext(Dispatchers.IO) {
            db.dataDao().postMedicine(
               allMedicineResponseItem
            )
        }
    }


    override suspend fun getAllMedicine(): List<AllMedicineResponseItem> {
        return withContext(Dispatchers.IO) {
            db.dataDao().getAllMedicine()
        }
    }

    override suspend fun getUpcomingWaiting(): List<Medicine> {
        return withContext(Dispatchers.IO) {
            db.dataDao().getUpcomingWaiting()
        }
    }

    override suspend fun getUpcomingMissed(): List<Medicine> {
        return withContext(Dispatchers.IO) {
            db.dataDao().getUpcomingMissed()
        }
    }

    override suspend fun getUpcomingCompleted(): List<Medicine> {
        return withContext(Dispatchers.IO) {
            db.dataDao().getUpcomingCompleted()
        }
    }

    override suspend fun addUpComming(medicine: List<Medicine>) {
        return withContext(Dispatchers.IO) {
            db.dataDao().addUpComming(medicine)
        }
    }

    override suspend fun getAllNotification(): List<notificationData> {
        return withContext(Dispatchers.IO) {
            db.dataDao().getAllNotification()
        }
    }

    override suspend fun addNotify(medicine: List<notificationData>) {
        return withContext(Dispatchers.IO) {
            db.dataDao().addNotify(medicine)
        }
    }

    override suspend fun getPatientCircle(): List<Circles>? {
        return withContext(Dispatchers.IO) {
            db.dataDao().getPatientCircle()
        }
    }

    override suspend fun addPatientCircle(medicine: List<Circles>) {
        return withContext(Dispatchers.IO) {
            db.dataDao().addPatientCircle(medicine)
        }
    }

    override suspend fun getPatients(): List<MedicineUiModel> {
        return withContext(Dispatchers.IO) {
            db.dataDao().getPatients()
        }
    }

    override suspend fun addPatientsCareGiver(medicine: List<MedicineUiModel>) {
        return withContext(Dispatchers.IO) {
            db.dataDao().addPatientsCareGiver(medicine)
        }
    }

    override suspend fun getSingleUser(): SingleUserResponse {
        return withContext(Dispatchers.IO) {
            db.dataDao().getSingleUser()
        }
    }

    override suspend fun postSingleUser(user:SingleUserResponse) {
        return withContext(Dispatchers.IO) {
            db.dataDao().postSingleUser(user)
        }
    }
}