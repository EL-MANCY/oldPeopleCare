package com.example.oldpeoplecareapp.model.remote

import android.provider.ContactsContract
import android.util.Log
import com.example.oldpeoplecareapp.model.entity.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.Header
import retrofit2.http.Path

class RemoteRepositoryImp(private val api: ServiceAPI):RemoteRepository {
    override suspend fun addNewUser(
        fullname: String, email: String, phone: String, dateOfBirth: String,
        gender: String, registAs: String, password: String
    ): Response<UserResponse> {
        return withContext(Dispatchers.IO) {
            Log.i("repoImp", fullname)
            api.addNewUser(
                fullname,
                email,
                phone,
                dateOfBirth,
                gender,
                registAs,
                password
            )
        }
    }

    override suspend fun logIn(
        emailOrPhone: String,
        email: String,
        password: String
    ): Response<UserLogInInfo> {
        return withContext(Dispatchers.IO) {
            api.logIn(emailOrPhone, email, password)
        }
    }

    override suspend fun postMedicine(
        id: String,
        token: String,
        name: String,
        imgUrl: String,
        recordUrl: String,
        type: String,
        date: String,
        time: String,
        repeatDays: Int,
        description: String
    ): Response<MedicineResponse> {
        return withContext((Dispatchers.IO)) {
            api.postMedicine(
                id,
                token,
                name,
                imgUrl,
                recordUrl,
                type,
                date,
                time,
                repeatDays,
                description
            )
        }
    }

    override suspend fun getAllMedicine(
        id: String,
        token: String
    ): Response<List<AllMedicineRespone>> {
        return withContext((Dispatchers.IO)) {
            api.getAllMedicine(id, token)
        }
    }

    override suspend fun updateMedicine(
        medId: String,
        userId: String,
        token: String,
        name: String,
        imgUrl: String,
        recordUrl: String,
        type: String,
        date: String,
        time: String,
        repeatDays: Int,
        description: String,
    ): Response<AllMedicineRespone> {
        return withContext((Dispatchers.IO)) {
            api.updateMedicine(
                medId, userId, token,
                name,
                imgUrl,
                recordUrl,
                type,
                date,
                time,
                repeatDays,
                description
            )
        }
    }

    override suspend fun DeleteMedicine(
        medId: String,
        userId: String,
        token: String
    ): Response<Any> {
        return withContext((Dispatchers.IO)) {
            api.DeleteMedicine(medId, userId, token)
        }
    }

    override suspend fun getPatientCircle(token: String):Response<List<Circles>?> {
        return withContext((Dispatchers.IO)) {
            api.getPatientCircle(token)
        }
    }

}







//    override suspend fun getSingleUser(id: String): Response<UserResponse> {
//        return withContext(Dispatchers.IO){
//            api.getSingleUser(id)
//        }
//    }
