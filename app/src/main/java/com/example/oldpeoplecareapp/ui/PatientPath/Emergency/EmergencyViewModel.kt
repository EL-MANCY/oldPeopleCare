package com.example.oldpeoplecareapp.ui.PatientPath.Emergency

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.oldpeoplecareapp.model.entity.Circles
import com.example.oldpeoplecareapp.model.local.LocalRepositoryImpl
import com.example.oldpeoplecareapp.model.local.OldCareDB
import com.example.oldpeoplecareapp.model.remote.RemoteRepositoryImp
import com.example.oldpeoplecareapp.model.remote.RetroBuilder
import kotlinx.coroutines.launch

class EmergencyViewModel(application: Application): AndroidViewModel(application) {
    private var remoteRepositoryImp: RemoteRepositoryImp
    private var localRepositoryImp: LocalRepositoryImpl

    val Tag = "CaregiverViewModel"

    init {
        val db = OldCareDB.getInstance(application)
        localRepositoryImp= LocalRepositoryImpl(db)

        val serviceInstant = RetroBuilder.builder
        remoteRepositoryImp = RemoteRepositoryImp(serviceInstant)
    }

    private var InfoMutableLiveData = MutableLiveData<List<Circles>?>()
    val InfoLiveData: LiveData<List<Circles>?>
        get() = InfoMutableLiveData

    fun getCircles(token: String) {
        viewModelScope.launch {
            if(isNetworkAvailable(getApplication())) {
                val CircleList = remoteRepositoryImp.getPatientCircle(token)
                CircleList.body()?.let {
                    localRepositoryImp.addPatientCircle(it)
                 //   getCirclesDao()
                }

                if (CircleList.isSuccessful) {
                    InfoMutableLiveData.postValue(CircleList.body())
                    Log.i(Tag, CircleList.body().toString())
                } else {
                    Log.i(Tag, CircleList.toString())
                }
            }else{
                getCirclesDao()
            }
        }
    }

    fun getCirclesDao() {
        viewModelScope.launch {
            val Dao = localRepositoryImp.getPatientCircle()
            InfoMutableLiveData.postValue(Dao)
        }
    }


    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

}