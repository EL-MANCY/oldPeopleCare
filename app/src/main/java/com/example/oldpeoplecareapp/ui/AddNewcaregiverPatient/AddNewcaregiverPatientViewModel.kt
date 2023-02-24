package com.example.oldpeoplecareapp.ui.AddNewcaregiverPatient

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.oldpeoplecareapp.model.remote.RemoteRepositoryImp
import com.example.oldpeoplecareapp.model.remote.RetroBuilder
import kotlinx.coroutines.launch

class AddNewcaregiverPatientViewModel(application: Application): AndroidViewModel(application) {
    val Tag = "AddNewcaregiverX"
    private var remoteRepositoryImp: RemoteRepositoryImp

    private var sucessMutableLiveData = MutableLiveData<Any>()
    val sucessLiveData: LiveData<Any>
        get() = sucessMutableLiveData

    init {
        val serviceInstant = RetroBuilder.builder
        remoteRepositoryImp = RemoteRepositoryImp(serviceInstant)
    }

    fun sendReq(
        token: String,
        email: String,
        role: String
    ) {
        viewModelScope.launch {
            val sucess = remoteRepositoryImp.sendRequest(token, email, role)
            if (sucess.isSuccessful) {
                sucessMutableLiveData.postValue(sucess.body())
                Log.i(Tag, sucess.body().toString())
            } else {
                Log.i(Tag, sucess.body().toString())
            }
        }
    }


}