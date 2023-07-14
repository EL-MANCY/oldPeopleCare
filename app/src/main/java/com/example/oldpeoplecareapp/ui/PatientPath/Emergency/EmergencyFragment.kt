package com.example.oldpeoplecareapp.ui.PatientPath.Emergency

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.oldpeoplecareapp.R
import com.example.oldpeoplecareapp.databinding.FragmentEmergencyBinding
import com.example.oldpeoplecareapp.databinding.FragmentPatientHomeBinding
import com.example.oldpeoplecareapp.model.entity.Circles
import com.example.oldpeoplecareapp.ui.PatientPath.CaregiversPatient.CaregiversPatientViewModel
import com.example.oldpeoplecareapp.ui.PatientPath.CaregiversPatient.CircleRecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView

class EmergencyFragment : Fragment(),OnEmergyClickListener {
    val TAG = "EmergencyFragment"
    lateinit var binding:FragmentEmergencyBinding
    lateinit var emergencyViewModel: EmergencyViewModel
    lateinit var  retrivedToken:String
    val emergencyRecyclerview by lazy { EmergencyRecyclerview() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentEmergencyBinding.inflate(inflater, container, false)
        val navBar = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)
        navBar.visibility = View.GONE
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val getpreferences = requireActivity().getSharedPreferences("MY_APP", Context.MODE_PRIVATE)
        retrivedToken = getpreferences.getString("TOKEN", null).toString()

        emergencyViewModel = ViewModelProvider(requireActivity()).get(EmergencyViewModel::class.java)
        emergencyViewModel.getCircles("barier " + retrivedToken)

        binding.BackBtn.setOnClickListener {
//            findNavController().navigate(EmergencyFragmentDirections.actionEmergencyFragmentToPatientHomeFragment("","",""))
            findNavController().navigateUp()
        }

        binding.EmergyRecyclerView.adapter = emergencyRecyclerview
        emergencyViewModel.InfoLiveData.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                emergencyRecyclerview.setList(it)
                Log.i(TAG, it.toString())
            }
        })

        emergencyRecyclerview.onListItemClick = this

    }

    override fun onItemClick(info: Circles) {
        val phoneNumber = info.id.phone // Replace with the phone number you want to call
        val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:$phoneNumber"))
        view?.context?.startActivity(intent)
    }


}