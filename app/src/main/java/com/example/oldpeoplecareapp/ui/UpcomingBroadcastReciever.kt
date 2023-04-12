package com.example.oldpeoplecareapp.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.oldpeoplecareapp.model.remote.RemoteRepositoryImp
import com.example.oldpeoplecareapp.model.remote.RetroBuilder
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class UpcomingBroadcastReciever: BroadcastReceiver() {


    override fun onReceive(context: Context?, intent: Intent?) {
        val serviceInstant = RetroBuilder.builder
        var remoteRepositoryImp = RemoteRepositoryImp(serviceInstant)
        val getpreferences = context?.getSharedPreferences("MY_APP", AppCompatActivity.MODE_PRIVATE)
        var retrivedToken = getpreferences!!.getString("TOKEN", "null")
        Log.i("xxxx","xxxxx")

        // Your function to be executed every day at 12 am goes here
        GlobalScope.launch {
            val upcoming= remoteRepositoryImp.getUpcomingDaily("barier " + retrivedToken)
            Log.i("xxxx","yyyyy")
            Log.i("xxxx",upcoming.errorBody()?.string().toString())
            Log.i("xxxx",upcoming.body().toString())


        }

    }
}