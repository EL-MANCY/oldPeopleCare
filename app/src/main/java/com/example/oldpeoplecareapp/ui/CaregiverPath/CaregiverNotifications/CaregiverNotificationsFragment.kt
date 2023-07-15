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
import android.widget.Button
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.oldpeoplecareapp.LoadingDialog
import com.example.oldpeoplecareapp.R
import com.example.oldpeoplecareapp.databinding.FragmentCaregiverNotificationsBinding
import com.example.oldpeoplecareapp.model.entity.notificationData
import com.example.oldpeoplecareapp.ui.CaregiverPath.AllPatients.AllPatientsFragmentDirections
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_add_new_medicine.*

class CaregiverNotificationsFragment : Fragment(), OnItemClickListner3 {

    val TAG = "CaregiverNotifiFrag"
    lateinit var binding: FragmentCaregiverNotificationsBinding
    lateinit var caregiverNotifyViewModel: CaregiverNotifyViewModel
    lateinit var retrivedToken: String
    val caregiverNotifyRecyclerview by lazy { CaregiverNotifyRecyclerview(requireActivity()) }


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
        //val loading= LoadingDialog(requireActivity())


        val getpreferences = requireActivity().getSharedPreferences("MY_APP", Context.MODE_PRIVATE)
        retrivedToken = getpreferences.getString("TOKEN", null).toString()
        val retrivedID = getpreferences.getString("ID", null)

        caregiverNotifyViewModel = ViewModelProvider(requireActivity()).get(
            CaregiverNotifyViewModel::class.java
        )
        caregiverNotifyViewModel.getAllNotification("barier " + retrivedToken)

        binding.caregiverNotifyRecyclerView.adapter = caregiverNotifyRecyclerview

        caregiverNotifyViewModel.NotificationLiveData.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                caregiverNotifyRecyclerview.setList(it)
                Log.i(TAG, it.toString())
                if(caregiverNotifyRecyclerview.NotificationList.isEmpty()){
                    binding.base.visibility=View.VISIBLE
                }else{
                    binding.base.visibility=View.GONE

                }

            }
        })
        if(caregiverNotifyRecyclerview.NotificationList.isEmpty()){
            binding.base.visibility=View.VISIBLE
        }else{
            binding.base.visibility=View.GONE

        }

        createChannel(
            getString(R.string.FCM_CHANNEL_ID),
            getString(R.string.FCM_CHANNEL_STRING)
        )

        caregiverNotifyViewModel.StatusLiveData.observe(viewLifecycleOwner, Observer {
            if (it != null) {
             //   loading.isDismiss()
                Log.i("TTTTT","TTTTTTTTT")
            } else {
              //  loading.isDismiss()
                Snackbar.make(MED, caregiverNotifyViewModel.error.toString(), Snackbar.LENGTH_SHORT)
                    .show()
            }
        })

        caregiverNotifyViewModel.getUserInfo("barier " + retrivedToken, retrivedID.toString())


        caregiverNotifyViewModel.UserLiveData.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if (it != null) {
                binding.userInfo.setBackgroundResource(R.drawable.oval)
                Glide.with(this).load(it.image.url).into(binding.userInfo)

            } else if(caregiverNotifyViewModel.error!=null) {
                Snackbar.make(
                    view,
                    caregiverNotifyViewModel.error.toString(),
                    Snackbar.LENGTH_SHORT
                ).show()
                caregiverNotifyViewModel.error =null
            }
        })

        binding.userInfo.setOnClickListener {
            findNavController().navigate(CaregiverNotificationsFragmentDirections.actionCaregiverNotificationsFragmentToBasicInformationFragment())

        }
        binding.backBtn.setOnClickListener {
            findNavController().navigateUp()
        }
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
       // loading.startLoading()
        caregiverNotifyViewModel.Accept(item._id,"barier " + retrivedToken)

    }

    override fun rejected(item: notificationData) {
       // loading.startLoading()
        caregiverNotifyViewModel.Reject(item._id,"barier " + retrivedToken)
    }

}