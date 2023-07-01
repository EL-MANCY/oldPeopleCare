package com.example.oldpeoplecareapp.ui.Authentication

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.oldpeoplecareapp.databinding.FragmentSplashScreenBinding
import com.example.oldpeoplecareapp.ui.Chat.ChatScreen.socketHandler
import com.example.oldpeoplecareapp.ui.PatientPath.AlarmScreen.AlarmHelper
import java.util.*

class SplashScreenFragment : Fragment() {

    lateinit var binding:FragmentSplashScreenBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSplashScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val getpreferences = requireActivity().getSharedPreferences("MY_APP", Context.MODE_PRIVATE)
        var retrivedToken:String? = getpreferences.getString("TOKEN", "null")
        var REGIST:String? = getpreferences.getString("REGIST", "null")



        binding.StatBtn.setOnClickListener {
//            val alarmHelper = AlarmHelper()
//            val calendar = Calendar.getInstance()
//            calendar.set(Calendar.HOUR_OF_DAY, 15)
//            calendar.set(Calendar.MINUTE, 24)
//            calendar.set(Calendar.SECOND, 0)
//            calendar.set(Calendar.MILLISECOND, 0)
//
//            alarmHelper.setAlarm(
//                requireContext(),
//                7787,
//                calendar,
//                "meds.medicine.imgUrl",
//                "meds.medicine.name",
//                "meds.medicine.name",
//                "4444"
//            )

            if (retrivedToken == "null" ) {
               findNavController().navigate(SplashScreenFragmentDirections.actionSplashScreenFragmentToLogIn())
                Log.i("TOKEN Login",retrivedToken.toString())
            } else if(REGIST=="patient")
            {
                socketHandler.setSocket()
                socketHandler.establishConnection()

                findNavController().navigate(SplashScreenFragmentDirections.actionSplashScreenFragmentToPatientHomeFragment("","",""))
                Log.i("TOKEN Home",retrivedToken.toString())
            }else if(REGIST=="caregiver") {
                findNavController().navigate(SplashScreenFragmentDirections.actionSplashScreenFragmentToCaregiveHomeFragment())
                Log.i("TOKEN Home",retrivedToken.toString())
            }

        }

//        calendar.set(Calendar.HOUR_OF_DAY, 19)
//        calendar.set(Calendar.MINUTE, 54)
//        alarmHelper.setAlarm(requireContext(),calendar,"xxxxxxxxxx","xxxxxxxxxxxxxxxx","meds.medicine.imgUrl")

    }

}