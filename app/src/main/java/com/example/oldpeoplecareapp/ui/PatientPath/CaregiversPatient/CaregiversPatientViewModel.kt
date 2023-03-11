package com.example.oldpeoplecareapp.ui.PatientPath.CaregiversPatient

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

class CaregiversPatientViewModel(application: Application): AndroidViewModel(application) {
    private var remoteRepositoryImp: RemoteRepositoryImp
    val Tag = "CaregiverViewModel"

    init {
        val serviceInstant = RetroBuilder.builder
        remoteRepositoryImp = RemoteRepositoryImp(serviceInstant)
    }

    private var CircleMutableLiveData = MutableLiveData<List<Circles>>()
    val CircleLiveData: LiveData<List<Circles>>
        get() = CircleMutableLiveData

    fun getCircles( token: String) {
        viewModelScope.launch {
            val CircleList = remoteRepositoryImp.getPatientCircle(token)

            if (CircleList.isSuccessful) {
                CircleMutableLiveData.postValue(CircleList.body())
                Log.i(Tag, CircleList.body().toString())
            } else {
                Log.i(Tag, CircleList.toString())
            }
        }
    }


}