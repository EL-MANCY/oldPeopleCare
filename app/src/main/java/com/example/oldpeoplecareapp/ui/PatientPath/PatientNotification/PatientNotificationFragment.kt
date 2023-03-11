package com.example.oldpeoplecareapp.ui.PatientPath.PatientNotification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.oldpeoplecareapp.R
import com.example.oldpeoplecareapp.databinding.FragmentPatientNotificationBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class PatientNotificationFragment : Fragment() {

    var TAG="PatientNotificationFragment"
    lateinit var binding:FragmentPatientNotificationBinding
    lateinit var  retrivedToken:String
    lateinit var notificationViewModel: PatientNotificationViewModel
    val notificationRecyclerView by lazy { NotificationRecyclerView() }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentPatientNotificationBinding.inflate(inflater, container, false)
        val navBar = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)
        navBar.visibility = View.VISIBLE
        navBar?.selectedItemId =R.id.notification_icon
        return binding.root    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val getpreferences = requireActivity().getSharedPreferences("MY_APP", Context.MODE_PRIVATE)
        retrivedToken = getpreferences.getString("TOKEN", null).toString()

        notificationViewModel = ViewModelProvider(requireActivity()).get(PatientNotificationViewModel::class.java)
        notificationViewModel.getAllNotification("barier " + retrivedToken)

        binding.notificationRecyclerView.adapter = notificationRecyclerView

        notificationViewModel.NotificationLiveData.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                notificationRecyclerView.setList(it)
                Log.i(TAG, it.toString())
            }
        })

        createChannel(
            getString(R.string.FCM_CHANNEL_ID),
            getString(R.string.FCM_CHANNEL_STRING)
        )

    }

    private fun createChannel(channelId: String, channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            val notificationChannel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
                .apply {
                    setShowBadge(true)
                }
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = "Med Time"

            val notificationManager = requireActivity().getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }



}