package com.example.oldpeoplecareapp.ui.PatientPath.patientHome

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.oldpeoplecareapp.model.entity.AllMedicineRespone
import com.example.oldpeoplecareapp.model.remote.RemoteRepositoryImp
import com.example.oldpeoplecareapp.model.remote.RetroBuilder
import kotlinx.coroutines.launch

class PatientHomeViewModel(application: Application): AndroidViewModel(application) {
    private var remoteRepositoryImp: RemoteRepositoryImp
    val Tag="PatientHomeViewModelx"

    init {
        val serviceInstant = RetroBuilder.builder
        remoteRepositoryImp = RemoteRepositoryImp(serviceInstant)
    }

    private var allMedicineMutableLiveData= MutableLiveData<List<AllMedicineRespone>>()
    val allMedicinLiveData: LiveData<List<AllMedicineRespone>>
        get() =allMedicineMutableLiveData

    fun getAllMedicine(id: String, token: String){
        viewModelScope.launch {
           val MedicineList= remoteRepositoryImp.getAllMedicine(id,token)

            if(MedicineList.isSuccessful){
                allMedicineMutableLiveData.postValue(MedicineList.body())
                Log.i(Tag,MedicineList.body().toString())
            }else{
                Log.i(Tag,MedicineList.toString())
            }
        }
    }


}