package com.example.oldpeoplecareapp.ui.CaregiverPath.CaregiverNotifications

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.oldpeoplecareapp.model.entity.notificationData
import com.example.oldpeoplecareapp.model.remote.RemoteRepositoryImp
import com.example.oldpeoplecareapp.model.remote.RetroBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response


// request // request done // missing  // alert
class CaregiverNotifyViewModel(application: Application): AndroidViewModel(application) {
    private var remoteRepositoryImp: RemoteRepositoryImp
    val Tag = "CareNotifyViewModel"

    init {
        val serviceInstant = RetroBuilder.builder
        remoteRepositoryImp = RemoteRepositoryImp(serviceInstant)
    }

    private var NotificationMutableLiveData = MutableLiveData<List<notificationData>>()
    val NotificationLiveData: LiveData<List<notificationData>>
        get() = NotificationMutableLiveData

    private var StatusMutableLiveData = MutableLiveData<Any>()
    val StatusLiveData: LiveData<Any>
        get() = StatusMutableLiveData

    fun getAllNotification(token: String) {
        viewModelScope.launch {
            val NotificationList = remoteRepositoryImp.getAllNotification(token)

            if (NotificationList.isSuccessful) {
                NotificationMutableLiveData.postValue(NotificationList.body())
                Log.i(Tag, NotificationList.body().toString())
            } else {
                Log.i(Tag, NotificationList.toString())
            }
        }
    }

    fun Accept(notifyId: String, token: String) {
         viewModelScope.launch{
             val state = remoteRepositoryImp.Accept(notifyId,token)
         }
    }

    fun Reject(notifyId: String, token: String) {
       viewModelScope.launch {
           val state = remoteRepositoryImp.Reject(notifyId,token)
       }
    }


}