package com.example.oldpeoplecareapp.ui.PatientPath.AllAlarms

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.oldpeoplecareapp.model.entity.AllMedicineResponseItem
import com.example.oldpeoplecareapp.model.remote.RemoteRepositoryImp
import com.example.oldpeoplecareapp.model.remote.RetroBuilder
import kotlinx.coroutines.launch

class AllAlarmsViewModel (application: Application): AndroidViewModel(application) {
    private var remoteRepositoryImp: RemoteRepositoryImp
    val Tag="PatientHomeViewModelx"
    var error :String?=null


    init {
        val serviceInstant = RetroBuilder.builder
        remoteRepositoryImp = RemoteRepositoryImp(serviceInstant)
    }

    private var allMedicineMutableLiveData= MutableLiveData<List<AllMedicineResponseItem>>()
    val allMedicinLiveData: LiveData<List<AllMedicineResponseItem>>
        get() =allMedicineMutableLiveData

    fun getAllMedicine(id: String, token: String){
        viewModelScope.launch {
            val MedicineList= remoteRepositoryImp.getAllMedicine(id,token)

            if(MedicineList.isSuccessful){
                allMedicineMutableLiveData.postValue(MedicineList.body())
                Log.i(Tag,MedicineList.body().toString())
            }else{
                error=MedicineList.errorBody()?.string()!!.toString()
                allMedicineMutableLiveData.postValue(MedicineList.body())
                Log.i(Tag,MedicineList.toString())
            }
        }
    }


}