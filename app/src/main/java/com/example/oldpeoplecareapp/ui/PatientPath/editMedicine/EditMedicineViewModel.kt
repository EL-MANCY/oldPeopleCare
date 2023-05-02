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
import com.example.oldpeoplecareapp.model.remote.RemoteRepositoryImp
import com.example.oldpeoplecareapp.model.remote.RetroBuilder
import com.google.gson.JsonIOException
import kotlinx.coroutines.launch

class EditMedicineViewModel (application: Application): AndroidViewModel(application) {
    private var remoteRepositoryImp: RemoteRepositoryImp
    val Tag = "EditMedicineViewModelX"
    var error :String?=null



    init {
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

    fun updateAllMedicine(
        medId: String,
        userId: String,
        token: String,
        name: String,
        imgUrl: String,
        recordUrl: String,
        type: String,
        description: String,
        time: Array<String>,
        weakly: Array<String>
    ) {
        viewModelScope.launch {
            if (isNetworkAvailable(getApplication())) {
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