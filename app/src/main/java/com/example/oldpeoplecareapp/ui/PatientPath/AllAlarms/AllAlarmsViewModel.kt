package com.example.oldpeoplecareapp.ui.PatientPath.AllAlarms

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.room.Dao
import com.example.oldpeoplecareapp.model.entity.AllMedicineResponseItem
import com.example.oldpeoplecareapp.model.local.LocalRepositoryImpl
import com.example.oldpeoplecareapp.model.local.OldCareDB
import com.example.oldpeoplecareapp.model.remote.RemoteRepositoryImp
import com.example.oldpeoplecareapp.model.remote.RetroBuilder
import kotlinx.coroutines.launch

class AllAlarmsViewModel (application: Application): AndroidViewModel(application) {
    private var remoteRepositoryImp: RemoteRepositoryImp
    private var localRepositoryImp: LocalRepositoryImpl

    val Tag="PatientHomeViewModelx"
    var error :String?=null


    init {
        val db = OldCareDB.getInstance(application)
        localRepositoryImp= LocalRepositoryImpl(db)

        val serviceInstant = RetroBuilder.builder
        remoteRepositoryImp = RemoteRepositoryImp(serviceInstant)
    }

    private var allMedicineMutableLiveData= MutableLiveData<List<AllMedicineResponseItem>>()
    val allMedicinLiveData: LiveData<List<AllMedicineResponseItem>>
        get() =allMedicineMutableLiveData

    fun getAllMedicine(id: String, token: String){
        viewModelScope.launch {

            if(isNetworkAvailable(getApplication())){
                val MedicineList= remoteRepositoryImp.getAllMedicine(id,token)

                if(MedicineList.isSuccessful){
                    allMedicineMutableLiveData.postValue(MedicineList.body())

                    MedicineList.body()?.let {
                        localRepositoryImp.postMedicine(it)
                        allMedicineMutableLiveData.postValue(localRepositoryImp.getAllMedicine())
                       // getAllMedicineDao()
                    }

                    Log.i(Tag,MedicineList.body().toString())
                }else{
                    error=MedicineList.errorBody()?.string()!!.toString()
                    allMedicineMutableLiveData.postValue(MedicineList.body())
                    getAllMedicineDao()
                    Log.i(Tag,MedicineList.toString())
                }
            }else{
                getAllMedicineDao()
            }

        }
    }
    fun getAllMedicineDao() {
        viewModelScope.launch {
            val Dao = localRepositoryImp.getAllMedicine()
            allMedicineMutableLiveData.postValue(Dao)

        }
    }

    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }


}