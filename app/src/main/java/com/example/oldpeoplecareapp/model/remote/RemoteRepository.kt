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
        password: String
    ): Response<UserResponse>

    suspend fun logIn(
        emailOrPhone: String,
        email: String,
        password: String
    ):Response<UserLogInInfo>

    suspend fun postMedicine(
        id: String,
        token:String,
        name: String,
        imgUrl: String,
        recordUrl: String,
        type: String,
        date: String,
        time: String,
        repeatDays: Int,
        description: String,
        ):Response<MedicineResponse>

    suspend fun getAllMedicine(
       id: String,
       token:String
        ):Response<List<AllMedicineRespone>>

    suspend fun updateMedicine( medId: String,
                                userId:String,
                                token:String,
                                name: String,
                                imgUrl: String,
                                recordUrl: String,
                                type: String,
                                date: String,
                                time: String,
                                repeatDays: Int,
                                description: String):Response<AllMedicineRespone>

    suspend fun DeleteMedicine( medId: String,
                                userId:String,
                                token:String ):Response<Any>

}














  //  suspend fun getSingleUser( id:String):Response<UserResponse>

