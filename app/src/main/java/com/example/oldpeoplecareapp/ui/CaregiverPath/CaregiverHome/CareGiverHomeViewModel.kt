package com.example.oldpeoplecareapp.ui.CaregiverPath.CaregiverHome

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.oldpeoplecareapp.model.remote.RemoteRepositoryImp
import com.example.oldpeoplecareapp.model.remote.RetroBuilder
import com.example.oldpeoplecareapp.ui.CaregiverPath.CaregiverHome.UiModel.MedicineUiModel
import kotlinx.coroutines.launch

class CareGiverHomeViewModel(application: Application): AndroidViewModel(application) {
    private var remoteRepositoryImp: RemoteRepositoryImp
    val Tag = "CareGiverHomeViewModel"
    var error :String?=null

    init {
        val serviceInstant = RetroBuilder.builder
        remoteRepositoryImp = RemoteRepositoryImp(serviceInstant)
    }

    private var MedItemMutableLiveData = MutableLiveData<List<MedicineUiModel>>()
    val MedItemLiveData: LiveData<List<MedicineUiModel>>
        get() = MedItemMutableLiveData


    fun getPatients(token: String) {
        viewModelScope.launch {
            val result = remoteRepositoryImp.getPatients(token)

            if (result.isSuccessful) {
                val patients = result.body()
                val medicineUiModels = patients!!.flatMap { CareGiverResponse ->
                    CareGiverResponse.medicines.map {
                        MedicineUiModel(
                            name = CareGiverResponse.user.fullname,
                            med = it.medicine.name,
                            time = it.medicine.time.firstOrNull() ?: "",
                            imgUrlMed = it.medicine.imgUrl,
                            imgUrlUser = CareGiverResponse.user.image.url,
                            state = it.state
                        )
                    }
                }

                MedItemMutableLiveData.postValue(medicineUiModels)
                Log.i(Tag, result.body().toString())
            } else {
                Log.i(Tag, result.errorBody()!!.string().toString())
            }
        }
    }

}

