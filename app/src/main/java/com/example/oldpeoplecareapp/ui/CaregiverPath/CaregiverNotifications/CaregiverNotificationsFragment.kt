package com.example.oldpeoplecareapp.ui.CaregiverPath.CaregiverNotifications

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
import com.example.oldpeoplecareapp.databinding.ActivityMainBinding.inflate
import com.example.oldpeoplecareapp.databinding.FragmentAllPatientsBinding
import com.example.oldpeoplecareapp.databinding.FragmentCaregiverNotificationsBinding
import com.example.oldpeoplecareapp.model.entity.notificationData
import com.example.oldpeoplecareapp.ui.CaregiverPath.AllPatients.AllPatientViewModel
import com.example.oldpeoplecareapp.ui.CaregiverPath.AllPatients.OnItemClickListener2
import com.example.oldpeoplecareapp.ui.CaregiverPath.AllPatients.PatientsRecyclerView
import com.example.oldpeoplecareapp.ui.PatientPath.PatientNotification.PatientNotificationViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class CaregiverNotificationsFragment : Fragment(), OnItemClickListner3 {

    val TAG = "CaregiverNotifiFrag"
    lateinit var binding: FragmentCaregiverNotificationsBinding
    lateinit var caregiverNotifyViewModel: CaregiverNotifyViewModel
    lateinit var retrivedToken: String
    val caregiverNotifyRecyclerview by lazy { CaregiverNotifyRecyclerview() }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCaregiverNotificationsBinding.inflate(inflater, container, false)
        val navBar2 = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation2)
        navBar2.visibility = View.VISIBLE
        navBar2?.selectedItemId = R.id.notifi_icon
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val getpreferences = requireActivity().getSharedPreferences("MY_APP", Context.MODE_PRIVATE)
        retrivedToken = getpreferences.getString("TOKEN", null).toString()

        caregiverNotifyViewModel = ViewModelProvider(requireActivity()).get(
            CaregiverNotifyViewModel::class.java
        )
        caregiverNotifyViewModel.getAllNotification("barier " + retrivedToken)

        binding.caregiverNotifyRecyclerView.adapter = caregiverNotifyRecyclerview

        caregiverNotifyViewModel.NotificationLiveData.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                caregiverNotifyRecyclerview.setList(it)
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
            val notificationChannel =
                NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
                    .apply {
                        setShowBadge(true)
                    }
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = "Med Time"

            val notificationManager =
                requireActivity().getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(notificationChannel)
        }

        caregiverNotifyRecyclerview.onListItemClick3 = this

    }

    override fun accepted(item: notificationData) {
        caregiverNotifyViewModel.Accept(item._id,"barier " + retrivedToken)
    }

    override fun rejected(item: notificationData) {
        caregiverNotifyViewModel.Reject(item._id,"barier " + retrivedToken)
    }

}