package com.example.oldpeoplecareapp.ui.PatientPath.PatientNotification

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.oldpeoplecareapp.model.entity.notificationData
import com.example.oldpeoplecareapp.model.local.LocalRepositoryImpl
import com.example.oldpeoplecareapp.model.local.OldCareDB
import com.example.oldpeoplecareapp.model.remote.RemoteRepositoryImp
import com.example.oldpeoplecareapp.model.remote.RetroBuilder
import kotlinx.coroutines.launch

class PatientNotificationViewModel(application: Application): AndroidViewModel(application) {
    private var remoteRepositoryImp: RemoteRepositoryImp
    private var localRepositoryImp: LocalRepositoryImpl

    val Tag="NotificationViewModel"
    var error :String?=null


    init {
        val db = OldCareDB.getInstance(application)
        localRepositoryImp= LocalRepositoryImpl(db)

        val serviceInstant = RetroBuilder.builder
        remoteRepositoryImp = RemoteRepositoryImp(serviceInstant)
    }

    private var NotificationMutableLiveData= MutableLiveData<List<notificationData>>()
    val NotificationLiveData: LiveData<List<notificationData>>
        get() =NotificationMutableLiveData

    fun getAllNotification(token: String){
        viewModelScope.launch {

            if (isNetworkAvailable(getApplication())) {
                val NotificationList = remoteRepositoryImp.getAllNotification(token)

                if (NotificationList.isSuccessful) {
                    NotificationMutableLiveData.postValue(NotificationList.body())
                    Log.i(Tag, NotificationList.body().toString())

                    NotificationList.body()?.let {
                        localRepositoryImp.addNotify(it)
                        getAllNotificationDao()
                    }

                } else {
                    error = NotificationList.errorBody()?.string()!!.toString()
                    NotificationMutableLiveData.postValue(NotificationList.body())
                    Log.i(Tag, NotificationList.toString())
                }
            }else{
                getAllNotificationDao()
            }
        }
    }

    fun getAllNotificationDao() {
        viewModelScope.launch {
                val Dao = localRepositoryImp.getAllNotification()
                NotificationMutableLiveData.postValue(Dao)


        }
    }


    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

}