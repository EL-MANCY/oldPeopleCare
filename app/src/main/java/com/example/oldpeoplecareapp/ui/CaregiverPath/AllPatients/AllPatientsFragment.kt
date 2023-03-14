package com.example.oldpeoplecareapp.ui.CaregiverPath.AllPatients

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.oldpeoplecareapp.R
import com.example.oldpeoplecareapp.databinding.FragmentAllPatientsBinding
import com.example.oldpeoplecareapp.databinding.FragmentCaregiveHomeBinding

class AllPatientsFragment : Fragment() {
    lateinit var binding:FragmentAllPatientsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAllPatientsBinding.inflate(inflater, container, false)
        return binding.root
    }


}