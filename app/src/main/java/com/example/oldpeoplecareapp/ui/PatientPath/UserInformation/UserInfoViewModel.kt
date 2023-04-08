package com.example.oldpeoplecareapp.ui.PatientPath.UserInformation

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.oldpeoplecareapp.model.entity.SingleUserResponse
import com.example.oldpeoplecareapp.model.entity.notificationData
import com.example.oldpeoplecareapp.model.remote.RemoteRepositoryImp
import com.example.oldpeoplecareapp.model.remote.RetroBuilder
import kotlinx.coroutines.launch

class UserInfoViewModel(application: Application): AndroidViewModel(application) {
    private var remoteRepositoryImp: RemoteRepositoryImp
    val Tag = "UserInfoViewModel"
    var error: String? = null

    init {
        val serviceInstant = RetroBuilder.builder
        remoteRepositoryImp = RemoteRepositoryImp(serviceInstant)
    }

    private var UserMutableLiveData = MutableLiveData<SingleUserResponse?>()
    val UserLiveData: LiveData<SingleUserResponse?>
        get() = UserMutableLiveData

    fun getUserInfo(token: String, userID: String) {
        viewModelScope.launch {
            val result = remoteRepositoryImp.getSingleUser(token, userID)

            if (result.isSuccessful) {
                UserMutableLiveData.postValue(result.body())
                Log.i(Tag, result.body().toString())
            } else {
                error = result.errorBody()?.string()!!.toString()
                UserMutableLiveData.postValue(result.body())
                Log.i(Tag, result.toString())
            }
        }
    }

    fun Empty() {
        viewModelScope.launch {
            UserMutableLiveData.value = null
            error = null
        }
    }
}