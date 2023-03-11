package com.example.oldpeoplecareapp.ui.PatientPath.AddNewMedicine

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.oldpeoplecareapp.model.remote.RemoteRepositoryImp
import com.example.oldpeoplecareapp.model.remote.RetroBuilder
import kotlinx.coroutines.launch

class AddNewMedicineViewModel(application: Application): AndroidViewModel(application) {
    private var remoteRepositoryImp: RemoteRepositoryImp
    val Tag="AddNewMedicineViewModel"

    init {
        val serviceInstant = RetroBuilder.builder
        remoteRepositoryImp = RemoteRepositoryImp(serviceInstant)
    }
    private var AddedMutableLiveData = MutableLiveData<Any>()
    val AddLiveData: LiveData<Any>
        get() = AddedMutableLiveData

    fun addMedicine(id: String, token: String, name: String, imgUrl: String, recordUrl: String, type: String,
                    date: String, time: String, repeatDays: Int, description: String, ) {
        viewModelScope.launch {
            val result=remoteRepositoryImp.postMedicine(id,token,name,imgUrl,recordUrl,type,date,time,repeatDays,description)

            if(result.isSuccessful){
                AddedMutableLiveData.postValue(result.body())
                Log.i(Tag,result.body().toString())
            }else{
                Log.i(Tag,result.toString())
            }
        }

    }
}