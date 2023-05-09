package com.example.oldpeoplecareapp.ui.PatientPath.AlarmScreen

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.oldpeoplecareapp.model.entity.Medicine
import com.example.oldpeoplecareapp.model.local.LocalRepositoryImpl
import com.example.oldpeoplecareapp.model.local.OldCareDB
import com.example.oldpeoplecareapp.model.remote.RemoteRepositoryImp
import com.example.oldpeoplecareapp.model.remote.RetroBuilder
import kotlinx.coroutines.launch

class AlarmScreenViewModel(application: Application): AndroidViewModel(application) {
    private var remoteRepositoryImp: RemoteRepositoryImp
    private var localRepositoryImp: LocalRepositoryImpl

    val Tag="AlarmScreenViewModel"
    var error :String?=null


    init {
        val db = OldCareDB.getInstance(application)
        localRepositoryImp= LocalRepositoryImpl(db)

        val serviceInstant = RetroBuilder.builder
        remoteRepositoryImp = RemoteRepositoryImp(serviceInstant)
    }


    private val snackBarMutableLiveData = MutableLiveData<String>()
    val snackBarLiveData: LiveData<String>
        get() = snackBarMutableLiveData

    private var stateMutableLiveData= MutableLiveData<Any>()
    val stateLiveData: LiveData<Any>
        get() =stateMutableLiveData


    fun changeState(token: String,userID: String,medID: String, state:String){
        viewModelScope.launch {
            if(isNetworkAvailable(getApplication())) {

                val state = remoteRepositoryImp.changeState(token, userID, medID, state)

                if (state.isSuccessful) {
                    stateMutableLiveData.postValue(state.body())
                    Log.i(Tag, state.body().toString())
                } else {
                    error = state.errorBody()?.string()!!.toString()
                    Log.i(Tag, state.toString())
                    Log.i(Tag, state.errorBody()!!.string().toString())
                }
            }else{
                snackBarMutableLiveData.value = "Check Your Internet Connection"
            }
        }
    }

    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

}