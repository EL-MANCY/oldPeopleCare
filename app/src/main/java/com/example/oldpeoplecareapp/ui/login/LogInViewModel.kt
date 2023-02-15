package com.example.oldpeoplecareapp.ui.login

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.oldpeoplecareapp.model.entity.UserLogInInfo
import com.example.oldpeoplecareapp.model.entity.UserResponse
import com.example.oldpeoplecareapp.model.remote.RemoteRepositoryImp
import com.example.oldpeoplecareapp.model.remote.RetroBuilder
import kotlinx.coroutines.launch

class LogInViewModel(application: Application): AndroidViewModel(application) {
    private var remoteRepositoryImp: RemoteRepositoryImp

    private var tokenMutableLiveData = MutableLiveData<UserLogInInfo>()
    val tokenLiveData: LiveData<UserLogInInfo>
        get() = tokenMutableLiveData

    init {
        val serviceInstant = RetroBuilder.builder
        remoteRepositoryImp = RemoteRepositoryImp(serviceInstant)
    }
    fun logIn(emailOrPhone: String,
              email: String,
              password: String) {
        Log.i("scopeTag","reached")

        viewModelScope.launch {
            val token = remoteRepositoryImp.logIn(
               emailOrPhone,email,password)
            if(token.isSuccessful){
                tokenMutableLiveData.postValue(token.body())
                Log.i("success",token.body().toString())
            }else{
                Log.i("failed",token.toString())
            }

        }
    }


}