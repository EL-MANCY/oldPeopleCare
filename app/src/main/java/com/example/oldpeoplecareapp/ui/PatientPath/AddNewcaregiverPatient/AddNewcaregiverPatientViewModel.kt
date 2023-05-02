package com.example.oldpeoplecareapp.ui.PatientPath.AddNewcaregiverPatient

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.oldpeoplecareapp.model.remote.RemoteRepositoryImp
import com.example.oldpeoplecareapp.model.remote.RetroBuilder
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class AddNewcaregiverPatientViewModel(application: Application): AndroidViewModel(application) {
    val Tag = "AddNewcaregiverX"
    private var remoteRepositoryImp: RemoteRepositoryImp
    var errorMutableLiveData :String?=null

    private var sucessMutableLiveData = MutableLiveData<Any>()
    val sucessLiveData: LiveData<Any>
        get() = sucessMutableLiveData

    private val snackBarMutableLiveData = MutableLiveData<String>()
    val snackBarLiveData: LiveData<String>
        get() = snackBarMutableLiveData

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
            if(isNetworkAvailable(getApplication())) {
                val sucess = remoteRepositoryImp.sendRequest(token, email, role)
                if (sucess.isSuccessful) {
                    sucessMutableLiveData.postValue(sucess.body())
                    Log.i(Tag, sucess.body().toString())
                } else {
                    errorMutableLiveData = sucess.errorBody()?.string()!!.toString()
                    Log.i(Tag, errorMutableLiveData.toString())
                    sucessMutableLiveData.postValue(sucess.body())
                }
            }else{
                snackBarMutableLiveData.value = "Check Your Internet Connection"

            }
        }
    }

    fun clear(){
        snackBarMutableLiveData.value=""
    }
    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }
}