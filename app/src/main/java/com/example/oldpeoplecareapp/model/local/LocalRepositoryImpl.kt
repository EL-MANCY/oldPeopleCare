package com.example.oldpeoplecareapp.model.local

import com.example.oldpeoplecareapp.model.entity.AllMedicineResponseItem
import com.example.oldpeoplecareapp.model.entity.Circles
import com.example.oldpeoplecareapp.model.entity.Medicine
import com.example.oldpeoplecareapp.model.entity.notificationData
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

//    override suspend fun DeleteMedicine(medId: String, userId: String, token: String) {
//        return withContext(Dispatchers.IO) {
//            db.dataDao().DeleteMedicine(
//
//            )
//        }
//    }

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
}