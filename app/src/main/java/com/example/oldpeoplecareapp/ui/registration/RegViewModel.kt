package com.example.oldpeoplecareapp.ui.registration

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.oldpeoplecareapp.model.entity.UserResponse
import com.example.oldpeoplecareapp.model.remote.RemoteRepositoryImp
import com.example.oldpeoplecareapp.model.remote.RetroBuilder
import kotlinx.coroutines.launch

class RegViewModel(application: Application):AndroidViewModel(application) {
    private var remoteRepositoryImp: RemoteRepositoryImp

    private var addUserAPIMutableLiveData = MutableLiveData<UserResponse>()
    val addUserAPILiveData: LiveData<UserResponse>
        get() = addUserAPIMutableLiveData

    init {
        val serviceInstant = RetroBuilder.builder
        remoteRepositoryImp = RemoteRepositoryImp(serviceInstant)
    }
    fun addUsersAPI(fullname: String, email: String, phone: String, dateOfBirth: String,
        gender: String, registerAs: String, password: String) {
        Log.i("scopeTag","reached")

        viewModelScope.launch {
            val result = remoteRepositoryImp.addNewUser(
                fullname,
                email,
                phone,
                dateOfBirth,
                gender,
                registerAs,
                password
            )
            if(result.isSuccessful){
                    addUserAPIMutableLiveData.postValue(result.body())
                    Log.i("add",result.message())
            }else{
                Log.i("after",result.message())
            }

        }
    }
}