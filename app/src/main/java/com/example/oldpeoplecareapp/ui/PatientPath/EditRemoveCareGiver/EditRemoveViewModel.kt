package com.example.oldpeoplecareapp.ui.PatientPath.EditRemoveCareGiver

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.oldpeoplecareapp.model.entity.AllMedicineRespone
import com.example.oldpeoplecareapp.model.entity.UpdateResponse
import com.example.oldpeoplecareapp.model.remote.RemoteRepositoryImp
import com.example.oldpeoplecareapp.model.remote.RetroBuilder
import com.google.gson.JsonIOException
import kotlinx.coroutines.launch
import retrofit2.http.Path

class EditRemoveViewModel(application: Application): AndroidViewModel(application) {
    private var remoteRepositoryImp: RemoteRepositoryImp
    val Tag = "EditRemoveViewModel"
    var error :String?=null

    init {
        val serviceInstant = RetroBuilder.builder
        remoteRepositoryImp = RemoteRepositoryImp(serviceInstant)
    }

    private var updateCaregiverMutableLiveData = MutableLiveData<UpdateResponse?>()
    val updateCaregiverLiveData: LiveData<UpdateResponse?>
        get() = updateCaregiverMutableLiveData

    private val snackBarMutableLiveData = MutableLiveData<String>()
    val snackBarLiveData: LiveData<String>
        get() = snackBarMutableLiveData


    fun updateRole(token: String,
                   caregiverID: String,
                   newRole: String) {
        viewModelScope.launch {
            if (isNetworkAvailable(getApplication())) {
                val result = remoteRepositoryImp.updateRole(
                    token,
                    caregiverID,
                    newRole
                )
                if (result.isSuccessful) {
                    updateCaregiverMutableLiveData.postValue(result.body())
                    Log.i(Tag, result.body().toString())
                } else {
                    error = result.errorBody()?.string()!!.toString()
                    updateCaregiverMutableLiveData.postValue(result.body())
                    Log.i(Tag, result.toString())
                }
            }else{
                snackBarMutableLiveData.value = "Check Your Internet Connection"
            }
        }
    }


    fun Empty() {
        viewModelScope.launch {
            updateCaregiverMutableLiveData.value=null
            error=null
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