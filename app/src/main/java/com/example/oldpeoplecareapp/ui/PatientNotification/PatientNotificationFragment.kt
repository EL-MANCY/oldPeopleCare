package com.example.oldpeoplecareapp.ui.PatientNotification

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.oldpeoplecareapp.R
import com.example.oldpeoplecareapp.databinding.FragmentPatientNotificationBinding
import com.example.oldpeoplecareapp.databinding.FragmentRegistrationBinding

class PatientNotificationFragment : Fragment() {

    lateinit var binding:FragmentPatientNotificationBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentPatientNotificationBinding.inflate(inflater, container, false)
        return binding.root    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {



    }


}