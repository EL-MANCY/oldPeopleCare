package com.example.oldpeoplecareapp.ui.PatientNotification

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.oldpeoplecareapp.model.entity.notificationData
import com.example.oldpeoplecareapp.model.remote.RemoteRepositoryImp
import com.example.oldpeoplecareapp.model.remote.RetroBuilder
import kotlinx.coroutines.launch

class PatientNotificationViewModel(application: Application): AndroidViewModel(application) {
    private var remoteRepositoryImp: RemoteRepositoryImp
    val Tag="NotificationViewModel"

    init {
        val serviceInstant = RetroBuilder.builder
        remoteRepositoryImp = RemoteRepositoryImp(serviceInstant)
    }

    private var NotificationMutableLiveData= MutableLiveData<List<notificationData>>()
    val NotificationLiveData: LiveData<List<notificationData>>
        get() =NotificationMutableLiveData

    fun getAllNotification(token: String){
        viewModelScope.launch {
            val NotificationList= remoteRepositoryImp.getAllNotification(token)

            if(NotificationList.isSuccessful){
                NotificationMutableLiveData.postValue(NotificationList.body())
                Log.i(Tag,NotificationList.body().toString())
            }else{
                Log.i(Tag,NotificationList.toString())
            }
        }
    }


}