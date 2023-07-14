package com.example.oldpeoplecareapp.ui.CaregiverPath.CaregiverHome

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.oldpeoplecareapp.model.entity.SingleUserResponse
import com.example.oldpeoplecareapp.model.local.LocalRepositoryImpl
import com.example.oldpeoplecareapp.model.local.OldCareDB
import com.example.oldpeoplecareapp.model.remote.RemoteRepositoryImp
import com.example.oldpeoplecareapp.model.remote.RetroBuilder
import com.example.oldpeoplecareapp.ui.CaregiverPath.CaregiverHome.UiModel.MedicineUiModel
import kotlinx.coroutines.launch

class CareGiverHomeViewModel(application: Application): AndroidViewModel(application) {
    private var remoteRepositoryImp: RemoteRepositoryImp
    private var localRepositoryImp: LocalRepositoryImpl
    lateinit var medicineUiModels:List<MedicineUiModel>

    val Tag = "CareGiverHomeViewModelxxxx"
    var error :String?=null

    init {
        val db = OldCareDB.getInstance(application)
        localRepositoryImp= LocalRepositoryImpl(db)

        val serviceInstant = RetroBuilder.builder
        remoteRepositoryImp = RemoteRepositoryImp(serviceInstant)
    }

    private var MedItemMutableLiveData = MutableLiveData<List<MedicineUiModel>>()
    val MedItemLiveData: LiveData<List<MedicineUiModel>>
        get() = MedItemMutableLiveData


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

    fun getPatients(token: String) {
        viewModelScope.launch {

            if(isNetworkAvailable(getApplication())) {
                val result = remoteRepositoryImp.getPatients(token)

                if (result.isSuccessful) {
                    val patients = result.body()



                     medicineUiModels = patients!!.flatMap { CareGiverResponse ->
                        CareGiverResponse.medicines.map {
                            MedicineUiModel(
                                name = CareGiverResponse.user.fullname,
                                med = it.medicine.name,
                                medId = it.medicine._id,
                                userId = CareGiverResponse.user._id,
                                time = it.medicine.time.firstOrNull() ?: "",
                                imgUrlMed = it.medicine.image.url,
                                imgUrlUser = CareGiverResponse.user.image.url,
                                state = it.state,
                            )
                        }
                    }

                    result.body()?.let {
                        localRepositoryImp.addPatientsCareGiver(medicineUiModels)
                    }

                    MedItemMutableLiveData.postValue(medicineUiModels)
                    Log.i(Tag, result.body().toString())
                } else {
                    error = result.errorBody()?.string()!!.toString()
                    MedItemMutableLiveData.postValue(medicineUiModels)
                    Log.i(Tag, result.errorBody()?.string()!!.toString())
                }
            }else{
                getPatientsDao()
            }
        }
    }

    fun getPatientsDao() {
        viewModelScope.launch {
            val Dao = localRepositoryImp.getPatients()
            MedItemMutableLiveData.postValue(Dao)
        }
    }

    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }
}

