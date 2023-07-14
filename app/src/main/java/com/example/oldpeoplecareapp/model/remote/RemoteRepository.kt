package com.example.oldpeoplecareapp.model.remote

import com.example.oldpeoplecareapp.model.entity.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response

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
        name: RequestBody,
        image: MultipartBody.Part,
        audio: MultipartBody.Part,
        type: RequestBody,
        description: RequestBody,
        time: List<MultipartBody.Part>,
        weakly: List<MultipartBody.Part>,
    ): Response<AllMedicineResponseItem>

    suspend fun getAllMedicine(
        id: String,
        token: String
    ): Response<List<AllMedicineResponseItem>>

    suspend fun getSingleMedicine(
        medId: String,
        userId: String,
        token: String
    ): Response<AllMedicineResponseItem>

    suspend fun updateMedicine(
        medId: String,
        userId: String,
        token: String,
        name: RequestBody,
        image: MultipartBody.Part,
        audio: MultipartBody.Part,
        type: RequestBody,
        description: RequestBody,
        time: MultipartBody.Part,
        weakly:MultipartBody.Part
    ): Response<MedicineResponseX>

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

    suspend fun updateRole(
        token: String,
        caregiverID: String,
        newRole: String
    ): Response<UpdateResponse>

    suspend fun getSingleUser(token: String, userID: String): Response<SingleUserResponse>

    suspend fun getUpcoming(
        token: String,
        userID: String,
        state: String
    ): Response<List<Medicine>>

    suspend fun getUpcomingDaily(
        token: String,
    ): Response<Any>

    suspend fun changeState(
        token: String,
        userID: String,
        medID: String,
        state: String
    ): Response<Any>

    suspend fun sendCode(
        token: String,
    ): Response<CodeResponse>

    suspend fun getPatients(
        token: String
    ): Response<List<CaregiverHomeResponseItem>>


    suspend fun sendMessage(
        token: String,
        receiverId: String,
        content: String
    ): Response<MessageResponse>

    suspend fun getConversation(
        token: String,
        receiverId: String
    ): Response<List<MessageResponse>>

    suspend fun getAllConversations(
        token: String,
    ): Response<List<AllConversationsResponseItem>>

    suspend fun deleteConversation(
        conversationId: String,
        token: String
    ): Response<DeleteConversationResponse>

    suspend fun deleteMessage(
        messageId: String,
        token: String
    ): Response<DeleteConversationResponse>


    suspend fun searchUser(
        token: String,
        user: String
    ): Response<List<SearchResponseItem>>

    suspend fun deleteCareGiver(
        caregiverId: String,
        token: String
    ): Response<DeleteConversationResponse>

    suspend fun updatetSingleUser(
        token: String,
        fullname: RequestBody,
        email: RequestBody,
        phone: RequestBody,
        dateOfBirth: RequestBody,
        gender: RequestBody,
        image: MultipartBody.Part
    ): Response<SingleUserResponse>
}















