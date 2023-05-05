package com.example.oldpeoplecareapp.ui.Authentication

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.oldpeoplecareapp.R
import com.example.oldpeoplecareapp.databinding.FragmentSplashScreenBinding

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
            if (retrivedToken == "null" ) {
               findNavController().navigate(SplashScreenFragmentDirections.actionSplashScreenFragmentToLogIn())
                Log.i("TOKEN Login",retrivedToken.toString())
            } else if(REGIST=="patient") {
                findNavController().navigate(SplashScreenFragmentDirections.actionSplashScreenFragmentToPatientHomeFragment("","",""))
                Log.i("TOKEN Home",retrivedToken.toString())
            }else if(REGIST=="caregiver") {
                findNavController().navigate(SplashScreenFragmentDirections.actionSplashScreenFragmentToCaregiveHomeFragment())
                Log.i("TOKEN Home",retrivedToken.toString())
            }

        }
    }

}