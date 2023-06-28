package com.example.oldpeoplecareapp.ui.Chat.Conversations

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.oldpeoplecareapp.model.entity.AllConversationsResponseItem
import com.example.oldpeoplecareapp.model.entity.ConversationResponseItem
import com.example.oldpeoplecareapp.model.entity.DeleteConversationResponse
import com.example.oldpeoplecareapp.model.entity.MessageResponse
import com.example.oldpeoplecareapp.model.local.LocalRepositoryImpl
import com.example.oldpeoplecareapp.model.local.OldCareDB
import com.example.oldpeoplecareapp.model.remote.RemoteRepositoryImp
import com.example.oldpeoplecareapp.model.remote.RetroBuilder
import kotlinx.coroutines.launch

class ConversationsViewModel(application: Application) : AndroidViewModel(application) {
    private var remoteRepositoryImp: RemoteRepositoryImp
    private var localRepositoryImp: LocalRepositoryImpl

    val Tag = "ConversationsViewModel"
    var error: String? = null


    init {
        val db = OldCareDB.getInstance(application)
        localRepositoryImp = LocalRepositoryImpl(db)

        val serviceInstant = RetroBuilder.builder
        remoteRepositoryImp = RemoteRepositoryImp(serviceInstant)
    }

    private var ConversationMutableLiveData = MutableLiveData<List<AllConversationsResponseItem>?>()
    val ConversationLiveData: LiveData<List<AllConversationsResponseItem>?>
        get() = ConversationMutableLiveData


    fun getAllConversations(token: String) {
        viewModelScope.launch {
            if (isNetworkAvailable(getApplication())) {
                val conversations = remoteRepositoryImp.getAllConversations(token)

                if (conversations.isSuccessful) {
                    ConversationMutableLiveData.postValue(conversations.body())

//                    conversation.body()?.let {
//                        localRepositoryImp.addPatientCircle(it)
//                        //  getCirclesDao()
//                    }
                    Log.i(Tag, conversations.body().toString())
                } else {
                    error = conversations.errorBody()?.string()!!.toString()
                    ConversationMutableLiveData.postValue(conversations.body())
                    Log.i(Tag, conversations.errorBody()?.string()!!.toString())
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