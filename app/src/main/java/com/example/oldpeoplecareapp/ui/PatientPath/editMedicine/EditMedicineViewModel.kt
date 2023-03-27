package com.example.oldpeoplecareapp.ui.PatientPath.editMedicine

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.oldpeoplecareapp.model.entity.AllMedicineRespone
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

    private var updateMedicineMutableLiveData = MutableLiveData<AllMedicineRespone>()
    val updateMedicinLiveData: LiveData<AllMedicineRespone>
        get() = updateMedicineMutableLiveData

    private var deleteMedicineMutableLiveData = MutableLiveData<Any?>()
    val deleteMedicineLiveData: LiveData<Any?>
        get() = deleteMedicineMutableLiveData

    fun updateAllMedicine(
        medId: String, userId: String, token: String, name: String,
        imgUrl: String, recordUrl: String, type: String, date: String,
        time: String, repeatDays: Int, description: String,
    ) {
        viewModelScope.launch {
            val MedicineList = remoteRepositoryImp.updateMedicine(
                medId, userId, token,
                name,
                imgUrl,
                recordUrl,
                type,
                date,
                time,
                repeatDays,
                description,
            )

            if (MedicineList.isSuccessful) {
                updateMedicineMutableLiveData.postValue(MedicineList.body())
                Log.i(Tag, MedicineList.body().toString())
            } else {
                error=MedicineList.errorBody()?.string()!!.toString()
                updateMedicineMutableLiveData.postValue(MedicineList.body())
                Log.i(Tag, MedicineList.toString())
            }
        }
    }

    fun DeleteAllMedicine(medId: String, userId: String, token: String) {
        viewModelScope.launch {
            try {
                val MedicineList = remoteRepositoryImp.DeleteMedicine(medId, userId, token)
                    deleteMedicineMutableLiveData.postValue("deleted")
                    Log.i(Tag, MedicineList.body().toString())

            } catch (e: JsonIOException) {
                deleteMedicineMutableLiveData.postValue("deleted")
                println("JsonIOException caught: ${e.message}")
            }
        }
    }
    fun EmptyMedicine() {
        viewModelScope.launch {
            deleteMedicineMutableLiveData.postValue(null)
        }
    }
}