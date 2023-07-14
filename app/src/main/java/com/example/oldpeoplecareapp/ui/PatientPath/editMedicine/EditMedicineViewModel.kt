package com.example.oldpeoplecareapp.ui.PatientPath.editMedicine

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.oldpeoplecareapp.model.entity.AllMedicineRespone
import com.example.oldpeoplecareapp.model.entity.MedicineResponseX
import com.example.oldpeoplecareapp.model.entity.SingleUserResponse
import com.example.oldpeoplecareapp.model.local.LocalRepositoryImpl
import com.example.oldpeoplecareapp.model.local.OldCareDB
import com.example.oldpeoplecareapp.model.remote.RemoteRepositoryImp
import com.example.oldpeoplecareapp.model.remote.RetroBuilder
import com.google.gson.JsonIOException
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class EditMedicineViewModel (application: Application): AndroidViewModel(application) {
    private var remoteRepositoryImp: RemoteRepositoryImp
    private var localRepositoryImp: LocalRepositoryImpl

    val Tag = "EditMedicineViewModelX"
    var error :String?=null



    init {
        val db = OldCareDB.getInstance(application)
        localRepositoryImp= LocalRepositoryImpl(db)

        val serviceInstant = RetroBuilder.builder
        remoteRepositoryImp = RemoteRepositoryImp(serviceInstant)
    }

    private var updateMedicineMutableLiveData = MutableLiveData<MedicineResponseX>()
    val updateMedicinLiveData: LiveData<MedicineResponseX>
        get() = updateMedicineMutableLiveData

    private var deleteMedicineMutableLiveData = MutableLiveData<Any?>()
    val deleteMedicineLiveData: LiveData<Any?>
        get() = deleteMedicineMutableLiveData

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

    fun updateAllMedicine(
        medId: String,
        userId: String,
        token: String,
        name: RequestBody,
        imgUrl: MultipartBody.Part,
        recordUrl: MultipartBody.Part,
        type: RequestBody,
        description: RequestBody,
        time:MultipartBody.Part,
        weakly: MultipartBody.Part
    ) {
        viewModelScope.launch {
            if (isNetworkAvailable(getApplication())) {
                Log.i(Tag, "entered")

                val MedicineList = remoteRepositoryImp.updateMedicine(
                    medId,
                    userId,
                    token,
                    name,
                    imgUrl,
                    recordUrl,
                    type,
                    description,
                    time,
                    weakly,
                )

                if (MedicineList.isSuccessful) {
                    updateMedicineMutableLiveData.postValue(MedicineList.body())
                    Log.i(Tag, MedicineList.body().toString())
                } else {
                    error = MedicineList.errorBody()?.string()!!.toString()
                    updateMedicineMutableLiveData.postValue(MedicineList.body())
                    Log.i(Tag, MedicineList.toString())
                }
            }else{
                snackBarMutableLiveData.value = "Check Your Internet Connection"
            }
        }
    }

    fun DeleteAllMedicine(medId: String, userId: String, token: String) {
        Log.i(Tag, "enter")

        viewModelScope.launch {
            if (isNetworkAvailable(getApplication())) {
                try {
                    val MedicineList = remoteRepositoryImp.DeleteMedicine(medId, userId, token)
                    Log.i(Tag, MedicineList.body().toString())
                    Log.i(Tag, "try")

                    if (MedicineList.isSuccessful) {
                        deleteMedicineMutableLiveData.postValue("deleted")
                        Log.i(Tag, MedicineList.body().toString())
                        Log.i(Tag, "success")
                    } else {
                        error = MedicineList.errorBody()?.string()!!.toString()
                        deleteMedicineMutableLiveData.postValue("NOT DELETED")
                        Log.i(Tag, error.toString())
                        Log.i(Tag, "failed")
                    }

                } catch (e: JsonIOException) {
                    deleteMedicineMutableLiveData.postValue("deleted")
                    println("JsonIOException caught: ${e.message}")
                    Log.i(Tag, "JsonIOException caught: ${e.message}")

                }
            }else{
                snackBarMutableLiveData.value = "Check Your Internet Connection"
            }
        }
    }
    fun EmptyMedicine() {
        viewModelScope.launch {
            deleteMedicineMutableLiveData.postValue(null)
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