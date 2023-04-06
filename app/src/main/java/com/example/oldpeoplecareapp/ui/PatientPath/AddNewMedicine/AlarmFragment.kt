package com.example.oldpeoplecareapp.ui.PatientPath.AddNewMedicine

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.oldpeoplecareapp.R
import com.example.oldpeoplecareapp.databinding.FragmentAddNewMedicineBinding
import com.example.oldpeoplecareapp.databinding.FragmentAlarmBinding


class AlarmFragment : Fragment() {

    lateinit var binding:FragmentAlarmBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAlarmBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val alarmHelper = AlarmHelper()

        binding.cancelBtn.setOnClickListener {
            alarmHelper.cancelAlarm(requireContext())
        }

    }

}