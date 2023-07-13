package com.example.oldpeoplecareapp.ui.PatientPath.EditRemoveCareGiver

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.oldpeoplecareapp.model.entity.DeleteConversationResponse
import com.example.oldpeoplecareapp.model.entity.SingleUserResponse
import com.example.oldpeoplecareapp.model.entity.UpdateResponse
import com.example.oldpeoplecareapp.model.local.LocalRepositoryImpl
import com.example.oldpeoplecareapp.model.local.OldCareDB
import com.example.oldpeoplecareapp.model.remote.RemoteRepositoryImp
import com.example.oldpeoplecareapp.model.remote.RetroBuilder
import kotlinx.coroutines.launch

class EditRemoveViewModel(application: Application): AndroidViewModel(application) {
    private var remoteRepositoryImp: RemoteRepositoryImp
    private var localRepositoryImp: LocalRepositoryImpl

    val Tag = "EditRemoveViewModel"
    var error: String? = null

    init {
        val db = OldCareDB.getInstance(application)
        localRepositoryImp= LocalRepositoryImpl(db)

        val serviceInstant = RetroBuilder.builder
        remoteRepositoryImp = RemoteRepositoryImp(serviceInstant)
    }

    private var updateCaregiverMutableLiveData = MutableLiveData<UpdateResponse?>()
    val updateCaregiverLiveData: LiveData<UpdateResponse?>
        get() = updateCaregiverMutableLiveData

    private var deleteCaregiverMutableLiveData = MutableLiveData<DeleteConversationResponse?>()
    val deleteCaregiverLiveData: LiveData<DeleteConversationResponse?>
        get() = deleteCaregiverMutableLiveData

    private val snackBarMutableLiveData = MutableLiveData<String>()
    val snackBarLiveData: LiveData<String>
        get() = snackBarMutableLiveData
    private var UserMutableLiveData = MutableLiveData<SingleUserResponse?>()
    val UserLiveData: LiveData<SingleUserResponse?>
        get() = UserMutableLiveData


    fun getUserInfo(token: String, userID: String) {
        viewModelScope.launch {
            if(isNetworkAvailable(getApplication())) {
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
            }else{
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

    fun updateRole(
        token: String,
        caregiverID: String,
        newRole: String
    ) {
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
            } else {
                snackBarMutableLiveData.value = "Check Your Internet Connection"
            }
        }
    }

    fun deleteCaregiver(
        token: String,
        caregiverID: String,
    ) {
        viewModelScope.launch {
            if (isNetworkAvailable(getApplication())) {
                val result = remoteRepositoryImp.deleteCareGiver(
                    caregiverID,
                    token,
                    )
                if (result.isSuccessful) {
                    deleteCaregiverMutableLiveData.postValue(result.body())
                    Log.i(Tag, result.body().toString())
                } else {
                    error = result.errorBody()?.string()!!.toString()
                    deleteCaregiverMutableLiveData.postValue(result.body())
                    Log.i(Tag, result.toString())
                }
            } else {
                snackBarMutableLiveData.value = "Check Your Internet Connection"
            }
        }
    }


    fun Empty() {
        viewModelScope.launch {
            updateCaregiverMutableLiveData.value = null
            error = null
        }
    }

    fun clear() {
        snackBarMutableLiveData.value=""
    }
    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }
}