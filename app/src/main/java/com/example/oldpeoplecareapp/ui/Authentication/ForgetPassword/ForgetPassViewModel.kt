package com.example.oldpeoplecareapp.ui.Authentication.ForgetPassword

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.oldpeoplecareapp.model.entity.UserLogInInfo
import com.example.oldpeoplecareapp.model.remote.RemoteRepositoryImp
import com.example.oldpeoplecareapp.model.remote.RetroBuilder
import com.google.gson.JsonIOException
import kotlinx.coroutines.launch

class ForgetPassViewModel(application: Application): AndroidViewModel(application) {
    private var remoteRepositoryImp: RemoteRepositoryImp
    private val TAG = "ForgetPassViewModel"
    var error:String?=null


    private var responseMutableLiveData = MutableLiveData<Any>()
    val responseLiveData: LiveData<Any>
        get() = responseMutableLiveData

    init {
        val serviceInstant = RetroBuilder.builder
        remoteRepositoryImp = RemoteRepositoryImp(serviceInstant)
    }


    fun resetPass(email: String) {
        Log.i(TAG, "reached")

        viewModelScope.launch {
            try {
                val response = remoteRepositoryImp.ResetPassword(email)
                if (response.isSuccessful) {
                    responseMutableLiveData.postValue(response.body())
                    Log.i(TAG, response.body().toString())
                } else {
                    error=response.errorBody()!!.string().toString()
                    responseMutableLiveData.postValue(response.body())
                    Log.i(TAG, response.toString())
                }
            } catch (e: JsonIOException) {
                Log.i(TAG, e.toString())
            }
        }
    }
}
