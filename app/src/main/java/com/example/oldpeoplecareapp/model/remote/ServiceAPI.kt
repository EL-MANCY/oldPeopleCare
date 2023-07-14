package com.example.oldpeoplecareapp.model.remote

import com.example.oldpeoplecareapp.model.entity.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

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

    @Multipart
    @PUT("/user/")
    suspend fun updatetSingleUser(
        @Header("token") token: String,
        @Part("fullname") fullname: RequestBody,
        @Part("email") email: RequestBody,
        @Part("phone") phone: RequestBody,
        @Part("dateOfBirth") dateOfBirth: RequestBody,
        @Part("gender") gender: RequestBody,
        @Part image: MultipartBody.Part
    ): Response<SingleUserResponse>

    @Multipart
    @POST("/medicine/{id}")
    suspend fun postMedicine(
        @Path("id") id: String,
        @Header("token") token: String,
        @Part("name") name: RequestBody,
        @Part image: MultipartBody.Part,
        @Part audio: MultipartBody.Part,
        @Part("type") type: RequestBody,
        @Part("description") description: RequestBody,
        @Part time: List<MultipartBody.Part>,
        @Part weakly: List<MultipartBody.Part>
    ): Response<AllMedicineResponseItem>

    @GET("/medicine/{id}")
    suspend fun getAllMedicine(
        @Path("id") id: String,
        @Header("token") token: String
    ): Response<List<AllMedicineResponseItem>>

    @GET("/medicine/{medicineId}/{userId}")
    suspend fun getSingleMedicine(
        @Path("medicineId") medId: String,
        @Path("userId") userId: String,
        @Header("token") token: String
    ): Response<AllMedicineResponseItem>

    @Multipart
    @PUT("/medicine/{medId}/{userId}")
    suspend fun updateMedicine(
        @Path("medId") medId: String,
        @Path("userId") userId: String,
        @Header("token") token: String,
        @Part("name") name: RequestBody,
        @Part image: MultipartBody.Part,
        @Part audio: MultipartBody.Part,
        @Part("type") type: RequestBody,
        @Part("description") description: RequestBody,
        @Part time: MultipartBody.Part,
        @Part weakly: MultipartBody.Part
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
    ): Response<Any>

    @GET("/notification")
    suspend fun getAllNotification(
        @Header("token") token: String,
    ): Response<List<notificationData>>

    @POST("/notification/accept/{notifyId}")
    suspend fun Accept(
        @Path("notifyId") notifyId: String,
        @Header("token") token: String,
    ): Response<Any>

    @POST("/notification/refuse/{notifyId}")
    suspend fun Reject(
        @Path("notifyId") notifyId: String,
        @Header("token") token: String,
    ): Response<Any>

    @FormUrlEncoded
    @POST("/auth/reset")
    suspend fun ResetPassword(@Field("email") email: String): Response<Any>

    @PUT("/user/circles/editRole/{caregiverID}")
    suspend fun updateRole(
        @Header("token") token: String,
        @Path("caregiverID") caregiverID: String,
        @Query("role") newRole: String
    ): Response<UpdateResponse>

    @GET("/user/{userID}")
    suspend fun getSingleUser(
        @Header("token") token: String,
        @Path("userID") userID: String,
    ): Response<SingleUserResponse>


    @GET("/upcoming/{userID}")
    suspend fun getUpcoming(
        @Header("token") token: String,
        @Path("userID") userID: String,
        @Query("state") state: String
    ): Response<List<Medicine>>

    @POST("/upcoming")
    suspend fun getUpcomingDaily(
        @Header("token") token: String,
    ): Response<Any>

    @PUT("/upcoming/{userID}/{medID}")
    suspend fun changeState(
        @Header("token") token: String,
        @Path("userID") userID: String,
        @Path("medID") medID: String,
        @Query("state") state: String
    ): Response<Any>

    @FormUrlEncoded
    @POST("/auth/reset/token")
    suspend fun sendCode(
        @Field("token") token: String,
    ): Response<CodeResponse>

    @GET("/upcoming")
    suspend fun getPatients(@Header("token") token: String): Response<List<CaregiverHomeResponseItem>>

    @FormUrlEncoded
    @POST("/message/{receiverId}")
    suspend fun sendMessage(
        @Header("token") token: String,
        @Path("receiverId") receiverId: String,
        @Field("content") content: String
    ): Response<MessageResponse>

    @GET("/message/{receiverId}")
    suspend fun getConversation(
        @Header("token") token: String,
        @Path("receiverId") receiverId: String
    ): Response<List<MessageResponse>>

    @GET("/conversation")
    suspend fun getAllConversations(
        @Header("token") token: String,
    ): Response<List<AllConversationsResponseItem>>

    @DELETE("/conversation/{conversationId}")
    suspend fun deleteConversation(
        @Path("conversationId") conversationId: String,
        @Header("token") token: String
    ): Response<DeleteConversationResponse>

    @DELETE("/conversation/{messageId}")
    suspend fun deleteMessage(
        @Path("messageId") messageId: String,
        @Header("token") token: String
    ): Response<DeleteConversationResponse>

 @DELETE("/user/user/circles/{Id}")
    suspend fun deleteCareGiver(
        @Path("Id") caregiverId: String,
        @Header("token") token: String
    ): Response<DeleteConversationResponse>

    @FormUrlEncoded
    @POST("/user/find/more")
    suspend fun searchUser(
        @Header("token") token: String,
        @Field("user") user: String
    ): Response<List<SearchResponseItem>>

}

