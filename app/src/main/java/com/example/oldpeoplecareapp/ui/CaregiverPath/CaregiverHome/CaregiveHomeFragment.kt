package com.example.oldpeoplecareapp.ui.CaregiverPath.CaregiverHome

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.oldpeoplecareapp.R
import com.example.oldpeoplecareapp.databinding.FragmentCaregiveHomeBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class CaregiveHomeFragment : Fragment() {
    lateinit var binding: FragmentCaregiveHomeBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCaregiveHomeBinding.inflate(inflater, container, false)
        val navBar = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)
        navBar.visibility = View.GONE
        val navBar2 = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation2)
        navBar2.visibility = View.VISIBLE
        navBar2?.selectedItemId =R.id.home_page
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.userInfo.setOnClickListener {
            findNavController().navigate(CaregiveHomeFragmentDirections.actionCaregiveHomeFragmentToBasicInformationFragment())

        }
    }


}