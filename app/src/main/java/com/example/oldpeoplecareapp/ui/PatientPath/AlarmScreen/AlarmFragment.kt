package com.example.oldpeoplecareapp.ui.PatientPath.AlarmScreen

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.oldpeoplecareapp.databinding.FragmentAlarmBinding
import com.example.oldpeoplecareapp.ui.PatientPath.AlarmScreen.AlarmHelper


class AlarmFragment : Fragment() {

    lateinit var binding:FragmentAlarmBinding
    var medImageUrl:String =""
    var medName:String=""
    var alarmSoundPath:String=""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAlarmBinding.inflate(inflater, container, false)
         medImageUrl = arguments?.getString("medImageUrl").toString()
         medName = arguments?.getString("medName").toString()
         alarmSoundPath = arguments?.getString("alarmSoundPath").toString()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val alarmHelper = AlarmHelper()

        binding.medTxtfield.text=medName
        binding.cancelBtn.setOnClickListener {
            alarmHelper.cancelAlarm(requireContext())
        }

    }

}