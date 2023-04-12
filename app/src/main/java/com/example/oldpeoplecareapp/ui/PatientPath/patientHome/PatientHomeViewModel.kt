package com.example.oldpeoplecareapp.ui.PatientPath.patientHome

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.oldpeoplecareapp.model.entity.AllMedicineRespone
import com.example.oldpeoplecareapp.model.entity.AllMedicineResponseItem
import com.example.oldpeoplecareapp.model.entity.Medicine
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

    private var allMedicineMutableLiveData= MutableLiveData<List<Medicine>>()
    val allMedicinLiveData: LiveData<List<Medicine>>
        get() =allMedicineMutableLiveData
    private var stateMutableLiveData= MutableLiveData<Any>()
    val stateLiveData: LiveData<Any>
        get() =stateMutableLiveData

    fun getAllMedicine(token: String,id: String, state:String){
        viewModelScope.launch {
           val MedicineList= remoteRepositoryImp.getUpcoming(token,id,state)

            if(MedicineList.isSuccessful){
                allMedicineMutableLiveData.postValue(MedicineList.body())
                Log.i(Tag,MedicineList.body().toString())
            }else{
                Log.i(Tag,MedicineList.toString())
                Log.i(Tag,MedicineList.errorBody()!!.string().toString())
            }
        }
    }
    fun changeState(token: String,userID: String,medID: String, state:String){
        viewModelScope.launch {
           val state= remoteRepositoryImp.changeState(token,userID,medID,state)

            if(state.isSuccessful){
                stateMutableLiveData.postValue(state.body())
                Log.i(Tag,state.body().toString())
            }else{
                Log.i(Tag,state.toString())
                Log.i(Tag,state.errorBody()!!.string().toString())
            }
        }
    }


}