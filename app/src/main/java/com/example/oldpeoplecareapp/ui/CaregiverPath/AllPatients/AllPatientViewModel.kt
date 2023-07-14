package com.example.oldpeoplecareapp.ui.CaregiverPath.AllPatients

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.oldpeoplecareapp.model.entity.Circles
import com.example.oldpeoplecareapp.model.entity.SingleUserResponse
import com.example.oldpeoplecareapp.model.local.LocalRepositoryImpl
import com.example.oldpeoplecareapp.model.local.OldCareDB
import com.example.oldpeoplecareapp.model.remote.RemoteRepositoryImp
import com.example.oldpeoplecareapp.model.remote.RetroBuilder
import kotlinx.coroutines.launch

class AllPatientViewModel(application: Application): AndroidViewModel(application) {
    private var remoteRepositoryImp: RemoteRepositoryImp
    private var localRepositoryImp: LocalRepositoryImpl

    val Tag = "AllPatientViewModel"
    var error :String?=null


    init {
        val db = OldCareDB.getInstance(application)
        localRepositoryImp= LocalRepositoryImpl(db)

        val serviceInstant = RetroBuilder.builder
        remoteRepositoryImp = RemoteRepositoryImp(serviceInstant)
    }

    private var CircleMutableLiveData = MutableLiveData<List<Circles>?>()
    val CircleLiveData: LiveData<List<Circles>?>
        get() = CircleMutableLiveData


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

    fun getCircles( token: String) {
        viewModelScope.launch {
            if(isNetworkAvailable(getApplication())) {
                val CircleList = remoteRepositoryImp.getPatientCircle(token)

                if (CircleList.isSuccessful) {
                    CircleMutableLiveData.postValue(CircleList.body())
                    CircleList.body()?.let {
                        localRepositoryImp.addPatientCircle(it)
                        //  getCirclesDao()
                    }
                } else {
                    error = CircleList.errorBody()?.string()!!.toString()
                    CircleMutableLiveData.postValue(CircleList.body())
                    Log.i(Tag, CircleList.errorBody()?.string()!!.toString())                }
            }else{
                getCirclesDao()
            }
        }
    }

    fun getCirclesDao() {
        viewModelScope.launch {
            val Dao = localRepositoryImp.getPatientCircle()
            CircleMutableLiveData.postValue(Dao)
        }
    }


    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

}