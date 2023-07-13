package com.example.oldpeoplecareapp.ui.PatientPath.EditUserInformation

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.oldpeoplecareapp.model.entity.SingleUserResponse
import com.example.oldpeoplecareapp.model.local.LocalRepositoryImpl
import com.example.oldpeoplecareapp.model.local.OldCareDB
import com.example.oldpeoplecareapp.model.remote.RemoteRepositoryImp
import com.example.oldpeoplecareapp.model.remote.RetroBuilder
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class EditUserViewModel(application: Application) : AndroidViewModel(application) {
    private var remoteRepositoryImp: RemoteRepositoryImp
    private var localRepositoryImp: LocalRepositoryImpl

    val Tag = "UserInfoViewModel"
    var error: String? = null

    init {
        val db = OldCareDB.getInstance(application)
        localRepositoryImp = LocalRepositoryImpl(db)

        val serviceInstant = RetroBuilder.builder
        remoteRepositoryImp = RemoteRepositoryImp(serviceInstant)
    }

    private var UserMutableLiveData = MutableLiveData<SingleUserResponse?>()
    val UserLiveData: LiveData<SingleUserResponse?>
        get() = UserMutableLiveData

    private var UpdatMutableLiveData = MutableLiveData<SingleUserResponse?>()
    val UpdatLiveData: LiveData<SingleUserResponse?>
        get() = UpdatMutableLiveData

    fun getUserInfo(token: String, userID: String) {
        viewModelScope.launch {
            if (isNetworkAvailable(getApplication())) {
                val result = remoteRepositoryImp.getSingleUser(token, userID)

                if (result.isSuccessful) {
                    UserMutableLiveData.postValue(result.body())

                    result.body()?.let {
                        localRepositoryImp.postSingleUser(it)
                        //  getAllNotificationDao()
                    }
                    Log.i(Tag, result.body().toString())
                } else {
                    error = result.errorBody()?.string()!!.toString()
                    UserMutableLiveData.postValue(result.body())
                    Log.i(Tag, result.toString())
                }
            } else {
                getUser()
            }
        }
    }

    fun udpateUserInfo(
        token: String,
        fullname: RequestBody,
        email: RequestBody,
        phone: RequestBody,
        dateOfBirth: RequestBody,
        gender: RequestBody,
        image: MultipartBody.Part
    ) {
        viewModelScope.launch {
            if (isNetworkAvailable(getApplication())) {
                val result = remoteRepositoryImp.updatetSingleUser(
                    token,
                    fullname,
                    email,
                    phone,
                    dateOfBirth,
                    gender,
                    image
                )

                if (result.isSuccessful) {
                    UpdatMutableLiveData.postValue(result.body())

//                    result.body()?.let {
//                        localRepositoryImp.postSingleUser(it)
//                        //  getAllNotificationDao()
//                    }
                    Log.i(Tag, result.body().toString())
                } else {
                    error = result.errorBody()?.string()!!.toString()
                    UpdatMutableLiveData.postValue(result.body())
                    Log.i(Tag, result.toString())
                }
            } else {
                getUser()
            }
        }
    }

    fun getUser() {
        viewModelScope.launch {
            val Dao = localRepositoryImp.getSingleUser()
            UserMutableLiveData.postValue(Dao)


        }
    }


    fun Empty() {
        viewModelScope.launch {
            UserMutableLiveData.value = null
            error = null
        }
    }

    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

}