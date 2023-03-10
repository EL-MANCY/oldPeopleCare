package com.example.oldpeoplecareapp.model.remote

import com.example.oldpeoplecareapp.model.entity.*
import retrofit2.Response
import retrofit2.http.*

interface ServiceAPI {
    //Registration
    @FormUrlEncoded
    @POST("/auth/signup")
    suspend fun addNewUser(
        @Field("fullname") fullname: String,
        @Field("email") email: String,
        @Field("phone") phone: String,
        @Field("dateOfBirth") dateOfBirth: String,
        @Field("gender") gender: String,
        @Field("registAs") registAs: String,
        @Field("password") password: String,
        @Field("fcmToken") FcmToken: String
    ): Response<UserResponse>

    @FormUrlEncoded
    @POST("/auth/login")
    suspend fun logIn(
        @Field("emailOrPhone") emailOrPhone: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("fcmToken") FcmToken: String
    ): Response<UserLogInInfo>

    @FormUrlEncoded
    @POST("/medicine/{id}")
    suspend fun postMedicine(
        @Path("id") id: String,
        @Header("token") token: String,
        @Field("name") name: String,
        @Field("imgUrl") imgUrl: String,
        @Field("recordUrl") recordUrl: String,
        @Field("type") type: String,
        @Field("date") date: String,
        @Field("time") time: String,
        @Field("repeatDays") repeatDays: Int,
        @Field("description") description: String
    ): Response<MedicineResponse>

    @GET("/medicine/{id}")
    suspend fun getAllMedicine(
        @Path("id") id: String,
        @Header("token") token: String
    ): Response<List<AllMedicineRespone>>

    @FormUrlEncoded
    @PUT("/medicine/{medId}/{userId}")
    suspend fun updateMedicine(
        @Path("medId") medId: String,
        @Path("userId") userId: String,
        @Header("token") token: String,
        @Field("name") name: String,
        @Field("imgUrl") imgUrl: String,
        @Field("recordUrl") recordUrl: String,
        @Field("type") type: String,
        @Field("date") date: String,
        @Field("time") time: String,
        @Field("repeatDays") repeatDays: Int,
        @Field("description") description: String
    ): Response<AllMedicineRespone>

    @DELETE("/medicine/{medId}/{userId}")
    suspend fun DeleteMedicine(
        @Path("medId") medId: String,
        @Path("userId") userId: String,
        @Header("token") token: String
    ): Response<Any>

    @GET("/user/user/circles")
    suspend fun getPatientCircle(@Header("token") token: String): Response<List<Circles>?>

    @FormUrlEncoded
    @POST("/notification/request")
    suspend fun sendRequest(
        @Header("token") token: String,
        @Field("email") email: String,
        @Field("role") role: String
    ):Response<Any>

    @GET("/notification")
    suspend fun getAllNotification(
        @Header("token") token: String,
        ):Response<List<notificationData>>
}









//    @GET("user/")
//    suspend fun getSingleUser(@Query("id") id:String):Response<UserResponse>


