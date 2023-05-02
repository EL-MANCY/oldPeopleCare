package com.example.oldpeoplecareapp.ui.PatientPath.AddNewMedicine

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.oldpeoplecareapp.model.entity.AllMedicineResponseItem
import com.example.oldpeoplecareapp.model.entity.MedicineResponse
import com.example.oldpeoplecareapp.model.entity.MedicineResponseX
import com.example.oldpeoplecareapp.model.local.LocalRepositoryImpl
import com.example.oldpeoplecareapp.model.local.OldCareDB
import com.example.oldpeoplecareapp.model.remote.RemoteRepositoryImp
import com.example.oldpeoplecareapp.model.remote.RetroBuilder
import kotlinx.coroutines.launch

class AddNewMedicineViewModel(application: Application): AndroidViewModel(application) {
    private var remoteRepositoryImp: RemoteRepositoryImp
    private var localRepositoryImp: LocalRepositoryImpl
    val Tag = "AddNewMedicineViewModel"
    var error :String?=null


    init {
        val db = OldCareDB.getInstance(application)
        localRepositoryImp= LocalRepositoryImpl(db)

        val serviceInstant = RetroBuilder.builder
        remoteRepositoryImp = RemoteRepositoryImp(serviceInstant)
    }

    private var AddedMutableLiveData = MutableLiveData<AllMedicineResponseItem>()
    val AddLiveData: LiveData<AllMedicineResponseItem>
        get() = AddedMutableLiveData

    private val snackBarMutableLiveData = MutableLiveData<String>()
    val snackBarLiveData: LiveData<String>
        get() = snackBarMutableLiveData

    fun addMedicine(
        id: String,
        token: String,
        name: String,
        imgUrl: String,
        recordUrl: String,
        type: String,
        description: String,
        time: List<String>,
        weakly: Array<String>
    ) {
        viewModelScope.launch {
            if (isNetworkAvailable(getApplication())) {
                val result = remoteRepositoryImp.postMedicine(
                    id,
                    token,
                    name,
                    imgUrl,
                    recordUrl,
                    type,
                    description,
                    time,
                    weakly,
                )

                if (result.isSuccessful) {
                    AddedMutableLiveData.postValue(result.body())
                    Log.i(Tag, result.body().toString())
                } else {
                    error = result.errorBody()?.string()!!.toString()
                    AddedMutableLiveData.postValue(result.body())
                    Log.i(Tag, result.errorBody()?.string().toString())
                }
            }else{
                snackBarMutableLiveData.value = "Check Your Internet Connection"
            }
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