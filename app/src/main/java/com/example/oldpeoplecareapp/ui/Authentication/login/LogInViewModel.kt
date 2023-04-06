package com.example.oldpeoplecareapp.ui.Authentication.login

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

class LogInViewModel(application: Application): AndroidViewModel(application) {
    val TAG = "LogInViewModel"
    private var remoteRepositoryImp: RemoteRepositoryImp

    private var tokenMutableLiveData = MutableLiveData<LogInStatus>()
    val tokenLiveData: LiveData<LogInStatus>
        get() = tokenMutableLiveData

    private var responseMutableLiveData = MutableLiveData<Any>()
    val responseLiveData: LiveData<Any>
        get() = responseMutableLiveData

    init {
        val serviceInstant = RetroBuilder.builder
        remoteRepositoryImp = RemoteRepositoryImp(serviceInstant)
    }

    fun logIn(
        emailOrPhone: String,
        email: String,
        password: String,
        FcmToken: String
    ) {
        Log.i("scopeTag", "reached")

        viewModelScope.launch {
            val token = remoteRepositoryImp.logIn(
                emailOrPhone, email, password, FcmToken
            )
            if (token.isSuccessful) {
                tokenMutableLiveData.postValue(LogInStatus.sucess(token.body()))
                Log.i(TAG, token.body().toString())
            } else {
                tokenMutableLiveData.postValue(LogInStatus.error(token.errorBody()!!.string().toString()))
                Log.i(TAG, token.body().toString())
            }
        }
    }

    fun resetPass(email: String) {
        Log.i("scopeTag", "reached")

        viewModelScope.launch {
            try {
                val response = remoteRepositoryImp.ResetPassword(email)
                if (response.isSuccessful) {
                    responseMutableLiveData.postValue(response.body())
                    Log.i(TAG, response.body().toString())
                } else {
                    Log.i(TAG, response.toString())
                }
            } catch (e: JsonIOException) {
                Log.i(TAG, e.toString())
            }
        }
    }

    sealed class LogInStatus{
        class error(val errormessage:String) : LogInStatus()
        class sucess(var result :UserLogInInfo?) : LogInStatus()
    }
}
