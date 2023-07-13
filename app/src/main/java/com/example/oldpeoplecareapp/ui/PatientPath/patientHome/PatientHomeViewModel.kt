package com.example.oldpeoplecareapp.ui.PatientPath.patientHome

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.oldpeoplecareapp.model.entity.AllMedicineRespone
import com.example.oldpeoplecareapp.model.entity.AllMedicineResponseItem
import com.example.oldpeoplecareapp.model.entity.Medicine
import com.example.oldpeoplecareapp.model.entity.SingleUserResponse
import com.example.oldpeoplecareapp.model.local.LocalRepositoryImpl
import com.example.oldpeoplecareapp.model.local.OldCareDB
import com.example.oldpeoplecareapp.model.remote.RemoteRepositoryImp
import com.example.oldpeoplecareapp.model.remote.RetroBuilder
import kotlinx.coroutines.launch

class PatientHomeViewModel(application: Application): AndroidViewModel(application) {
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

    private var allMedicineMutableLiveData= MutableLiveData<List<Medicine>>()
    val allMedicinLiveData: LiveData<List<Medicine>>
        get() =allMedicineMutableLiveData

    private val snackBarMutableLiveData = MutableLiveData<String>()
    val snackBarLiveData: LiveData<String>
        get() = snackBarMutableLiveData

    private var stateMutableLiveData= MutableLiveData<Any>()
    val stateLiveData: LiveData<Any>
        get() =stateMutableLiveData

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



    fun getAllMedicine(token: String,id: String, state:String){
        viewModelScope.launch {

            if (isNetworkAvailable(getApplication())) {
                val MedicineList = remoteRepositoryImp.getUpcoming(token, id, state)

                if (MedicineList.isSuccessful) {
                    allMedicineMutableLiveData.postValue(MedicineList.body())

                    MedicineList.body()?.let {
                        localRepositoryImp.addUpComming(it)
                       // getAllMedicineDao(state)
                    }

                    Log.i(Tag, MedicineList.body().toString())
                } else {
                    error = MedicineList.errorBody()?.string()!!.toString()
                    Log.i(Tag, MedicineList.toString())
                    Log.i(Tag, MedicineList.errorBody()!!.string().toString())
                }
            }else{
                getAllMedicineDao(state)
            }
        }
    }

    fun getAllMedicineDao(state: String) {
        viewModelScope.launch {
            if(state=="Waiting") {
                val Dao = localRepositoryImp.getUpcomingWaiting()
                allMedicineMutableLiveData.postValue(Dao)
            }else if (state=="Completed"){
                val Dao = localRepositoryImp.getUpcomingCompleted()
                allMedicineMutableLiveData.postValue(Dao)
            }else if (state=="Missed"){
                val Dao = localRepositoryImp.getUpcomingMissed()
                allMedicineMutableLiveData.postValue(Dao)
            }

        }
    }




    fun changeState(token: String,userID: String,medID: String, state:String){
        viewModelScope.launch {
            if(isNetworkAvailable(getApplication())) {

                val state = remoteRepositoryImp.changeState(token, userID, medID, state)

                if (state.isSuccessful) {
                    stateMutableLiveData.postValue(state.body())
                    Log.i(Tag, state.body().toString())
                } else {
                    error = state.errorBody()?.string()!!.toString()
                    Log.i(Tag, state.toString())
                    Log.i(Tag, state.errorBody()!!.string().toString())
                }
            }else{
                snackBarMutableLiveData.value = "Check Your Internet Connection"
            }
        }
    }

    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

}