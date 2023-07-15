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
import androidx.core.view.isEmpty
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.oldpeoplecareapp.R
import com.example.oldpeoplecareapp.databinding.FragmentPatientNotificationBinding
import com.example.oldpeoplecareapp.ui.PatientPath.EditRemoveCareGiver.EditRemoveCaregiverRoleDirections
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_all_alarms.*
import kotlinx.android.synthetic.main.fragment_edit_remove_caregiver_role.*
import kotlinx.android.synthetic.main.fragment_patient_notification.*

class PatientNotificationFragment : Fragment() {

    var TAG = "PatientNotificationFragment"
    lateinit var binding: FragmentPatientNotificationBinding
    lateinit var notificationViewModel: PatientNotificationViewModel
    val notificationRecyclerView by lazy { NotificationRecyclerView() }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPatientNotificationBinding.inflate(inflater, container, false)
        val navBar = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)
        navBar.visibility = View.VISIBLE
        navBar?.selectedItemId = R.id.notification_icon
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val preferences = requireActivity().getSharedPreferences("MY_APP", Context.MODE_PRIVATE)
        val retrivedToken = preferences.getString("TOKEN", null)
        val retrivedID = preferences.getString("ID", null)

        notificationViewModel =
            ViewModelProvider(requireActivity()).get(PatientNotificationViewModel::class.java)
        notificationViewModel.getAllNotification("barier " + retrivedToken)

        binding.notificationRecyclerView.adapter = notificationRecyclerView

        binding.backBtn.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.userInfo.setOnClickListener {
            findNavController().navigate(PatientNotificationFragmentDirections.actionPatientNotificationFragmentToBasicInformationFragment())
        }

        notificationViewModel.getUserInfo("barier " + retrivedToken,retrivedID.toString())
        notificationViewModel.UserLiveData.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                binding.userInfo.setBackgroundResource(R.drawable.oval)
                Glide.with(this).load(it.image.url).into(binding.userInfo)

            } else if(notificationViewModel.error!=null) {
                Snackbar.make(
                    view,
                    notificationViewModel.error.toString(),
                    Snackbar.LENGTH_SHORT
                ).show()
                notificationViewModel.error =null
            }
        })


        notificationViewModel.NotificationLiveData.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                notificationRecyclerView.setList(it)
                if(notificationRecyclerView.NotificationList.isEmpty()){
                    binding.base.visibility=View.VISIBLE
                }else{
                    binding.base.visibility=View.GONE

                }

                Log.i(TAG, it.toString())
            } else if (notificationViewModel.error != null) {
                Snackbar.make(
                    NOTIFYP,
                    notificationViewModel.error.toString(),
                    Snackbar.LENGTH_SHORT
                ).show()
                notificationViewModel.error = null
            }
        })
        createChannel(
            getString(R.string.FCM_CHANNEL_ID),
            getString(R.string.FCM_CHANNEL_STRING)
        )

        if(notificationRecyclerView.NotificationList.isEmpty()){
            binding.base.visibility=View.VISIBLE
        }else{
            binding.base.visibility=View.GONE

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
    }


}