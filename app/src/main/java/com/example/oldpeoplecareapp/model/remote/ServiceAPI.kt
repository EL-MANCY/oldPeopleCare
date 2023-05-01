package com.example.oldpeoplecareapp.model.remote

import com.example.oldpeoplecareapp.model.entity.*
import retrofit2.Response
import retrofit2.http.*
import java.util.*

interface ServiceAPI {

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
        @Field("description") description: String,
        @Field("time") time: List<String>,
        @Field("weakly") weakly:Array<String>
    ): Response<AllMedicineResponseItem>

    @GET("/medicine/{id}")
    suspend fun getAllMedicine(
        @Path("id") id: String,
        @Header("token") token: String
    ): Response<List<AllMedicineResponseItem>>

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
        @Field("description") description: String,
        @Field("time") time: Array<String>,
        @Field("weakly") weakly:Array<String>
    ): Response<MedicineResponseX>

    @DELETE("/medicine/{medId}/{userId}")
    suspend fun DeleteMedicine(
        @Path("medId") medId: String,
        @Path("userId") userId: String,
        @Header("token") token: String
    ): Response<Any>

    @GET("/user/user/circles")
    suspend fun getPatientCircle(
        @Header("token") token: String
    ): Response<List<Circles>?>

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

    @POST("/notification/accept/{notifyId}")
    suspend fun Accept(
        @Path("notifyId") notifyId: String,
        @Header("token") token: String,
    ):Response<Any>

    @POST("/notification/refuse/{notifyId}")
    suspend fun Reject(
        @Path("notifyId") notifyId: String,
        @Header("token") token: String,
    ):Response<Any>

    @FormUrlEncoded
    @POST("/auth/reset")
    suspend fun ResetPassword(@Field("email") email: String):Response<Any>

    @PUT("/user/circles/editRole/{caregiverID}")
    suspend fun updateRole(@Header("token") token: String,
                           @Path("caregiverID") caregiverID: String,
                           @Query("role") newRole: String): Response<UpdateResponse>

    @GET("/user/{userID}")
    suspend fun getSingleUser(
        @Header("token") token: String,
        @Path("userID") userID: String,
    ):Response<SingleUserResponse>

    @GET("/upcoming/{userID}")
    suspend fun getUpcoming(
        @Header("token") token: String,
        @Path("userID") userID: String,
        @Query("state") state:String
    ):Response<List<Medicine>>

    @POST("/upcoming")
    suspend fun getUpcomingDaily(
        @Header("token") token: String,
    ):Response<Any>

    @PUT("/upcoming/{userID}/{medID}")
    suspend fun changeState(
        @Header("token") token: String,
        @Path("userID") userID: String,
        @Path("medID") medID: String,
        @Query("state") state:String
    ):Response<Any>

    @FormUrlEncoded
    @POST("/auth/reset/token")
    suspend fun sendCode(
        @Field("token") token: String,
    ):Response<CodeResponse>

    @GET("/upcoming")
    suspend fun getPatients(@Header("token") token: String):Response<List<CaregiverHomeResponseItem>>

}

