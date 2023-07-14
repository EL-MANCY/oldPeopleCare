package com.example.oldpeoplecareapp.model.remote

import android.util.Log
import com.example.oldpeoplecareapp.model.entity.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response

class RemoteRepositoryImp(private val api: ServiceAPI) : RemoteRepository {
    override suspend fun addNewUser(
        fullname: String, email: String, phone: String, dateOfBirth: String,
        gender: String, registAs: String, password: String, FcmToken: String

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
                password,
                FcmToken
            )
        }
    }

    override suspend fun logIn(
        emailOrPhone: String,
        email: String,
        password: String,
        FcmToken: String
    ): Response<UserLogInInfo> {
        return withContext(Dispatchers.IO) {
            api.logIn(emailOrPhone, email, password, FcmToken)
        }
    }


    override suspend fun postMedicine(
        id: String,
        token: String,
        name: RequestBody,
        image: MultipartBody.Part,
        audio: MultipartBody.Part,
        type: RequestBody,
        description: RequestBody,
        time:List<MultipartBody.Part>,
        weekly:List<MultipartBody.Part>,
    ): Response<AllMedicineResponseItem> {

        return withContext(Dispatchers.IO) {
            api.postMedicine(
                id,
                token,
                name,
                image,
                audio,
                type,
                description,
                time,
                weekly
            )
        }
    }

    override suspend fun getAllMedicine(
        id: String,
        token: String
    ): Response<List<AllMedicineResponseItem>> {
        return withContext((Dispatchers.IO)) {
            api.getAllMedicine(id, token)
        }
    }

    override suspend fun getSingleMedicine(
        medId: String,
        userId: String,
        token: String
    ): Response<AllMedicineResponseItem> {
        return withContext((Dispatchers.IO)) {
            api.getSingleMedicine(medId, userId, token)
        }
    }

    override suspend fun updateMedicine(
        medId: String,
        userId: String,
        token: String,
        name: RequestBody,
        image: MultipartBody.Part,
        audio: MultipartBody.Part,
        type: RequestBody,
        description: RequestBody,
        time: MultipartBody.Part,
        weakly: MultipartBody.Part,
    ): Response<MedicineResponseX> {
        return withContext((Dispatchers.IO)) {
            api.updateMedicine(
                medId,
                userId,
                token,
                name,
                image,
                audio,
                type,
                description,
                time,
                weakly,
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

    override suspend fun getPatientCircle(token: String): Response<List<Circles>?> {
        return withContext((Dispatchers.IO)) {
            api.getPatientCircle(token)
        }
    }

    override suspend fun sendRequest(token: String, email: String, role: String): Response<Any> {
        return withContext((Dispatchers.IO)) {
            api.sendRequest(token, email, role)
        }
    }

    override suspend fun getAllNotification(token: String): Response<List<notificationData>> {
        return withContext((Dispatchers.IO)) {
            api.getAllNotification(token)
        }
    }

    override suspend fun Accept(notifyId: String, token: String): Response<Any> {
        return withContext((Dispatchers.IO)) {
            api.Accept(notifyId, token)
        }
    }

    override suspend fun Reject(notifyId: String, token: String): Response<Any> {
        return withContext((Dispatchers.IO)) {
            api.Reject(notifyId, token)
        }
    }

    override suspend fun ResetPassword(email: String): Response<Any> {
        return withContext((Dispatchers.IO)) {
            api.ResetPassword(email)
        }
    }

    override suspend fun updateRole(
        token: String,
        caregiverID: String,
        newRole: String
    ): Response<UpdateResponse> {
        return withContext((Dispatchers.IO)) {
            api.updateRole(token, caregiverID, newRole)
        }
    }

    override suspend fun getSingleUser(
        token: String,
        userID: String
    ): Response<SingleUserResponse> {
        return withContext((Dispatchers.IO)) {
            api.getSingleUser(token, userID)
        }
    }

    override suspend fun getUpcoming(
        token: String,
        userID: String,
        state: String
    ): Response<List<Medicine>> {
        return withContext((Dispatchers.IO)) {
            api.getUpcoming(token, userID, state)
        }
    }

    override suspend fun getUpcomingDaily(token: String): Response<Any> {
        return withContext((Dispatchers.IO)) {
            api.getUpcomingDaily(token)
        }
    }

    override suspend fun changeState(
        token: String,
        userID: String,
        medID: String,
        state: String
    ): Response<Any> {
        return withContext((Dispatchers.IO)) {
            api.changeState(token, userID, medID, state)
        }
    }

    override suspend fun sendCode(token: String): Response<CodeResponse> {
        return withContext((Dispatchers.IO)) {
            api.sendCode(token)
        }
    }

    override suspend fun getPatients(token: String): Response<List<CaregiverHomeResponseItem>> {
        return withContext((Dispatchers.IO)) {
            api.getPatients(token)
        }
    }

    override suspend fun sendMessage(
        token: String,
        receiverId: String,
        content: String
    ): Response<MessageResponse> {
        return withContext((Dispatchers.IO)) {
            api.sendMessage(token, receiverId, content)
        }
    }

    override suspend fun getConversation(
        token: String,
        receiverId: String
    ): Response<List<MessageResponse>> {
        return withContext((Dispatchers.IO)) {
            api.getConversation(token, receiverId)
        }
    }

    override suspend fun getAllConversations(token: String): Response<List<AllConversationsResponseItem>> {
        return withContext((Dispatchers.IO)) {
            api.getAllConversations(token)
        }
    }

    override suspend fun deleteConversation(
        conversationId: String,
        token: String
    ): Response<DeleteConversationResponse> {
        return withContext((Dispatchers.IO)) {
            api.deleteConversation(conversationId, token)
        }
    }

    override suspend fun deleteMessage(
        messageId: String,
        token: String
    ): Response<DeleteConversationResponse> {
        return withContext((Dispatchers.IO)) {
            api.deleteMessage(messageId, token)
        }
    }

    override suspend fun searchUser(
        token: String,
        user: String
    ): Response<List<SearchResponseItem>> {
        return withContext((Dispatchers.IO)) {
            api.searchUser(token, user)
        }
    }

    override suspend fun deleteCareGiver(
        caregiverId: String,
        token: String
    ): Response<DeleteConversationResponse> {
        return withContext((Dispatchers.IO)) {
            api.deleteCareGiver(caregiverId, token)
        }
    }

    override suspend fun updatetSingleUser(
        token: String,
        fullname: RequestBody,
        email: RequestBody,
        phone: RequestBody,
        dateOfBirth: RequestBody,
        gender: RequestBody,
        image: MultipartBody.Part
    ): Response<SingleUserResponse> {
        return withContext((Dispatchers.IO)) {
            api.updatetSingleUser(token, fullname, email, phone, dateOfBirth, gender, image)
        }
    }
}
