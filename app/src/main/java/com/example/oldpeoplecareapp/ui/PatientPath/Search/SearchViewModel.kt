package com.example.oldpeoplecareapp.ui.PatientPath.Search

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.oldpeoplecareapp.model.entity.SearchResponseItem
import com.example.oldpeoplecareapp.model.local.LocalRepositoryImpl
import com.example.oldpeoplecareapp.model.local.OldCareDB
import com.example.oldpeoplecareapp.model.remote.RemoteRepositoryImp
import com.example.oldpeoplecareapp.model.remote.RetroBuilder
import kotlinx.coroutines.launch

class SearchViewModel(application: Application): AndroidViewModel(application) {
    private var remoteRepositoryImp: RemoteRepositoryImp
    private var localRepositoryImp: LocalRepositoryImpl

    val Tag = "SearchViewModel"
    var error :String?=null


    init {
        val db = OldCareDB.getInstance(application)
        localRepositoryImp= LocalRepositoryImpl(db)

        val serviceInstant = RetroBuilder.builder
        remoteRepositoryImp = RemoteRepositoryImp(serviceInstant)
    }

    private var UsersMutableLiveData = MutableLiveData<List<SearchResponseItem>?>()
    val UsersLiveData: LiveData<List<SearchResponseItem>?>
        get() = UsersMutableLiveData

    fun searchUser( token: String,user:String) {
        viewModelScope.launch {
            if (isNetworkAvailable(getApplication())) {
                val UsersList = remoteRepositoryImp.searchUser(token,user)

                if (UsersList.isSuccessful) {
                    UsersMutableLiveData.postValue(UsersList.body())

//                    CircleList.body()?.let {
//                        localRepositoryImp.addPatientCircle(it)
//                        //  getCirclesDao()
//                    }
                    Log.i(Tag, UsersList.body().toString())
                } else {
                    error = UsersList.errorBody()?.string()!!.toString()
                    UsersMutableLiveData.postValue(UsersList.body())
                    Log.i(Tag, UsersList.errorBody()?.string()!!.toString())
                }
            }else{
          //      getCirclesDao()
            }
        }
    }

//    fun getCirclesDao() {
//        viewModelScope.launch {
//            val Dao = localRepositoryImp.getPatientCircle()
//            CircleMutableLiveData.postValue(Dao)
//        }
//    }


    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

}