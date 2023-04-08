package com.example.oldpeoplecareapp.model.remote

import com.example.oldpeoplecareapp.model.entity.*
import retrofit2.Response
import retrofit2.http.*

interface RemoteRepository {
    suspend fun addNewUser(
        fullname: String,
        email: String,
        phone: String,
        dateOfBirth: String,
        gender: String,
        registAs: String,
        password: String,
        FcmToken: String

    ): Response<UserResponse>

    suspend fun logIn(
        emailOrPhone: String,
        email: String,
        password: String,
        FcmToken: String

    ): Response<UserLogInInfo>

    suspend fun postMedicine(
        id: String,
        token: String,
        name: String,
        imgUrl: String,
        recordUrl: String,
        type: String,
        description: String,
        time: List<String>,
        weakly: List<String>
    ): Response<MedicineResponse>

    suspend fun getAllMedicine(
        id: String,
        token: String
    ): Response<List<AllMedicineRespone>>

    suspend fun updateMedicine(
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
        description: String
    ): Response<AllMedicineRespone>

    suspend fun DeleteMedicine(
        medId: String,
        userId: String,
        token: String
    ): Response<Any>

    suspend fun getPatientCircle(token: String): Response<List<Circles>?>

    suspend fun sendRequest(
        token: String,
        email: String,
        role: String
    ): Response<Any>

    suspend fun getAllNotification(
        token: String,
    ): Response<List<notificationData>>


    suspend fun Accept(
        notifyId: String,
        token: String,
    ): Response<Any>

    suspend fun Reject(
        notifyId: String,
        token: String,
    ): Response<Any>

    suspend fun ResetPassword(email: String): Response<Any>

    suspend fun updateRole(token: String,
                           caregiverID: String,
                           newRole: String): Response<UpdateResponse>

    suspend fun getSingleUser(token: String,userID: String, ):Response<SingleUserResponse>

}













  //  suspend fun getSingleUser( id:String):Response<UserResponse>


