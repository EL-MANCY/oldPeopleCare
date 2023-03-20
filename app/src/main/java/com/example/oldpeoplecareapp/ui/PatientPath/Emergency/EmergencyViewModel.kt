package com.example.oldpeoplecareapp.ui.PatientPath.Emergency

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.oldpeoplecareapp.model.entity.Circles
import com.example.oldpeoplecareapp.model.remote.RemoteRepositoryImp
import com.example.oldpeoplecareapp.model.remote.RetroBuilder
import kotlinx.coroutines.launch

class EmergencyViewModel(application: Application): AndroidViewModel(application) {
    private var remoteRepositoryImp: RemoteRepositoryImp
    val Tag = "CaregiverViewModel"

    init {
        val serviceInstant = RetroBuilder.builder
        remoteRepositoryImp = RemoteRepositoryImp(serviceInstant)
    }

    private var InfoMutableLiveData = MutableLiveData<List<Circles>>()
    val InfoLiveData: LiveData<List<Circles>>
        get() = InfoMutableLiveData

    fun getCircles(token: String) {
        viewModelScope.launch {
            val CircleList = remoteRepositoryImp.getPatientCircle(token)

            if (CircleList.isSuccessful) {
                InfoMutableLiveData.postValue(CircleList.body())
                Log.i(Tag, CircleList.body().toString())
            } else {
                Log.i(Tag, CircleList.toString())
            }
        }
    }


}