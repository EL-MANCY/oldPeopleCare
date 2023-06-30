package com.example.oldpeoplecareapp.ui.Chat.ChatScreen

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.oldpeoplecareapp.model.entity.ConversationResponseItem
import com.example.oldpeoplecareapp.model.entity.DeleteConversationResponse
import com.example.oldpeoplecareapp.model.entity.MessageResponse
import com.example.oldpeoplecareapp.model.local.LocalRepositoryImpl
import com.example.oldpeoplecareapp.model.local.OldCareDB
import com.example.oldpeoplecareapp.model.remote.RemoteRepositoryImp
import com.example.oldpeoplecareapp.model.remote.RetroBuilder
import kotlinx.coroutines.launch

class ChatViewModel(application: Application) : AndroidViewModel(application) {
    private var remoteRepositoryImp: RemoteRepositoryImp
    private var localRepositoryImp: LocalRepositoryImpl

    val Tag = "CaregiverViewModel"
    var error: String? = null


    init {
        val db = OldCareDB.getInstance(application)
        localRepositoryImp = LocalRepositoryImpl(db)

        val serviceInstant = RetroBuilder.builder
        remoteRepositoryImp = RemoteRepositoryImp(serviceInstant)
    }

    private var ConversationMutableLiveData = MutableLiveData<List<MessageResponse>?>()
    val ConversationLiveData: LiveData<List<MessageResponse>?>
        get() = ConversationMutableLiveData

    private var MessageMutableLiveData = MutableLiveData<MessageResponse>()
    val MessageLiveData: LiveData<MessageResponse>
        get() = MessageMutableLiveData

    private var MessageDeletedMutableLiveData = MutableLiveData<DeleteConversationResponse>()
    val MessageDeletedLiveData: LiveData<DeleteConversationResponse>
        get() = MessageDeletedMutableLiveData

    fun getConversation(token: String, recieverId: String) {
        viewModelScope.launch {
            if (isNetworkAvailable(getApplication())) {
                val conversation = remoteRepositoryImp.getConversation(token, recieverId)

                if (conversation.isSuccessful) {
                    ConversationMutableLiveData.postValue(conversation.body())

//                    conversation.body()?.let {
//                        localRepositoryImp.addPatientCircle(it)
//                        //  getCirclesDao()
//                    }
                    Log.i(Tag, conversation.body().toString())
                } else {
                    error = conversation.errorBody()?.string()!!.toString()
                    ConversationMutableLiveData.postValue(conversation.body())
                    Log.i(Tag, conversation.errorBody()?.string()!!.toString())
                }
            } else {
                //   getCirclesDao()
            }
        }
    }

    fun sendMessage(token: String, recieverId: String, content: String) {
        viewModelScope.launch {
            if (isNetworkAvailable(getApplication())) {
                val message = remoteRepositoryImp.sendMessage(token, recieverId, content)

                if (message.isSuccessful) {
                    MessageMutableLiveData.postValue(message.body())

//                    conversation.body()?.let {
//                        localRepositoryImp.addPatientCircle(it)
//                        //  getCirclesDao()
//                    }
                    Log.i(Tag, "suc" + message.body().toString())
                } else {
                    error = message.errorBody()?.string()!!.toString()
                    MessageMutableLiveData.postValue(message.body())
                    Log.i(Tag,"Fail"+error)
                }
            } else {
                //   getCirclesDao()
            }
        }
    }

    fun deleteMessage(messageId: String,token: String) {
        viewModelScope.launch {
            if (isNetworkAvailable(getApplication())) {
                val message = remoteRepositoryImp.deleteMessage(messageId,token)

                if (message.isSuccessful) {
                    MessageDeletedMutableLiveData.postValue(message.body())

//                    conversation.body()?.let {
//                        localRepositoryImp.addPatientCircle(it)
//                        //  getCirclesDao()
//                    }
                    Log.i(Tag, message.body().toString())
                } else {
                    error = message.errorBody()?.string()!!.toString()
                    MessageDeletedMutableLiveData.postValue(message.body())
                    Log.i(Tag, message.errorBody()?.string()!!.toString())
                }
            } else {
                //   getCirclesDao()
            }
        }
    }



//    fun getCirclesDao() {
//        viewModelScope.launch {
//            val Dao = localRepositoryImp.getPatientCircle()
//            CircleMutableLiveData.postValue(Dao)
//        }
//    }


    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

}